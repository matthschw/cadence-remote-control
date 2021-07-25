package edlab.eda.cadence.rc.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edlab.eda.cadence.rc.SkillMasterSession;

public class SkillComplexDataobject extends SkillDataobject {

  @SuppressWarnings("unused")
  private SkillMasterSession session;
  private int identifier;
  public static final String TYPE_ID = "complex";

  public SkillComplexDataobject(SkillMasterSession session, int identifier) {
    this.session = session;
    this.identifier = identifier;
  }

  @Override
  public boolean isTrue() {
    return true;
  }

  @Override
  protected String toSkillHierarchical(int depth) {

    return "(arrayref " + SkillMasterSession.CDS_RC_GLOBAL + "."
        + SkillMasterSession.CDS_RC_RETURN_VALUES + " " + this.identifier + ")";
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {
    // TODO Auto-generated method stub
    return null;
  }

}
