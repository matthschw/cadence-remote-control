package edlab.eda.cadence.rc;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

import edlab.eda.cadence.rc.api.IncorrectSyntaxException;
import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.api.SkillCommandTemplate;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.session.EvaluationFailedException;
import edlab.eda.cadence.rc.session.InvalidDataobjectReferenceExecption;
import edlab.eda.cadence.rc.session.SkillInteractiveSession;
import edlab.eda.cadence.rc.session.UnableToStartSession;

class WaveformTest {

  public static final File TSTFILE = new File("./fuubar.xml");

  @Test
  void test() throws UnableToStartSession, EvaluationFailedException,
      IncorrectSyntaxException, InvalidDataobjectReferenceExecption {

    if (TSTFILE.exists()) {
      TSTFILE.delete();
    }

    SkillInteractiveSession session = new SkillInteractiveSession(
        new File("./src/test/resources/waves"));

    // Start the session
    session.start();

    // create a command template for the command "plus"
    SkillCommandTemplate template = SkillCommandTemplate.build("getWaves");

    // create the command "(plus 1 41)"
    SkillCommand command = template.buildCommand();

    // evaluate the command in the session
    SkillDataobject obj = session.evaluate(command);

    obj.writeSkillDataobjectToXML(TSTFILE);

    SkillDataobject obj2 = SkillDataobject.getSkillDataobjectFromXML(TSTFILE);

    if (!obj.equals(obj2)) {
      fail("Waveforms dont match");
    }

    session.stop();

    if (TSTFILE.exists()) {
      TSTFILE.delete();
    }
  }
}