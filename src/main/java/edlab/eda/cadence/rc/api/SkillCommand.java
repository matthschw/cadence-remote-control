package edlab.eda.cadence.rc.api;

import java.util.List;
import java.util.Map;

import edlab.eda.cadence.rc.session.EvaluableToSkill;
import edlab.eda.cadence.rc.session.SkillSession;

/**
 * Representation of a SKILL command
 */
public class SkillCommand implements EvaluableToSkill {

  private SkillCommandTemplate template;
  private EvaluableToSkill[] formalParameters;
  private Map<String, EvaluableToSkill> keywordParameters;
  private List<EvaluableToSkill> optionalAndRestParameters;

  SkillCommand(SkillCommandTemplate template,
      EvaluableToSkill[] formalParameters,
      Map<String, EvaluableToSkill> keywordParameters,
      List<EvaluableToSkill> optionalAndRestParameters) {

    this.template = template;
    this.formalParameters = formalParameters;
    this.keywordParameters = keywordParameters;
    this.optionalAndRestParameters = optionalAndRestParameters;
  }

  @Override
  public String toSkill() {

    String retval = "(" + this.template.getName();

    if (this.formalParameters != null) {

      for (int i = 0; i < this.formalParameters.length; i++) {

        if (this.formalParameters[i] == null) {
          retval += " nil";
        } else {
          retval += " " + this.formalParameters[i].toSkill();
        }
      }
    }

    if (this.keywordParameters != null) {

      for (String key : this.keywordParameters.keySet()) {

        if (this.keywordParameters.get(key) == null) {
          retval += " nil";
        } else {
          retval += " ?" + key + " " + this.keywordParameters.get(key).toSkill();
        }
      }
    }

    if (this.optionalAndRestParameters != null) {

      for (EvaluableToSkill evaluableToSkill : this.optionalAndRestParameters) {

        if (evaluableToSkill == null) {
          retval += " nil";
        } else {
          retval += " " + evaluableToSkill.toSkill();
        }
      }
    }

    retval += ")";

    return retval;
  }

  @Override
  public boolean canBeUsedInSession(SkillSession session) {

    if (this.formalParameters != null) {
      for (int i = 0; i < formalParameters.length; i++) {
        if (!formalParameters[i].canBeUsedInSession(session)) {
          return false;
        }
      }
    }

    if (this.keywordParameters != null) {
      for (String key : this.keywordParameters.keySet()) {
        if (!keywordParameters.get(key).canBeUsedInSession(session)) {
          return false;
        }
      }
    }

    if (this.optionalAndRestParameters != null) {
      for (EvaluableToSkill evaluableToSkill : this.formalParameters) {
        if (!evaluableToSkill.canBeUsedInSession(session)) {
          return false;
        }
      }
    }

    return true;
  }
}