package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SkillDoubleVector extends SkillVector {

  private BigDecimal[] values;

  public SkillDoubleVector(BigDecimal[] values) {
    this.values = values;
  }

  static SkillVector getVectorFromList(SkillList list) {

    BigDecimal[] values = new BigDecimal[list.getLength()];

    SkillFlonum flonum;

    for (int i = 0; i < values.length; i++) {
      flonum = (SkillFlonum) list.getByIndex(i);
      values[i] = flonum.getFlonum();
    }

    return new SkillDoubleVector(values);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof SkillDoubleVector) {
      SkillDoubleVector vector = (SkillDoubleVector) o;

      if (this.values.length != vector.values.length) {
        return false;
      }

      for (int i = 0; i < values.length; i++) {
        if (!this.values[i].equals(values[i])) {
          
          //System.err.println("VEC " + i + "|"  this.values[i] +"!=" + values[i]);
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

    for (BigDecimal value : this.values) {
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
