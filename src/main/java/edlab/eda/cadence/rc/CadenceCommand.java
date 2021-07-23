package edlab.eda.cadence.rc;

import java.util.HashMap;
import java.util.Map;

import edlab.eda.cadence.rc.api.CadenceCommandTemplate;
import edlab.eda.cadence.rc.data.SkillDataobject;

public class CadenceCommand implements EvaluateableInVirtuoso {

  private CadenceCommandTemplate template;
  private EvaluateableInVirtuoso[] formalParameters;
  private Map<String, EvaluateableInVirtuoso> keywordParameters;

  private CadenceCommand(CadenceCommandTemplate template) {
    this.template = template;
    this.formalParameters = new SkillDataobject[0];
    this.keywordParameters = new HashMap<String, EvaluateableInVirtuoso>();
  }

  private CadenceCommand(CadenceCommandTemplate template,
      EvaluateableInVirtuoso[] formalParameters) {
    this.template = template;
    this.formalParameters = formalParameters;
    this.keywordParameters = new HashMap<String, EvaluateableInVirtuoso>();
  }

  private CadenceCommand(CadenceCommandTemplate template,
      Map<String, EvaluateableInVirtuoso> keywordParameters) {
    this.template = template;
    this.formalParameters = new SkillDataobject[0];
    this.keywordParameters = keywordParameters;
  }

  private CadenceCommand(CadenceCommandTemplate template,
      EvaluateableInVirtuoso[] formalParameters,
      Map<String, EvaluateableInVirtuoso> keywordParameters) {
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
      EvaluateableInVirtuoso[] formalParameters) {

    if (template.getNumOfFormalParameters() == formalParameters.length) {
      return new CadenceCommand(template, formalParameters);
    }
    return null;
  }

  public static CadenceCommand buildCommand(CadenceCommandTemplate template,
      Map<String, EvaluateableInVirtuoso> keywordParameters) {

    for (String key : keywordParameters.keySet()) {

      if (!template.getKeywordParameters().contains(key)) {
        return null;
      }
    }

    return new CadenceCommand(template, keywordParameters);
  }

  public static CadenceCommand buildCommand(CadenceCommandTemplate template,
      EvaluateableInVirtuoso[] formalParameters,
      Map<String, EvaluateableInVirtuoso> keywordParameters) {

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
