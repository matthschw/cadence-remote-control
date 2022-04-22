package edlab.eda.cadence.rc;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.Random;

import edlab.eda.cadence.rc.api.GenericSkillCommandTemplates;
import edlab.eda.cadence.rc.api.IncorrectSyntaxException;
import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.api.SkillCommandTemplate;
import edlab.eda.cadence.rc.data.SkillComplexNumber;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.data.SkillFixnum;
import edlab.eda.cadence.rc.data.SkillFlonum;
import edlab.eda.cadence.rc.data.SkillList;
import edlab.eda.cadence.rc.data.SkillNumber;
import edlab.eda.cadence.rc.data.SkillString;
import edlab.eda.cadence.rc.data.SkillSymbol;
import edlab.eda.cadence.rc.session.EvaluableToSkill;
import edlab.eda.cadence.rc.session.EvaluationFailedException;
import edlab.eda.cadence.rc.session.InvalidDataobjectReferenceExecption;
import edlab.eda.cadence.rc.session.SkillSession;
import edlab.eda.cadence.rc.session.UnableToStartSession;

public class SkillSessionMethods {

  private static final String FILE_NAME = "fuubar.txt";
  private static final String STR1 = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
      + "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi\n"
      + "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.\n"
      + "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.\n"
      + "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.\n"
      + "Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum.\n"
      + "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.\n"
      + "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.\n"
      + "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, At accusam aliquyam diam diam dolore dolores duo eirmod eos erat, et nonumy sed tempor et et invidunt justo labore Stet clita ea et gubergren, kasd magna no rebum.\n"
      + "sanctus sea sed takimata ut vero voluptua. est Lorem ipsum dolor sit amet.\n"
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat.\n"
      + "Consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus.\n"
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.";

  private static final String STR2 = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum."
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum."
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
      + "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.\n"
      + "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.\n"
      + "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.\n"
      + "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.\n"
      + "Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum.\n"
      + "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.\n"
      + "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.\n"
      + "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, At accusam aliquyam diam diam dolore dolores duo eirmod eos erat, et nonumy sed tempor et et invidunt justo labore Stet clita ea et gubergren, kasd magna no rebum. sanctus sea sed takimata ut vero voluptua.\n"
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat.\n"
      + "Consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua\n."
      + "At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus.\n"
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
      + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\n"
      + "At vero eos et accusam et justo duo dolores et ea rebum.\n"
      + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n"
      + "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.\n"
      + "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.\n"
      + "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.\n"
      + "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.\n";

  private static final String STR3 = "Franz faehrt im komplett verwahrlosten Taxi quer durch Bayern.";

  private static final String STR = STR1 + STR2 + STR3;

  private static final String TESTFUN_NAME = "EDtestFun";

  static void strcat(SkillSession session)
      throws IncorrectSyntaxException, UnableToStartSession,
      EvaluationFailedException, InvalidDataobjectReferenceExecption {

    LinkedList<EvaluableToSkill> rest = new LinkedList<>();

    rest.add(new SkillString(STR2));
    rest.add(new SkillString(STR3));

    SkillCommand command = GenericSkillCommandTemplates
        .getTemplate(GenericSkillCommandTemplates.STRCAT)
        .buildCommand(new SkillString(STR1), rest);

    SkillString retval = (SkillString) session.evaluate(command);

    if (!STR.contentEquals(retval.getString())) {
      fail("Strcat failed");
    }
  }

  static void writeFile(SkillSession session) throws UnableToStartSession,
      EvaluationFailedException, IncorrectSyntaxException, IOException,
      InvalidDataobjectReferenceExecption {

    File file = new File(FILE_NAME);

    if (file.exists()) {
      file.delete();
    }

    SkillCommand command = GenericSkillCommandTemplates
        .getTemplate(GenericSkillCommandTemplates.OUTFILE)
        .buildCommand(new SkillString(FILE_NAME));

    SkillDataobject port = session.evaluate(command);

    LinkedList<EvaluableToSkill> rest = new LinkedList<>();
    rest.add(new SkillString(STR));

    command = GenericSkillCommandTemplates
        .getTemplate(GenericSkillCommandTemplates.FPRINTF)
        .buildCommand(port, rest);

    session.evaluate(command);

    command = GenericSkillCommandTemplates
        .getTemplate(GenericSkillCommandTemplates.CLOSE).buildCommand(port);

    session.evaluate(command);

    if (!file.exists()) {
      fail("File was not generated");

    }

    byte[] data = Files.readAllBytes(file.toPath());

    String content = new String(data);

    if (!content.equals(STR)) {
      fail("Content of file incorrect");
    }

    if (file.exists()) {
      file.delete();
    }
  }

