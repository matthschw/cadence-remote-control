package edlab.eda.cadence.rc;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;

import edlab.eda.cadence.rc.api.GenericSkillCommandTemplates;
import edlab.eda.cadence.rc.api.IncorrectSyntaxException;
import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.data.SkillString;

public class SocketExample {

  public static void main(String[] args) throws UnknownHostException,
      IOException, IncorrectSyntaxException, UnableToStartSkillSession,
      EvaluationFailedException, InvalidDataobjectReferenceExecption {

    SkillSocketSession session = new SkillSocketSession(42773);
    session.start();

    LinkedList<EvaluableToSkill> rest = new LinkedList<EvaluableToSkill>();

    rest.add(new SkillString("xx"));
    rest.add(new SkillString("yy"));

    SkillCommand command = GenericSkillCommandTemplates
        .getTemplate(GenericSkillCommandTemplates.STRCAT)
        .buildCommand(new SkillString("zz"), rest);

    session.evaluate(command);

    session.stop();

  }
}