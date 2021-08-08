package edlab.eda.cadence.rc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import edlab.eda.cadence.rc.api.GenericSkillCommandTemplates;
import edlab.eda.cadence.rc.api.IncorrectSyntaxException;
import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.data.SkillDisembodiedPropertyList;
import edlab.eda.cadence.rc.data.SkillString;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import net.sf.expectit.Result;
import net.sf.expectit.matcher.Matcher;
import net.sf.expectit.matcher.Matchers;

/**
 * Session for communicate with an interactive session in Cadence-SKILL syntax
 *
 */
public class SkillSession {

  private Process process = null;
  private Expect expect = null;

  private String command;
  private File workingDir;

  private long timeoutDuration = 1;
  private TimeUnit timeoutTimeUnit = TimeUnit.HOURS;
  private Date lastActivity = null;

  private SkillSessionWatchdog watchdog;

  private static final String DEFAULT_COMMAND = "virtuoso -nograph";

  private static final File DEFAULT_WORKING_DIR = new File("./");

  private static final String SKILL_RESOURCE = "EDcdsRemoteControl.il";

  @SuppressWarnings("unused")
  private static final String CONTEXT_RESOURCE = "64bit/EDcdsRC.cxt";

  private static final int MAX_CMD_LENGTH = 7500;

  // Prompt in Cadence Session
  private static final String PROMPT = ">";
  private static final String PROMPT_REGEX = "\n>";
  private static final Matcher<Result> NEXT_COMMAND = Matchers
      .regexp(PROMPT_REGEX);

  // Identifiers in Cadence Session
  public static final String CDS_RC_GLOBAL = "EDcdsRC";
  public static final String CDS_RC_SESSIONS = "session";
  public static final String CDS_RC_SESSION = "main";
  public static final String CDS_RC_RETURN_VALUES = "retVals";

  // Temporary file name prefix and suffix
  public static final String TMP_FILE_PREFIX = "ed_cds_rc";
  public static final String TMP_SKILL_FILE_SUFFIX = ".il";

  // XML
  private static final Matcher<Result> XML_MATCH = Matchers
      .regexp("<<1([\\S\\s]+)2>>");

  // Identifiers in DPL from Cadence tool
  public static final String ID_VALID = "valid";
  public static final String ID_DATA = "data";
  public static final String ID_FILE = "file";
  public static final String ID_ERROR = "error";

  /**
   * Create a Session
   */
  public SkillSession() {
    this.command = DEFAULT_COMMAND;
    this.workingDir = DEFAULT_WORKING_DIR.getAbsoluteFile();
  }

  /**
   * Create a Session
   * 
   * @param workingDir directory where the session is started
   */
  public SkillSession(File workingDir) {
    this.command = DEFAULT_COMMAND;
    this.workingDir = workingDir.getAbsoluteFile();
  }

  /**
   * Set the timeout for the session. The session will terminate when no action
   * is performed (command is sent) or the session does not respond in the
   * specified time.
   * 
   * @param duration Timeout
   * @param unit     Time Unit to be used
   * @return this
   */
  public SkillSession setTimeout(long duration, TimeUnit unit) {
    this.timeoutDuration = duration;
    this.timeoutTimeUnit = unit;
    return this;
  }

  /**
   * Specify the command to be used to invoke the session
   * 
   * @param command Start-command of the Cadence Tool
   * @return this
   */
  public SkillSession setCommand(String command) {
    this.command = command;
    return this;
  }

  /**
   * Start the session
   * 
   * @return this
   * @throws UnableToStartSkillSession When starting of the subprocess failed
   */
  public SkillSession start() throws UnableToStartSkillSession {

    if (!isActive()) {
      try {
        this.process = Runtime.getRuntime().exec(this.command + "\n", null,
            workingDir);

      } catch (IOException e) {
        System.err.println(
            "Unable to execute session" + " with error:\n" + e.getMessage());

        throw new UnableToStartSkillSession(this.command, workingDir);
      }

      try {
        expect = new ExpectBuilder().withInputs(this.process.getInputStream())
            .withOutput(this.process.getOutputStream()).withExceptionOnFailure()
            .build().withTimeout(this.timeoutDuration, this.timeoutTimeUnit);
        expect.expect(NEXT_COMMAND);

        try {
          this.expect.send(GenericSkillCommandTemplates
              .getTemplate(GenericSkillCommandTemplates.SET_PROMPTS)
              .build(new EvaluableToSkill[] { new SkillString(PROMPT),
                  new SkillString(PROMPT) })
              .toSkill() + "\n");
        } catch (IncorrectSyntaxException e) {
        }

        expect.expect(NEXT_COMMAND);

        File skillControlApi = getResourcePath(SKILL_RESOURCE,
            TMP_SKILL_FILE_SUFFIX);

        try {

          this.expect.send(GenericSkillCommandTemplates
              .getTemplate(GenericSkillCommandTemplates.LOAD)
              .build(new SkillString(skillControlApi.getAbsolutePath()))
              .toSkill() + "\n");
        } catch (IncorrectSyntaxException e) {
        }

        expect.expect(NEXT_COMMAND);

        skillControlApi.delete();

        this.lastActivity = new Date();
        this.watchdog = new SkillSessionWatchdog(this, this.timeoutDuration,
            this.timeoutTimeUnit);

        this.watchdog.start();

      } catch (IOException e) {

        System.err.println(
            "Unable to execute expect" + " with error:\n" + e.getMessage());

        this.process.destroy();

        throw new UnableToStartSkillSession(this.command, workingDir);
      }

      return this;
    } else {
      return this;
    }
  }

