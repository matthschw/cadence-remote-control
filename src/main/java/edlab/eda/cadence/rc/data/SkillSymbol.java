package edlab.eda.cadence.rc.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * SkillSymbol
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
   * @param printName
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
}