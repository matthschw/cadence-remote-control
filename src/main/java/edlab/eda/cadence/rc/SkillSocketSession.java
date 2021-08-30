package edlab.eda.cadence.rc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.data.SkillDataobject;

public class SkillSocketSession extends SkillSession {

  private int port;
  private Socket socket;

  private InputStream inputStream;
  private OutputStream outputStream;

  public SkillSocketSession(int port) {
    this.port = port;
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
    
    byte[] data;
    String outstring;

    if (command.canBeUsedInSession(this)) {

      String inputString = command.toSkill() + "\n";

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
      
      data = new byte[this.inputStream.available()];
      
      
      

    } else {
      throw new InvalidDataobjectReferenceExecption(command, this);
    }

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void stop() {
    // TODO Auto-generated method stub

  }

  @Override
  public String toString() {
    return "[PORT:" + this.port + "]";
  }

}
