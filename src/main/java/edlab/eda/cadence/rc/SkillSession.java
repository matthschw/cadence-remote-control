package edlab.eda.cadence.rc;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import edlab.eda.cadence.rc.api.GenericSkillCommandTemplates;
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

  private Matcher<Result> nextCommand = NEXT_COMMAND;

  private static final String DEFAULT_COMMAND = "virtuoso -nograph";
  private static final String PATH_TO_SKILL = "./src/main/skill/EDcdsRemoteControl.il";

  private static final int MAX_CMD_LENGTH = 8000;

  private static final String PROMPT = "[ED-CDS-RC]";
  private static final String PROMPT_REGEX = "\\[ED-CDS-RC\\]";

  private static final File DEFAULT_WORKING_DIR = new File("./");
  private static final Matcher<Result> NEXT_COMMAND = Matchers.regexp("\n>");

  public static final String CDS_RC_GLOBAL = "EDcdsRC";
  public static final String CDS_RC_SESSIONS = "session";
  public static final String CDS_RC_SESSION = "main";
  public static final String CDS_RC_RETURN_VALUES = "retVals";

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
   * @param command
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
        expect.expect(nextCommand);

        this.nextCommand = Matchers.regexp("\n" + PROMPT_REGEX);

        try {
          this.expect.send(SkillCommand.buildCommand(
              GenericSkillCommandTemplates
                  .getTemplate(GenericSkillCommandTemplates.SET_PROMPTS),
              new EvaluateableToSkill[] { new SkillString(PROMPT),
                  new SkillString(PROMPT) })
              .toSkill() + "\n");
        } catch (IncorrectSyntaxException e) {
        }

        expect.expect(nextCommand);

        try {
          this.expect.send(SkillCommand.buildCommand(
              GenericSkillCommandTemplates
                  .getTemplate(GenericSkillCommandTemplates.LOAD),
              new SkillString(PATH_TO_SKILL)).toSkill() + "\n");
        } catch (IncorrectSyntaxException e) {
        }
        expect.expect(nextCommand);

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
   * @return Skill Dataobject that is returned from the Session
   * @throws MaxCommandLengthExeeded
   * @throws UnableToStartSkillSession
   * @throws EvaluationFailedException
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
        outer = SkillCommand.buildCommand(
            GenericSkillCommandTemplates.getTemplate(
                GenericSkillCommandTemplates.ED_CDS_RC_FOMAT_COMMAND),
            SkillCommand.buildCommand(GenericSkillCommandTemplates
                .getTemplate(GenericSkillCommandTemplates.ERRSET), command));
      } catch (IncorrectSyntaxException e1) {
      }

      String skillCommand = outer.toSkill();

      if (skillCommand.length() > MAX_CMD_LENGTH) {
        throw new MaxCommandLengthExeeded(skillCommand, MAX_CMD_LENGTH);
      }

      String xml = communicate(skillCommand);
      xml = xml.substring(0, xml.length() - 1);

      SkillDataobject obj = SkillDataobject.getSkillDataobjectFromXML(this,
          xml);

      try {
        SkillDisembodiedPropertyList top = (SkillDisembodiedPropertyList) obj;

        if (top.getProperty("valid").isTrue()) {

          if (top.getKeys().contains("file")) {

            SkillString filePath = (SkillString) top.getProperty("file");

            File dataFile = new File(filePath.getString());

            xml = new String(Files.readAllBytes(dataFile.toPath()),
                StandardCharsets.US_ASCII);

            data = SkillDataobject.getSkillDataobjectFromXML(this, xml);
          } else {
            data = top.getProperty("data");
          }

        } else {

          SkillString errorstring = (SkillString) top.getProperty("error");

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
      retval = expect.expect(this.nextCommand).getBefore();

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
}