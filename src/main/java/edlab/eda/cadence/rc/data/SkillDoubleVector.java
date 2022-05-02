package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Skill vector of doubles (BigDecimal)
 */
public final class SkillDoubleVector extends SkillVector {

  private final BigDecimal[] values;

  /**
   * Create a new double vector
   * 
   * @param values
   */
  SkillDoubleVector(final BigDecimal[] values) {
    this.values = values;
  }

  /**
   * Create vector of doubles
   * 
   * @param values
   */
  SkillDoubleVector(final double[] values) {
    this.values = new BigDecimal[values.length];

    for (int i = 0; i < values.length; i++) {
      this.values[i] = new BigDecimal(values[i]).round(MathContext.DECIMAL64);
    }
  }

  /**
   * Create vector of doubles
   * 
   * @param values
   */
  SkillDoubleVector(final List<BigDecimal> values) {

    this.values = new BigDecimal[values.size()];

    int i = 0;

    for (final BigDecimal value : values) {
      this.values[i++] = value;
    }
  }

  /**
   * Create a {@link SkillDoubleVector} from a {@link SkillList}
   * 
   * @param list List
   * @return vector when the list consists uniquely of double. Non double
   *         elements are omitted
   */
  static SkillDoubleVector getVectorFromList(final SkillList list) {

    final List<BigDecimal> values = new ArrayList<>();

    SkillFlonum flonum;
    SkillFixnum fixnum;

    for (final SkillDataobject obj : list) {

      if (obj instanceof SkillFlonum) {
        flonum = (SkillFlonum) obj;
        values.add(flonum.getFlonum());
      } else if (obj instanceof SkillFixnum) {
        fixnum = (SkillFixnum) obj;
        values.add(new BigDecimal(fixnum.getFixnum()));
      }
    }

    return new SkillDoubleVector(values);
  }

  @Override
  public boolean equals(final Object o) {
    if (o instanceof SkillDoubleVector) {
      final SkillDoubleVector vector = (SkillDoubleVector) o;

      if (this.values.length != vector.values.length) {
        return false;
      }

      for (final BigDecimal value : this.values) {
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
  public BigDecimal[] getValues() {
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
    element.setAttribute(SkillDataobject.TYPE_ID, SkillDataobject.TYPE_ID);

    for (final BigDecimal value : this.values) {
      element.appendChild(new SkillFlonum(value)
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
    return o instanceof SkillDoubleVector;
  }
}