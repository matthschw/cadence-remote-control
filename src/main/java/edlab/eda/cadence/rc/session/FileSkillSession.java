package edlab.eda.cadence.rc.session;

import java.io.File;

import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.data.SkillList;
import edlab.eda.cadence.rc.data.SkillNativeDataobject;

/**
 * Session of Skill data that is loaded froma file
 */
public final class FileSkillSession extends SkillSession {

  private File file;
  private SkillNativeDataobject obj;

  private FileSkillSession() {
  }

  /**
   * Init a {@link FileSkillSession}
   * 
   * @param file File with Skill data
   * @return session
   */
  public static FileSkillSession init(final File file) {

    final FileSkillSession session = new FileSkillSession();

    session.file = file;
    session.obj = (SkillNativeDataobject) SkillDataobject
        .getSkillDataobjectFromXML(session, file);

    return session;
  }

  /**
   * Get file
   * 
   * @return file
   */
  public File getFile() {
    return this.file;
  }

  /**
   * Get Skill data
   * 
   * @return data
   */
  public SkillNativeDataobject getSkillData() {
    return this.obj;
  }

  @Override
  public SkillDataobject evaluate(final SkillCommand command)
      throws UnableToStartSession, EvaluationFailedException,
      InvalidDataobjectReferenceExecption {
    return null;
  }

  @Override
  public SkillSession start()
      throws UnableToStartSession, EvaluationFailedException {
    return this;
  }

  @Override
  public boolean isActive() {
    return false;
  }

  @Override
  public SkillDataobject evaluate(final SkillCommand command, final Thread parent)
      throws UnableToStartSession, EvaluationFailedException,
      InvalidDataobjectReferenceExecption {
    return new SkillList();
  }

  @Override
  public SkillSession stop() {
    return this;
  }
}