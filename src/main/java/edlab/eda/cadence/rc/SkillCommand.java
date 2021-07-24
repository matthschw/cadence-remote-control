package edlab.eda.cadence.rc;

import java.util.HashMap;
import java.util.Map;

import edlab.eda.cadence.rc.api.SkillCommandTemplate;
import edlab.eda.cadence.rc.data.SkillDataobject;

public class SkillCommand implements EvaluateabletoSkill {

  private SkillCommandTemplate template;
  private EvaluateabletoSkill[] formalParameters;
  private Map<String, EvaluateabletoSkill> keywordParameters;

  private SkillCommand(SkillCommandTemplate template) {
    this.template = template;
    this.formalParameters = new SkillDataobject[0];
    this.keywordParameters = new HashMap<String, EvaluateabletoSkill>();
  }

  private SkillCommand(SkillCommandTemplate template,
      EvaluateabletoSkill[] formalParameters) {
    this.template = template;
    this.formalParameters = formalParameters;
    this.keywordParameters = new HashMap<String, EvaluateabletoSkill>();
  }

  private SkillCommand(SkillCommandTemplate template,
      Map<String, EvaluateabletoSkill> keywordParameters) {
    this.template = template;
    this.formalParameters = new SkillDataobject[0];
    this.keywordParameters = keywordParameters;
  }

  private SkillCommand(SkillCommandTemplate template,
      EvaluateabletoSkill[] formalParameters,
      Map<String, EvaluateabletoSkill> keywordParameters) {
    this.template = template;
    this.formalParameters = formalParameters;
    this.keywordParameters = keywordParameters;
  }

  public static SkillCommand buildCommand(SkillCommandTemplate template) {
    return new SkillCommand(template);
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

  public static SkillCommand buildCommand(SkillCommandTemplate template,
      EvaluateabletoSkill[] formalParameters) {

    if (template.getNumOfFormalParameters() == formalParameters.length) {
      return new SkillCommand(template, formalParameters);
    }
    return null;
  }

  public static SkillCommand buildCommand(SkillCommandTemplate template,
      EvaluateabletoSkill formalParameter) {

    if (template.getNumOfFormalParameters() == 1) {
      return new SkillCommand(template,
          new EvaluateabletoSkill[] { formalParameter });
    }
    return null;
  }

  public static SkillCommand buildCommand(SkillCommandTemplate template,
      Map<String, EvaluateabletoSkill> keywordParameters) {

    for (String key : keywordParameters.keySet()) {

      if (!template.getKeywordParameters().contains(key)) {
        return null;
      }
    }

    return new SkillCommand(template, keywordParameters);
  }

  public static SkillCommand buildCommand(SkillCommandTemplate template,
      EvaluateabletoSkill[] formalParameters,
      Map<String, EvaluateabletoSkill> keywordParameters) {

    for (String key : keywordParameters.keySet()) {

      if (!template.getKeywordParameters().contains(key)) {
        return null;
      }
    }
    if (template.getNumOfFormalParameters() == formalParameters.length) {
      return new SkillCommand(template, formalParameters, keywordParameters);
    } else {
      return null;
    }
  }
}
