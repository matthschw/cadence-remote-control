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
import edlab.eda.cadence.rc.data.SkillList;
import edlab.eda.cadence.rc.data.SkillString;
import edlab.eda.cadence.rc.data.SkillSymbol;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import net.sf.expectit.matcher.Matchers;

/**
 * Session for communication with an interactive session using Cadence Skill
 * syntax
 */
public final class SkillInteractiveSession extends SkillSession {

  private Process process = null;
  private Expect expect = null;

  private String command;
  private final File workingDir;

  private static final String DEFAULT_COMMAND = "virtuoso -nograph";

  private static final File DEFAULT_WORKING_DIR = new File("");

  // Watchdog
  private SkillSessionWatchdog watchdog;
  private Date lastActivity = null;
  private long watchdogTimeoutDuration = 10;
  private TimeUnit watchdogTimeoutTimeUnit = TimeUnit.HOURS;

  /**
   * Create a Session
   */
  public SkillInteractiveSession() {
    super();
    this.command = DEFAULT_COMMAND;
    this.workingDir = DEFAULT_WORKING_DIR.getAbsoluteFile();
  }

  /**
   * Create a Session
   *
   * @param workingDir directory where the session is started
   */
  public SkillInteractiveSession(final File workingDir) {
    super();
    this.command = DEFAULT_COMMAND;
    this.workingDir = workingDir.getAbsoluteFile();
  }

  /**
   * Specify the command to be used to invoke the session
   *
   * @param command Start command of the Cadence tool
   *
   * @return this
   */
  public SkillInteractiveSession setCommand(final String command) {
    this.command = command;
    return this;
  }

  /**
   * Set the timeout of the watchdog. The watchdog will close the
   * {@link SkillInteractiveSession} when the timeout expires. When a
   * timeoutDuration less than 0 is provided, no watchdog is created
   * 
   * @param timeoutDuration duration if the watchdog timeout
   * @param timeoutTimeUnit unit of the watchog tomeout
   * @return this when the parameters are valid, <code>null</code> otherwise
   */
  public SkillInteractiveSession setWatchdogTimeout(final long timeoutDuration,
      final TimeUnit timeoutTimeUnit) {

    if (timeoutTimeUnit instanceof TimeUnit) {
      this.watchdogTimeoutDuration = timeoutDuration;
      this.watchdogTimeoutTimeUnit = timeoutTimeUnit;
      return this;
    } else {
      return null;
    }
  }

  /**
   * Get the watchdog timeout duration
   * 
   * @return watchdogTimeoutDuration
   */
  public long getWatchdogTimeoutDuration() {
    return this.watchdogTimeoutDuration;
  }

  /**
   * Get the watchdog timeout time unit
   * 
   * @return timeoutTimeUnit
   */
  public TimeUnit getWatchdogTimeUnit() {
    return this.watchdogTimeoutTimeUnit;
  }

  @Override
  public SkillInteractiveSession start()
      throws UnableToStartInteractiveSession, EvaluationFailedException {
    return this.start(Thread.currentThread());
  }

