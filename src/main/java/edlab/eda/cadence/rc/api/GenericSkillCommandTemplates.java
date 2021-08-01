package edlab.eda.cadence.rc.api;

import java.util.HashMap;
import java.util.Map;

public class GenericSkillCommandTemplates {

  private static GenericSkillCommandTemplates commandTemplates = null;

  private Map<String, SkillCommandTemplate> templates;

  private GenericSkillCommandTemplates() {

    templates = new HashMap<String, SkillCommandTemplate>();

    templates.put(GenericSkillCommands.SET,
        new SkillCommandTemplate(GenericSkillCommands.SET, 2));
    templates.put(GenericSkillCommands.GET,
        new SkillCommandTemplate(GenericSkillCommands.GET, 2));

    templates.put(GenericSkillCommands.CAR,
        new SkillCommandTemplate(GenericSkillCommands.CAR, 1));
    templates.put(GenericSkillCommands.CADR,
        new SkillCommandTemplate(GenericSkillCommands.CADR, 1));
    templates.put(GenericSkillCommands.NTH,
        new SkillCommandTemplate(GenericSkillCommands.NTH, 2));
    templates.put(GenericSkillCommands.LIST,
        new SkillCommandTemplate(GenericSkillCommands.LIST));
    
    templates.put(GenericSkillCommands.APPLY,
        new SkillCommandTemplate(GenericSkillCommands.APPLY, 2));
    templates.put(GenericSkillCommands.EVAL,
        new SkillCommandTemplate(GenericSkillCommands.EVAL, 2));
    
    templates.put(GenericSkillCommands.ARRAYREF,
        new SkillCommandTemplate(GenericSkillCommands.ARRAYREF, 2));

    templates.put(GenericSkillCommands.OUTFILE,
        new SkillCommandTemplate(GenericSkillCommands.OUTFILE, 1));
    templates.put(GenericSkillCommands.FPRINTF,
        new SkillCommandTemplate(GenericSkillCommands.FPRINTF, 2));
    templates.put(GenericSkillCommands.CLOSE,
        new SkillCommandTemplate(GenericSkillCommands.CLOSE, 1));

    templates.put(GenericSkillCommands.ERRSET,
        new SkillCommandTemplate(GenericSkillCommands.ERRSET, 1));
    templates.put(GenericSkillCommands.EXIT,
        new SkillCommandTemplate(GenericSkillCommands.EXIT, 0));
    templates.put(GenericSkillCommands.LOAD,
        new SkillCommandTemplate(GenericSkillCommands.LOAD, 1));
    templates.put(GenericSkillCommands.LOADI,
        new SkillCommandTemplate(GenericSkillCommands.LOADI, 1));
    templates.put(GenericSkillCommands.SET_PROMPTS,
        new SkillCommandTemplate(GenericSkillCommands.SET_PROMPTS, 2));
  }

  public static SkillCommandTemplate getTemplate(String name) {
    if (commandTemplates == null) {
      commandTemplates = new GenericSkillCommandTemplates();
    }

    return commandTemplates.templates.get(name);
  }

}
