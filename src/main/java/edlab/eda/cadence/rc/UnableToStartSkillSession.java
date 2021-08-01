package edlab.eda.cadence.rc;

import java.io.File;

public class UnableToStartSkillSession extends Exception {

  private static final long serialVersionUID = -5809804414514574838L;

  public UnableToStartSkillSession(String command) {
    super("Unable to start skill-session with command " + command);
  }

  public UnableToStartSkillSession(String command, File workingDir) {
    super("Unable to start skill-session with command " + command
        + " in working-dir " + workingDir.getAbsolutePath());
  }
}
