package edlab.eda.cadence.rc;

import java.io.IOException;
import java.util.Scanner;

import edlab.eda.cadence.rc.api.GenericSkillCommandTemplates;
import edlab.eda.cadence.rc.api.GenericSkillCommands;
import edlab.eda.cadence.rc.data.SkillDataobject;
import edlab.eda.cadence.rc.data.SkillString;

public class SlaveSession {

  public static void main(String[] args) throws IOException {

    SkillChildSession session = new SkillChildSession(new Scanner(System.in));

    SkillCommand command = SkillCommand.buildCommand(
        GenericSkillCommandTemplates
            .getTemplate(GenericSkillCommands.OUTFILE),
        new SkillDataobject[] { new SkillString("fuubar2.txt") });

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
    
    
   
  }

}
