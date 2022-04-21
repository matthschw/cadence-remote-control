package edlab.eda.cadence.rc.session;

import java.io.File;

public final class UnableToStartSocketSession extends UnableToStartSession {

  private static final long serialVersionUID = 3137649998348947919L;

  private final int port;
  private final File workingDir;

  public UnableToStartSocketSession(final int port, final File workingDir) {
    super("Unable to connect to session @ \"" + workingDir.getAbsolutePath()
        + "\" with port " + port);
    this.port = port;
    this.workingDir = workingDir;
  }

  public UnableToStartSocketSession(final File workingDir) {
    super("Unable to connect to session @ \"" + workingDir.getAbsolutePath()
        + "\"");
    this.port = -1;
    this.workingDir = workingDir;
  }

  public UnableToStartSocketSession(final int port) {
    super("Unable to connect to session @ port " + port);
    this.port = -1;
    this.workingDir = null;
  }

  public int getPort() {
    return this.port;
  }

  public File getWorkingDir() {
    return this.workingDir;
  }
}