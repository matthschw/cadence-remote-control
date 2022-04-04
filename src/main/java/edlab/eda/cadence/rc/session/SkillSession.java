package edlab.eda.cadence.rc.session;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.data.SkillDataobject;
import net.sf.expectit.Result;
import net.sf.expectit.matcher.Matcher;
import net.sf.expectit.matcher.Matchers;

/**
 * A session which is capable of executing SKill commands
 */
public abstract class SkillSession implements SkillEvaluationEnvironment {

  // Context file
  public static final String CONTEXT_RESOURCE = "cxt/64bit/EDcdsRC.cxt";
  // Skill file
  public static final String SKILL_RESOURCE = "skill/EDcdsRC.il";

  protected static final int MAX_CMD_LENGTH = 7500;

  // Prompt in Cadence Session
  protected static final String PROMPT_ENV_VAR = "ED_CDS_INIT_PROMPT";
  protected static final String PROMPT_DEFAULT = ">";

  // Timeout
  protected long timeoutDuration = 1;
  protected TimeUnit timeoutTimeUnit = TimeUnit.HOURS;
  protected long timeout_ms = this.timeoutTimeUnit
      .toMillis(this.timeoutDuration);

  protected String prompt = PROMPT_DEFAULT;
  protected Matcher<Result> nextCommand;

  // Identifiers in Cadence Session
  public static final String CDS_RC_GLOBAL = "EDcdsRC";
  public static final String CDS_RC_SESSIONS = "session";
  public static final String CDS_RC_SESSION = "main";
  public static final String CDS_RC_RETURN_VALUES = "retVals";

  // Temporary file name prefix and suffixes
  public static final String TMP_FILE_PREFIX = "ed_cds_rc";
  public static final String TMP_SKILL_FILE_SUFFIX = ".il";
  public static final String TMP_SKILLPP_FILE_SUFFIX = ".ils";
  public static final String TMP_CXT_FILE_SUFFIX = ".cxt";

  // XML
  protected static final Matcher<Result> XML_MATCH = Matchers
      .regexp("<<1([\\S\\s]+)2>>");

  // Identifiers in DPL from Cadence tool
  public static final String ID_VALID = "valid";
  public static final String ID_DATA = "data";
  public static final String ID_FILE = "file";
  public static final String ID_ERROR = "error";

  protected SkillSession() {

    final String content = System.getenv(PROMPT_ENV_VAR);

    if (content instanceof String) {
      this.prompt = content;
    }

    this.nextCommand = Matchers.regexp("\n" + this.prompt);
  }

  /**
   * Specify the prompt for return value recognition. Can be done only, when the
   * session is not active. When this method is not used, the prompt is
   * extracted of <code>ED_CDS_INIT_PROMPT</code>. When this environment
   * variable is not set, <code>&gt;</code> is used.
   * 
   * @param prompt Prompt to be used (when not specified <code>&gt;</code> is
   *               used).
   * @return <code>true</code> when change is valid, <code>false</code>
   *         otherwise
   */
  public boolean setPrompt(final String prompt) {

    if (this.isActive()) {
      return false;
    } else {
      this.prompt = prompt;
      this.nextCommand = Matchers.regexp("\n" + this.prompt);
      return true;
    }
  }

  /**
   * Set the timeout for the session. The session will wait the here provided
   * amount of time until the evaluation of a command is finished
   *
   * @param timeoutDuration duration
   * @param timeoutTimeUnit Time Unit to be used
   * @return this when chanhing was valid, <code>null</code> otherwise
   */
  public SkillSession setTimeout(final long timeoutDuration,
      final TimeUnit timeoutTimeUnit) {

    if ((timeoutDuration > 0) && (timeoutTimeUnit instanceof TimeUnit)) {
      this.timeoutDuration = timeoutDuration;
      this.timeoutTimeUnit = timeoutTimeUnit;
      this.timeout_ms = this.timeoutTimeUnit.toMillis(this.timeoutDuration);
      return this;
    } else {
      return null;
    }
  }

  /**
   * Get the timeout duration
   * 
   * @return watchdogTimeoutDuration
   */
  public long getTimeoutDuration() {
    return this.timeoutDuration;
  }

  /**
   * Get the timeout time unit
   * 
   * @return timeoutTimeUnit
   */
  public TimeUnit getTimeUnit() {
    return this.timeoutTimeUnit;
  }

  /**
   * Start the session
   *
   * @return this
   * @throws UnableToStartSession      When starting of the subprocess failed
   * @throws EvaluationFailedException When the setup of the session failed
   */
  public abstract SkillSession start()
      throws UnableToStartSession, EvaluationFailedException;

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
   * @param parent  Parent thread that is used by the watchdog for
   *                identification whether the parent thread is finished
   * @return Skill data-object that is returned from the session
   * @throws UnableToStartSession                When the session could not be
   *                                             started
   * @throws EvaluationFailedException           When evaluation of the command
   *                                             failed
   * @throws InvalidDataobjectReferenceExecption When the command contains data
   *                                             that is is not referenced in
   *                                             this session
   */
  public abstract SkillDataobject evaluate(SkillCommand command, Thread parent)
      throws UnableToStartSession, EvaluationFailedException,
      InvalidDataobjectReferenceExecption;

  /**
   * Stop the session
   * 
   * @return this
   */
  public abstract SkillSession stop();

  @Override
  protected void finalize() {
    this.stop();
  }

  @Override
  public File getResourcePath(final String fileName, final String suffix) {
    return this.getResourcePathFromAscii(fileName, suffix);
  }

  @Override
  public File getResourcePathFromBinary(final String fileName, final String suffix) {

    final InputStream stream = this.getClass().getClassLoader()
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
    } catch (final IOException e) {
      file = null;
    }

    return file;
  }

  @Override
  public File getResourcePathFromAscii(final String fileName, final String suffix) {

    final InputStream stream = this.getClass().getClassLoader()
        .getResourceAsStream(fileName);

    final Scanner scanner = new Scanner(stream);

    FileWriter writer;

    try {
      final File file = File.createTempFile(TMP_FILE_PREFIX, suffix);
      writer = new FileWriter(file);
      while (scanner.hasNextLine()) {
        writer.append(scanner.nextLine());
      }
      writer.close();
      scanner.close();
      return file;

    } catch (final IOException e1) {
    }
    scanner.close();
    return null;
  }

  @Override
  public String getResourceFromAscii(final String fileName) {

    final InputStream stream = this.getClass().getClassLoader()
        .getResourceAsStream(fileName);

    final Scanner scanner = new Scanner(stream);
    final StringBuilder builder = new StringBuilder();

    boolean first = true;

    while (scanner.hasNext()) {

      if (first) {
        first = false;
      } else {
        builder.append("\n");
      }

      builder.append(scanner.next());
    }
    scanner.close();

    return builder.toString();
  }
}