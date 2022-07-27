package edlab.eda.cadence.rc.api;

import edlab.eda.cadence.rc.data.SkillBoolean;
import edlab.eda.cadence.rc.session.EvaluableToSkill;
import edlab.eda.cadence.rc.session.SkillSession;

/**
 * If-Else statement
 */
public final class SkillIfElseStatement extends SkillControlStructure {

  private final EvaluableToSkill statement;
  private final EvaluableToSkill[] ifBranch;
  private final EvaluableToSkill[] elseBranch;

  public SkillIfElseStatement(final EvaluableToSkill statement,
      final EvaluableToSkill ifBranch, final EvaluableToSkill elseBranch) {
    this.statement = statement;
    this.ifBranch = new EvaluableToSkill[] { ifBranch };
    this.elseBranch = new EvaluableToSkill[] { elseBranch };
  }

  public SkillIfElseStatement(final EvaluableToSkill statement,
      final EvaluableToSkill[] ifBranch, final EvaluableToSkill elseBranch) {
    this.statement = statement;
    this.ifBranch = ifBranch;
    this.elseBranch = new EvaluableToSkill[] { elseBranch };
  }

  public SkillIfElseStatement(final EvaluableToSkill statement,
      final EvaluableToSkill ifBranch, final EvaluableToSkill[] elseBranch) {
    this.statement = statement;
    this.ifBranch = new EvaluableToSkill[] { ifBranch };
    this.elseBranch = elseBranch;
  }

  public SkillIfElseStatement(final EvaluableToSkill statement,
      final EvaluableToSkill[] ifBranch, final EvaluableToSkill[] elseBranch) {
    this.statement = statement;
    this.ifBranch = ifBranch;
    this.elseBranch = elseBranch;
  }

  @Override
  public String toSkill() {

    final StringBuilder builder = new StringBuilder().append("(if ")
        .append(this.statement.toSkill()).append(" then");

    if (this.ifBranch.length > 0) {

      for (final EvaluableToSkill element : this.ifBranch) {
        builder.append(" ").append(element.toSkill());
      }
    } else {
      builder.append(" ").append(SkillBoolean.FALSE);
    }

    builder.append(" else");

    if (this.elseBranch.length > 0) {

      for (final EvaluableToSkill element : this.elseBranch) {
        builder.append(" ").append(element.toSkill());
      }
    } else {
      builder.append(" ").append(SkillBoolean.FALSE);
    }

    builder.append(")");

    return builder.toString();
  }

  @Override
  public boolean canBeUsedInSession(final SkillSession session) {

    if (!this.statement.canBeUsedInSession(session)) {
      return false;
    }

    for (final EvaluableToSkill element : this.ifBranch) {

      if (!element.canBeUsedInSession(session)) {
        return false;
      }
    }

    for (final EvaluableToSkill element : this.elseBranch) {

      if (!element.canBeUsedInSession(session)) {
        return false;
      }
    }

    return true;
  }
}