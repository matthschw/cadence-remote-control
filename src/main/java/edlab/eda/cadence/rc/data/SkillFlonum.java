package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Representation of a Skill flonum
 *
 */
public final class SkillFlonum extends SkillNumber {

  private final BigDecimal value;

  /**
   * Type-Identifier in XML
   */
  public static final String TYPE_ID = "flonum";

  /**
   * Create a new SKILL representation of a flonum
   *
   * @param flonum Flonum
   */
  public SkillFlonum(final BigDecimal flonum) {
    super();
    this.value = flonum;
  }

  /**
   * Get the flonum
   *
   * @return flonum
   */
  public BigDecimal getFlonum() {
    return this.value;
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(final String name,
      final Document document) {
    final Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, SkillFlonum.TYPE_ID);
    element.setTextContent(this.value.toString());
    return element;
  }

  @Override
  public boolean equals(final Object o) {

    BigDecimal value = null;

    if (o instanceof SkillFlonum) {
      value = ((SkillFlonum) o).getFlonum();
    } else if (o instanceof Double) {
      value = new BigDecimal((Double) o);
    } else if (o instanceof Float) {
      value = new BigDecimal((Float) o);
    } else if (o instanceof BigDecimal) {
      value = (BigDecimal) o;
    }

    return this.getFlonum().equals(value);
  }

  @Override
  protected String toSkillHierarchical(final int depth) {
    return "" + this.value.toString();
  }

  /**
   * Identify whether an object is an instance of this class
   *
   * @param o Object to be checked
   * @return <code>true</code> when the object is an instance of this class,
   *         <code>false</code> otherwise
   */
  public static boolean isInstanceOf(final Object o) {
    return o instanceof SkillFlonum;
  }
}