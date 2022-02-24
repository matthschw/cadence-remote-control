package edlab.eda.cadence.rc.session;

import java.io.File;

public class UnableToStartInteractiveSession extends UnableToStartSession {

  private static final long serialVersionUID = 6251560870791473512L;

  private String command;
  private File workingDir;

  public UnableToStartInteractiveSession(String command, File workingDir) {

    super("Unable to start session with command \"" + command
        + "\" in directory " + workingDir.getAbsolutePath());

    this.command = command;
    this.workingDir = workingDir;
  }

  public String getCommand() {
    return command;
  }

  public File getWorkingDir() {
    return workingDir;
  }
}
