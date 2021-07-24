package edlab.eda.cadence.rc.api;

import java.util.HashMap;
import java.util.Map;

public class GenericSkillCommandTemplates {

  private static GenericSkillCommandTemplates commandTemplates = null;

  private Map<String, SkillCommandTemplate> templates;

  private GenericSkillCommandTemplates() {
    templates = new HashMap<String, SkillCommandTemplate>();
    templates.put(GenericSkillCommands.EXIT,
        new SkillCommandTemplate(GenericSkillCommands.EXIT));
    templates.put(GenericSkillCommands.LOAD,
        new SkillCommandTemplate(GenericSkillCommands.LOAD, 1));
    templates.put(GenericSkillCommands.OUTFILE,
        new SkillCommandTemplate(GenericSkillCommands.OUTFILE, 1));
    templates.put(GenericSkillCommands.FPRINTF,
        new SkillCommandTemplate(GenericSkillCommands.FPRINTF, 2));
    templates.put(GenericSkillCommands.CLOSE,
        new SkillCommandTemplate(GenericSkillCommands.CLOSE, 1));
    templates.put(GenericSkillCommands.ERRSET,
        new SkillCommandTemplate(GenericSkillCommands.ERRSET, 1));

  }

  public static SkillCommandTemplate getTemplate(String name) {
    if (commandTemplates == null) {
      commandTemplates = new GenericSkillCommandTemplates();
    }

    return commandTemplates.templates.get(name);
  }

}
