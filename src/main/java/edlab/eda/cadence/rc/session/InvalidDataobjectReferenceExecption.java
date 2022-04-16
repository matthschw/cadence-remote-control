package edlab.eda.cadence.rc.session;

import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.data.SkillComplexDataobject;

/**
 * This exception is thrown when a command references a
 * {@link SkillComplexDataobject}, that is not defined in the session
 *
 */
public class InvalidDataobjectReferenceExecption extends Exception {

  private static final long serialVersionUID = 5007664766986732239L;

  private final SkillCommand command;
  private final SkillSession session;

  /**
   * Create Exception
   *
   * @param command Command which contains invalid data-object references
   * @param session Session where the command cannot be executed
   */
  public InvalidDataobjectReferenceExecption(final SkillCommand command,
      final SkillSession session) {

    super("The command \"" + command.toSkill()
        + "\" contains references that are not defined in session= "
        + session.toString());

    this.command = command;
    this.session = session;
  }

  /**
   * Get the command that contains the invalid reference
   * 
   * @return command
   */
  public SkillCommand getCommand() {
    return this.command;
  }

  /**
   * Session that does not contain a reference to the object
   * 
   * @return session
   */
  public SkillSession getSession() {
    return this.session;
  }
}