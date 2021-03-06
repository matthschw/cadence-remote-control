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
 * syntax. This session will create a watchdog by default. This watchdog will
 * {@link SkillInteractiveSession#stop()} the session when no communciation was
 * conducted within the specified timeout
 * {@link SkillInteractiveSession#setWatchdogTimeout(long, TimeUnit)}. This
 * behavior can be deactivated by specifying a negative timeout or by setting
 * the environment variable <code>ED_CDS_RC_NO_WATCHDOG</code>. At
 * initialization of a {@link SkillInteractiveSession} this information is read
 * from the environment variable and cannot be changed during the lifetime of
 * the object.
 */
public final class SkillInteractiveSession extends SkillSession {

  private Process process = null;
  private Expect expect = null;

  private String command;
  private final File workingDir;
  private Thread shutdownHook = null;

  private static final String DEFAULT_COMMAND = "virtuoso -nograph";

  /**
   * The {@link SkillSessionWatchdog} can be disabled by setting this
   * environemnt variable.
   */
  public static final String ENVVAR_NO_WATCHDOG = "ED_CDS_RC_NO_WATCHDOG";

  private static final File DEFAULT_WORKING_DIR = new File("");

  // Watchdog
  private final boolean useWatchdog;
  private SkillSessionWatchdog watchdog;
  private Date lastActivity = null;
  private long watchdogTimeoutDuration = 10;
  private TimeUnit watchdogTimeoutTimeUnit = TimeUnit.HOURS;

  /**
   * Create a Session
   */
  public SkillInteractiveSession() {
    this(DEFAULT_WORKING_DIR.getAbsoluteFile());
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
    this.useWatchdog = SkillInteractiveSession.useWatchdog();

    if (this.logger instanceof Logger) {
      this.logger.add(Logger.MSG_CODE_12, Logger.INFO_STEP,
          new String[] { "Create interactive session with",
              " -command:" + this.command,
              " -useWatchdog:" + this.useWatchdog });
    }
  }

  /**
   * Identfy if a watchdog should be used
   * 
   * @return bool
   */
  private static boolean useWatchdog() {

    try {

      final String envvar = System.getenv(ENVVAR_NO_WATCHDOG);

      return !((envvar instanceof String) && (envvar.length() > 0));

    } catch (final Exception e) {
    }

    return true;
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

    if (this.logger instanceof Logger) {
      this.logger.add(Logger.MSG_CODE_13, Logger.INFO_STEP,
          "Set command \"" + command + "\"");
    }

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

      if (this.logger instanceof Logger) {
        this.logger.add(Logger.MSG_CODE_14, Logger.INFO_STEP,
            "Set watchdogTimeoutDuration=" + this.watchdogTimeoutDuration
                + " and watchdogTimeoutTimeUnit="
                + this.watchdogTimeoutTimeUnit);
      }

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

      if (this.logger instanceof Logger) {
        this.logger.add(Logger.MSG_CODE_15, Logger.START_STEP,
            "Starting session...");
      }

      try {

        this.process = Runtime.getRuntime().exec(this.command + "\n", null,
            this.workingDir);

        if (this.logger instanceof Logger) {
          this.logger.add(Logger.MSG_CODE_16, Logger.START_STEP,
              "Starting process with command \"" + this.command
                  + "\" in directory \"" + this.workingDir + "\"");
        }

      } catch (final IOException e) {

        if (this.logger instanceof Logger) {

          this.logger.add(Logger.MSG_CODE_17, Logger.START_STEP,
              "Starting process with command \"" + this.command
                  + "\" in directory \"" + this.workingDir + "\" failed");
        }

        this.stop();
        throw new UnableToStartInteractiveSession(this.command,
            this.workingDir);
      }

      // add shutdown hook for process
      this.shutdownHook = new Thread() {
        @Override
        public void run() {
          try {
            SkillInteractiveSession.this.process.destroyForcibly();
          } catch (final Exception e) {
          }
        }
      };

      Runtime.getRuntime().addShutdownHook(this.shutdownHook);

      try {

        this.expect = new ExpectBuilder()
            .withInputs(this.process.getInputStream())
            .withOutput(this.process.getOutputStream()).withExceptionOnFailure()
            .build().withTimeout(this.timeoutDuration, this.timeoutTimeUnit);

        if (this.logger instanceof Logger) {
          this.logger.add(Logger.MSG_CODE_18, Logger.START_STEP,
              "Starting expect...");
        }

      } catch (final IOException e) {

        if (this.logger instanceof Logger) {
          this.logger.add(Logger.MSG_CODE_19, Logger.START_STEP,
              new String[] { "Starting if expect failed", e.getMessage() });
        }

        this.stop();
        throw new UnableToStartInteractiveSession(this.command,
            this.workingDir);
      }

      try {

        this.expect.expect(this.nextCommand);

        if (this.logger instanceof Logger) {

          this.logger.add(Logger.MSG_CODE_20, Logger.START_STEP,
              "Initialization of Skill session finished, token \""
                  + this.nextCommand + "\" found");
        }

      } catch (final IOException e) {

        if (this.logger instanceof Logger) {

          this.logger
              .add(Logger.MSG_CODE_21, Logger.START_STEP,
                  new String[] {
                      "Initialization of Skill session finished, token \""
                          + this.nextCommand + "\" NOT found:",
                      e.getMessage() });
        }

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
      this.nextCommand = Matchers.anyOf(Matchers.regexp("\n" + this.prompt));

      try {

        this.expect.send(skillPromptsCommand.toSkill() + "\n");

        if (this.logger instanceof Logger) {

          this.logger.add(Logger.MSG_CODE_22, Logger.START_STEP,
              "Send Skill prompt command \"" + skillPromptsCommand.toSkill()
                  + "\"");
        }

      } catch (final IOException e) {

        if (this.logger instanceof Logger) {

          this.logger.add(Logger.MSG_CODE_23, Logger.START_STEP,
              new String[] { "Send Skill prompt command \""
                  + skillPromptsCommand.toSkill() + "\" failed:",
                  e.getMessage() });
        }

        this.stop();
        throw new UnableToStartInteractiveSession(this.command,
            this.workingDir);
      }

      try {
        this.expect.expect(this.nextCommand);

        if (this.logger instanceof Logger) {
          this.logger.add(Logger.MSG_CODE_24, Logger.START_STEP,
              "Wating for return of Skill prompt finished");
        }

      } catch (final IOException e) {

        if (this.logger instanceof Logger) {
          this.logger.add(Logger.MSG_CODE_25, Logger.START_STEP,
              new String[] {
                  "Waiting for return of Skill prompt did not finish",
                  e.getMessage() });
        }

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

        if (this.logger instanceof Logger) {
          this.logger.add(Logger.MSG_CODE_26, Logger.START_STEP,
              "Loading Skill commands with \"" + skillLoadCommand.toSkill()
                  + "\"");
        }

      } catch (final IOException e) {

        if (this.logger instanceof Logger) {
          this.logger.add(Logger.MSG_CODE_27, Logger.START_STEP,
              "Loading Skill commands with \"" + skillLoadCommand.toSkill()
                  + "\" failed");
        }

        this.stop();
        skillControlApi.delete();
        throw new EvaluationFailedException(skillLoadCommand.toSkill());
      }

      try {
        this.expect.expect(this.nextCommand);

        if (this.logger instanceof Logger) {
          this.logger.add(Logger.MSG_CODE_28, Logger.START_STEP,
              "Loading Skill commands finished");
        }

      } catch (final IOException e) {

        if (this.logger instanceof Logger) {
          this.logger.add(Logger.MSG_CODE_29, Logger.START_STEP, new String[] {
              "Loading Skill commands did not finish", e.getMessage() });
        }

        this.stop();
        skillControlApi.delete();
        throw new UnableToStartInteractiveSession(this.command,
            this.workingDir);
      }

      skillControlApi.delete();

      this.lastActivity = new Date();

      if (this.useWatchdog && (this.watchdogTimeoutDuration > 0)) {

        this.watchdog = new SkillSessionWatchdog(this,
            this.watchdogTimeoutDuration, this.watchdogTimeoutTimeUnit, parent);

        this.watchdog.start();

        if (this.logger instanceof Logger) {
          this.logger.add(Logger.MSG_CODE_30, Logger.START_STEP,
              "Start watchdog");
        }
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

      if (this.logger instanceof Logger) {
        this.logger.add(Logger.MSG_CODE_31, "Kill watchdog");
      }
    }

    if (this.isActive()) {

      // identify if command contains invalid complex dataobejcts that are not
      // available in the session
      if (!command.canBeUsedInSession(this)) {

        final InvalidDataobjectReferenceExecption e = new InvalidDataobjectReferenceExecption(
            command, this);

        if (this.logger instanceof Logger) {
          this.logger.add(Logger.MSG_CODE_32, "Command \""
              + e.getCommand().toSkill() + "\" cannot be evaluated in session");
          this.logger.next();
        }

        throw e;
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

      if (this.logger instanceof Logger) {
        this.logger.add(Logger.MSG_CODE_33,
            "Format Skill command to \"" + outer.toSkill() + "\"");
      }

      // store command in a file when MAX_CMD_LENGTH exceeded
      if (skillCommand.length() > SkillSession.MAX_CMD_LENGTH) {

        if (this.logger instanceof Logger) {
          this.logger.add(Logger.MSG_CODE_34,
              "Skill command is too long, store command in file...");
        }

        try {

          final File skillCommandFile = File.createTempFile(
              SkillSession.TMP_FILE_PREFIX, SkillSession.TMP_SKILL_FILE_SUFFIX);

          final FileWriter writer = new FileWriter(skillCommandFile);
          writer.write(command.toSkill());
          writer.close();

          if (this.logger instanceof Logger) {

            this.logger.add(Logger.MSG_CODE_35,
                "Write Skill command to file \"" + skillCommandFile + "\"");
          }

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

          // "/tmp" is always writable, error very unlikely
          if (this.logger instanceof Logger) {
            this.logger.add(Logger.MSG_CODE_36, new String[] {
                "Cannot write command to file", e.getMessage() });
          }
        }
      }

      String xml = this.communicate(skillCommand);

      final SkillDataobject obj = SkillDataobject
          .getSkillDataobjectFromXML(this, xml);

      if (this.logger instanceof Logger) {
        this.logger.add(Logger.MSG_CODE_37,
            new String[] { "Parsed return value:", obj.toSkill() });
      }

      try {

        SkillDisembodiedPropertyList top = (SkillDisembodiedPropertyList) obj;

        if (top.get(SkillSession.ID_VALID).isTrue()) {

          if (this.logger instanceof Logger) {
            this.logger.add(Logger.MSG_CODE_38,
                new String[] { "Return value is valid" });
          }

          if (top.containsKey(SkillSession.ID_FILE)) {

            final SkillString filePath = (SkillString) top
                .get(SkillSession.ID_FILE);

            final File dataFile = new File(filePath.getString());

            if (this.logger instanceof Logger) {
              this.logger.add(Logger.MSG_CODE_39,
                  new String[] { "Return value is stored in file \""
                      + dataFile.toString() + "\"" });
            }

            xml = new String(Files.readAllBytes(dataFile.toPath()),
                StandardCharsets.US_ASCII);

            top = (SkillDisembodiedPropertyList) SkillDataobject
                .getSkillDataobjectFromXML(this, xml);

            if (this.logger instanceof Logger) {
              this.logger.add(Logger.MSG_CODE_40, new String[] {
                  "Parsed return value from file:", obj.toSkill() });
            }

            dataFile.delete();
          }

          data = top.get(SkillSession.ID_DATA);

          if (this.logger instanceof Logger) {
            this.logger.add(Logger.MSG_CODE_41, new String[] {
                "Extract data from return value", data.toSkill() });
          }

        } else {

          final SkillList errorList = (SkillList) top
              .get(SkillSession.ID_ERROR);

          final SkillList messageList = (SkillList) errorList
              .get(errorList.length() - 1);

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

          EvaluationFailedException e = new EvaluationFailedException(
              command.toSkill(), builder.toString());

          if (this.logger instanceof Logger) {
            this.logger.add(Logger.MSG_CODE_42,
                new String[] { "Return value is invalid", builder.toString() });
            this.logger.next();
          }

          throw e;
        }
      } catch (final Exception e) {

        if (this.logger instanceof Logger) {

          this.logger.add(Logger.MSG_CODE_43,
              new String[] { "Exeception during evaluation", e.getMessage() });
          this.logger.next();
        }

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

    if (this.useWatchdog && (this.watchdogTimeoutDuration > 0)) {

      if (this.logger instanceof Logger) {

        this.logger.add(Logger.MSG_CODE_44, "Create watchdog");
        this.logger.next();
      }

      this.watchdog = new SkillSessionWatchdog(this,
          this.watchdogTimeoutDuration, this.watchdogTimeoutTimeUnit, parent);

      this.watchdog.start();
    }

    return data;
  }

  @Override
  public SkillInteractiveSession stop() {

    if (this.logger instanceof Logger) {
      this.logger.add(Logger.MSG_CODE_49, Logger.STOP_STEP, "Stop session");
    }

    if (this.useWatchdog && (this.watchdog instanceof SkillSessionWatchdog)) {

      if (this.logger instanceof Logger) {
        this.logger.add(Logger.MSG_CODE_48, Logger.STOP_STEP, "Kill watchdog");
      }
      this.watchdog.kill();
      this.watchdog = null;
    }

    try {

      this.communicate(GenericSkillCommandTemplates
          .getTemplate(GenericSkillCommandTemplates.EXIT).buildCommand()
          .toSkill());

      if (this.logger instanceof Logger) {
        this.logger.add(Logger.MSG_CODE_50, Logger.STOP_STEP, "Exit session");
      }

    } catch (final IncorrectSyntaxException e) {
    }

    try {
      this.expect.close();
    } catch (final IOException e) {
    }

    if (this.process != null) {
      this.process.destroyForcibly();
    }

    try {
      Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
    } catch (final Exception e) {
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

      if (this.logger instanceof Logger) {
        this.logger.add(Logger.MSG_CODE_45,
            new String[] { "Send command ", " " + cmd, "to Skill session" });
      }

      retval = this.expect.expect(SkillSession.XML_MATCH).group(1);

      if (this.logger instanceof Logger) {
        this.logger.add(Logger.MSG_CODE_46,
            new String[] { "Skill session returned", retval });
      }

    } catch (final Exception e) {

      if (this.logger instanceof Logger) {
        this.logger.add(Logger.MSG_CODE_47,
            new String[] { "Communication failed with", e.getMessage() });
      }
    }

    return retval;
  }

  /**
   * Get date of last activity in the session
   *
   * @return date of last activity in the session
   */
  protected Date getLastActivity() {
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