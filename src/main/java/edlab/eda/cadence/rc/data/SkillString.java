package edlab.eda.cadence.rc.data;

import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Representation of a SKILL-String
 *
 */
public class SkillString extends SkillBoolean {

  /**
   * Type-Identifier in XML
   */
  public static final String TYPE_ID = "string";

  private String string;

  /**
   * Create a new SKILL representation of a String
   * 
   * @param string String
   */
  public SkillString(String string) {
    super(true);
    this.string = string;
  }

  public SkillString() {
    super(true);
    this.string = "";
  }

  public String getString() {
    return this.string;
  }

  @Override
  protected String toSkillHierarchical(int depth) {
    return "\"" + StringEscapeUtils.ESCAPE_JAVA.translate(this.string) + "\"";
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {
    Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);
    element.setTextContent(this.string);
    return element;
  }

  @Override
  public boolean equals(Object o) {

    if (o instanceof SkillString) {
      SkillString object = (SkillString) o;
      return this.string.equals(object.string);
    } else {
      return false;
    }
  }
}
