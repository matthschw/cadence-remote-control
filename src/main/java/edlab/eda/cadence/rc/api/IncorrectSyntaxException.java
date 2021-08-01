package edlab.eda.cadence.rc.api;

public class IncorrectSyntaxException extends Exception {

  private static final long serialVersionUID = -6242260639498297179L;

  public IncorrectSyntaxException(int providedFormalParamaters,
      int neededFormalParameters) {
    super(providedFormalParamaters + " format parameters are provided, but "
        + neededFormalParameters + " are requried");
  }

  public IncorrectSyntaxException(String incorrectKeywordParameter) {
    super(incorrectKeywordParameter
        + " is not a keyword paramter of the funtction");
  }
}
