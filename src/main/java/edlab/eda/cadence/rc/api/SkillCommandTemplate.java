package edlab.eda.cadence.rc.api;

import java.util.List;
import java.util.Map;

import edlab.eda.cadence.rc.session.EvaluableToSkill;

/**
 * Template of a SKILL-Command
 *
 */
public class SkillCommandTemplate {

  private String name;
  private int formalParameters;
  private boolean canHaveRest;

  /**
   * Create a SKILL-Command template with an arbitrary number of formal
   * parameters
   * 
   * @param name Name of the command
   */
  protected SkillCommandTemplate(String name) {
    this.name = name;
    this.formalParameters = 0;
    this.canHaveRest = false;
  }

  /**
   * Create a SKILL-Command template with an arbitrary number of formal
   * parameters
   * 
   * @param name Name of the command
   */
  protected SkillCommandTemplate(String name, boolean canHaveRest) {
    this.name = name;
    this.formalParameters = 0;
    this.canHaveRest = canHaveRest;
  }

  /**
   * Create a SKILL-Command template with an fixed number of formal parameters
   * 
   * @param name                  Name of the command
   * @param numOfFormalParameters Number of formal parameters
   */
  protected SkillCommandTemplate(String name, int numOfFormalParameters) {
    this.name = name;
    this.formalParameters = numOfFormalParameters;
    this.canHaveRest = false;
  }

  /**
   * Create a SKILL-Command template with an fixed number of formal parameters
   * 
   * @param name                  Name of the command
   * @param numOfFormalParameters Number of formal parameters
   */
  protected SkillCommandTemplate(String name, int numOfFormalParameters,
      boolean canHaveRest) {
    this.name = name;
    this.formalParameters = numOfFormalParameters;
    this.canHaveRest = canHaveRest;
  }

  /**
   * Get name of the command
   * 
   * @return name of command
   */
  public String getName() {
    return name;
  }

  /**
   * Get number of formal parameters of the SKILL-Command template
   * 
   * @return number of formal parameters
   */
  public int getFormalParameters() {
    return this.formalParameters;
  }

  /**
   * Returns if the the SKILL-Command can have a rest
   * 
   * @return <code>true</code> when the command can have rest,
   *         <code>false</code> otherwise
   */
  public boolean canHaveRest() {
    return this.canHaveRest;
  }

  /**
   * Build a SKILL command template
   * 
   * @param name Name of the command
   * @return SKILL-Command template
   */
  public static SkillCommandTemplate build(String name) {
    return new SkillCommandTemplate(name);
  }

  /**
   * Build a SKILL command template
   * 
   * @param name        Name of the command
   * @param canHaveRest <code>true</code> when the command can have rest,
   *                    <code>false</code> otherwise
   * @return SKILL-Command template
   */
  public static SkillCommandTemplate build(String name, boolean canHaveRest) {
    return new SkillCommandTemplate(name, canHaveRest);
  }

  /**
   * Build a SKILL command template
   * 
   * @param name             Name of the command
   * @param formalParameters Number of formal parameters
   * @return SKILL-Command template
   */
  public static SkillCommandTemplate build(String name, int formalParameters) {
    return new SkillCommandTemplate(name, formalParameters);
  }

  /**
   * Build a SKILL command template
   * 
   * @param name             Name of the command
   * @param formalParameters Number of formal parameters
   * @param canHaveRest      <code>true</code> when the command can have rest,
   *                         <code>false</code> otherwise
   * @return SKILL-Command template
   */
  public static SkillCommandTemplate build(String name, int formalParameters,
      boolean canHaveRest) {
    return new SkillCommandTemplate(name, formalParameters, canHaveRest);
  }

  /**
   * Build a SKILL command
   * 
   * @return SKILL command
   * @throws IncorrectSyntaxException when the provided parameters do not match
   *                                  with the template
   */
  public SkillCommand buildCommand() throws IncorrectSyntaxException {

    this.checkFormalParameters(0);
    return new SkillCommand(this, null, null, null);
  }

  /**
   * Build a SKILL command
   * 
   * @param formalParamater Single formal parameter
   * @return SKILL command
   * @throws IncorrectSyntaxException when the provided parameters do not match
   *                                  with the template
   */
  public SkillCommand buildCommand(EvaluableToSkill formalParamater)
      throws IncorrectSyntaxException {

    this.checkFormalParameters(1);

    return new SkillCommand(this, new EvaluableToSkill[] { formalParamater },
        null, null);
  }

