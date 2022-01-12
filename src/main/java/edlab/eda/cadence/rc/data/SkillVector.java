package edlab.eda.cadence.rc.data;

import edlab.eda.cadence.rc.session.SkillSession;

public abstract class SkillVector extends SkillDataobject {

  static SkillVector getVectorFromList(SkillList list) {

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
  public boolean canBeUsedInSession(SkillSession session) {
    return false;
  }

  @Override
  public boolean isTrue() {
    return true;
  }

  @Override
  String toSkillHierarchical(int depth) {
    return "nil";
  }
}
