package edlab.eda.cadence.rc.session;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.data.SkillDataobject;
import net.sf.expectit.Result;
import net.sf.expectit.matcher.Matcher;
import net.sf.expectit.matcher.Matchers;

public abstract class SkillSession {

  protected static final String SKILL_RESOURCE = "EDcdsRemoteControl.il";

  @SuppressWarnings("unused")
  private static final String CONTEXT_RESOURCE = "64bit/EDcdsRC.cxt";

  protected static final int MAX_CMD_LENGTH = 7500;

  // Prompt in Cadence Session
  protected static final String PROMPT = ">";
  protected static final String PROMPT_REGEX = "\n>";
  protected static final Matcher<Result> NEXT_COMMAND = Matchers
      .regexp(PROMPT_REGEX);

  // Identifiers in Cadence Session
  public static final String CDS_RC_GLOBAL = "EDcdsRC";
  public static final String CDS_RC_SESSIONS = "session";
  public static final String CDS_RC_SESSION = "main";
  public static final String CDS_RC_RETURN_VALUES = "retVals";

  // Temporary file name prefix and suffix
  public static final String TMP_FILE_PREFIX = "ed_cds_rc";
  public static final String TMP_SKILL_FILE_SUFFIX = ".il";
  public static final String TMP_SKILLPP_FILE_SUFFIX = ".ils";

  // XML
  protected static final Matcher<Result> XML_MATCH = Matchers
      .regexp("<<1([\\S\\s]+)2>>");

  // Identifiers in DPL from Cadence tool
  public static final String ID_VALID = "valid";
  public static final String ID_DATA = "data";
  public static final String ID_FILE = "file";
  public static final String ID_ERROR = "error";

  /**
   * Start the session
   * 
   * @return this
   * @throws UnableToStartSkillSession When starting of the subprocess failed
   */
  public abstract SkillSession start() throws UnableToStartSkillSession;

  /**
   * Check if the session is active
   * 
   * @return <code>true</code> when the session is active, <code>false</code>
   *         otherwise
   */
  public abstract boolean isActive();

  /**
   * Evaluate a Skill command in the session
   * 
   * @param command Command to be evaluated
   * @return Skill data-object that is returned from the session
   * @throws UnableToStartSkillSession           When the session could not be
   *                                             started
   * @throws EvaluationFailedException           When evaluation of the command
   *                                             failed
   * @throws InvalidDataobjectReferenceExecption When the command contains data
   *                                             that is is not referenced in
   *                                             this session
   */
  public abstract SkillDataobject evaluate(SkillCommand command)
      throws UnableToStartSkillSession, EvaluationFailedException,
      InvalidDataobjectReferenceExecption;

  /**
   * Stop the session
   */
  public abstract void stop();

  /**
   * Get the path to a resource
   * 
   * @param fileName File name of the resource
   * @param suffix   Suffix of the file which will be generated
   * @return Path to the resource
   */
  public File getResourcePath(String fileName, String suffix) {

    InputStream stream = getClass().getClassLoader()
        .getResourceAsStream(fileName);

    byte[] data;
    File file = null;

    try {
      data = new byte[stream.available()];
      stream.read(data);
      stream.close();
      file = File.createTempFile(TMP_FILE_PREFIX, suffix);
      Files.write(file.toPath(), data);
      file.deleteOnExit();
    } catch (IOException e) {
      file = null;
    }

    return file;
  }
}
