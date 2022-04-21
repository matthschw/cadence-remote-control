package edlab.eda.cadence.rc.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edlab.eda.cadence.rc.session.SkillInteractiveSession;
import edlab.eda.cadence.rc.session.SkillSession;

/**
 * Reference to an object in a {@link SkillInteractiveSession}
 */
public final class SkillComplexDataobject extends SkillDataobject {

  private final SkillSession session;
  private final int identifier;

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
  SkillComplexDataobject(final SkillSession session, final int identifier) {
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
  protected String toSkillHierarchical(final int depth) {

    return "(arrayref (arrayref " + SkillSession.CDS_RC_GLOBAL + "."
        + SkillSession.CDS_RC_SESSIONS + " \""
        + SkillSession.CDS_RC_SESSION + "\")->"
        + SkillSession.CDS_RC_RETURN_VALUES + " " + this.identifier
        + ")";
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(final String name,
      final Document document) {
    return null;
  }

  @Override
  public boolean equals(final Object o) {

    if (o instanceof SkillComplexDataobject) {
      final SkillComplexDataobject object = (SkillComplexDataobject) o;

      return (this.session == object.session)
          && (this.identifier == object.identifier);
    } else {
      return false;
    }
  }

  @Override
  public boolean canBeUsedInSession(final SkillSession session) {
    return (session != null) && (this.session != null)
        && session.equals(this.session);
  }
  
  /**
   * Identify whether an object is an instance of this class
   *
   * @param o Object to be checked
   * @return <code>true</code> when the object is an instance of this class,
   *         <code>false</code> otherwise
   */
  public static boolean isInstanceOf(final Object o) {
    return o instanceof SkillComplexDataobject;
  }
}