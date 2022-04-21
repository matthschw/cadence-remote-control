package edlab.eda.cadence.rc;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import edlab.eda.cadence.rc.api.IncorrectSyntaxException;
import edlab.eda.cadence.rc.session.EvaluationFailedException;
import edlab.eda.cadence.rc.session.InvalidDataobjectReferenceExecption;
import edlab.eda.cadence.rc.session.SkillInteractiveSession;
import edlab.eda.cadence.rc.session.UnableToStartSession;

public class SkillInteractiveSessionTest {

  @Test
  void test() throws IncorrectSyntaxException, IOException,
      InvalidDataobjectReferenceExecption, EvaluationFailedException {

    SkillInteractiveSession session = new SkillInteractiveSession();

    try {

      session.start();

      SkillSessionMethods.detectError(session);

      SkillSessionMethods.writeFile(session);

      for (int i = 0; i < 100; i++) {
        SkillSessionMethods.addUpValuesInList(session);
      }

      SkillSessionMethods.strcat(session);

      SkillSessionMethods.complexNumber(session);

      SkillSessionMethods.detectFailingCommand(session);

      session.stop();

    } catch (UnableToStartSession e) {
      session.stop();
      fail("Unable to start session");
    } catch (IncorrectSyntaxException e) {
      session.stop();
      throw e;
    } catch (IOException e) {
      session.stop();
      throw e;
    } catch (InvalidDataobjectReferenceExecption e) {
      session.stop();
      throw e;
    } catch (EvaluationFailedException e) {
      session.stop();
      throw e;
    }
  }
}