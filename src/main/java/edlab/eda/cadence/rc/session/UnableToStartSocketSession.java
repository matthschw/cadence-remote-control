package edlab.eda.cadence.rc.session;

import java.io.File;

public class UnableToStartSocketSession extends UnableToStartSession {

  private static final long serialVersionUID = 3137649998348947919L;

  private int port;
  private File workingDir;

  public UnableToStartSocketSession(int port, File workingDir) {
    super("Unable to connect to session @ \"" + workingDir.getAbsolutePath()
        + "\" with port " + port);
    this.port = port;
    this.workingDir = workingDir;
  }

  public UnableToStartSocketSession(File workingDir) {
    super("Unable to connect to session @ \"" + workingDir.getAbsolutePath()
        + "\"");
    this.port = -1;
    this.workingDir = workingDir;
  }

  public UnableToStartSocketSession(int port) {
    super("Unable to connect to session @ port " + port);
    this.port = -1;
    this.workingDir = null;
  }

  public int getPort() {
    return this.port;
  }

  public File getWorkingDir() {
    return workingDir;
  }
}