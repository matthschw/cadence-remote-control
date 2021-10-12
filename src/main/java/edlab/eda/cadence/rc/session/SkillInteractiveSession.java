package edlab.eda.cadence.rc.session;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
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

/**
 * Session for communication with an interactive session using Cadence Skill
 * syntax
 *
 */
public class SkillInteractiveSession extends SkillSession {

  private Process process = null;
  private Expect expect = null;

  private String command;
  private File workingDir;

  private long timeoutDuration = 1;
  private TimeUnit timeoutTimeUnit = TimeUnit.HOURS;
  private Date lastActivity = null;

  private SkillSessionWatchdog watchdog;

  private static final String DEFAULT_COMMAND = "virtuoso -nograph";

  private static final File DEFAULT_WORKING_DIR = new File("");

  /**
   * Create a Session
   */
  public SkillInteractiveSession() {
    this.command = DEFAULT_COMMAND;
    this.workingDir = DEFAULT_WORKING_DIR.getAbsoluteFile();
  }

  /**
   * Create a Session
   * 
   * @param workingDir directory where the session is started
   */
  public SkillInteractiveSession(File workingDir) {
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
  public SkillInteractiveSession setTimeout(long duration, TimeUnit unit) {
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
  public SkillInteractiveSession setCommand(String command) {
    this.command = command;
    return this;
  }

  @Override
  public SkillInteractiveSession start() throws UnableToStartSkillSession {

    if (!isActive()) {

      try {

        // List<String> command = new LinkedList<String>();

        // command.add("virtuoso");
        // command.add("-nograph");

        // ProcessBuilder builder = new ProcessBuilder(command);
        // builder.directory(this.workingDir);
        // builder.inheritIO();
        // builder.redirectInput(Redirect.INHERIT);

        this.process = Runtime.getRuntime().exec(this.command + "\n", null,
            this.workingDir);

        // this.process = builder.start();

      } catch (IOException e) {

        System.err.println(
            "Unable to execute session" + " with error:\n" + e.getMessage());

        throw new UnableToStartSkillSession(this.command, workingDir);
      }

      // add shutdown hook for process
      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          try {
            process.destroyForcibly();
          } catch (Exception e) {
          }
        }
      });

      try {

        expect = new ExpectBuilder().withInputs(this.process.getInputStream())
            .withOutput(this.process.getOutputStream()).withExceptionOnFailure()
            .build().withTimeout(10, TimeUnit.DAYS);

        expect.expect(SkillSession.NEXT_COMMAND);

        try {

          this.expect
              .send(GenericSkillCommandTemplates
                  .getTemplate(GenericSkillCommandTemplates.SET_PROMPTS)
                  .buildCommand(new EvaluableToSkill[] {
                      new SkillString(SkillSession.PROMPT),
                      new SkillString(SkillSession.PROMPT) })
                  .toSkill() + "\n");
          
        } catch (IncorrectSyntaxException e) {
        }

        expect.expect(SkillSession.NEXT_COMMAND);

        File skillControlApi = this.getResourcePath(SkillSession.SKILL_RESOURCE,
            SkillSession.TMP_SKILL_FILE_SUFFIX);

        try {

          this.expect.send(GenericSkillCommandTemplates
              .getTemplate(GenericSkillCommandTemplates.LOAD)
              .buildCommand(new SkillString(skillControlApi.getAbsolutePath()))
              .toSkill() + "\n");
        } catch (IncorrectSyntaxException e) {
        }

        expect.expect(SkillSession.NEXT_COMMAND);

        skillControlApi.delete();

        this.lastActivity = new Date();

        if (this.timeoutDuration > 0) {
          this.watchdog = new SkillSessionWatchdog(this, this.timeoutDuration,
              this.timeoutTimeUnit, Thread.currentThread());
          this.watchdog.start();
        }

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

  @Override
  public boolean isActive() {
    if (process == null || !process.isAlive()) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  public SkillDataobject evaluate(SkillCommand command)
      throws UnableToStartSkillSession, EvaluationFailedException,
      InvalidDataobjectReferenceExecption {

    if (!isActive()) {
      this.start();
    }

    SkillDataobject data = null;

    if (this.watchdog instanceof SkillSessionWatchdog) {
      this.watchdog.kill();
      this.watchdog = null;
    }

    if (isActive()) {

      if (!command.canBeUsedInSession(this)) {
        throw new InvalidDataobjectReferenceExecption(command, this);
      }

      SkillCommand outer = null;

      try {
        outer = GenericSkillCommandTemplates
            .getTemplate(GenericSkillCommandTemplates.ED_CDS_RC_FOMAT_COMMAND)
            .buildCommand(GenericSkillCommandTemplates
                .getTemplate(GenericSkillCommandTemplates.ERRSET)
                .buildCommand(command));
      } catch (IncorrectSyntaxException e) {
        // cannot occur
      }

      String skillCommand = outer.toSkill();

      // store command in a file when MAX_CMD_LENGTH exceeded
      if (skillCommand.length() > SkillSession.MAX_CMD_LENGTH) {

        try {

          File file = File.createTempFile(SkillSession.TMP_FILE_PREFIX,
              SkillSession.TMP_SKILL_FILE_SUFFIX);

          FileWriter writer = new FileWriter(file);
          writer.write(command.toSkill());
          writer.close();

          try {

            outer = GenericSkillCommandTemplates.getTemplate(
                GenericSkillCommandTemplates.ED_CDS_RC_EXECUTE_COMMAND_FROM_FILE)
                .buildCommand(new SkillString(file.getAbsolutePath()));
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

        if (top.get(SkillSession.ID_VALID).isTrue()) {

          if (top.containsKey(SkillSession.ID_FILE)) {

            SkillString filePath = (SkillString) top.get(SkillSession.ID_FILE);

            File dataFile = new File(filePath.getString());

            xml = new String(Files.readAllBytes(dataFile.toPath()),
                StandardCharsets.US_ASCII);

            top = (SkillDisembodiedPropertyList) SkillDataobject
                .getSkillDataobjectFromXML(this, xml);

            dataFile.delete();
          }

          data = top.get(SkillSession.ID_DATA);

        } else {

          SkillString errorstring = (SkillString) top
              .get(SkillSession.ID_ERROR);

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

    if (this.timeoutDuration > 0) {
      this.watchdog = new SkillSessionWatchdog(this, this.timeoutDuration,
          this.timeoutTimeUnit, Thread.currentThread());
      this.watchdog.start();
    }

    return data;
  }

  @Override
  public void stop() {

    if (watchdog instanceof SkillSessionWatchdog) {
      this.watchdog.kill();
      this.watchdog = null;
    }

    try {
      communicate(GenericSkillCommandTemplates
          .getTemplate(GenericSkillCommandTemplates.EXIT).buildCommand()
          .toSkill());
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

  /**
   * Execute a Skill command
   * 
   * @param cmd Skill command to be executed
   * @return XML from Skill session
   */
  private String communicate(String cmd) {

    String retval = null;

    try {

      this.expect.send(cmd + "\n");
      retval = expect.expect(SkillSession.XML_MATCH).group(1);

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

  @Override
  public String toString() {
    return "[CMD=" + this.command + " DIR=" + this.workingDir + " PID="
        + this.getPid() + "]";
  }

  /**
   * Get the PID of the subprocess
   * 
   * @return PID of subprocess if running, <code>-1</code> otherwise
   */
  public long getPid() {

    long pid = -1;

    try {
      if (this.process.getClass().getName().equals("java.lang.UNIXProcess")) {
        Field f = this.process.getClass().getDeclaredField("pid");
        f.setAccessible(true);
        pid = f.getLong(this.process);
        f.setAccessible(false);
      }
    } catch (Exception e) {
      pid = -1;
    }

    return pid;
  }
}