package edlab.eda.cadence.rc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

public class SkillSession {

  private Process process = null;
  private Expect expect = null;

  private String command;
  private File workingDir;

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

  public SkillSession() {
    this.command = DEFAULT_COMMAND;
    this.workingDir = DEFAULT_WORKING_DIR;

  }

  public SkillSession(String command) {
    this.command = command;
    this.workingDir = DEFAULT_WORKING_DIR;
  }

  public SkillSession(File workingDir) {
    this.command = DEFAULT_COMMAND;
    this.workingDir = workingDir;
  }

  public SkillSession(String command, File workingDir) {
    this.command = command;
    this.workingDir = workingDir;
  }

  public boolean start() {

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
          .build().withTimeout(1, TimeUnit.HOURS);

      expect.expect(nextCommand);
      this.nextCommand = Matchers.regexp("\n" + PROMPT);

      SkillString skillFile = new SkillString(PATH_TO_SKILL);

      this.expect.send(SkillCommand
          .buildCommand(GenericSkillCommandTemplates
              .getTemplate(GenericSkillCommands.LOAD), skillFile)
          .toSkill() + "\n");

      expect.expect(this.nextCommand);

    } catch (IOException e) {

      System.err.println(
          "Unable to execute expect" + " with error:\n" + e.getMessage());

      this.process.destroy();
      return false;
    }

    return true;
  }

  public SkillDataobject evaluate(SkillCommand command) {

    SkillCommand outer = SkillCommand.buildCommand(controlCommand,
        SkillCommand.buildCommand(GenericSkillCommandTemplates
            .getTemplate(GenericSkillCommands.ERRSET), command));

    String skillCommand = outer.toSkill();

    if (skillCommand.length() > MAX_CMD_LENGTH) {
      // throw error
    }

    String xml = communicate(outer.toSkill());
    xml = xml.substring(0, xml.length() - 1);

    SkillDataobject obj = SkillDataobject.getSkillDataobjectFromXML(this, xml);
    SkillDisembodiedPropertyList top = (SkillDisembodiedPropertyList) obj;

    SkillDataobject data;

    if (top.getProperty("valid").isTrue()) {

      if (top.getKeys().contains("file")) {

        SkillString filePath = (SkillString) top.getProperty("file");

        File dataFile = new File(filePath.getString());

        xml = readFile(dataFile);

        data = SkillDataobject.getSkillDataobjectFromXML(this, xml);
      } else {
        data = top.getProperty("data");
      }

      return data;

    } else {
      return null;
      // throw error
    }
  }

  public boolean stop() {

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