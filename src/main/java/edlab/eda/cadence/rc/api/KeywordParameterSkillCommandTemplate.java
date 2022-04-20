package edlab.eda.cadence.rc.api;

import java.util.Set;

/**
 * Template of a Skill command with keyword parameters
 *
 */
public final class KeywordParameterSkillCommandTemplate
    extends SkillCommandTemplate {

  private final Set<String> keywordParameters;

  private KeywordParameterSkillCommandTemplate(final String name,
      final Set<String> keywordParameters) {
    super(name);
    this.keywordParameters = keywordParameters;
  }

  private KeywordParameterSkillCommandTemplate(final String name,
      final int numberOfFormalParameters, final Set<String> keywordParameters) {
    super(name, numberOfFormalParameters);
    this.keywordParameters = keywordParameters;
  }

  private KeywordParameterSkillCommandTemplate(final String name,
      final Set<String> keywordParameters, final boolean canHaveRest) {
    super(name, canHaveRest);
    this.keywordParameters = keywordParameters;
  }

  private KeywordParameterSkillCommandTemplate(final String name,
      final int numberOfFormalParameters, final Set<String> keywordParameters,
      final boolean canHaveRest) {
    super(name, numberOfFormalParameters, canHaveRest);
    this.keywordParameters = keywordParameters;
  }

  /**
   * Get keyword parameters of the Skill-Command template
   *
   * @return Names of keyword-parameters
   */
  public Set<String> getKeywordParameters() {
    return this.keywordParameters;
  }

  /**
   * Build a Skill command template with keyword parameters
   *
   * @param name              Name of the command
   * @param keywordParameters Keyword parameters
   * @return Skill-Command template
   */
  public static KeywordParameterSkillCommandTemplate build(final String name,
      final Set<String> keywordParameters) {
    return new KeywordParameterSkillCommandTemplate(name, keywordParameters);
  }

  /**
   * Build a Skill command template with formal and keyword parameters
   *
   * @param name              Name of the command
   * @param formalParameters  Number of formal parameters
   * @param keywordParameters Keyword parameters
   * @return Skill-Command template
   */
  public static KeywordParameterSkillCommandTemplate build(final String name,
      final int formalParameters, final Set<String> keywordParameters) {

    if (formalParameters < 0) {
      return null;
    } else {
      return new KeywordParameterSkillCommandTemplate(name, formalParameters,
          keywordParameters);
    }
  }

  /**
   * Build a Skill command template with keyword parameters and rest
   *
   * @param name              Name of the command
   * @param keywordParameters Keyword parameters
   * @param canHaveRest       <code>true</code> when the command can have rest,
   *                          <code>false</code> otherwise
   * @return Skill-Command template
   */
  public static KeywordParameterSkillCommandTemplate build(final String name,
      final Set<String> keywordParameters, final boolean canHaveRest) {
    return new KeywordParameterSkillCommandTemplate(name, keywordParameters,
        canHaveRest);
  }

  /**
   * Build a Skill command template with formal and keyword parameters and rest
   *
   * @param name              Name of the command
   * @param formalParameters  Number of formal parameters
   * @param keywordParameters Keyword parameters
   * @param canHaveRest       <code>true</code> when the command can have rest,
   *                          <code>false</code> otherwise
   * @return Skill-Command template
   */
  public static KeywordParameterSkillCommandTemplate build(final String name,
      final int formalParameters, final Set<String> keywordParameters,
      final boolean canHaveRest) {

    if (formalParameters < 0) {
      return null;
    } else {
      return new KeywordParameterSkillCommandTemplate(name, formalParameters,
          keywordParameters, canHaveRest);
    }
  }
}