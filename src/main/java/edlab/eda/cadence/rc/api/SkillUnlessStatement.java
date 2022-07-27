package edlab.eda.cadence.rc.api;

import edlab.eda.cadence.rc.session.EvaluableToSkill;
import edlab.eda.cadence.rc.session.SkillSession;

/**
 * unless statement
 */
public final class SkillUnlessStatement extends SkillControlStructure {

  private final EvaluableToSkill statement;
  private final EvaluableToSkill[] unlessBranch;

  public SkillUnlessStatement(final EvaluableToSkill statement,
      final EvaluableToSkill unlessBranch) {
    this.statement = statement;
    this.unlessBranch = new EvaluableToSkill[] { unlessBranch };
  }

  public SkillUnlessStatement(final EvaluableToSkill statement,
      final EvaluableToSkill[] unlessBranch) {
    this.statement = statement;
    this.unlessBranch = unlessBranch;
  }

  @Override
  public String toSkill() {

    final StringBuilder builder = new StringBuilder().append("(unless ")
        .append(this.statement.toSkill());

    for (final EvaluableToSkill element : this.unlessBranch) {
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

    for (final EvaluableToSkill element : this.unlessBranch) {

      if (!element.canBeUsedInSession(session)) {
        return false;
      }
    }

    return true;
  }
}
