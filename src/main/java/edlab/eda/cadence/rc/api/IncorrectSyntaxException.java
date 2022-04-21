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
   * Create a new exception
   * 
   * @param providedFormalParamaters Number of provided formal parameter
   * @param neededFormalParameters   Number of needed formal parameter
   */
  @Deprecated
  public IncorrectSyntaxException(final int providedFormalParamaters,
      final int neededFormalParameters) {
    super(providedFormalParamaters + " format parameters are provided, but "
        + neededFormalParameters + " are requried");
  }

  /**
   * Create a new exception
   * 
   * @param name                     Name of function
   * @param providedFormalParamaters Number of provided formal parameter
   * @param neededFormalParameters   Number of needed formal parameter
   */
  public IncorrectSyntaxException(final String name,
      final int providedFormalParamaters, final int neededFormalParameters) {
    super("Function \"" + name + "\" requires " + neededFormalParameters
        + " formal parameters are provided, but " + providedFormalParamaters
        + " are requried");
  }

  /**
   * This exception is thrown when keyword paramaters are added to a template
   * with no keyword parameters
   * 
   * @param name Name of the function
   * @return exception
   */
  static IncorrectSyntaxException createNoKeywordParameterFuntionException(
      final String name) {
    return new IncorrectSyntaxException(
        "Function \"" + name + "\" has no keyword parameters");
  }

  /**
   * This exception is thrown when an invalid keyword paramater is added to a
   * template
   * 
   * @param name    Name of the function
   * @param keyword Invalid keyword parameter
   * @return exception
   */
  static IncorrectSyntaxException createInvalidKeywordExecption(
      final String name, final String keyword) {
    return new IncorrectSyntaxException("Function \"" + name
        + "\" has no keyword parameter with name \"" + keyword + "\"");
  }

  /**
   * This exception is thrown when a rest is provided to a template where no
   * rest can be added
   * 
   * @param name Name of the function
   * @return exception
   */
  static IncorrectSyntaxException createHasNoRestExecption(final String name) {
    return new IncorrectSyntaxException(
        "Cannot provide rest to function \"" + name + "\"");
  }

  /**
   * This exception is thrown when optional parameters are provided to a
   * template where no optional parameters can be added
   * 
   * @param name Name of the function
   * @return exception
   */
  static IncorrectSyntaxException createHasNoOptionalParametersExecption(
      final String name) {
    return new IncorrectSyntaxException(
        "Cannot provide optional parameters to function \"" + name + "\"");
  }

  /**
   * This exception is thrown when more optional parameters are provided than
   * available
   * 
   * @param name          Name of the function
   * @param maxParameters Maximum number of allowed parameters
   * @return exception
   */
  static IncorrectSyntaxException createInvalidOptionalParametersExecption(
      final String name, final int maxParameters) {
    return new IncorrectSyntaxException("Cannot provide more than "
        + maxParameters + " optional parameters to function \"" + name + "\"");
  }
}