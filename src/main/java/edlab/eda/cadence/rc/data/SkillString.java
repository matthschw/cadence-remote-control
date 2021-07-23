package edlab.eda.cadence.rc.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SkillString extends SkillBoolean {

  public static final String TYPE_ID = "string";
  private String string;

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

    return "\"" + this.string + "\"";
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {
    Element element = document.createElement(name);
    element.setAttribute("type", "string");
    element.setTextContent(this.string);
    return element;
  }
}
