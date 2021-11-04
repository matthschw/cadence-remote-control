package edlab.eda.cadence.rc;

import edlab.eda.cadence.rc.api.IncorrectSyntaxException;
import edlab.eda.cadence.rc.session.EvaluationFailedException;
import edlab.eda.cadence.rc.session.InvalidDataobjectReferenceExecption;
import edlab.eda.cadence.rc.session.SkillInteractiveSession;
import edlab.eda.cadence.rc.session.UnableToStartSession;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class SkillInteractiveSessionTest {

  @Test
  void test() throws EvaluationFailedException, UnableToStartSession,
      IncorrectSyntaxException, IOException,
      InvalidDataobjectReferenceExecption {

    SkillInteractiveSession session = new SkillInteractiveSession();
    
    System.err.println(session);
    
    try {
      session.start();
    } catch (UnableToStartSession e) {
      session.stop();
      fail("Unable to start session");
    }
    
    SkillSessionMethods.detectError(session);

    SkillSessionMethods.writeFile(session);

    for (int i = 0; i < 100; i++) {
      SkillSessionMethods.addUpValuesInList(session);
    }

    SkillSessionMethods.strcat(session);

    session.stop();
  }
}