package edlab.eda.cadence.rc;

public class EvaluationFailedException extends Exception {

  private static final long serialVersionUID = -1887101604969243884L;

  public EvaluationFailedException(String command, String retval) {
    super("Evaluation of command : " + command + " failed with exception "
        + retval);
  }
}