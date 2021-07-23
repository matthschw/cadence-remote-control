package edlab.eda.cadence.rc;

import java.io.File;
import java.io.IOException;

import edlab.eda.cadence.rc.api.CadenceGenericCommandTemplates;
import edlab.eda.cadence.rc.api.CadenceSkillCommands;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.data.SkillString;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import net.sf.expectit.Result;
import net.sf.expectit.matcher.Matcher;
import net.sf.expectit.matcher.Matchers;

public class Session {

  private Process process = null;
  private Expect expect = null;

  private String command;
  private File workingDir;
  private Matcher<Result> nextCommand = NEXT_COMMAND;
  private CadenceGenericCommandTemplates commandTemplates = new CadenceGenericCommandTemplates();

  private static final String DEFAULT_COMMAND = "virtuoso -nograph";
  private static final String PATH_TO_SKILL = "/src/main/skill/EDcdsRemoteControl.il";
  private static final String PROMPT = "[ED-CDS_RC]";

  private static final File DEFAULT_WORKING_DIR = new File("./");
  private static final Matcher<Result> NEXT_COMMAND = Matchers.regexp("\n>");

  public static final String CDS_RC_GLOBAL = "EDcdsRemoteControl";
  public static final String CDS_RC_RETURN_VALUES = "returnValues";

  public Session() {
    this.command = DEFAULT_COMMAND;
    this.workingDir = DEFAULT_WORKING_DIR;
  }

  public Session(String command) {
    this.command = command;
    this.workingDir = DEFAULT_WORKING_DIR;
  }

  public Session(File workingDir) {
    this.command = DEFAULT_COMMAND;
    this.workingDir = workingDir;
  }

  public Session(String command, File workingDir) {
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
          .build();

      expect.expect(nextCommand);
      this.nextCommand = Matchers.regexp("\n" + PROMPT);

      SkillString skillFile = new SkillString(PATH_TO_SKILL);

      this.expect.send(CadenceCommand
          .buildCommand(commandTemplates.getTemplate(CadenceSkillCommands.LOAD),
              new SkillDataobject[] { skillFile })
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

  public SkillDataobject evaluate(CadenceCommand command) {
    return null;
  }

  public boolean stop() {

    communicate(CadenceCommand
        .buildCommand(commandTemplates.getTemplate(CadenceSkillCommands.EXIT))
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
}