  /**
   * Check if the session is active
   * 
   * @return <code>true</code> when the session is active, <code>false</code>
   *         otherwise
   */
  public boolean isActive() {
    if (process == null || !process.isAlive()) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Evaluate a Skill Command in the session
   * 
   * @param command Command to be evaluated
   * @return Skill data-object that is returned from the Session
   * @throws MaxCommandLengthExeeded   When the maximal command length is
   *                                   exceeded
   * @throws UnableToStartSkillSession When the session could not be started
   * @throws EvaluationFailedException When evaluation of the command failed
   */
  public SkillDataobject evaluate(SkillCommand command)
      throws MaxCommandLengthExeeded, UnableToStartSkillSession,
      EvaluationFailedException {

    if (!isActive()) {
      start();
    }

    SkillDataobject data = null;

    this.watchdog.kill();
    this.watchdog = null;

    if (isActive()) {

      SkillCommand outer = null;

      try {
        outer = GenericSkillCommandTemplates
            .getTemplate(GenericSkillCommandTemplates.ED_CDS_RC_FOMAT_COMMAND)
            .build(GenericSkillCommandTemplates
                .getTemplate(GenericSkillCommandTemplates.ERRSET)
                .build(command));
      } catch (IncorrectSyntaxException e) {
        // cannot occur
      }

      String skillCommand = outer.toSkill();

      // store command in a file when MAX_CMD_LENGTH exceeded
      if (skillCommand.length() > MAX_CMD_LENGTH) {

        try {

          File file = File.createTempFile(TMP_FILE_PREFIX,
              TMP_SKILL_FILE_SUFFIX);

          FileWriter writer = new FileWriter(file);
          writer.write(command.toSkill());
          writer.close();

          try {

            outer = GenericSkillCommandTemplates.getTemplate(
                GenericSkillCommandTemplates.ED_CDS_RC_EXECUTE_COMMAND_FROM_FILE)
                .build(new SkillString(file.getAbsolutePath()));
            skillCommand = outer.toSkill();
          } catch (IncorrectSyntaxException e) {
            // cannot occur
          }
        } catch (IOException e) {
          // "/tmp" is always writable
        }
      }

      String xml = communicate(skillCommand);

      SkillDataobject obj = SkillDataobject.getSkillDataobjectFromXML(this,
          xml);

      try {

        SkillDisembodiedPropertyList top = (SkillDisembodiedPropertyList) obj;

        if (top.getProperty(ID_VALID).isTrue()) {

          if (top.getKeys().contains(ID_FILE)) {

            SkillString filePath = (SkillString) top.getProperty(ID_FILE);

            File dataFile = new File(filePath.getString());

            xml = new String(Files.readAllBytes(dataFile.toPath()),
                StandardCharsets.US_ASCII);

            top = (SkillDisembodiedPropertyList) SkillDataobject
                .getSkillDataobjectFromXML(this, xml);

            dataFile.delete();
          }

          data = top.getProperty(ID_DATA);

        } else {

          SkillString errorstring = (SkillString) top.getProperty(ID_ERROR);

          throw new EvaluationFailedException(skillCommand,
              errorstring.getString());
        }
      } catch (Exception e) {
        throw new EvaluationFailedException(skillCommand, xml);
      }
    } else {
      throw new UnableToStartSkillSession(this.command, workingDir);
    }

    this.lastActivity = new Date();
    this.watchdog = new SkillSessionWatchdog(this, this.timeoutDuration,
        this.timeoutTimeUnit);
    this.watchdog.start();

    return data;
  }

  /**
   * Stop the session
   */
  public void stop() {

    this.watchdog.kill();
    this.watchdog = null;

    try {
      communicate(SkillCommand.buildCommand(GenericSkillCommandTemplates
          .getTemplate(GenericSkillCommandTemplates.EXIT)).toSkill());
    } catch (IncorrectSyntaxException e) {
    }

    try {
      this.expect.close();
    } catch (IOException e) {
    }

    if (this.process != null) {
      this.process.destroyForcibly();
    }

    this.process = null;

  }

  private String communicate(String cmd) {

    String retval = null;

    try {

      this.expect.send(cmd + "\n");
      retval = expect.expect(XML_MATCH).group(1);

    } catch (Exception e) {
    }

    return retval;
  }

  /**
   * Get date of last activity in the session
   * 
   * @return date of last activity in the session
   */
  Date getLastActivity() {
    return this.lastActivity;
  }

  /**
   * Get path to a resource
   * 
   * @param fileName File Name of the Resource
   * @return Path to the resource
   */
  private File getResourcePath(String fileName, String suffix) {

    InputStream stream = getClass().getClassLoader()
        .getResourceAsStream(fileName);

    byte[] data;
    File file = null;

    try {
      data = stream.readAllBytes();
      stream.close();
      file = File.createTempFile(TMP_FILE_PREFIX, suffix);
      Files.write(file.toPath(), data);
    } catch (IOException e) {
      file = null;
    }

    return file;
  }
}