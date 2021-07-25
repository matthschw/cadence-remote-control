package edlab.eda.cadence.rc;

import edlab.eda.cadence.rc.api.GenericSkillCommandTemplates;
import edlab.eda.cadence.rc.api.GenericSkillCommands;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.data.SkillString;

public class Test {

  public static void main(String[] args) {

    SkillMasterSession session = new SkillMasterSession();

    session.start();

    SkillCommand command = SkillCommand.buildCommand(
        GenericSkillCommandTemplates
            .getTemplate(GenericSkillCommands.OUTFILE),
        new SkillDataobject[] { new SkillString("fuubar.txt") });

    SkillDataobject port = session.evaluate(command);

    command = SkillCommand.buildCommand(
        GenericSkillCommandTemplates
            .getTemplate(GenericSkillCommands.FPRINTF),
        new SkillDataobject[] { port, new SkillString("Heinz Banane") });
    session.evaluate(command);

    command = SkillCommand.buildCommand(
        GenericSkillCommandTemplates.getTemplate(GenericSkillCommands.CLOSE),
        new SkillDataobject[] { port });

    session.evaluate(command);

    session.stop();
  }
}