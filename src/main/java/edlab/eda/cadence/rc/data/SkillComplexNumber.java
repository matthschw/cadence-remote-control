package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SkillComplexNumber extends SkillNumber{

  /**
   * Type-Identifier in XML
   */
  public static final String TYPE_ID = "doublecomplex";
  
  public SkillComplexNumber(BigDecimal number) {
    super();
  }

  @Override
  protected String toSkillHierarchical(int depth) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean equals(Object o) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {
    // TODO Auto-generated method stub
    return null;
  }
}