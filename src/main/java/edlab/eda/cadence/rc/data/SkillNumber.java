package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;

public abstract class SkillNumber extends SkillDataobject {

  public abstract String toSkillHierarchical(int depth);

  public abstract BigDecimal getValue();
}