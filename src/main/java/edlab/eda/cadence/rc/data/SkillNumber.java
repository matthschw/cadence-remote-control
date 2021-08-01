package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;

/**
 * Representation of a Number
 *
 */
public abstract class SkillNumber extends SkillBoolean {

  protected BigDecimal number;

  public SkillNumber(BigDecimal number) {
    super(true);
    this.number = number;
  }

  @Override
  protected String toSkillHierarchical(int depth) {

    return "" + this.number.toString();
  }

  public BigDecimal getValue() {
    return this.number;
  }
}