package edlab.eda.cadence.rc.session;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;

import edlab.eda.cadence.rc.api.GenericSkillCommandTemplates;
import edlab.eda.cadence.rc.api.IncorrectSyntaxException;
import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.data.SkillDisembodiedPropertyList;
import edlab.eda.cadence.rc.data.SkillString;
import edlab.eda.cadence.rc.data.SkillSymbol;

public class SkillSocketSession extends SkillSession {

  private int port;
  private Socket socket;

  private InputStream inputStream;
  private OutputStream outputStream;

  private Map<String, EvaluableToSkill> keywords;

  private static final int WAIT = 10;
  private int maxWaitSteps = 2000;

  private SkillSocketSession(int port) {
    super();
    this.port = port;

    this.keywords = new HashMap<>();
    this.keywords.put("returnType", new SkillString("string"));
  }

  public void setMaxWaitTime(int time) {
    this.maxWaitSteps = time / WAIT;
  }

  @Override
  public SkillSession start() throws UnableToStartSession {

    if (!this.isActive()) {
      try {
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress("0.0.0.0", this.port), 1000);
      } catch (IOException e) {
        throw new UnableToStartSession(this.port);
      }

      try {
        this.inputStream = this.socket.getInputStream();
      } catch (IOException e) {
        try {
          this.socket.close();
        } catch (IOException e1) {
        }
        throw new UnableToStartSession(this.port);
      }

      try {
        this.outputStream = this.socket.getOutputStream();
      } catch (IOException e) {
        try {
          this.socket.close();
        } catch (IOException e1) {
        }
        throw new UnableToStartSession(this.port);
      }

      // add shutdown hook for process
      Runtime.getRuntime().addShutdownHook(new Thread() {

        @Override
        public void run() {
          try {
            outputStream.write(0);
            outputStream.flush();

            inputStream.close();
            outputStream.close();
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
          } catch (Exception e) {
          }
        }
      });

      SkillCommand cmd = null;

      try {
        cmd = GenericSkillCommandTemplates
            .getTemplate(GenericSkillCommandTemplates.IS_CALLABLE)
            .buildCommand(new SkillSymbol(
                GenericSkillCommandTemplates.ED_CDS_RC_FOMAT_COMMAND));
      } catch (IncorrectSyntaxException e) {
        // cannot happen
      }

      String retval = this.communicate(cmd.toSkill(), 1);

      if (retval != null) {

        if (retval.equals("nil")) {

          File skillControlApi = getResourcePath(SkillSession.SKILL_RESOURCE,
              SkillSession.TMP_SKILL_FILE_SUFFIX);

          try {
            cmd = GenericSkillCommandTemplates
                .getTemplate(GenericSkillCommandTemplates.LOAD).buildCommand(
                    new SkillString(skillControlApi.getAbsolutePath()));
          } catch (IncorrectSyntaxException e) {
            // cannot happen
          }

          this.communicate(cmd.toSkill(), 1);

          skillControlApi.delete();
        }

        return this;

      } else {
        throw new UnableToStartSession(this.port);
      }
    } else {
      return this;
    }
  }

  @Override
  public boolean isActive() {
    if (this.socket == null) {
      return false;
    } else {
      return this.socket.isConnected();
    }
  }

  @Override
  public SkillDataobject evaluate(SkillCommand command, Thread parent)
      throws UnableToStartSession, EvaluationFailedException,
      InvalidDataobjectReferenceExecption {
    return this.evaluate(command);
  }

  @Override
  public SkillDataobject evaluate(SkillCommand command)
      throws UnableToStartSession, EvaluationFailedException,
      InvalidDataobjectReferenceExecption {

    String xml;

    if (!command.canBeUsedInSession(this)) {
      throw new InvalidDataobjectReferenceExecption(command, this);
    }

    SkillCommand outer = null;

    try {
      outer = GenericSkillCommandTemplates
          .getTemplate(GenericSkillCommandTemplates.ED_CDS_RC_FOMAT_COMMAND)
          .buildCommand(GenericSkillCommandTemplates
              .getTemplate(GenericSkillCommandTemplates.ERRSET)
              .buildCommand(command), this.keywords);

    } catch (IncorrectSyntaxException e) {
      // cannot happen
    }

    String inputString = outer.toSkill() + "\n";

    xml = this.communicate(inputString, 2);

    xml = StringEscapeUtils.UNESCAPE_JAVA.translate(xml);

    SkillDataobject obj = SkillDataobject.getSkillDataobjectFromXML(this, xml);

    SkillDataobject content;

    try {

      SkillDisembodiedPropertyList top = (SkillDisembodiedPropertyList) obj;

      if (top.get(SkillSession.ID_VALID).isTrue()) {

        if (top.containsKey(SkillSession.ID_FILE)) {

          SkillString filePath = (SkillString) top.get(SkillSession.ID_FILE);

          File dataFile = new File(filePath.getString());

          xml = new String(Files.readAllBytes(dataFile.toPath()),
              StandardCharsets.US_ASCII);

          top = (SkillDisembodiedPropertyList) SkillDataobject
              .getSkillDataobjectFromXML(this, xml);

          dataFile.delete();
        }

        content = top.get(SkillSession.ID_DATA);

      } else {

        SkillString errorstring = (SkillString) top.get(SkillSession.ID_ERROR);

        throw new EvaluationFailedException(inputString,
            errorstring.getString());
      }
    } catch (Exception e) {
      throw new EvaluationFailedException(inputString, xml);
    }

    return content;
  }

  @Override
  public SkillSocketSession stop() {

    try {
      this.outputStream.write(0);
      this.outputStream.flush();
    } catch (IOException e) {
    }

    try {
      this.inputStream.close();
      this.outputStream.close();
      this.socket.shutdownInput();
      this.socket.shutdownOutput();
      this.socket.close();
    } catch (IOException e) {
    }

    this.socket = null;
    this.outputStream = null;
    this.inputStream = null;

    return this;
  }

  @Override
  public String toString() {
    return "[PORT:" + this.port + "]";
  }

  /**
   * Connect to a socket
   * 
   * @param dir Directory where the socket was started
   * @return session
   * @throws UnableToStartSession when no connection can be established
   */
  public static SkillSocketSession connect(File dir)
      throws UnableToStartSession {

    int port = -1;
    SkillSocketSession session;

    if (dir.isDirectory() && dir.canRead()) {

      File socketFile = new File(dir, CadenceSocket.SOCKET_PORT_FILE_NAME);

      if (socketFile.exists() && socketFile.canRead()) {

        try {
          port = Integer
              .parseInt(Files.readAllLines(socketFile.toPath()).get(0));

        } catch (Exception e) {
        }

        if (port > 0) {
          session = new SkillSocketSession(port);
          session.start();
          return session;
        } else {
          throw new UnableToStartSession(
              "File \"" + socketFile.getAbsolutePath()
                  + "\" does not contain a valid port");
        }
      } else {
        throw new UnableToStartSession("Cannot find port in file \""
            + socketFile.getAbsolutePath() + "\"");
      }
    } else {
      throw new UnableToStartSession(
          "Cannot find directory \"" + dir.getAbsolutePath() + "\"");
    }
  }

  /**
   * Internal function
   * 
   * @param msg    Message to be
   * @param indent
   * @return
   */
  private String communicate(String msg, int indent) {

    try {
      this.outputStream.write(msg.getBytes());
    } catch (IOException e) {
      this.stop();
      return null;
    }

    int n = 0;

    try {
      while (this.inputStream.available() == 0 && n < this.maxWaitSteps) {

        try {
          Thread.sleep(WAIT);
        } catch (InterruptedException e) {
        }

        n++;
      }
    } catch (IOException e) {
      return null;
    }

    if (n >= this.maxWaitSteps) {
      return null;
    }

    byte[] data = null;

    try {
      data = new byte[this.inputStream.available()];
      this.inputStream.read(data);
    } catch (IOException e) {
      return null;
    }

    String retval;
    retval = new String(data);
    return retval.substring(indent, retval.length() - indent);
  }
}