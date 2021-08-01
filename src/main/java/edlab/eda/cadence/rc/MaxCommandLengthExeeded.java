package edlab.eda.cadence.rc;

public class MaxCommandLengthExeeded extends Exception {

  private static final long serialVersionUID = 5929631070701197449L;

  public MaxCommandLengthExeeded(String command, int maxLen) {
    super("Command:\n" + command + "\n exceedes max length of " + maxLen
        + " characters");
  }
}