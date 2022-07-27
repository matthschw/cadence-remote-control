package edlab.eda.cadence.rc;

import static org.junit.jupiter.api.Assertions.*;
import static com.github.stefanbirkner.systemlambda.SystemLambda.*;

import java.io.File;

import org.junit.jupiter.api.Test;

import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.api.SkillCommandTemplate;
import edlab.eda.cadence.rc.data.SkillFixnum;
import edlab.eda.cadence.rc.session.EvaluableToSkill;
import edlab.eda.cadence.rc.session.SkillInteractiveSession;
import edlab.eda.cadence.rc.session.SkillSession;

class AlteringPromptEnvVarSessionTest {

  @Test
  void test() throws Exception {

    withEnvironmentVariable(SkillSession.ENVVAR_PROMPT, "Ready >")
        .execute(() -> {
          SkillInteractiveSession session = null;

          try {

            session = new SkillInteractiveSession(
                new File("./src/test/resources/alterPrompt"));

            // Start the session
            session.start();

            // create a command template for the command "plus"
            SkillCommandTemplate template = SkillCommandTemplate.build("plus",
                2);

            // create the command "(plus 1 41)"
            SkillCommand command = template
                .buildCommand(new EvaluableToSkill[] { new SkillFixnum(1),
                    new SkillFixnum(41) });

            // evaluate the command in the session
            SkillFixnum obj = (SkillFixnum) session.evaluate(command);

            if (obj.getFixnum() != 42) {
              fail("Evaluation failed");
            }

            session.stop();
          } catch (Exception e) {

            session.stop();

            try {
              Thread.sleep(1000);
            } catch (InterruptedException e2) {
            }

            throw e;
          }
        });
  }
}