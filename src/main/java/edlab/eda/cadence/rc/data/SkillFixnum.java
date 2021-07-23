package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SkillFixnum extends SkillBoolean {

  public static final String TYPE_ID = "fixnum";
  private int fixnum;

  public SkillFixnum(int fixnum) {
    super(true);
    this.fixnum = fixnum;
  }

  public int getFixnum() {
    return this.fixnum;
  }

  public int setFixum(int fixnum) {
    return this.fixnum = fixnum;
  }

  public BigDecimal getValue() {
    return new BigDecimal(this.fixnum);
  }

  @Override
  protected String toSkillHierarchical(int depth) {

    return "" + this.fixnum;
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {
    Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);
    element.setTextContent(Integer.toString(this.fixnum));
    return element;
  }

}