  /**
   * Start the session
   * 
   * @param parent Parent thread that is used as reference for the watchdog
   * @return this when starting of the session was successfully,
   *         <code>null</code> otherwise
   * @throws UnableToStartInteractiveSession when starting of the session failed
   * @throws EvaluationFailedException       when the initalization of the
   *                                         session failed
   */
  public SkillInteractiveSession start(final Thread parent)
      throws UnableToStartInteractiveSession, EvaluationFailedException {

    if (!this.isActive()) {

      try {

        this.process = Runtime.getRuntime().exec(this.command + "\n", null,
            this.workingDir);

      } catch (final IOException e) {
        this.stop();
        throw new UnableToStartInteractiveSession(this.command,
            this.workingDir);
      }

      // add shutdown hook for process
      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          try {
            SkillInteractiveSession.this.process.destroyForcibly();
          } catch (final Exception e) {
          }
        }
      });

      try {
        this.expect = new ExpectBuilder()
            .withInputs(this.process.getInputStream())
            .withOutput(this.process.getOutputStream()).withExceptionOnFailure()
            .build().withTimeout(this.timeoutDuration, this.timeoutTimeUnit);

      } catch (final IOException e) {

        this.stop();
        throw new UnableToStartInteractiveSession(this.command,
            this.workingDir);
      }

      try {
        this.expect.expect(this.nextCommand);
      } catch (final IOException e) {
        this.stop();
        throw new UnableToStartInteractiveSession(this.command,
            this.workingDir);
      }

      SkillCommand skillPromptsCommand = null;

      try {
        skillPromptsCommand = GenericSkillCommandTemplates
            .getTemplate(GenericSkillCommandTemplates.SET_PROMPTS).buildCommand(
                new EvaluableToSkill[] { new SkillString(PROMPT_DEFAULT),
                    new SkillString(PROMPT_DEFAULT) });
      } catch (final IncorrectSyntaxException e) {
        // cannot happen
      }

      this.prompt = PROMPT_DEFAULT;
      this.nextCommand = Matchers.regexp("\n" + this.prompt);

      try {
        this.expect.send(skillPromptsCommand.toSkill() + "\n");
      } catch (final IOException e) {
        this.stop();
        throw new UnableToStartInteractiveSession(this.command,
            this.workingDir);
      }
      try {
        this.expect.expect(this.nextCommand);
      } catch (final IOException e) {
        this.stop();
        throw new UnableToStartInteractiveSession(this.command,
            this.workingDir);
      }

      final File skillControlApi = this.getResourcePath(
          SkillSession.SKILL_RESOURCE, SkillSession.TMP_SKILL_FILE_SUFFIX);

      SkillCommand skillLoadCommand = null;

      try {
        skillLoadCommand = GenericSkillCommandTemplates
            .getTemplate(GenericSkillCommandTemplates.LOAD)
            .buildCommand(new SkillString(skillControlApi.getAbsolutePath()));
      } catch (final IncorrectSyntaxException e) {
        // cannot happen
      }

      try {
        this.expect.send(skillLoadCommand.toSkill() + "\n");
      } catch (final IOException e) {
        this.stop();
        skillControlApi.delete();
        throw new EvaluationFailedException(skillLoadCommand.toSkill());
      }

      try {
        this.expect.expect(this.nextCommand);
      } catch (final IOException e) {
        this.stop();
        skillControlApi.delete();
        throw new UnableToStartInteractiveSession(this.command,
            this.workingDir);
      }

      skillControlApi.delete();

      this.lastActivity = new Date();

      if (this.watchdogTimeoutDuration > 0) {

        this.watchdog = new SkillSessionWatchdog(this,
            this.watchdogTimeoutDuration, this.watchdogTimeoutTimeUnit, parent);

        this.watchdog.start();
      }

      return this;

    } else {

      return null;
    }
  }

  @Override
  public boolean isActive() {
    if ((this.process == null) || !this.process.isAlive()) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  public SkillDataobject evaluate(final SkillCommand command)
      throws UnableToStartInteractiveSession, EvaluationFailedException,
      InvalidDataobjectReferenceExecption {
    return this.evaluate(command, Thread.currentThread());
  }

  @Override
  public SkillDataobject evaluate(final SkillCommand command,
      final Thread parent) throws UnableToStartInteractiveSession,
      EvaluationFailedException, InvalidDataobjectReferenceExecption {

    if (!this.isActive()) {
      this.start(parent);
    }

    SkillDataobject data = null;

    if (this.watchdog instanceof SkillSessionWatchdog) {
      // kill watchdog when available
      this.watchdog.kill();
      this.watchdog = null;
    }

    if (this.isActive()) {

      // identify if command contains invalid complex dataobejcts that are not
      // available in the session
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
      } catch (final IncorrectSyntaxException e) {
        // cannot occur
      }

      String skillCommand = outer.toSkill();

      // store command in a file when MAX_CMD_LENGTH exceeded
      if (skillCommand.length() > SkillSession.MAX_CMD_LENGTH) {

        try {

          final File skillCommandFile = File.createTempFile(
              SkillSession.TMP_FILE_PREFIX, SkillSession.TMP_SKILL_FILE_SUFFIX);

          final FileWriter writer = new FileWriter(skillCommandFile);
          writer.write(command.toSkill());
          writer.close();

          skillCommandFile.deleteOnExit();

          try {

            outer = GenericSkillCommandTemplates.getTemplate(
                GenericSkillCommandTemplates.ED_CDS_RC_EXECUTE_COMMAND_FROM_FILE)
                .buildCommand(
                    new SkillString(skillCommandFile.getAbsolutePath()));
            skillCommand = outer.toSkill();
          } catch (final IncorrectSyntaxException e) {
            // cannot occur
          }
        } catch (final IOException e) {
          // "/tmp" is always writable
        }
      }

      String xml = this.communicate(skillCommand);

      final SkillDataobject obj = SkillDataobject
          .getSkillDataobjectFromXML(this, xml);

      try {

        SkillDisembodiedPropertyList top = (SkillDisembodiedPropertyList) obj;

        if (top.get(SkillSession.ID_VALID).isTrue()) {

          if (top.containsKey(SkillSession.ID_FILE)) {

            final SkillString filePath = (SkillString) top
                .get(SkillSession.ID_FILE);

            final File dataFile = new File(filePath.getString());

            xml = new String(Files.readAllBytes(dataFile.toPath()),
                StandardCharsets.US_ASCII);

            top = (SkillDisembodiedPropertyList) SkillDataobject
                .getSkillDataobjectFromXML(this, xml);

            dataFile.delete();
          }

          data = top.get(SkillSession.ID_DATA);

        } else {

          final SkillList errorList = (SkillList) top
              .get(SkillSession.ID_ERROR);

          final SkillList messageList = (SkillList) errorList
              .getByIndex(errorList.getLength() - 1);

          SkillString errorMessage;
          SkillSymbol errorSymbol;

          final StringBuilder builder = new StringBuilder();

          boolean first = true;

          for (final SkillDataobject messageObj : messageList) {

            if (messageObj instanceof SkillString) {

              errorMessage = (SkillString) messageObj;

              if (first) {
                first = false;
              } else {
                builder.append("\n");
              }

              builder.append(errorMessage.getString());

            } else if (messageObj instanceof SkillSymbol) {

              if (first) {
                first = false;
              } else {
                builder.append(" - ");
              }

              errorSymbol = (SkillSymbol) messageObj;

              builder.append(errorSymbol.getPrintName());
            }
          }

          throw new EvaluationFailedException(command.toSkill(),
              builder.toString());
        }
      } catch (final Exception e) {

        if (e instanceof EvaluationFailedException) {

          throw (EvaluationFailedException) e;

        } else {

          throw new EvaluationFailedException(skillCommand, xml);
        }
      }
    } else {
      throw new UnableToStartInteractiveSession(this.command, this.workingDir);
    }

    this.lastActivity = new Date();

    if (this.watchdogTimeoutDuration > 0) {

      this.watchdog = new SkillSessionWatchdog(this,
          this.watchdogTimeoutDuration, this.watchdogTimeoutTimeUnit, parent);

      this.watchdog.start();
    }

    return data;
  }

  @Override
  public SkillInteractiveSession stop() {

    if (this.watchdog instanceof SkillSessionWatchdog) {
      this.watchdog.kill();
      this.watchdog = null;
    }

    try {
      this.communicate(GenericSkillCommandTemplates
          .getTemplate(GenericSkillCommandTemplates.EXIT).buildCommand()
          .toSkill());
    } catch (final IncorrectSyntaxException e) {
    }

    try {
      this.expect.close();
    } catch (final IOException e) {
    }

    if (this.process != null) {
      this.process.destroyForcibly();
    }

    this.process = null;

    return this;
  }

  /**
   * Execute a Skill command
   *
   * @param cmd Skill command to be executed
   * @return XML from Skill session
   */
  private String communicate(final String cmd) {

    String retval = null;

    try {
      this.expect.send(cmd + "\n");
      retval = this.expect.expect(SkillSession.XML_MATCH).group(1);

    } catch (final Exception e) {
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
        final Field f = this.process.getClass().getDeclaredField("pid");
        f.setAccessible(true);
        pid = f.getLong(this.process);
        f.setAccessible(false);
      }
    } catch (final Exception e) {
      pid = -1;
    }

    return pid;
  }
}