package edlab.eda.cadence.rc;

import java.util.HashMap;
import java.util.Map;

import edlab.eda.cadence.rc.api.SkillCommandTemplate;
import edlab.eda.cadence.rc.data.SkillDataobject;

/**
 * Class which represents a Skill-Command
 *
 */
public class SkillCommand implements EvaluateableToSkill {

  private SkillCommandTemplate template;
  private EvaluateableToSkill[] formalParameters;
  private Map<String, EvaluateableToSkill> keywordParameters;

  private SkillCommand(SkillCommandTemplate template) {
    this.template = template;
    this.formalParameters = new SkillDataobject[0];
    this.keywordParameters = new HashMap<String, EvaluateableToSkill>();
  }

  private SkillCommand(SkillCommandTemplate template,
      EvaluateableToSkill[] formalParameters) {
    this.template = template;
    this.formalParameters = formalParameters;
    this.keywordParameters = new HashMap<String, EvaluateableToSkill>();
  }

  private SkillCommand(SkillCommandTemplate template,
      Map<String, EvaluateableToSkill> keywordParameters) {
    this.template = template;
    this.formalParameters = new SkillDataobject[0];
    this.keywordParameters = keywordParameters;
  }

  private SkillCommand(SkillCommandTemplate template,
      EvaluateableToSkill[] formalParameters,
      Map<String, EvaluateableToSkill> keywordParameters) {
    this.template = template;
    this.formalParameters = formalParameters;
    this.keywordParameters = keywordParameters;
  }
  
  @Override
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

  /**
   * 
   * @param template
   * @return
   */
  public static SkillCommand buildCommand(SkillCommandTemplate template) {
    return new SkillCommand(template);
  }

  public static SkillCommand buildCommand(SkillCommandTemplate template,
      EvaluateableToSkill[] formalParameters) {

    if (template.getNumOfFormalParameters() < 0
        || template.getNumOfFormalParameters() == formalParameters.length) {
      return new SkillCommand(template, formalParameters);
    }

    return null;
  }

  public static SkillCommand buildCommand(SkillCommandTemplate template,
      EvaluateableToSkill formalParameter) {

    if (template.getNumOfFormalParameters() == 1) {
      return new SkillCommand(template,
          new EvaluateableToSkill[] { formalParameter });
    }
    return null;
  }

  public static SkillCommand buildCommand(SkillCommandTemplate template,
      Map<String, EvaluateableToSkill> keywordParameters) {

    for (String key : keywordParameters.keySet()) {

      if (!template.getKeywordParameters().contains(key)) {
        return null;
      }
    }

    return new SkillCommand(template, keywordParameters);
  }

  public static SkillCommand buildCommand(SkillCommandTemplate template,
      EvaluateableToSkill[] formalParameters,
      Map<String, EvaluateableToSkill> keywordParameters) {

    for (String key : keywordParameters.keySet()) {

      if (!template.getKeywordParameters().contains(key)) {
        return null;
      }
    }

    if (template.getNumOfFormalParameters() < 0
        || template.getNumOfFormalParameters() == formalParameters.length) {
      return new SkillCommand(template, formalParameters, keywordParameters);
    } else {
      return null;
    }
  }
}
