package edlab.eda.cadence.rc.api;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edlab.eda.cadence.rc.EvaluableToSkill;

/**
 * Template of a SKILL Command
 *
 */
public class SkillCommandTemplate {

  private String name;
  private int numOfFormalParameters;
  private Set<String> keywordParameters;

  /**
   * Create a SKILL-Command template with an arbitrary number of formal
   * parameters
   * 
   * @param name Name of the command
   */
  public SkillCommandTemplate(String name) {
    this.name = name;
    this.numOfFormalParameters = -1;
  }

  /**
   * Create a SKILL-Command template with an fixed number of formal parameters
   * 
   * @param name                  Name of the command
   * @param numOfFormalParameters Number of formal parameters
   */
  public SkillCommandTemplate(String name, int numOfFormalParameters) {
    this.name = name;
    this.numOfFormalParameters = numOfFormalParameters;
    this.keywordParameters = new HashSet<String>();
  }

  /**
   * Create a SKILL-Command template with keyword parameters
   * 
   * @param name              Name of the command
   * @param keywordParameters Names of keyword-parameters
   */
  public SkillCommandTemplate(String name, Set<String> keywordParameters) {
    this.name = name;
    this.numOfFormalParameters = 0;
    this.keywordParameters = keywordParameters;
  }

  /**
   * @param name                  Name of the command
   * @param numOfFormalParameters Number of formal parameters
   * @param keywordParameters     Names of keyword-parameters
   */
  public SkillCommandTemplate(String name, int numOfFormalParameters,
      Set<String> keywordParameters) {
    this.name = name;
    this.numOfFormalParameters = numOfFormalParameters;
    this.keywordParameters = keywordParameters;
  }

  /**
   * Get name of the command
   * 
   * @return name of command
   */
  public String getName() {
    return name;
  }

  /**
   * Get number of formal parameters of the SKILL-Command template
   * 
   * @return number of formal parameters
   */
  public int getNumOfFormalParameters() {
    return numOfFormalParameters;
  }

  /**
   * Get number of keyword parameters of the SKILL-Command template
   * 
   * @return Names of keyword-parameters
   */
  public Set<String> getKeywordParameters() {
    return keywordParameters;
  }

  /**
   * Create a SKILL command from a given template
   * 
   * @return SKILL command
   * @throws IncorrectSyntaxException When syntax from the template is violated
   */
  public SkillCommand build() throws IncorrectSyntaxException {
    return SkillCommand.buildCommand(this);
  }

  /**
   * Create a SKILL command from a given template
   * 
   * @param formalParameters List of formal parameters
   * @return SKILL command
   * @throws IncorrectSyntaxException When syntax from the template is violated
   */
  public SkillCommand build(EvaluableToSkill[] formalParameters)
      throws IncorrectSyntaxException {
    return SkillCommand.buildCommand(this, formalParameters);
  }

  /**
   * Create a SKILL command from a given template
   * 
   * @param formalParameter Formal parameter
   * @return SKILL command
   * @throws IncorrectSyntaxException When syntax from the template is violated
   */
  public SkillCommand build(EvaluableToSkill formalParameter)
      throws IncorrectSyntaxException {
    return SkillCommand.buildCommand(this, formalParameter);
  }

  /**
   * Create a SKILL command from a given template
   * 
   * @param keywordParameters Map of keyword parameters
   * @return SKILL command
   * @throws IncorrectSyntaxException When syntax from the template is violated
   */
  public SkillCommand build(Map<String, EvaluableToSkill> keywordParameters)
      throws IncorrectSyntaxException {
    return SkillCommand.buildCommand(this, keywordParameters);
  }

  /**
   * Create a SKILL command from a given template
   * 
   * @param formalParameters  List of formal parameters
   * @param keywordParameters Map of keyword parameters
   * @return SKILL command
   * @throws IncorrectSyntaxException When syntax from the template is violated
   */
  public SkillCommand build(EvaluableToSkill[] formalParameters,
      Map<String, EvaluableToSkill> keywordParameters)
      throws IncorrectSyntaxException {
    return SkillCommand.buildCommand(this, formalParameters, keywordParameters);
  }
}