package edlab.eda.cadence.rc.data;

import org.apache.commons.math3.complex.Complex;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class SkillComplexVector extends SkillVector {
  private final Complex[] values;

  SkillComplexVector(final Complex[] values) {
    this.values = values;
  }

  static SkillVector getVectorFromList(final SkillList list) {

    final Complex[] values = new Complex[list.getLength()];

    SkillComplexNumber complex;

    for (int i = 0; i < values.length; i++) {
      complex = (SkillComplexNumber) list.getByIndex(i);
      values[i] = complex.getComplex();
    }

    return new SkillComplexVector(values);
  }

  @Override
  public boolean equals(final Object o) {
    if (o instanceof SkillComplexVector) {
      final SkillComplexVector vector = (SkillComplexVector) o;

      if (this.values.length != vector.values.length) {
        return false;
      }

      for (final Complex value : this.values) {
        if (!value.equals(value)) {
          return false;
        }
      }

      return true;
    } else {
      return false;
    }
  }

  public Complex[] getValues() {
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

    for (final Complex value : this.values) {
      element.appendChild(new SkillComplexNumber(value)
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
    return o instanceof SkillComplexNumber;
  }
}
