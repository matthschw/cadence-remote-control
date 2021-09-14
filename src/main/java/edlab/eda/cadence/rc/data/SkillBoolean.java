package edlab.eda.cadence.rc.data;

/**
 * Representation of a boolean value in SKILL
 *
 */
public abstract class SkillBoolean extends SkillNativeDataobject {

  protected boolean bool;

  public SkillBoolean(boolean bool) {
    this.bool = bool;
  }

  @Override
  protected abstract String toSkillHierarchical(int depth);

  @Override
  public boolean isTrue() {
    return this.bool;
  }

  public static SkillDataobject getFalse() {
    return new SkillList();
  }

  public static SkillDataobject getTrue() {
    return new SkillSymbol("t");
  }

  public static SkillDataobject get(boolean bool) {
    
    if (bool) {
      return SkillBoolean.getTrue();
    } else {
      return SkillBoolean.getFalse();
    }
  }
}