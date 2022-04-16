package edlab.eda.cadence.rc.session;

/**
 * Exception that is thrown when a Skill command cannot be evaluated, i.e.
 * throws an error
 */
public final class EvaluationFailedException extends Exception {

  private static final long serialVersionUID = 1887101604969243884L;

  private final String command;
  private final String errorMessage;

  /**
   * Create exception
   * 
   * @param command      command
   * @param errorMessage error message
   */
  public EvaluationFailedException(final String command,
      final String errorMessage) {
    super("Evaluation of command \"" + command + "\" failed with exception "
        + errorMessage);
    this.errorMessage = errorMessage;
    this.command = command;
  }

  /**
   * Create exception
   * 
   * @param command command
   */
  public EvaluationFailedException(final String command) {
    super("Evaluation of command \"" + command + "\" failed");
    this.errorMessage = "";
    this.command = command;
  }

  /**
   * Get the error message
   * 
   * @return error message
   */
  public String getErrorMessage() {
    return this.errorMessage;
  }

  /**
   * Get the command that lead to the error
   * 
   * @return command
   */
  public String getCommand() {
    return this.command;
  }
}