package edlab.eda.cadence.rc.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Representation of a Skill fixnum
 *
 */
public class SkillFixnum extends SkillNumber {

  private int value;

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
    super();
    this.value = fixnum;

  }

  /**
   * Get the fixnum value
   * 
   * @return fixnum
   */
  public int getFixnum() {
    return this.value;
  }

  /**
   * Set the fixnum value
   * 
   * @param fixnum Value as integer
   * @return value
   */
  public int setFixum(int fixnum) {
    this.value = fixnum;
    return this.value;
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {
    Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);
    element.setTextContent(String.valueOf(this.value));
    return element;
  }

  @Override
  public boolean equals(Object o) {

    if (o instanceof SkillFixnum) {
      SkillFixnum object = (SkillFixnum) o;
      return this.value == object.value;
    } else {
      return false;
    }
  }

  @Override
  protected String toSkillHierarchical(int depth) {
    return String.valueOf(this.value);
  }
}