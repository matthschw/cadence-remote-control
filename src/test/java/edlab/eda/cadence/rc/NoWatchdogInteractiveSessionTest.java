package edlab.eda.cadence.rc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.api.SkillCommandTemplate;
import edlab.eda.cadence.rc.data.SkillFixnum;
import edlab.eda.cadence.rc.session.EvaluableToSkill;
import edlab.eda.cadence.rc.session.SkillInteractiveSession;

import static com.github.stefanbirkner.systemlambda.SystemLambda.*;

class NoWatchdogInteractiveSessionTest {

  @Test
  void test() throws Exception {

    withEnvironmentVariable(SkillInteractiveSession.ENVVAR_NO_WATCHDOG, "1")
        .execute(() -> {

          SkillInteractiveSession session = new SkillInteractiveSession();

          // Start the session
          session.start();

          // create a command template for the command "plus"
          SkillCommandTemplate template = SkillCommandTemplate.build("plus", 2);

          // create the command "(plus 1 41)"
          SkillCommand command = template.buildCommand(new EvaluableToSkill[] {
              new SkillFixnum(1), new SkillFixnum(41) });

          // evaluate the command in the session
          SkillFixnum obj = (SkillFixnum) session.evaluate(command);

          if (obj.getFixnum() != 42) {
            fail("Evaluation failed");
          }
          session.stop();
        });
  }

}
