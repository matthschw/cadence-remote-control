package edlab.eda.cadence.rc.session;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.text.StringEscapeUtils;

import edlab.eda.cadence.rc.api.GenericSkillCommandTemplates;
import edlab.eda.cadence.rc.api.IncorrectSyntaxException;
import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.data.SkillDisembodiedPropertyList;
import edlab.eda.cadence.rc.data.SkillString;
import edlab.eda.cadence.rc.data.SkillSymbol;

/**
 * Session to a Skill socket
 */
public final class SkillSocketSession extends SkillSession {

  private final int port;
  private Socket socket;

  private InputStream inputStream;
  private OutputStream outputStream;

  private final Map<String, EvaluableToSkill> keywords;

  private Thread shutdownHook;

  private static final int WAIT_PERIOD_MS = 10;

  private static final long INIT_TIMEOUT_DURATION = 10;
  private static final TimeUnit INIT_TIMEOUT_TIME_UNIT = TimeUnit.SECONDS;

  private SkillSocketSession(final int port) {
    super();
    this.port = port;

    this.keywords = new HashMap<>();
    this.keywords.put("returnType", new SkillString("string"));

    if (this.logger instanceof Logger) {
      this.logger.add(Logger.MSG_CODE_51, Logger.INFO_STEP,
          "Create socket session @ port:" + this.port);
    }
  }

  @Override
  public SkillSession start() throws UnableToStartSocketSession {

    if (!this.isActive()) {

      try {

        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress("0.0.0.0", this.port), 1000);

        if (this.logger instanceof Logger) {
          this.logger.add(Logger.MSG_CODE_52, Logger.START_STEP,
              "Connected to socket " + this.port);
        }

      } catch (final IOException e) {

        if (this.logger instanceof Logger) {
          this.logger.add(Logger.MSG_CODE_53, Logger.START_STEP,
              "Unable to connect to socket " + this.port);
        }

        throw new UnableToStartSocketSession(this.port);
      }

      try {
        this.inputStream = this.socket.getInputStream();
      } catch (final IOException e) {
        try {
          this.socket.close();
        } catch (final IOException e1) {
        }

        if (this.logger instanceof Logger) {
          this.logger.add(Logger.MSG_CODE_54, Logger.START_STEP,
              "Cannot open input stream to socket " + this.port);
        }

        throw new UnableToStartSocketSession(this.port);
      }

      try {
        this.outputStream = this.socket.getOutputStream();
      } catch (final IOException e) {
        try {
          this.socket.close();
        } catch (final IOException e1) {
        }

        if (this.logger instanceof Logger) {
          this.logger.add(Logger.MSG_CODE_55, Logger.START_STEP,
              "Cannot open output stream to socket " + this.port);
        }

        throw new UnableToStartSocketSession(this.port);
      }

      // add shutdown hook for process
      this.shutdownHook = new Thread() {
        @Override
        public void run() {
          try {
            SkillSocketSession.this.outputStream.write(0);
            SkillSocketSession.this.outputStream.flush();

            SkillSocketSession.this.inputStream.close();
            SkillSocketSession.this.outputStream.close();
            SkillSocketSession.this.socket.shutdownInput();
            SkillSocketSession.this.socket.shutdownOutput();
            SkillSocketSession.this.socket.close();
          } catch (final Exception e) {
          }
        }
      };

      Runtime.getRuntime().addShutdownHook(this.shutdownHook);

      SkillCommand cmd = null;

      try {
        cmd = GenericSkillCommandTemplates
            .getTemplate(GenericSkillCommandTemplates.IS_CALLABLE)
            .buildCommand(new SkillSymbol(
                GenericSkillCommandTemplates.ED_CDS_RC_FOMAT_COMMAND));
      } catch (final IncorrectSyntaxException e) {
        // cannot happen
      }

      final String retval = this.communicate(cmd.toSkill(), 1);

      if (retval != null) {

        if (retval.equals("nil")) {

          final File skillControlApi = this.getResourcePath(
              SkillSession.SKILL_RESOURCE, SkillSession.TMP_SKILL_FILE_SUFFIX);

          try {
            cmd = GenericSkillCommandTemplates
                .getTemplate(GenericSkillCommandTemplates.LOAD).buildCommand(
                    new SkillString(skillControlApi.getAbsolutePath()));
          } catch (final IncorrectSyntaxException e) {
            // cannot happen
          }

          this.communicate(cmd.toSkill(), 1);

          skillControlApi.delete();
        }

        return this;

      } else {
        throw new UnableToStartSocketSession(this.port);
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
  public SkillDataobject evaluate(final SkillCommand command,
      final Thread parent) throws UnableToStartSocketSession,
      EvaluationFailedException, InvalidDataobjectReferenceExecption {
    return this.evaluate(command);
  }

  @Override
  public SkillDataobject evaluate(final SkillCommand command)
      throws UnableToStartSocketSession, EvaluationFailedException,
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

    } catch (final IncorrectSyntaxException e) {
      // cannot happen
    }

    final String inputString = outer.toSkill() + "\n";

    xml = this.communicate(inputString, 2);

    xml = StringEscapeUtils.UNESCAPE_JAVA.translate(xml);

    final SkillDataobject obj = SkillDataobject.getSkillDataobjectFromXML(this,
        xml);

    SkillDataobject content;

    try {

      SkillDisembodiedPropertyList top = (SkillDisembodiedPropertyList) obj;

      if (top.get(SkillSession.ID_VALID).isTrue()) {

        if (top.containsKey(SkillSession.ID_FILE)) {

          final SkillString filePath = (SkillString) top
              .get(SkillSession.ID_FILE);

          final File dataFile = new File(filePath.getString());

          xml = new String(Files.readAllBytes(dataFile.toPath()),
              StandardCharsets.US_ASCII);

          top = (SkillDisembodiedPropertyList) SkillDataobject
              .getSkillDataobjectFromXML(this, xml);

          dataFile.delete();
        }

        content = top.get(SkillSession.ID_DATA);

      } else {

        final SkillString errorstring = (SkillString) top
            .get(SkillSession.ID_ERROR);

        throw new EvaluationFailedException(inputString,
            errorstring.getString());
      }
    } catch (final Exception e) {
      throw new EvaluationFailedException(inputString, xml);
    }

    return content;
  }

