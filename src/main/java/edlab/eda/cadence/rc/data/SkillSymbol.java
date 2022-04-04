package edlab.eda.cadence.rc.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Representation of a SKILL-Symbol
 *
 */
public final class SkillSymbol extends SkillBoolean {

  /**
   * Type-Identifier in XML
   */
  public static final String TYPE_ID = "symbol";

  private final String printName;

  /**
   * Create a SkillSymbol from a given print name
   *
   * @param printName Symbol name
   */
  public SkillSymbol(final String printName) {
    super(true);
    this.printName = printName;
  }

  @Override
  protected String toSkillHierarchical(final int depth) {
    return "'" + this.printName;
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(final String name,
      final Document document) {
    final Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);
    element.setTextContent(this.printName);
    return element;
  }

  @Override
  public boolean equals(final Object o) {

    if (o instanceof SkillSymbol) {
      final SkillSymbol object = (SkillSymbol) o;
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