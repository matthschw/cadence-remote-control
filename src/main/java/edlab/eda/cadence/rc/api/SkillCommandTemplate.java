package edlab.eda.cadence.rc.api;

import java.util.List;
import java.util.Map;

import edlab.eda.cadence.rc.EvaluableToSkill;

/**
 * Template of a SKILL Command
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
  public SkillCommandTemplate(String name) {
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
  public SkillCommandTemplate(String name, boolean canHaveRest) {
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
  public SkillCommandTemplate(String name, int numOfFormalParameters) {
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
  public SkillCommandTemplate(String name, int numOfFormalParameters,
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
   * Build a SKILL command
   * 
   * @return
   * @throws IncorrectSyntaxException
   */
  public SkillCommand buildCommand() throws IncorrectSyntaxException {

    this.checkFormalParameters(0);
    return new SkillCommand(this, null, null, null);
  }

  /**
   * @param formalParamater
   * @return
   * @throws IncorrectSyntaxException
   */
  public SkillCommand buildCommand(EvaluableToSkill formalParamater)
      throws IncorrectSyntaxException {

    this.checkFormalParameters(1);

    return new SkillCommand(this, new EvaluableToSkill[] { formalParamater },
        null, null);
  }

  /**
   * @param formalParamaters
   * @return
   * @throws IncorrectSyntaxException
   */
  public SkillCommand buildCommand(EvaluableToSkill[] formalParamaters)
      throws IncorrectSyntaxException {

    this.checkFormalParameters(formalParamaters.length);

    return new SkillCommand(this, formalParamaters, null, null);
  }

  /**
   * @param optionalAndRestParameters
   * @return
   * @throws IncorrectSyntaxException
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

          // add exception
          return null;
        }

      } else {
        // add exception
        return null;
      }
    }

    return new SkillCommand(this, null, null, optionalAndRestParameters);
  }

  /**
   * @param formalParamater
   * @param optionalAndRestParameters
   * @return
   * @throws IncorrectSyntaxException
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

          // add exception
          return null;
        }

      } else {
        // add exception
        return null;
      }
    }

    return new SkillCommand(this, new EvaluableToSkill[] { formalParamater },
        null, optionalAndRestParameters);
  }

  /**
   * @param formalParamaters
   * @param optionalAndRestParameters
   * @return
   * @throws IncorrectSyntaxException
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
          // add exception
          return null;
        }

      } else {
        // add exception
        return null;
      }
    }

    return new SkillCommand(this, formalParamaters, null,
        optionalAndRestParameters);
  }

  /**
   * @param keywordParameters
   * @return
   * @throws IncorrectSyntaxException
   */
  public SkillCommand buildCommand(
      Map<String, EvaluableToSkill> keywordParameters)
      throws IncorrectSyntaxException {

    this.checkFormalParameters(0);
    this.checkKeywordParameters(keywordParameters);

    return new SkillCommand(this, null, keywordParameters, null);
  }

  /**
   * @param keywordParameters
   * @param restParameters
   * @return
   * @throws IncorrectSyntaxException
   */
  public SkillCommand buildCommand(
      Map<String, EvaluableToSkill> keywordParameters,
      List<EvaluableToSkill> restParameters) throws IncorrectSyntaxException {

    this.checkFormalParameters(0);

    if (!this.canHaveRest && !restParameters.isEmpty()) {
      // add exception
      return null;
    }

    this.checkKeywordParameters(keywordParameters);

    return new SkillCommand(this, null, keywordParameters, restParameters);
  }

  /**
   * @param formalParamater
   * @param keywordParameters
   * @return
   * @throws IncorrectSyntaxException
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
   * @param formalParamater
   * @param keywordParameters
   * @param restParameters
   * @return
   * @throws IncorrectSyntaxException
   */
  public SkillCommand buildCommand(EvaluableToSkill formalParamater,
      Map<String, EvaluableToSkill> keywordParameters,
      List<EvaluableToSkill> restParameters) throws IncorrectSyntaxException {

    this.checkFormalParameters(1);

    if (!this.canHaveRest && !restParameters.isEmpty()) {
      // add exception
      return null;
    }

    this.checkKeywordParameters(keywordParameters);

    return new SkillCommand(this, new EvaluableToSkill[] { formalParamater },
        keywordParameters, restParameters);
  }

  /**
   * @param formalParamaters
   * @param keywordParameters
   * @return
   * @throws IncorrectSyntaxException
   */
  public SkillCommand buildCommand(EvaluableToSkill[] formalParamaters,
      Map<String, EvaluableToSkill> keywordParameters)
      throws IncorrectSyntaxException {

    this.checkFormalParameters(formalParamaters.length);
    this.checkKeywordParameters(keywordParameters);

    return new SkillCommand(this, formalParamaters, keywordParameters, null);
  }

  /**
   * @param formalParamaters
   * @param keywordParameters
   * @param restParameters
   * @return
   * @throws IncorrectSyntaxException
   */
  public SkillCommand buildCommand(EvaluableToSkill[] formalParamaters,
      Map<String, EvaluableToSkill> keywordParameters,
      List<EvaluableToSkill> restParameters) throws IncorrectSyntaxException {

    this.checkFormalParameters(formalParamaters.length);

    if (!this.canHaveRest && !restParameters.isEmpty()) {
      // add exception
      return null;
    }

    this.checkKeywordParameters(keywordParameters);

    return new SkillCommand(this, formalParamaters, keywordParameters,
        restParameters);
  }

  /**
   * @param formalParameters
   * @throws IncorrectSyntaxException
   */
  private void checkFormalParameters(int formalParameters)
      throws IncorrectSyntaxException {

    if (this.formalParameters != formalParameters) {
      throw new IncorrectSyntaxException(formalParameters,
          this.formalParameters);
    }
  }

  /**
   * @param keywordParameters
   */
  private void checkKeywordParameters(
      Map<String, EvaluableToSkill> keywordParameters) {

    if (this instanceof KeywordParameterSkillCommandTemplate) {

      KeywordParameterSkillCommandTemplate template = (KeywordParameterSkillCommandTemplate) this;

      for (String key : keywordParameters.keySet()) {

        if (!template.getKeywordParameters().contains(key)) {
          // throw error;
        }
      }

    } else {
      if (keywordParameters.size() > 0) {
        // throw error;
      }
    }
  }

}