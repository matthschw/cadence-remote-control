package edlab.eda.cadence.rc.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edlab.eda.cadence.rc.session.SkillSession;

public final class SkillSingleWave extends SkillDataobject {

  /**
   * Type-Identifier in XML
   */
  public static final String TYPE_ID = "wave";

  private SkillVector x;
  private SkillVector y;

  public String toSkill() {
    return "nil";
  }

  public SkillSingleWave(SkillVector x, SkillVector y) {
    this.x = x;
    this.y = y;
  }

  public SkillVector getX() {
    return this.x;
  }

  public SkillVector getY() {
    return this.y;
  }

  public static SkillDataobject build(SkillSession session, Element element) {

    SkillList xList = (SkillList) SkillList.build(session,
        (Element) element.getElementsByTagName("x").item(0));

    SkillList yList = (SkillList) SkillList.build(session,
        (Element) element.getElementsByTagName("y").item(0));

    return new SkillSingleWave(SkillVector.getVectorFromList(xList),
        SkillVector.getVectorFromList(yList));
  }

  @Override
  public boolean canBeUsedInSession(SkillSession session) {
    return false;
  }

  @Override
  public boolean isTrue() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof SkillSingleWave) {
      SkillSingleWave wave = (SkillSingleWave) o;
      return this.x.equals(wave.x) && this.y.equals(wave.y);
    } else {
      return false;
    }
  }

  @Override
  String toSkillHierarchical(int depth) {
    return "nil";
  }

  @Override
  Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {

    Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);

    element.appendChild(
        this.x.traverseSkillDataobjectForXMLGeneration("x", document));
    element.appendChild(
        this.y.traverseSkillDataobjectForXMLGeneration("y", document));

    return element;
  }

  /**
   * Identify whether an object is an instance of this class
   *
   * @param o Object to be checked
   * @return <code>true</code> when the object is an instance of this class,
   *         <code>false</code> otherwise
   */
  public static boolean isInstanceOf(final Object o) {
    return o instanceof SkillSingleWave;
  }
}