package edlab.eda.cadence.rc.api;

import java.util.HashSet;
import java.util.Set;

public class SkillCommandTemplate {

  private String name;
  private int numOfFormalParameters;
  private Set<String> keywordParameters;

  public SkillCommandTemplate(String name) {
    this.name = name;
    this.numOfFormalParameters = 0;
  }

  public SkillCommandTemplate(String name, int numOfFormalParameters) {
    this.name = name;
    this.numOfFormalParameters = numOfFormalParameters;
    this.keywordParameters = new HashSet<String>();
  }

  public SkillCommandTemplate(String name, Set<String> keywordParameters) {
    this.name = name;
    this.numOfFormalParameters = 0;
    this.keywordParameters = keywordParameters;
  }

  public SkillCommandTemplate(String name, int numOfFormalParameters,
      Set<String> keywordParameters) {
    this.name = name;
    this.numOfFormalParameters = numOfFormalParameters;
    this.keywordParameters = keywordParameters;
  }

  public String getName() {
    return name;
  }

  public int getNumOfFormalParameters() {
    return numOfFormalParameters;
  }

  public Set<String> getKeywordParameters() {
    return keywordParameters;
  }
}