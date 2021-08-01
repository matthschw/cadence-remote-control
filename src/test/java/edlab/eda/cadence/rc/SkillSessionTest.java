package edlab.eda.cadence.rc;

import edlab.eda.cadence.rc.api.GenericSkillCommandTemplates;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.data.SkillFixnum;
import edlab.eda.cadence.rc.data.SkillList;
import edlab.eda.cadence.rc.data.SkillNumber;
import edlab.eda.cadence.rc.data.SkillString;
import edlab.eda.cadence.rc.data.SkillSymbol;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Random;

import org.junit.jupiter.api.Test;

public class SkillSessionTest {

  private static String FILE_NAME = "fuubar.txt";

  @Test
  void test() throws MaxCommandLengthExeeded, EvaluationFailedException,
      UnableToStartSkillSession, IncorrectSyntaxException {

    SkillSession session = new SkillSession();

    try {
      session.start();
    } catch (UnableToStartSkillSession e) {
      fail("Unable to start session");
      session.stop();
    }

    writeFile(session);
    addUpValuesInList(session);

    session.stop();
  }

  private static void writeFile(SkillSession session)
      throws MaxCommandLengthExeeded, UnableToStartSkillSession,
      EvaluationFailedException, IncorrectSyntaxException {
    File file = new File(FILE_NAME);

    if (file.exists()) {
      file.delete();
    }

    SkillCommand command = SkillCommand
        .buildCommand(
            GenericSkillCommandTemplates
                .getTemplate(GenericSkillCommandTemplates.OUTFILE),
            new SkillString(FILE_NAME));

    SkillDataobject port = session.evaluate(command);

    command = SkillCommand.buildCommand(
        GenericSkillCommandTemplates
            .getTemplate(GenericSkillCommandTemplates.FPRINTF),
        new SkillDataobject[] { port, new SkillString("Heinz Banane") });
    session.evaluate(command);

    command = SkillCommand.buildCommand(
        GenericSkillCommandTemplates
            .getTemplate(GenericSkillCommandTemplates.CLOSE),
        new SkillDataobject[] { port });

    session.evaluate(command);

    if (!file.exists()) {
      fail("File was not generated");
    }

    if (file.exists()) {
      file.delete();
    }
  }

  private static void addUpValuesInList(SkillSession session)
      throws MaxCommandLengthExeeded, UnableToStartSkillSession,
      EvaluationFailedException, IncorrectSyntaxException {

    SkillList list = new SkillList();

    Random random = new Random();
    int sum = 0;
    int rand;

    for (int i = 0; i < 100; i++) {
      rand = random.nextInt(1000);

      sum += rand;
      list.addAtLast(new SkillFixnum(rand));
    }

    SkillCommand command = SkillCommand.buildCommand(
        GenericSkillCommandTemplates
            .getTemplate(GenericSkillCommandTemplates.APPLY),
        new SkillDataobject[] { new SkillSymbol("plus"), list });

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