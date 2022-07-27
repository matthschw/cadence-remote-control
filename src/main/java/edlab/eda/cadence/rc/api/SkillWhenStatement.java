package edlab.eda.cadence.rc.api;

import edlab.eda.cadence.rc.session.EvaluableToSkill;
import edlab.eda.cadence.rc.session.SkillSession;

/**
 * When statement
 */
public final class SkillWhenStatement extends SkillControlStructure {

  private final EvaluableToSkill statement;
  private final EvaluableToSkill[] whenBranch;

  public SkillWhenStatement(final EvaluableToSkill statement,
      final EvaluableToSkill whenBranch) {
    this.statement = statement;
    this.whenBranch = new EvaluableToSkill[] { whenBranch };
  }

  public SkillWhenStatement(final EvaluableToSkill statement,
      final EvaluableToSkill[] whenBranch) {
    this.statement = statement;
    this.whenBranch = whenBranch;
  }

  @Override
  public String toSkill() {

    final StringBuilder builder = new StringBuilder().append("(when ")
        .append(this.statement.toSkill());

    for (final EvaluableToSkill element : this.whenBranch) {
      builder.append(" ").append(element.toSkill());
    }

    builder.append(")");

    return builder.toString();
  }

  @Override
  public boolean canBeUsedInSession(final SkillSession session) {

    if (!this.statement.canBeUsedInSession(session)) {
      return false;
    }

    for (final EvaluableToSkill element : this.whenBranch) {

      if (!element.canBeUsedInSession(session)) {
        return false;
      }
    }

    return true;
  }

}
