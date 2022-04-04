package edlab.eda.cadence.rc.session;

import java.io.File;

public final class UnableToStartInteractiveSession extends UnableToStartSession {

  private static final long serialVersionUID = 6251560870791473512L;

  private final String command;
  private final File workingDir;

  public UnableToStartInteractiveSession(final String command, final File workingDir) {

    super("Unable to start session with command \"" + command
        + "\" in directory " + workingDir.getAbsolutePath());

    this.command = command;
    this.workingDir = workingDir;
  }

  public String getCommand() {
    return this.command;
  }

  public File getWorkingDir() {
    return this.workingDir;
  }
}
