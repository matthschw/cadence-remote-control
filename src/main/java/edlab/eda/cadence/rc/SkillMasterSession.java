package edlab.eda.cadence.rc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import edlab.eda.cadence.rc.api.SkillCommandTemplate;
import edlab.eda.cadence.rc.api.GenericSkillCommandTemplates;
import edlab.eda.cadence.rc.api.GenericSkillCommands;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.data.SkillDisembodiedPropertyList;
import edlab.eda.cadence.rc.data.SkillString;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import net.sf.expectit.Result;
import net.sf.expectit.matcher.Matcher;
import net.sf.expectit.matcher.Matchers;

public class SkillMasterSession {

  private Process process = null;
  private Expect expect = null;

  private String command;
  private File workingDir;

  private long timeoutDuration = 1;
  private TimeUnit timeoutTimeUnit = TimeUnit.HOURS;
  private Date lastActivity = null;

  private SkillSessionWatchdog watchdog;

  private Matcher<Result> nextCommand = NEXT_COMMAND;

  private SkillCommandTemplate controlCommand = new SkillCommandTemplate(
      "EDcdsRCfmtCmd", 1);

  private static final String DEFAULT_COMMAND = "virtuoso -nograph";
  private static final String PATH_TO_SKILL = "./src/main/skill/EDcdsRemoteControl.il";

  private static final int MAX_CMD_LENGTH = 8000;

  private static final String PROMPT = "\\[ED-CDS-RC\\]";

  private static final File DEFAULT_WORKING_DIR = new File("./");
  private static final Matcher<Result> NEXT_COMMAND = Matchers.regexp("\n>");

  public static final String CDS_RC_GLOBAL = "EDcdsRC";
  public static final String CDS_RC_RETURN_VALUES = "returnValues";

  public SkillMasterSession() {
    this.command = DEFAULT_COMMAND;
    this.workingDir = DEFAULT_WORKING_DIR;
  }

  public SkillMasterSession(String command) {
    this.command = command;
    this.workingDir = DEFAULT_WORKING_DIR;
  }

  public SkillMasterSession(File workingDir) {
    this.command = DEFAULT_COMMAND;
    this.workingDir = workingDir;
  }

  public SkillMasterSession(String command, File workingDir) {
    this.command = command;
    this.workingDir = workingDir;
  }

  public SkillMasterSession setTimeout(long duration, TimeUnit unit) {
    this.timeoutDuration = duration;
    this.timeoutTimeUnit = unit;
    return this;
  }

  public boolean start() {

    if (!isActive()) {
      try {
        this.process = Runtime.getRuntime().exec(this.command + "\n", null,
            workingDir);

      } catch (IOException e) {
        System.err.println(
            "Unable to execute session" + " with error:\n" + e.getMessage());
        return false;
      }

      try {
        expect = new ExpectBuilder().withInputs(this.process.getInputStream())
            .withOutput(this.process.getOutputStream()).withExceptionOnFailure()
            .build().withTimeout(this.timeoutDuration, this.timeoutTimeUnit);

        expect.expect(nextCommand);
        this.nextCommand = Matchers.regexp("\n" + PROMPT);

        SkillString skillFile = new SkillString(PATH_TO_SKILL);

        this.expect.send(SkillCommand
            .buildCommand(GenericSkillCommandTemplates
                .getTemplate(GenericSkillCommands.LOAD), skillFile)
            .toSkill() + "\n");

        expect.expect(this.nextCommand);
        this.lastActivity = new Date();
        this.watchdog = new SkillSessionWatchdog(this, this.timeoutDuration,
            this.timeoutTimeUnit);
        
        this.watchdog.start();
        
      } catch (IOException e) {

        System.err.println(
            "Unable to execute expect" + " with error:\n" + e.getMessage());

        this.process.destroy();
        return false;
      }

      return true;
    } else {
      return true;
    }
  }

  public boolean isActive() {
    if (process == null || !process.isAlive()) {
      return false;
    } else {
      return true;
    }
  }

  public SkillDataobject evaluate(SkillCommand command) {

    if (!isActive()) {
      start();
    }
    
    SkillDataobject data;
    
    this.watchdog.kill();
    this.watchdog = null;
    
    if (isActive()) {

      SkillCommand outer = SkillCommand.buildCommand(controlCommand,
          SkillCommand.buildCommand(GenericSkillCommandTemplates
              .getTemplate(GenericSkillCommands.ERRSET), command));

      String skillCommand = outer.toSkill();

      if (skillCommand.length() > MAX_CMD_LENGTH) {
        // throw error
      }

      String xml = communicate(outer.toSkill());
      xml = xml.substring(0, xml.length() - 1);

      SkillDataobject obj = SkillDataobject.getSkillDataobjectFromXML(this,
          xml);
      SkillDisembodiedPropertyList top = (SkillDisembodiedPropertyList) obj;

      if (top.getProperty("valid").isTrue()) {

        if (top.getKeys().contains("file")) {

          SkillString filePath = (SkillString) top.getProperty("file");

          File dataFile = new File(filePath.getString());

          xml = readFile(dataFile);

          data = SkillDataobject.getSkillDataobjectFromXML(this, xml);
        } else {
          data = top.getProperty("data");
        }

      } else {
        data = null;
        // throw error
      }
    } else {
      data = null;
    }
    
    this.lastActivity = new Date();
    this.watchdog = new SkillSessionWatchdog(this, this.timeoutDuration,
        this.timeoutTimeUnit);
    this.watchdog.start();
    
    return data;
  }

  public boolean stop() {
    
    this.watchdog.kill();
    this.watchdog = null;

    communicate(SkillCommand
        .buildCommand(
            GenericSkillCommandTemplates.getTemplate(GenericSkillCommands.EXIT))
        .toSkill());

    try {
      this.expect.close();
    } catch (IOException e) {
    }

    if (this.process != null) {
      this.process.destroyForcibly();
    }
    
    this.process = null;

    return true;
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

  Date getLastActivity() {
    return this.lastActivity;
  }

  public static String readFile(File file) {

    Scanner scanner;
    String rtn = "";

    try {
      scanner = new Scanner(file);
      while (scanner.hasNextLine()) {
        rtn += scanner.nextLine() + "\n";
      }

      scanner.close();

      return rtn;

    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }
}