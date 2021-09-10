package edlab.eda.cadence.rc;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.Random;

import edlab.eda.cadence.rc.api.GenericSkillCommandTemplates;
import edlab.eda.cadence.rc.api.IncorrectSyntaxException;
import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.data.SkillFixnum;
import edlab.eda.cadence.rc.data.SkillList;
import edlab.eda.cadence.rc.data.SkillNumber;
import edlab.eda.cadence.rc.data.SkillString;
import edlab.eda.cadence.rc.data.SkillSymbol;
import edlab.eda.cadence.rc.session.EvaluableToSkill;
import edlab.eda.cadence.rc.session.EvaluationFailedException;
import edlab.eda.cadence.rc.session.InvalidDataobjectReferenceExecption;
import edlab.eda.cadence.rc.session.SkillSession;
import edlab.eda.cadence.rc.session.UnableToStartSkillSession;

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
  
  static void strcat(SkillSession session)
      throws IncorrectSyntaxException, UnableToStartSkillSession,
      EvaluationFailedException, InvalidDataobjectReferenceExecption {

    LinkedList<EvaluableToSkill> rest = new LinkedList<EvaluableToSkill>();

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

  static void writeFile(SkillSession session)
      throws UnableToStartSkillSession, EvaluationFailedException,
      IncorrectSyntaxException, IOException,
      InvalidDataobjectReferenceExecption {

    File file = new File(FILE_NAME);

    if (file.exists()) {
      file.delete();
    }

    SkillCommand command = GenericSkillCommandTemplates
        .getTemplate(GenericSkillCommandTemplates.OUTFILE)
        .buildCommand(new SkillString(FILE_NAME));

    SkillDataobject port = session.evaluate(command);

    LinkedList<EvaluableToSkill> rest = new LinkedList<EvaluableToSkill>();
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

  static void addUpValuesInList(SkillSession session)
      throws UnableToStartSkillSession, EvaluationFailedException,
      IncorrectSyntaxException, InvalidDataobjectReferenceExecption {

    SkillList list = new SkillList();

    Random random = new Random();
    int sum = 0;
    int rand;

    for (int i = 0; i < 100; i++) {
      rand = random.nextInt(10000);

      sum += rand;
      list.addAtLast(new SkillFixnum(rand));
    }

    SkillCommand command = GenericSkillCommandTemplates
        .getTemplate(GenericSkillCommandTemplates.APPLY)
        .buildCommand(new SkillDataobject[] { new SkillSymbol("plus"), list });

    SkillDataobject retval = session.evaluate(command);

    if (retval instanceof SkillNumber) {

      SkillNumber num = (SkillNumber) retval;

      if (num.getValue().intValue() != sum) {

        fail("Summation incorrect (" + num.getValue().intValue() + "!=" + sum
            + ")");
      }
    } else {
      fail("Incorrect return value");
    }
  }

}
