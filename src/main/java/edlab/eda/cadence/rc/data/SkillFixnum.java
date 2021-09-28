package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Representation of a Skill fixnum
 *
 */
public class SkillFixnum extends SkillNumber {

  /**
   * Type-Identifier in XML
   */
  public static final String TYPE_ID = "fixnum";

  /**
   * Create a new Skill representation of a fixnum
   * 
   * @param fixnum Fixnum
   */
  public SkillFixnum(int fixnum) {
    super(new BigDecimal(fixnum));
  }

  /**
   * Get the fixnum value
   * 
   * @return fixnum
   */
  public int getFixnum() {
    return this.number.intValue();
  }

  /**
   * Set the fixnum value
   * 
   * @param fixnum Value as integer
   * @return value
   */
  public int setFixum(int fixnum) {
    this.number = new BigDecimal(fixnum);
    return this.number.intValue();
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {
    Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);
    element.setTextContent(this.number.toString());
    return element;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof SkillFixnum) {
      SkillFixnum object = (SkillFixnum) o;
      return this.getValue().equals(object.getValue());
    } else {
      return false;
    }
  }
}