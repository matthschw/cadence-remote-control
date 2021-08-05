package edlab.eda.cadence.rc.api;

import java.util.HashMap;
import java.util.Map;

import edlab.eda.cadence.rc.EvaluableToSkill;
import edlab.eda.cadence.rc.SkillSession;
import edlab.eda.cadence.rc.data.SkillDataobject;

/**
 * Class which represents a Skill-Command
 *
 */
public class SkillCommand implements EvaluableToSkill {

  private SkillCommandTemplate template;
  private EvaluableToSkill[] formalParameters;
  private Map<String, EvaluableToSkill> keywordParameters;

  private SkillCommand(SkillCommandTemplate template) {
    this.template = template;
    this.formalParameters = new SkillDataobject[0];
    this.keywordParameters = new HashMap<String, EvaluableToSkill>();
  }

  private SkillCommand(SkillCommandTemplate template,
      EvaluableToSkill[] formalParameters) {
    this.template = template;
    this.formalParameters = formalParameters;
    this.keywordParameters = new HashMap<String, EvaluableToSkill>();
  }

  private SkillCommand(SkillCommandTemplate template,
      Map<String, EvaluableToSkill> keywordParameters) {
    this.template = template;
    this.formalParameters = new SkillDataobject[0];
    this.keywordParameters = keywordParameters;
  }

  private SkillCommand(SkillCommandTemplate template,
      EvaluableToSkill[] formalParameters,
      Map<String, EvaluableToSkill> keywordParameters) {
    this.template = template;
    this.formalParameters = formalParameters;
    this.keywordParameters = keywordParameters;
  }

  @Override
  public String toSkill() {
    String retval = "(" + template.getName();

    for (int i = 0; i < formalParameters.length; i++) {
      retval += " " + formalParameters[i].toSkill();
    }

    for (String key : keywordParameters.keySet()) {
      retval += " ?" + key + " " + keywordParameters.get(key).toSkill();
    }

    retval += ")";

    return retval;
  }

  /**
   * Create a SKILL command from a given template
   * 
   * @param template SKILL-Command template
   * @return SkillCommand {
   * @throws IncorrectSyntaxException When syntax from the template is violated
   */
  public static SkillCommand buildCommand(SkillCommandTemplate template)
      throws IncorrectSyntaxException {
    if (template.getNumOfFormalParameters() > 0) {
      throw new IncorrectSyntaxException(0,
          template.getNumOfFormalParameters());
    } else {
      return new SkillCommand(template);
    }
  }

  /**
   * Create a SKILL command from a given template
   * 
   * @param template         SKILL-Command template
   * @param formalParameters List of formal parameters
   * @return SkillCommand
   * @throws IncorrectSyntaxException When syntax from the template is violated
   */
  public static SkillCommand buildCommand(SkillCommandTemplate template,
      EvaluableToSkill[] formalParameters) throws IncorrectSyntaxException {

    if (template.getNumOfFormalParameters() < 0
        || template.getNumOfFormalParameters() == formalParameters.length) {
      return new SkillCommand(template, formalParameters);
    } else {
      throw new IncorrectSyntaxException(formalParameters.length,
          template.getNumOfFormalParameters());
    }
  }

  /**
   * Create a SKILL command from a given template
   * 
   * @param template        SKILL-Command template
   * @param formalParameter Formal parameter
   * @return SkillCommand
   * @throws IncorrectSyntaxException When syntax from the template is violated
   */
  public static SkillCommand buildCommand(SkillCommandTemplate template,
      EvaluableToSkill formalParameter) throws IncorrectSyntaxException {

    if (template.getNumOfFormalParameters() == 1
        || template.getNumOfFormalParameters() < 0) {
      return new SkillCommand(template,
          new EvaluableToSkill[] { formalParameter });
    } else {
      throw new IncorrectSyntaxException(1,
          template.getNumOfFormalParameters());
    }
  }

  /**
   * Create a SKILL command from a given template
   * 
   * @param template          SKILL-Command template
   * @param keywordParameters Map of keyword parameters
   * @return SkillCommand
   * @throws IncorrectSyntaxException When syntax from the template is violated
   */
  public static SkillCommand buildCommand(SkillCommandTemplate template,
      Map<String, EvaluableToSkill> keywordParameters)
      throws IncorrectSyntaxException {

    for (String key : keywordParameters.keySet()) {

      if (!template.getKeywordParameters().contains(key)) {
        throw new IncorrectSyntaxException(key);
      }
    }

    return new SkillCommand(template, keywordParameters);
  }

  /**
   * Create a SKILL command from a given template
   * 
   * @param template          SKILL-Command template
   * @param formalParameters  List of formal parameters
   * @param keywordParameters Map of keyword parameters
   * @return SkillCommand
   * @throws IncorrectSyntaxException When syntax from the template is violated
   */
  public static SkillCommand buildCommand(SkillCommandTemplate template,
      EvaluableToSkill[] formalParameters,
      Map<String, EvaluableToSkill> keywordParameters)
      throws IncorrectSyntaxException {

    for (String key : keywordParameters.keySet()) {

      if (!template.getKeywordParameters().contains(key)) {
        throw new IncorrectSyntaxException(key);
      }
    }

    if (template.getNumOfFormalParameters() < 0
        || template.getNumOfFormalParameters() == formalParameters.length) {
      return new SkillCommand(template, formalParameters, keywordParameters);
    } else {
      throw new IncorrectSyntaxException(formalParameters.length,
          template.getNumOfFormalParameters());
    }
  }

  @Override
  public boolean canBeUsedInSession(SkillSession session) {

    for (int i = 0; i < formalParameters.length; i++) {
      if (!formalParameters[i].canBeUsedInSession(session)) {
        return false;
      }
    }

    for (String key : this.keywordParameters.keySet()) {
      if (keywordParameters.get(key).canBeUsedInSession(session)) {
        return false;
      }

    }

    return true;
  }
}