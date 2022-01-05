package edlab.eda.cadence.rc;

import edlab.eda.cadence.rc.api.IncorrectSyntaxException;
import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.api.SkillCommandTemplate;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.data.SkillFixnum;
import edlab.eda.cadence.rc.session.EvaluableToSkill;
import edlab.eda.cadence.rc.session.EvaluationFailedException;
import edlab.eda.cadence.rc.session.InvalidDataobjectReferenceExecption;
import edlab.eda.cadence.rc.session.SkillInteractiveSession;
import edlab.eda.cadence.rc.session.UnableToStartSession;

public class SkillInteractiveExample {

  public static void main(String[] args)
      throws UnableToStartSession, EvaluationFailedException,
      IncorrectSyntaxException, InvalidDataobjectReferenceExecption {

    // Create an interactive session
    SkillInteractiveSession session = new SkillInteractiveSession();

    // Start the session
    session.start();

    // create a command template for the command "plus"
    SkillCommandTemplate template = SkillCommandTemplate.build("plus", 2);

    // create the command "(plus 1 41)"
    SkillCommand command = template.buildCommand(
        new EvaluableToSkill[] { new SkillFixnum(1), new SkillFixnum(41) });

    // evaluate the command in the session
    SkillDataobject obj = session.evaluate(command);

    // typecast result
    @SuppressWarnings("unused")
    SkillFixnum num = (SkillFixnum) obj;

    // close session
    session.stop();
  }
}
