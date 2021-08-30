package edlab.eda.cadence.rc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import edlab.eda.cadence.rc.api.GenericSkillCommandTemplates;
import edlab.eda.cadence.rc.api.IncorrectSyntaxException;
import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.data.SkillString;

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
    String outstring;

    if (command.canBeUsedInSession(this)) {

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
      
      System.err.println(inputString);

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

      outstring = new String(data);

      System.out.println(outstring);

    } else {
      throw new InvalidDataobjectReferenceExecption(command, this);
    }
    // TODO Auto-generated method stub
    return null;
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
