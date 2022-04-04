package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class SkillDoubleVector extends SkillVector {

  private final BigDecimal[] values;

   SkillDoubleVector(final BigDecimal[] values) {
    this.values = values;
  }

  static SkillVector getVectorFromList(final SkillList list) {

    final BigDecimal[] values = new BigDecimal[list.getLength()];

    SkillFlonum flonum;

    for (int i = 0; i < values.length; i++) {
      flonum = (SkillFlonum) list.getByIndex(i);
      values[i] = flonum.getFlonum();
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
          
          //System.err.println("VEC " + i + "|"  this.values[i] +"!=" + values[i]);
          return false;
        }
      }

      return true;
    } else {
      return false;
    }
  }
  
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
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);

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
