package edlab.eda.cadence.rc.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SkillIntegerVector extends SkillVector {
  private int[] values;

  public SkillIntegerVector(int[] values) {
    this.values = values;
  }

  static SkillVector getVectorFromList(SkillList list) {

    int[] values = new int[list.getLength()];

    SkillFixnum fixnum;

    for (int i = 0; i < values.length; i++) {
      fixnum = (SkillFixnum) list.getByIndex(i);
      values[i] = fixnum.getFixnum();
    }

    return new SkillIntegerVector(values);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof SkillIntegerVector) {
      SkillIntegerVector vector = (SkillIntegerVector) o;

      if (this.values.length != vector.values.length) {
        return false;
      }

      for (int i = 0; i < values.length; i++) {
        if (this.values[i] != vector.values[i]) {
          return false;
        }
      }

      return true;
    } else {
      return false;
    }
  }
  
  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {

    Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);

    for (int value : this.values) {
      element.appendChild(new SkillFixnum(value)
          .traverseSkillDataobjectForXMLGeneration("entry", document));
    }

    return element;
  }
}
