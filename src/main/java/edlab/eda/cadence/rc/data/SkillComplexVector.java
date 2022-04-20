package edlab.eda.cadence.rc.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Skill vector of complex values
 */
public final class SkillComplexVector extends SkillVector {

  private final Complex[] values;

  /**
   * Create a Skill vector of complex values
   */
  SkillComplexVector(final Complex[] values) {
    this.values = values;
  }

  /**
   * Create a Skill vector of complex values
   * 
   * @param values
   */
  SkillComplexVector(final List<Complex> values) {

    this.values = new Complex[values.size()];

    int i = 0;

    for (final Complex value : values) {
      this.values[i++] = value;
    }
  }

  /**
   * Create a {@link SkillComplexVector} from a {@link SkillList}
   * 
   * @param list List
   * @return vector when the list consists uniquely of complex objects. Non
   *         complex elements are omitted
   */
  static SkillComplexVector getVectorFromList(final SkillList list) {

    final List<Complex> values = new ArrayList<>();

    SkillComplexNumber complexNumber;

    for (final SkillDataobject obj : list) {

      try {

        complexNumber = (SkillComplexNumber) obj;
        values.add(complexNumber.getComplex());

      } catch (final Exception e) {
      }
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

  /**
   * Get all values in the vector as array
   * 
   * @return array
   */
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