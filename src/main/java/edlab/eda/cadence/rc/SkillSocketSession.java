package edlab.eda.cadence.rc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import edlab.eda.cadence.rc.api.GenericSkillCommandTemplates;
import edlab.eda.cadence.rc.api.IncorrectSyntaxException;
import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.data.SkillDisembodiedPropertyList;
import edlab.eda.cadence.rc.data.SkillString;

@SuppressWarnings("deprecation")
public class SkillSocketSession extends SkillSession {

  private int port;
  private Socket socket;

  private InputStream inputStream;
  private OutputStream outputStream;

  private Map<String, EvaluableToSkill> keywords;

  public SkillSocketSession(int port) {
    this.port = port;

    this.keywords = new HashMap<String, EvaluableToSkill>();
    this.keywords.put("returnType", new SkillString("string"));
  }

  @Override
  public SkillSession start() throws UnableToStartSkillSession {

    try {
      this.socket = new Socket("0.0.0.0", this.port);
    } catch (IOException e) {
      throw new UnableToStartSkillSession(this.port);
    }

    try {
      this.inputStream = this.socket.getInputStream();
    } catch (IOException e) {
      try {
        this.socket.close();
      } catch (IOException e1) {
      }
      throw new UnableToStartSkillSession(this.port);
    }

    try {
      this.outputStream = this.socket.getOutputStream();
    } catch (IOException e) {
      try {
        this.socket.close();
      } catch (IOException e1) {
      }
      throw new UnableToStartSkillSession(this.port);
    }
    return this;
  }

  @Override
  public boolean isActive() {
    return this.socket.isConnected();
  }

  @Override
  public SkillDataobject evaluate(SkillCommand command)
      throws UnableToStartSkillSession, EvaluationFailedException,
      InvalidDataobjectReferenceExecption {

    byte[] data = null;
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
    }

    String inputString = outer.toSkill() + "\n";

    try {
      this.outputStream.write(inputString.getBytes());
    } catch (IOException e) {
    }

    try {
      while (this.inputStream.available() == 0) {
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
        }
      }
    } catch (IOException e) {
      throw new UnableToStartSkillSession(this.port);
    }

    try {
      data = new byte[this.inputStream.available()];
      this.inputStream.read(data);
    } catch (IOException e) {
    }

    xml = new String(data);
    xml = xml.substring(2, xml.length() - 2);

    xml = StringEscapeUtils.unescapeJava(xml);

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
  public void stop() {

    try {
      this.socket.shutdownInput();
      this.socket.shutdownOutput();
      this.socket.close();
    } catch (IOException e) {
    }

    this.socket = null;
    this.outputStream = null;
    this.inputStream = null;
  }

  @Override
  public String toString() {
    return "[PORT:" + this.port + "]";
  }

}
