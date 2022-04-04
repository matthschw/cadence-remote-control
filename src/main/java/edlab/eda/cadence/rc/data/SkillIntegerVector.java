package edlab.eda.cadence.rc.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class SkillIntegerVector extends SkillVector {
  private final int[] values;

  SkillIntegerVector(final int[] values) {
    this.values = values;
  }

  static SkillVector getVectorFromList(final SkillList list) {

    final int[] values = new int[list.getLength()];

    SkillFixnum fixnum;

    for (int i = 0; i < values.length; i++) {
      fixnum = (SkillFixnum) list.getByIndex(i);
      values[i] = fixnum.getFixnum();
    }

    return new SkillIntegerVector(values);
  }

  @Override
  public boolean equals(final Object o) {
    if (o instanceof SkillIntegerVector) {
      final SkillIntegerVector vector = (SkillIntegerVector) o;

      if (this.values.length != vector.values.length) {
        return false;
      }

      for (int i = 0; i < this.values.length; i++) {
        if (this.values[i] != vector.values[i]) {
          return false;
        }
      }

      return true;
    } else {
      return false;
    }
  }

  public int[] getValues() {
    return this.values;
  }

  @Override
  public int getLength() {
    return this.values.length;
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(final String name,
      final Document document) {

    final Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);

    for (final int value : this.values) {
      element.appendChild(new SkillFixnum(value)
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
    return o instanceof SkillIntegerVector;
  }
}