  /**
   * Build a SKILL command
   * 
   * @param formalParamaters Array of formal parameters
   * @return SKILL command
   * @throws IncorrectSyntaxException when the number of provided parameters do
   *                                  not match
   */
  public SkillCommand buildCommand(EvaluableToSkill[] formalParamaters)
      throws IncorrectSyntaxException {

    this.checkFormalParameters(formalParamaters.length);

    return new SkillCommand(this, formalParamaters, null, null);
  }

  /**
   * Build a SKILL command
   * 
   * @param optionalAndRestParameters Optional parameters and rest as list
   * @return SKILL command
   * @throws IncorrectSyntaxException when the provided parameters do not match
   *                                  with the template
   */
  public SkillCommand buildCommand(
      List<EvaluableToSkill> optionalAndRestParameters)
      throws IncorrectSyntaxException {

    this.checkFormalParameters(0);

    if (!this.canHaveRest) {

      if (this instanceof OptionalParameterSkillCommandTemplate) {

        OptionalParameterSkillCommandTemplate template = (OptionalParameterSkillCommandTemplate) this;

        if (optionalAndRestParameters.size() > template
            .getOptionalParameters()) {

          throw IncorrectSyntaxException
              .createInvalidOptionalParametersExecption(this.name,
                  template.getOptionalParameters());
        }

      } else {
        throw IncorrectSyntaxException
            .createHasNoOptionalParametersExecption(this.name);
      }
    }

    return new SkillCommand(this, null, null, optionalAndRestParameters);
  }

  /**
   * Build a SKILL command
   * 
   * @param formalParamater           Single formal parameter
   * @param optionalAndRestParameters Optional parameters and rest as list
   * @return SKILL command
   * @throws IncorrectSyntaxException when the provided parameters do not match
   *                                  with the template
   */
  public SkillCommand buildCommand(EvaluableToSkill formalParamater,
      List<EvaluableToSkill> optionalAndRestParameters)
      throws IncorrectSyntaxException {

    this.checkFormalParameters(1);

    if (!this.canHaveRest) {

      if (this instanceof OptionalParameterSkillCommandTemplate) {

        OptionalParameterSkillCommandTemplate template = (OptionalParameterSkillCommandTemplate) this;

        if (optionalAndRestParameters.size() > template
            .getOptionalParameters()) {

          throw IncorrectSyntaxException
              .createInvalidOptionalParametersExecption(this.name,
                  template.getOptionalParameters());
        }

      } else {
        throw IncorrectSyntaxException
            .createHasNoOptionalParametersExecption(this.name);
      }
    }

    return new SkillCommand(this, new EvaluableToSkill[] { formalParamater },
        null, optionalAndRestParameters);
  }

  /**
   * Build a SKILL command
   * 
   * @param formalParamaters          Array of formal parameters
   * @param optionalAndRestParameters Optional parameters and rest as list
   * @return SKILL command
   * @throws IncorrectSyntaxException when the provided parameters do not match
   *                                  with the template
   */
  public SkillCommand buildCommand(EvaluableToSkill[] formalParamaters,
      List<EvaluableToSkill> optionalAndRestParameters)
      throws IncorrectSyntaxException {

    this.checkFormalParameters(formalParamaters.length);

    if (!this.canHaveRest) {

      if (this instanceof OptionalParameterSkillCommandTemplate) {

        OptionalParameterSkillCommandTemplate template = (OptionalParameterSkillCommandTemplate) this;

        if (optionalAndRestParameters.size() > template
            .getOptionalParameters()) {

          throw IncorrectSyntaxException
              .createInvalidOptionalParametersExecption(this.name,
                  template.getOptionalParameters());
        }

      } else {
        throw IncorrectSyntaxException
            .createHasNoOptionalParametersExecption(this.name);
      }
    }

    return new SkillCommand(this, formalParamaters, null,
        optionalAndRestParameters);
  }

  /**
   * Build a SKILL command
   * 
   * @param keywordParameters Map of keyword parameters
   * @return SKILL command
   * @throws IncorrectSyntaxException when the provided parameters do not match
   *                                  with the template
   */
  public SkillCommand buildCommand(
      Map<String, EvaluableToSkill> keywordParameters)
      throws IncorrectSyntaxException {

    this.checkFormalParameters(0);
    this.checkKeywordParameters(keywordParameters);

    return new SkillCommand(this, null, keywordParameters, null);
  }

  /**
   * Build a SKILL command
   * 
   * @param keywordParameters Map of keyword parameters
   * @param restParameters    Rest parameters as list
   * @return SKILL command
   * @throws IncorrectSyntaxException when the provided parameters do not match
   *                                  with the template
   */
  public SkillCommand buildCommand(
      Map<String, EvaluableToSkill> keywordParameters,
      List<EvaluableToSkill> restParameters) throws IncorrectSyntaxException {

    this.checkFormalParameters(0);

    if (!this.canHaveRest && !restParameters.isEmpty()) {
      throw IncorrectSyntaxException.createHasNoRestExecption(this.name);
    }

    this.checkKeywordParameters(keywordParameters);

    return new SkillCommand(this, null, keywordParameters, restParameters);
  }

