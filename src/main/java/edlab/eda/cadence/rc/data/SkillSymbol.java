package edlab.eda.cadence.rc.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Representation of a SKILL-Symbol
 *
 */
public class SkillSymbol extends SkillBoolean {

  /**
   * Type-Identifier in XML
   */
  public static final String TYPE_ID = "symbol";

  private String printName;

  /**
   * Create a SkillSymbol from a given print name
   *
   * @param printName Symbol name
   */
  public SkillSymbol(String printName) {
    super(true);
    this.printName = printName;
  }

  @Override
  protected String toSkillHierarchical(int depth) {
    return "'" + this.printName;
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {
    Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);
    element.setTextContent(this.printName);
    return element;
  }

  @Override
  public boolean equals(Object o) {

    if (o instanceof SkillSymbol) {
      SkillSymbol object = (SkillSymbol) o;
      return this.printName.equals(object.printName);
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
    return o instanceof SkillSymbol;
  }
}