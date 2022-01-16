package edlab.eda.cadence.rc.session;

import java.io.File;

import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.data.SkillDataobject;

public interface CanExecuteSkillCommands {

  /**
   * Evaluate a Skill command in the session
   *
   * @param command Command to be evaluated
   * @return Skill data-object that is returned from the session
   * @throws UnableToStartSession                When the session could not be
   *                                             started
   * @throws EvaluationFailedException           When evaluation of the command
   *                                             failed
   * @throws InvalidDataobjectReferenceExecption When the command contains data
   *                                             that is is not referenced in
   *                                             this session
   */
  public SkillDataobject evaluate(SkillCommand command)
      throws UnableToStartSession, EvaluationFailedException,
      InvalidDataobjectReferenceExecption;

  /**
   * Get the path to a resource
   *
   * @param fileName File name of the resource
   * @param suffix   Suffix of the file which will be generated
   * @return Path to the resource
   */
  public File getResourcePath(String fileName, String suffix);
}