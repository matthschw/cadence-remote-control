package edlab.eda.cadence.rc.api;

/**
 * Template of a SKILL-Command with optional parameters
 */
public final class OptionalParameterSkillCommandTemplate
    extends SkillCommandTemplate {

  private final int optionalParameters;

  private OptionalParameterSkillCommandTemplate(final String name,
      final int optionalParameters) {
    super(name);
    this.optionalParameters = optionalParameters;
  }

  private OptionalParameterSkillCommandTemplate(final String name,
      final int formalParameters, final int optionalParameters) {
    super(name, formalParameters);
    this.optionalParameters = optionalParameters;
  }

  private OptionalParameterSkillCommandTemplate(final String name,
      final int optionalParameters, final boolean canHaveRest) {
    super(name, canHaveRest);
    this.optionalParameters = optionalParameters;
  }

  private OptionalParameterSkillCommandTemplate(final String name,
      final int formalParameters, final int optionalParameters,
      final boolean canHaveRest) {
    super(name, formalParameters, canHaveRest);
    this.optionalParameters = optionalParameters;
  }

  /**
   * Get maximum number of optional parameters
   * 
   * @return number of optional parameters
   */
  public int getOptionalParameters() {
    return this.optionalParameters;
  }

  /**
   * Build a SKILL command template with optional parameters
   *
   * @param name               Name of the command
   * @param optionalParameters Number of optional parameters
   * @return SKILL-Command template
   */
  public static OptionalParameterSkillCommandTemplate build(final String name,
      final int optionalParameters) {

    return new OptionalParameterSkillCommandTemplate(name, optionalParameters);
  }

  /**
   * Build a SKILL command template with optional parameters
   *
   * @param name               Name of the command
   * @param formalParameters   Number of formal parameters
   * @param optionalParameters Number of optional parameters
   * @return SKILL-Command template
   */
  public static OptionalParameterSkillCommandTemplate build(final String name,
      final int formalParameters, final int optionalParameters) {

    if (formalParameters < 0) {
      return null;
    } else if (optionalParameters < 0) {
      return null;
    } else {
      return new OptionalParameterSkillCommandTemplate(name, formalParameters,
          optionalParameters);
    }
  }

  /**
   * Build a SKILL command template with optional parameters
   *
   * @param name               Name of the command
   * @param optionalParameters Number of optional parameters
   * @param canHaveRest        <code>true</code> when the command can have rest,
   *                           <code>false</code> otherwise
   * @return SKILL-Command template
   */
  public static OptionalParameterSkillCommandTemplate build(final String name,
      final int optionalParameters, final boolean canHaveRest) {

    if (optionalParameters < 0) {
      return null;
    } else {
      return new OptionalParameterSkillCommandTemplate(name, optionalParameters,
          canHaveRest);
    }
  }

  /**
   * Build a SKILL command template with optional parameters
   *
   * @param name               canHaveRest
   * @param formalParameters   Number of formal parameters
   * @param optionalParameters Number of optional parameters
   * @param canHaveRest        <code>true</code> when the command can have rest,
   *                           <code>false</code> otherwise
   * @return SKILL-Command template
   */
  public static OptionalParameterSkillCommandTemplate build(final String name,
      final int formalParameters, final int optionalParameters,
      final boolean canHaveRest) {

    if (formalParameters < 0) {
      return null;
    } else if (optionalParameters < 0) {
      return null;
    } else {
      return new OptionalParameterSkillCommandTemplate(name, formalParameters,
          optionalParameters, canHaveRest);
    }
  }
}