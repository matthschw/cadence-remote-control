package edlab.eda.cadence.rc;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import edlab.eda.cadence.rc.api.IncorrectSyntaxException;
import edlab.eda.cadence.rc.session.EvaluationFailedException;
import edlab.eda.cadence.rc.session.InvalidDataobjectReferenceExecption;
import edlab.eda.cadence.rc.session.SkillSocketSession;
import edlab.eda.cadence.rc.session.UnableToStartSession;

public class SkillSocketSessionTest {

  public static final File TST_DIR = new File("./");

  @Test
  void test() throws IOException, EvaluationFailedException,
      InvalidDataobjectReferenceExecption, UnableToStartSession,
      IncorrectSyntaxException {

    Process process = null;

    try {

      process = Runtime.getRuntime()
          .exec("virtuoso -replay ./src/test/resources/replay.il");

      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
      }

      SkillSocketSession session = SkillSocketSession.connect(TST_DIR);

      SkillSessionMethods.writeFile(session);
      session.stop();

      session = SkillSocketSession.connect(TST_DIR);

      for (int i = 0; i < 100; i++) {
        SkillSessionMethods.addUpValuesInList(session);
      }

      session.stop();
      session = SkillSocketSession.connect(TST_DIR);

      SkillSessionMethods.strcat(session);

      SkillSessionMethods.complexNumber(session);

      SkillSessionMethods.detectFailingCommand(session);

      SkillSessionMethods.loadingCode(session);

      session.stop();

    } catch (Exception e) {
      process.destroyForcibly();
      throw e;
    }

    process.destroyForcibly();
  }
}