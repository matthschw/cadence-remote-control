package edlab.eda.cadence.rc.api;

public class OptionalParameterSkillCommandTemplate
    extends SkillCommandTemplate {

  private int optionalParameters;

  private OptionalParameterSkillCommandTemplate(String name,
      int optionalParameters) {
    super(name);
    this.optionalParameters = optionalParameters;
  }

  private OptionalParameterSkillCommandTemplate(String name,
      int formalParameters, int optionalParameters) {
    super(name, formalParameters);
    this.optionalParameters = optionalParameters;
  }

  private OptionalParameterSkillCommandTemplate(String name,
      int optionalParameters, boolean canHaveRest) {
    super(name, canHaveRest);
    this.optionalParameters = optionalParameters;
  }

  private OptionalParameterSkillCommandTemplate(String name,
      int formalParameters, int optionalParameters, boolean canHaveRest) {
    super(name, formalParameters, canHaveRest);
    this.optionalParameters = optionalParameters;
  }

  public int getOptionalParameters() {
    return this.optionalParameters;
  }

  public static OptionalParameterSkillCommandTemplate build(String name,
      int optionalParameters) {
    return new OptionalParameterSkillCommandTemplate(name, optionalParameters);
  }

  public static OptionalParameterSkillCommandTemplate build(String name,
      int formalParameters, int optionalParameters) {

    if (formalParameters < 0) {
      return null;
    } else {
      if (optionalParameters < 0) {
        return null;
      } else {
        return new OptionalParameterSkillCommandTemplate(name, formalParameters,
            optionalParameters);
      }
    }
  }

  public static OptionalParameterSkillCommandTemplate build(String name,
      int optionalParameters, boolean canHaveRest) {

    if (optionalParameters < 0) {
      return null;
    } else {
      return new OptionalParameterSkillCommandTemplate(name, optionalParameters,
          canHaveRest);
    }
  }

  public static OptionalParameterSkillCommandTemplate build(String name,
      int formalParameters, int optionalParameters, boolean canHaveRest) {

    if (formalParameters < 0) {

      return null;
    } else {
      if (optionalParameters < 0) {
        return null;
      } else {
        return new OptionalParameterSkillCommandTemplate(name, formalParameters,
            optionalParameters, canHaveRest);
      }
    }
  }
}
