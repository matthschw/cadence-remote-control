package edlab.eda.cadence.rc;

import java.util.Map;

import edlab.eda.cadence.rc.api.CadenceCommandTemplate;
import edlab.eda.cadence.rc.data.SkillDataobject;

public class CadenceCommand {

  private CadenceCommandTemplate template;
  private SkillDataobject[] formalParameters;
  private Map<String, SkillDataobject> keywordParameters;

  private CadenceCommand(CadenceCommandTemplate template) {
    this.template = template;
  }

  private CadenceCommand(CadenceCommandTemplate template,
      SkillDataobject[] formalParameters) {
    this.template = template;
    this.formalParameters = formalParameters;
  }

  private CadenceCommand(CadenceCommandTemplate template,
      Map<String, SkillDataobject> keywordParameters) {
    this.template = template;
    this.keywordParameters = keywordParameters;
  }

  private CadenceCommand(CadenceCommandTemplate template,
      SkillDataobject[] formalParameters,
      Map<String, SkillDataobject> keywordParameters) {
    this.template = template;
    this.formalParameters = formalParameters;
    this.keywordParameters = keywordParameters;
  }

  public static CadenceCommand buildCommand(CadenceCommandTemplate template) {
    return new CadenceCommand(template);
  }

  public String toSkill() {
    String retval = "(" + template.getName();

    for (int i = 0; i < formalParameters.length; i++) {
      retval += " " + formalParameters[i].toSkill();
    }

    for (String key : keywordParameters.keySet()) {
      retval += " ?" + key + " " + keywordParameters.get(key).toSkill();
    }

    retval += ")";

    return retval;
  }

  public static CadenceCommand buildCommand(CadenceCommandTemplate template,
      SkillDataobject[] formalParameters) {

    if (template.getNumOfFormalParameters() == formalParameters.length) {
      return new CadenceCommand(template, formalParameters);
    }
    return null;
  }

  public static CadenceCommand buildCommand(CadenceCommandTemplate template,
      Map<String, SkillDataobject> keywordParameters) {

    for (String key : keywordParameters.keySet()) {

      if (!template.getKeywordParameters().contains(key)) {
        return null;
      }
    }

    return new CadenceCommand(template, keywordParameters);
  }

  public static CadenceCommand buildCommand(CadenceCommandTemplate template,
      SkillDataobject[] formalParameters,
      Map<String, SkillDataobject> keywordParameters) {

    for (String key : keywordParameters.keySet()) {

      if (!template.getKeywordParameters().contains(key)) {
        return null;
      }
    }
    if (template.getNumOfFormalParameters() == formalParameters.length) {
      return new CadenceCommand(template, formalParameters, keywordParameters);
    } else {
      return null;
    }
  }
}
