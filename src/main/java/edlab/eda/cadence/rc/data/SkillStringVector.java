package edlab.eda.cadence.rc.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class SkillStringVector extends SkillVector {

  private String[] values;

  SkillStringVector(String[] values) {
    this.values = values;
  }

  static SkillVector getVectorFromList(SkillList list) {

    String[] values = new String[list.getLength()];

    SkillString str;

    for (int i = 0; i < values.length; i++) {
      str = (SkillString) list.getByIndex(i);
      values[i] = str.getString();
    }

    return new SkillStringVector(values);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof SkillStringVector) {
      SkillStringVector vector = (SkillStringVector) o;

      if (this.values.length != vector.values.length) {
        return false;
      }

      for (int i = 0; i < values.length; i++) {
        if (!this.values[i].equals(vector.values[i])) {
          return false;
        }
      }

      return true;
    } else {
      return false;
    }
  }

  public String[] getValues() {
    return this.values;
  }

  @Override
  public int getLength() {
    return this.values.length;
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {

    Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);

    for (String value : this.values) {
      element.appendChild(new SkillString(value)
          .traverseSkillDataobjectForXMLGeneration("entry", document));
    }

    return element;
  }

  /**
   * Identify whether an object is an instance of this class
   *
   * @param o Object to be checked
   * @return <code>true</code> when the object is an instance of this class,
   *         <code>false</code> otherwise
   */
  public static boolean isInstanceOf(final Object o) {
    return o instanceof SkillStringVector;
  }

}
