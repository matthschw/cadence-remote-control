package edlab.eda.cadence.rc.session;

import java.io.File;

/**
 * Exception when a {@link SkillSession} cannot be started
 */
public class UnableToStartSession extends Exception {

  private static final long serialVersionUID = 5809804414514574838L;

  public UnableToStartSession(String message) {
    super(message);
  }

  public UnableToStartSession(String command, File workingDir) {
    super("Unable to start session with command \"" + command
        + "\" in directory " + workingDir.getAbsolutePath());
  }

  public UnableToStartSession(String command, File workingDir, File logfile) {
    super(
        "Unable to start session with command \"" + command + "\" in directory "
            + workingDir.getAbsolutePath() + ". " + "\nPlease investigate \""
            + logfile.getAbsolutePath() + "\" for detailed information.");
  }

  public UnableToStartSession(int port) {
    super("Unable to connect to session with port " + port);
  }
}