  /**
   * Build a SKILL command
   * 
   * @param formalParamater   Single formal parameter
   * @param keywordParameters Map of keyword parameters
   * @return
   * @throws IncorrectSyntaxException when the provided parameters do not match
   *                                  with the template
   */
  public SkillCommand buildCommand(EvaluableToSkill formalParamater,
      Map<String, EvaluableToSkill> keywordParameters)
      throws IncorrectSyntaxException {

    this.checkFormalParameters(1);
    this.checkKeywordParameters(keywordParameters);

    return new SkillCommand(this, new EvaluableToSkill[] { formalParamater },
        keywordParameters, null);
  }

  /**
   * Build a SKILL command
   * 
   * @param formalParamater   Single formal parameter
   * @param keywordParameters Map of keyword parameters
   * @param restParameters    Rest parameters as list
   * @return SKILL command
   * @throws IncorrectSyntaxException when the provided parameters do not match
   *                                  with the template
   */
  public SkillCommand buildCommand(EvaluableToSkill formalParamater,
      Map<String, EvaluableToSkill> keywordParameters,
      List<EvaluableToSkill> restParameters) throws IncorrectSyntaxException {

    this.checkFormalParameters(1);

    if (!this.canHaveRest && !restParameters.isEmpty()) {
      throw IncorrectSyntaxException.createHasNoRestExecption(this.name);
    }

    this.checkKeywordParameters(keywordParameters);

    return new SkillCommand(this, new EvaluableToSkill[] { formalParamater },
        keywordParameters, restParameters);
  }

  /**
   * Build a SKILL command
   * 
   * @param formalParamaters  Array of formal parameters
   * @param keywordParameters Map of keyword parameters
   * @return SKILL command
   * @throws IncorrectSyntaxException when the provided parameters do not match
   *                                  with the template
   */
  public SkillCommand buildCommand(EvaluableToSkill[] formalParamaters,
      Map<String, EvaluableToSkill> keywordParameters)
      throws IncorrectSyntaxException {

    this.checkFormalParameters(formalParamaters.length);
    this.checkKeywordParameters(keywordParameters);

    return new SkillCommand(this, formalParamaters, keywordParameters, null);
  }

  /**
   * Build a SKILL command
   * 
   * @param formalParamaters  Array of formal parameters
   * @param keywordParameters Map of keyword parameters
   * @param restParameters    Rest parameters as list
   * @return
   * @throws IncorrectSyntaxException when the provided parameters do not match
   *                                  with the template
   */
  public SkillCommand buildCommand(EvaluableToSkill[] formalParamaters,
      Map<String, EvaluableToSkill> keywordParameters,
      List<EvaluableToSkill> restParameters) throws IncorrectSyntaxException {

    this.checkFormalParameters(formalParamaters.length);

    if (!this.canHaveRest && !restParameters.isEmpty()) {
      throw IncorrectSyntaxException.createHasNoRestExecption(this.name);
    }

    this.checkKeywordParameters(keywordParameters);

    return new SkillCommand(this, formalParamaters, keywordParameters,
        restParameters);
  }

  /**
   * Check whether the correct number of formal parameters is provided
   * 
   * @param formalParameters Array of formal parameters
   * @throws IncorrectSyntaxException when the number of provided parameters do
   *                                  not match
   */
  private void checkFormalParameters(int formalParameters)
      throws IncorrectSyntaxException {

    if (this.formalParameters != formalParameters) {
      throw new IncorrectSyntaxException(formalParameters,
          this.formalParameters);
    }
  }

  /**
   * Check whether the provided keyword parameters match with template
   * 
   * @param keywordParameters Map of keyword parameters
   * @throws IncorrectSyntaxException when the keys of the provided parameters
   *                                  do not match with template
   */
  private void checkKeywordParameters(
      Map<String, EvaluableToSkill> keywordParameters)
      throws IncorrectSyntaxException {

    if (this instanceof KeywordParameterSkillCommandTemplate) {

      KeywordParameterSkillCommandTemplate template = (KeywordParameterSkillCommandTemplate) this;

      for (String key : keywordParameters.keySet()) {

        if (!template.getKeywordParameters().contains(key)) {
          IncorrectSyntaxException.createInvalidKeywordExecption(this.name,
              key);
        }
      }

    } else {
      if (keywordParameters.size() > 0) {
        IncorrectSyntaxException
            .createNoKeywordParameterFuntionException(this.name);
      }
    }
  }

}