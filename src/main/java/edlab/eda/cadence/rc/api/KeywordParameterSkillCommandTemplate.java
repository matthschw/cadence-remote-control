package edlab.eda.cadence.rc.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edlab.eda.cadence.rc.EvaluableToSkill;

public class KeywordParameterSkillCommandTemplate extends SkillCommandTemplate {

  private Set<String> keywordParameters;

  private KeywordParameterSkillCommandTemplate(String name,
      Set<String> keywordParameters) {
    super(name);
    this.keywordParameters = keywordParameters;
  }

  private KeywordParameterSkillCommandTemplate(String name,
      int numberOfFormalParameters, Set<String> keywordParameters) {
    super(name, numberOfFormalParameters);
    this.keywordParameters = keywordParameters;
  }

  private KeywordParameterSkillCommandTemplate(String name,
      Set<String> keywordParameters, boolean canHaveRest) {
    super(name, canHaveRest);
    this.keywordParameters = keywordParameters;
  }

  private KeywordParameterSkillCommandTemplate(String name,
      int numberOfFormalParameters, Set<String> keywordParameters,
      boolean canHaveRest) {
    super(name, numberOfFormalParameters, canHaveRest);
    this.keywordParameters = keywordParameters;
  }

  /**
   * Get number of keyword parameters of the SKILL-Command template
   * 
   * @return Names of keyword-parameters
   */
  public Set<String> getKeywordParameters() {
    return keywordParameters;
  }

  public static KeywordParameterSkillCommandTemplate build(String name,
      Set<String> keywordParameters) {
    return new KeywordParameterSkillCommandTemplate(name, keywordParameters);
  }

  public static KeywordParameterSkillCommandTemplate build(String name,
      int numberOfFormalParameters, Set<String> keywordParameters) {

    if (numberOfFormalParameters < 0) {
      return null;
    } else {
      return new KeywordParameterSkillCommandTemplate(name,
          numberOfFormalParameters, keywordParameters);
    }
  }

  public static KeywordParameterSkillCommandTemplate build(String name,
      Set<String> keywordParameters, boolean canHaveRest) {
    return new KeywordParameterSkillCommandTemplate(name, keywordParameters,
        canHaveRest);
  }

  public static KeywordParameterSkillCommandTemplate build(String name,
      int numberOfFormalParameters, Set<String> keywordParameters,
      boolean canHaveRest) {

    if (numberOfFormalParameters < 0) {
      return null;
    } else {
      return new KeywordParameterSkillCommandTemplate(name,
          numberOfFormalParameters, keywordParameters, canHaveRest);
    }
  }

  public SkillCommand buildCommand(
      Map<String, EvaluableToSkill> keywordParameter) {
    return null;
  }

  public SkillCommand buildCommand(EvaluableToSkill formalParamater,
      Map<String, EvaluableToSkill> keywordParameter) {
    return null;
  }

  public SkillCommand buildCommand(EvaluableToSkill[] formalParamaters,
      Map<String, EvaluableToSkill> keywordParameter) {
    return null;
  }

  public SkillCommand buildCommand(
      Map<String, EvaluableToSkill> keywordParameter,
      List<EvaluableToSkill> rest) {
    return null;
  }

  public SkillCommand buildCommand(EvaluableToSkill formalParamater,
      Map<String, EvaluableToSkill> keywordParameter,
      List<EvaluableToSkill> rest) {
    return null;
  }

  public SkillCommand buildCommand(EvaluableToSkill[] formalParamaters,
      Map<String, EvaluableToSkill> keywordParameter,
      List<EvaluableToSkill> rest) {
    return null;
  }
}