  @Override
  public SkillSocketSession stop() {

    try {
      this.outputStream.write(0);
      this.outputStream.flush();
    } catch (final IOException e) {
    }

    try {
      this.inputStream.close();
      this.outputStream.close();
      this.socket.shutdownInput();
      this.socket.shutdownOutput();
      this.socket.close();

    } catch (final IOException e) {
    }

    this.socket = null;
    this.outputStream = null;
    this.inputStream = null;

    Runtime.getRuntime().removeShutdownHook(shutdownHook);

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
   * @throws UnableToStartSocketSession when no connection can be established
   */
  public static SkillSocketSession connect(final File dir)
      throws UnableToStartSocketSession {

    int port = -1;
    SkillSocketSession session;

    if (dir.isDirectory() && dir.canRead()) {

      final File socketFile = new File(dir,
          CadenceSocket.SOCKET_PORT_FILE_NAME);

      if (socketFile.exists() && socketFile.canRead()) {

        try {
          port = Integer
              .parseInt(Files.readAllLines(socketFile.toPath()).get(0));

        } catch (final Exception e) {
        }

        if (port > 0) {

          session = new SkillSocketSession(port);

          final long timeoutDuration = session.getTimeoutDuration();
          final TimeUnit timeoutTimeUnit = session.getTimeUnit();

          session.setTimeout(INIT_TIMEOUT_DURATION, INIT_TIMEOUT_TIME_UNIT);

          session.start();

          session.setTimeout(timeoutDuration, timeoutTimeUnit);

          return session;

        } else {
          throw new UnableToStartSocketSession(port, dir);
        }
      } else {
        throw new UnableToStartSocketSession(dir);
      }
    } else {
      throw new UnableToStartSocketSession(dir);
    }
  }

  /**
   * Internal function for communicaton with the Cadence tool
   * 
   * @param msg    Message to be transferred
   * @param indent Intedation of the command
   * @return Return string
   */
  private String communicate(final String msg, final int indent) {

    try {
      this.outputStream.write(msg.getBytes());
    } catch (final IOException e) {
      e.printStackTrace();
      this.stop();
      return null;
    }

    final Date lastActivity = new Date();

    try {
      Thread.sleep(WAIT_PERIOD_MS);
    } catch (final InterruptedException e) {
    }

    Date now = new Date();

    try {

      while ((this.inputStream.available() == 0)
          && ((now.getTime() - lastActivity.getTime()) < this.timeout_ms)) {

        try {
          Thread.sleep(WAIT_PERIOD_MS);
        } catch (final InterruptedException e) {
        }

        now = new Date();
      }

    } catch (final IOException e) {
      return null;
    }

    if ((now.getTime() - lastActivity.getTime()) >= this.timeout_ms) {
      return null;
    }

    byte[] data = null;

    try {
      data = new byte[this.inputStream.available()];
      this.inputStream.read(data);
    } catch (final IOException e) {
      e.printStackTrace();
      return null;
    }

    String retval;
    retval = new String(data);

    return retval.substring(indent, retval.length() - indent);
  }
}