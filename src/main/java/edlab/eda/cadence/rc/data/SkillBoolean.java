package edlab.eda.cadence.rc.data;

public abstract class SkillBoolean extends SkillNativeDataobject {

  protected boolean bool;

  public SkillBoolean(boolean bool) {
    this.bool = bool;
  }

  @Override
  protected abstract String toSkillHierarchical(int depth);

  @Override
  public boolean isTrue() {
    // TODO Auto-generated method stub
    return bool;
  }
}