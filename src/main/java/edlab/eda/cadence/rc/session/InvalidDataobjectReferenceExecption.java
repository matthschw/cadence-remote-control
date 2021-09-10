package edlab.eda.cadence.rc.session;

import edlab.eda.cadence.rc.api.SkillCommand;

public class InvalidDataobjectReferenceExecption extends Exception {

  private static final long serialVersionUID = 5007664766986732239L;

  /**
   * Create Exception
   * 
   * @param command Command which contains invalid data-object references
   * @param session Session where the command cannot be executed
   */
  public InvalidDataobjectReferenceExecption(SkillCommand command,
      SkillSession session) {

    super("The command " + command.toSkill()
        + " contains references that are not defined in session= "
        + session.toString());
  }

}
