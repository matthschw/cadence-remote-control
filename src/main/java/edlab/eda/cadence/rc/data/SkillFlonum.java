package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SkillFlonum extends SkillNumber {

  public static final String TYPE_ID = "flonum";

  public SkillFlonum(BigDecimal flonum) {
    super(flonum);
  }
  
  public BigDecimal getFlonum() {

    return this.number;
  }

  public BigDecimal setFlonum(BigDecimal flonum) {

    return this.number = flonum;
  }
  
  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {
    Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);
    element.setTextContent(this.number.toString());
    return element;
  }
}