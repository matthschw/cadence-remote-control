package edlab.eda.cadence.rc.data;

import edlab.eda.cadence.rc.session.SkillSession;

public abstract class SkillVector extends SkillDataobject {

  static SkillVector getVectorFromList(final SkillList list) {

    if (list.getByIndex(0) instanceof SkillString) {
      return SkillStringVector.getVectorFromList(list);
    } else if (list.getByIndex(0) instanceof SkillFixnum) {
      return SkillIntegerVector.getVectorFromList(list);
    } else if (list.getByIndex(0) instanceof SkillFlonum) {
      return SkillDoubleVector.getVectorFromList(list);
    } else if (list.getByIndex(0) instanceof SkillComplexNumber) {
      return SkillComplexVector.getVectorFromList(list);
    }

    return null;
  }

  @Override
  public boolean canBeUsedInSession(final SkillSession session) {
    return false;
  }

  @Override
  public boolean isTrue() {
    return true;
  }

  @Override
  String toSkillHierarchical(final int depth) {
    return "nil";
  }

  public abstract int getLength();

  // public abstract Object[] getValues();

  /**
   * Identify whether an object is an instance of this class
   *
   * @param o Object to be checked
   * @return <code>true</code> when the object is an instance of this class,
   *         <code>false</code> otherwise
   */
  public static boolean isInstanceOf(final Object o) {
    return o instanceof SkillVector;
  }
}
