package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SkillFlonum extends SkillBoolean {

  private BigDecimal flonum;
  public static final String TYPE_ID = "flonum";

  public SkillFlonum(BigDecimal flonum) {
    super(true);
    this.flonum = flonum;
  }

  public BigDecimal getFlonum() {

    return this.flonum;
  }

  public BigDecimal setFlonum(BigDecimal flonum) {

    return this.flonum = flonum;
  }

  public BigDecimal getValue() {
    return flonum;
  }

  @Override
  protected String toSkillHierarchical(int depth) {

    return "" + this.flonum;
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {
    Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);
    element.setTextContent(flonum.toString());
    return element;
  }

}