  static void detectError(SkillSession session) {

    try {
      SkillCommand cmd = GenericSkillCommandTemplates
          .getTemplate(GenericSkillCommandTemplates.PLUS)
          .buildCommand(new EvaluableToSkill[] { new SkillFixnum(10),
              new SkillString("A") });

      try {

        session.evaluate(cmd);

      } catch (UnableToStartSession e) {
      } catch (EvaluationFailedException e) {
        return;
      } catch (InvalidDataobjectReferenceExecption e) {
      }

      fail("Error in command \"" + cmd.toSkill() + "\" not detected");

    } catch (IncorrectSyntaxException e) {

    }
  }

  static void addUpValuesInList(SkillSession session)
      throws UnableToStartSession, EvaluationFailedException,
      IncorrectSyntaxException, InvalidDataobjectReferenceExecption {

    SkillList list = new SkillList();

    Random random = new Random();
    int sum = 0;
    int rand;

    for (int i = 0; i < 100; i++) {
      rand = random.nextInt(10000);

      sum += rand;
      list.append(new SkillFixnum(rand));
    }

    SkillCommand command = GenericSkillCommandTemplates
        .getTemplate(GenericSkillCommandTemplates.APPLY)
        .buildCommand(new SkillDataobject[] { new SkillSymbol("plus"), list });

    SkillDataobject retval = session.evaluate(command);

    if (retval instanceof SkillNumber) {

      SkillFixnum num = (SkillFixnum) retval;

      if (num.getFixnum() != sum) {

        fail("Summation incorrect (" + num.getFixnum() + "!=" + sum + ")");
      }
    } else {
      fail("Incorrect return value");
    }
  }

  static void complexNumber(SkillSession session)
      throws IncorrectSyntaxException, UnableToStartSession,
      EvaluationFailedException, InvalidDataobjectReferenceExecption {

    SkillCommand command = GenericSkillCommandTemplates
        .getTemplate(GenericSkillCommandTemplates.SQRT)
        .buildCommand(new SkillFlonum(new BigDecimal("-1")));

    SkillDataobject retval = session.evaluate(command);

    if (!(retval instanceof SkillComplexNumber)) {
      fail("No complex number returned, but" + retval);
    }

    SkillComplexNumber num1 = new SkillComplexNumber(1, 1);
    SkillComplexNumber num2 = new SkillComplexNumber(1, -1);

    command = GenericSkillCommandTemplates
        .getTemplate(GenericSkillCommandTemplates.TIMES)
        .buildCommand(new SkillDataobject[] { num1, num2 });

    retval = session.evaluate(command);

    if (!(retval instanceof SkillComplexNumber)) {
      fail("No complex number returned, but" + retval);
    }
  }

  static void detectFailingCommand(SkillSession session)
      throws UnableToStartSession, InvalidDataobjectReferenceExecption,
      IncorrectSyntaxException {

    SkillCommandTemplate template = SkillCommandTemplate.build("plus", 2);

    SkillCommand command = template.buildCommand(new EvaluableToSkill[] {
        new SkillFixnum(1), new SkillString("banane") });

    try {
      @SuppressWarnings("unused")
      SkillDataobject obj = session.evaluate(command);
      fail("Invalid command not detected");
    } catch (EvaluationFailedException e) {
    }
  }

  static void loadingCode(SkillSession session)
      throws UnableToStartSession, EvaluationFailedException,
      InvalidDataobjectReferenceExecption, IncorrectSyntaxException {

    if (session.isSkillFunctionCallable(TESTFUN_NAME)) {
      fail("Function \"" + TESTFUN_NAME + "\" is callable, but should not");
    }

    if (!session.loadSkillCode(new File("./src/test/resources/EDTestFun.il"))) {
      fail("Cannot load Skill code");
    }

    if (!session.isSkillFunctionCallable(TESTFUN_NAME)) {
      fail("Function \"" + TESTFUN_NAME + "\" is not callable");
    }

    SkillCommandTemplate template = SkillCommandTemplate.build(TESTFUN_NAME);

    SkillDataobject obj = session.evaluate(template.buildCommand());

    if (obj instanceof SkillFixnum) {

      if (((SkillFixnum) obj).getFixnum() != 42) {
        fail("Function \"" + TESTFUN_NAME + "\" does not return 42");
      }

    } else {
      fail("Function \"" + TESTFUN_NAME + "\" return type invalid");
    }
  }
}