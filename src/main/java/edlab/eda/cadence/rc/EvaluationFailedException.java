package edlab.eda.cadence.rc;

/**
 * Exception that is thrown when a Skill command cannot be evaluated
 *
 */
public class EvaluationFailedException extends Exception {

  private static final long serialVersionUID = -1887101604969243884L;

  public EvaluationFailedException(String command, String retval) {
    super("Evaluation of command : " + command + " failed with exception "
        + retval);
  }
}