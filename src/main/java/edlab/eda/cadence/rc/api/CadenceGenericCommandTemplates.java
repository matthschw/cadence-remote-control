package edlab.eda.cadence.rc.api;

import java.util.HashMap;
import java.util.Map;

public class CadenceGenericCommandTemplates {

  private static CadenceGenericCommandTemplates commandTemplates = null;

  private Map<String, CadenceCommandTemplate> templates;

  private CadenceGenericCommandTemplates() {
    templates = new HashMap<String, CadenceCommandTemplate>();
    templates.put(CadenceSkillCommands.EXIT,
        new CadenceCommandTemplate(CadenceSkillCommands.EXIT));
    templates.put(CadenceSkillCommands.LOAD,
        new CadenceCommandTemplate(CadenceSkillCommands.LOAD, 1));
    templates.put(CadenceSkillCommands.OUTFILE,
        new CadenceCommandTemplate(CadenceSkillCommands.OUTFILE, 1));
    templates.put(CadenceSkillCommands.FPRINTF,
        new CadenceCommandTemplate(CadenceSkillCommands.FPRINTF, 2));
    templates.put(CadenceSkillCommands.CLOSE,
        new CadenceCommandTemplate(CadenceSkillCommands.CLOSE, 1));
  }

  public static CadenceCommandTemplate getTemplate(String name) {
    if (commandTemplates == null) {
      commandTemplates = new CadenceGenericCommandTemplates();
    }

    return commandTemplates.templates.get(name);
  }

}
