package edlab.eda.cadence.rc.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edlab.eda.cadence.rc.SkillSession;

public class SkillComplexDataobject extends SkillDataobject {

  private SkillSession session;
  private int identifier;
  public static final String TYPE_ID = "complex";

  public SkillComplexDataobject(SkillSession session, int identifier) {
    this.session = session;
    this.identifier = identifier;
  }

  @Override
  public boolean isTrue() {
    return true;
  }

  public SkillSession getSession() {
    return session;
  }

  @Override
  protected String toSkillHierarchical(int depth) {

    return "(arrayref (arrayref " + SkillSession.CDS_RC_GLOBAL + "."
        + SkillSession.CDS_RC_SESSIONS + " " + SkillSession.CDS_RC_SESSION
        + ")->" + SkillSession.CDS_RC_RETURN_VALUES + " " + this.identifier
        + ")";
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {
    // TODO Auto-generated method stub
    return null;
  }

}
