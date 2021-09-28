package edlab.eda.cadence.rc.api;

import java.util.Set;

/**
 * Template of a SKILL-Command with keyword parameters
 *
 */
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
   * Get keyword parameters of the SKILL-Command template
   * 
   * @return Names of keyword-parameters
   */
  public Set<String> getKeywordParameters() {
    return keywordParameters;
  }

  /**
   * Build a SKILL command template with keyword parameters
   * 
   * @param name              Name of the command
   * @param keywordParameters Keyword parameters
   * @return SKILL-Command template
   */
  public static KeywordParameterSkillCommandTemplate build(String name,
      Set<String> keywordParameters) {
    return new KeywordParameterSkillCommandTemplate(name, keywordParameters);
  }

  /**
   * Build a SKILL command template with formal and keyword parameters
   * 
   * @param name              Name of the command
   * @param formalParameters  Number of formal parameters
   * @param keywordParameters Keyword parameters
   * @return SKILL-Command template
   */
  public static KeywordParameterSkillCommandTemplate build(String name,
      int formalParameters, Set<String> keywordParameters) {

    if (formalParameters < 0) {
      return null;
    } else {
      return new KeywordParameterSkillCommandTemplate(name, formalParameters,
          keywordParameters);
    }
  }

  /**
   * Build a SKILL command template with keyword parameters and rest
   * 
   * @param name              Name of the command
   * @param keywordParameters Keyword parameters
   * @param canHaveRest       <code>true</code> when the command can have rest,
   *                          <code>false</code> otherwise
   * @return SKILL-Command template
   */
  public static KeywordParameterSkillCommandTemplate build(String name,
      Set<String> keywordParameters, boolean canHaveRest) {
    return new KeywordParameterSkillCommandTemplate(name, keywordParameters,
        canHaveRest);
  }

  /**
   * Build a SKILL command template with formal and keyword parameters and rest
   * 
   * @param name              Name of the command
   * @param formalParameters  Number of formal parameters
   * @param keywordParameters Keyword parameters
   * @param canHaveRest       <code>true</code> when the command can have rest,
   *                          <code>false</code> otherwise
   * @return SKILL-Command template
   */
  public static KeywordParameterSkillCommandTemplate build(String name,
      int formalParameters, Set<String> keywordParameters,
      boolean canHaveRest) {

    if (formalParameters < 0) {
      return null;
    } else {
      return new KeywordParameterSkillCommandTemplate(name, formalParameters,
          keywordParameters, canHaveRest);
    }
  }
}