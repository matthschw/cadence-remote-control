package edlab.eda.cadence.rc;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;

import edlab.eda.cadence.rc.api.GenericSkillCommandTemplates;
import edlab.eda.cadence.rc.api.IncorrectSyntaxException;
import edlab.eda.cadence.rc.api.SkillCommand;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.data.SkillString;

public class SocketExample {

  public static void main(String[] args) throws UnknownHostException,
      IOException, IncorrectSyntaxException, UnableToStartSkillSession,
      EvaluationFailedException, InvalidDataobjectReferenceExecption {

    SkillSocketSession session = new SkillSocketSession(38401);
    
    session.start();

    LinkedList<EvaluableToSkill> rest = new LinkedList<EvaluableToSkill>();

    rest.add(new SkillString("xx"));
    rest.add(new SkillString("yy44"));

    SkillCommand command = GenericSkillCommandTemplates
        .getTemplate(GenericSkillCommandTemplates.STRCAT)
        .buildCommand(new SkillString("zzd"), rest);

    SkillDataobject a = session.evaluate(command);
    
    System.out.println(a.toSkill());

    session.stop();
  }
}