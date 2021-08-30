package edlab.eda.cadence.rc;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import edlab.eda.cadence.rc.api.IncorrectSyntaxException;

public class SkillSocketSessionTest {

  @Test
  void test() throws EvaluationFailedException, UnableToStartSkillSession,
      IncorrectSyntaxException, IOException,
      InvalidDataobjectReferenceExecption {

    SkillInteractiveSession session = new SkillInteractiveSession();

    try {
      session.start();
    } catch (UnableToStartSkillSession e) {
      session.stop();
      fail("Unable to start session");
    }

    SkillSessionMethods.writeFile(session);

    for (int i = 0; i < 1000; i++) {
      SkillSessionMethods.addUpValuesInList(session);
    }

    SkillSessionMethods.strcat(session);

    session.stop();
  }
}
