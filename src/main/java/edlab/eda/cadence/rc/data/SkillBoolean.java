package edlab.eda.cadence.rc.data;

/**
 * Representation of a boolean value in Skill
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

  /**
   * Get a Skill data-object that corresponds to <code>nil</code>
   *
   * @return Skill data-object
   */
  public static SkillDataobject getFalse() {
    return new SkillList();
  }

  /**
   * Get a Skill data-object that corresponds to <code>'t</code>
   *
   * @return Skill data-object
   */
  public static SkillDataobject getTrue() {
    return new SkillSymbol("t");
  }

  /**
   * Create a Skill data-object that corresponds to a boolean value
   *
   * @param bool Boolean value
   * @return Skill data-object
   */
  public static SkillDataobject get(boolean bool) {

    if (bool) {
      return SkillBoolean.getTrue();
    } else {
      return SkillBoolean.getFalse();
    }
  }
  
  /**
   * Identify whether an object is an instance of this class
   *
   * @param o Object to be checked
   * @return <code>true</code> when the object is an instance of this class,
   *         <code>false</code> otherwise
   */
  public static boolean isInstanceOf(final Object o) {
    return o instanceof SkillBoolean;
  }
}