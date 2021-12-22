package edlab.eda.cadence.rc.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edlab.eda.cadence.rc.session.SkillInteractiveSession;
import edlab.eda.cadence.rc.session.SkillSession;

/**
 * Reference to an object in a {@link SkillInteractiveSession}
 *
 */
public class SkillComplexDataobject extends SkillDataobject {

  private SkillSession session;
  private int identifier;

  /**
   * Type-Identifier in XML
   */
  public static final String TYPE_ID = "complex";

  /**
   * Create a {@link SkillComplexDataobject}
   *
   * @param session    {@link SkillInteractiveSession} where the
   *                   {@link SkillComplexDataobject} is referenced
   * @param identifier Identifier of the {@link SkillComplexDataobject}
   */
  SkillComplexDataobject(SkillSession session, int identifier) {
    this.session = session;
    this.identifier = identifier;
  }

  @Override
  public boolean isTrue() {
    return true;
  }

  /**
   * Get the session where the {@link SkillComplexDataobject} is referenced
   *
   * @return session
   */
  public SkillSession getSession() {
    return this.session;
  }

  @Override
  protected String toSkillHierarchical(int depth) {

    return "(arrayref (arrayref " + SkillSession.CDS_RC_GLOBAL + "."
        + SkillSession.CDS_RC_SESSIONS + " \""
        + SkillSession.CDS_RC_SESSION + "\")->"
        + SkillSession.CDS_RC_RETURN_VALUES + " " + this.identifier
        + ")";
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {
    return null;
  }

  @Override
  public boolean equals(Object o) {

    if (o instanceof SkillComplexDataobject) {
      SkillComplexDataobject object = (SkillComplexDataobject) o;

      return this.session == object.session
          && this.identifier == object.identifier;

    } else {
      return false;
    }
  }

  @Override
  public boolean canBeUsedInSession(SkillSession session) {
    return session != null && this.session != null
        && session.equals(this.session);
  }
}