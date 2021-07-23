package edlab.eda.cadence.rc.api;

import java.util.HashMap;
import java.util.Map;

public class CadenceGenericCommandTemplates {

  private Map<String, CadenceCommandTemplate> templates;

  public CadenceGenericCommandTemplates() {
    templates = new HashMap<String, CadenceCommandTemplate>();
    templates.put(CadenceSkillCommands.EXIT,
        new CadenceCommandTemplate(CadenceSkillCommands.EXIT));
    templates.put(CadenceSkillCommands.LOAD,
        new CadenceCommandTemplate(CadenceSkillCommands.LOAD, 1));
  }

  public CadenceCommandTemplate getTemplate(String name) {
    return templates.get(name);
  }

}
