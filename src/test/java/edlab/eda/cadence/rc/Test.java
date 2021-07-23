package edlab.eda.cadence.rc;

import edlab.eda.cadence.rc.api.CadenceGenericCommandTemplates;
import edlab.eda.cadence.rc.api.CadenceSkillCommands;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.data.SkillString;

public class Test {

  public static void main(String[] args) {

    Session session = new Session();

    session.start();

    CadenceCommand command = CadenceCommand.buildCommand(
        CadenceGenericCommandTemplates
            .getTemplate(CadenceSkillCommands.OUTFILE),
        new SkillDataobject[] { new SkillString("fuubar.txt") });

    SkillDataobject port = session.evaluate(command);
    System.out.println(port.toSkill());

    command = CadenceCommand.buildCommand(
        CadenceGenericCommandTemplates
            .getTemplate(CadenceSkillCommands.FPRINTF),
        new SkillDataobject[] { port, new SkillString("Heinz Banane") });

    command = CadenceCommand.buildCommand(
        CadenceGenericCommandTemplates.getTemplate(CadenceSkillCommands.CLOSE),
        new SkillDataobject[] { port });

    session.evaluate(command);

    session.stop();

  }

}
