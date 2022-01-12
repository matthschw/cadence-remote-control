package edlab.eda.cadence.rc.api;

/**
 * Exception that is thrown when the parameters that are provided to a command template
 */
public class IncorrectSyntaxException extends Exception {

  private static final long serialVersionUID = 6242260639498297179L;

  private IncorrectSyntaxException(String message) {
    super(message);
  }

  public IncorrectSyntaxException(int providedFormalParamaters,
      int neededFormalParameters) {
    super(providedFormalParamaters + " format parameters are provided, but "
        + neededFormalParameters + " are requried");
  }

  static IncorrectSyntaxException createNoKeywordParameterFuntionException(
      String name) {
    return new IncorrectSyntaxException(
        "Function \"" + name + "\" has no keyword parameters");
  }

  static IncorrectSyntaxException createInvalidKeywordExecption(String name,
      String keyword) {
    return new IncorrectSyntaxException("Function \"" + name
        + "\" has no keyword with name \"" + keyword + "\"");
  }

  static IncorrectSyntaxException createHasNoRestExecption(String name) {
    return new IncorrectSyntaxException(
        "Cannot provide rest to function \"" + name + "\"");
  }

  static IncorrectSyntaxException createHasNoOptionalParametersExecption(
      String name) {
    return new IncorrectSyntaxException(
        "Cannot provide optional parameters to function \"" + name + "\"");
  }

  static IncorrectSyntaxException createInvalidOptionalParametersExecption(
      String name, int maxParameters) {
    return new IncorrectSyntaxException("Cannot provide more than "
        + maxParameters + " optional parameters to function \"" + name + "\"");
  }

}
