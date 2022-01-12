package edlab.eda.cadence.rc.api;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

    StringBuilder builder = new StringBuilder();

    builder.append("(").append(this.template.getName());

    if (this.formalParameters != null) {

      for (EvaluableToSkill formalParameter : this.formalParameters) {

        builder.append(" ");
        if (formalParameter == null) {
          builder.append("nil");
        } else {
          builder.append(formalParameter.toSkill());
        }
      }
    }

    if (this.keywordParameters != null) {

      for (Entry<String, EvaluableToSkill> entry : this.keywordParameters
          .entrySet()) {

        builder.append(" ");

        if (entry.getKey() == null) {
          builder.append("nil");
        } else {
          builder.append("?").append(entry.getKey()).append(" ")
              .append(entry.getValue().toSkill());
        }
      }
    }

    if (this.optionalAndRestParameters != null) {

      for (EvaluableToSkill evaluableToSkill : this.optionalAndRestParameters) {

        builder.append(" ");

        if (evaluableToSkill == null) {
          builder.append("nil");
        } else {
          builder.append(evaluableToSkill.toSkill());
        }
      }
    }
    
    builder.append(")");
    
    return builder.toString();
  }

  @Override
  public boolean canBeUsedInSession(SkillSession session) {

    if (this.formalParameters != null) {
      for (EvaluableToSkill formalParameter : formalParameters) {
        if (!formalParameter.canBeUsedInSession(session)) {
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