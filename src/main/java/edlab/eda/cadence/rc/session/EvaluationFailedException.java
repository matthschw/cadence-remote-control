package edlab.eda.cadence.rc.session;

/**
 * Exception that is thrown when a Skill command cannot be evaluated
 */
public class EvaluationFailedException extends Exception {

  private static final long serialVersionUID = -1887101604969243884L;

  private final String command;
  private final String errorMessage;

  public EvaluationFailedException(String command, String errorMessage) {
    super("Evaluation of command : " + command + " failed with exception "
        + errorMessage);
    this.errorMessage = errorMessage;
    this.command = command;
  }

  public EvaluationFailedException(String command) {
    super("Evaluation of command : " + command + " failed");
    this.errorMessage = "";
    this.command = command;
  }

  public String getErrorMessage() {
    return this.errorMessage;
  }

  public String getCommand() {
    return this.command;
  }
}