package edlab.eda.cadence.rc.api;

/**
 * Exception that is thrown when the parameters that are provided to a command
 * template
 */
public final class IncorrectSyntaxException extends Exception {

  private static final long serialVersionUID = 6242260639498297179L;

  private IncorrectSyntaxException(final String message) {
    super(message);
  }

  /**
   * Create a new exection
   * 
   * @param providedFormalParamaters Number of provided formal parameter
   * @param neededFormalParameters   Number of needed formal parameter
   */
  public IncorrectSyntaxException(final int providedFormalParamaters,
      final int neededFormalParameters) {
    super(providedFormalParamaters + " format parameters are provided, but "
        + neededFormalParameters + " are requried");
  }

  static IncorrectSyntaxException createNoKeywordParameterFuntionException(
      final String name) {
    return new IncorrectSyntaxException(
        "Function \"" + name + "\" has no keyword parameters");
  }

  static IncorrectSyntaxException createInvalidKeywordExecption(
      final String name, final String keyword) {
    return new IncorrectSyntaxException("Function \"" + name
        + "\" has no keyword with name \"" + keyword + "\"");
  }

  static IncorrectSyntaxException createHasNoRestExecption(final String name) {
    return new IncorrectSyntaxException(
        "Cannot provide rest to function \"" + name + "\"");
  }

  static IncorrectSyntaxException createHasNoOptionalParametersExecption(
      final String name) {
    return new IncorrectSyntaxException(
        "Cannot provide optional parameters to function \"" + name + "\"");
  }

  static IncorrectSyntaxException createInvalidOptionalParametersExecption(
      final String name, final int maxParameters) {
    return new IncorrectSyntaxException("Cannot provide more than "
        + maxParameters + " optional parameters to function \"" + name + "\"");
  }
}