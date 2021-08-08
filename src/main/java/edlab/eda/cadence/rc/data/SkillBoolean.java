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
    return bool;
  }
}