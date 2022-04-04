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

  private final String string;

  /**
   * Create a new SKILL representation of a String
   *
   * @param string String
   */
  public SkillString(final String string) {
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
  protected String toSkillHierarchical(final int depth) {
    return "\"" + StringEscapeUtils.ESCAPE_JAVA.translate(this.string) + "\"";
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(final String name,
      final Document document) {
    final Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);
    element.setTextContent(this.string);
    return element;
  }

  @Override
  public boolean equals(final Object o) {

    String str = null;

    if (o instanceof SkillString) {
      str = ((SkillString) o).getString();
    } else if (o instanceof String) {
      str = (String) o;
    }

    if (str != null) {
      return this.getString().equals(str);
    } else {
      return false;
    }
  }

  /**
   * Identify whether an object is an instance of this class
   *
   * @param o Object to be checked
   * @return <code>true</code> when the object is an instance of this class,
   *         <code>false</code> otherwise
   */
  public static boolean isInstanceOf(final Object o) {
    return o instanceof SkillString;
  }
}
