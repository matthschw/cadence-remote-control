package edlab.eda.cadence.rc.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edlab.eda.cadence.rc.session.SkillSession;

/**
 * Waveform with single axis
 */
public final class SkillSingleWave extends SkillDataobject {

  /**
   * Type-Identifier in XML
   */
  public static final String TYPE_ID = "wave";

  private final SkillVector x;
  private final SkillVector y;

  @Override
  public String toSkill() {
    return SkillBoolean.getFalse().toSkill();
  }

  /**
   * Create a wave
   * 
   * @param x Vector of X values
   * @param y Vector of Y values
   */
  SkillSingleWave(final SkillVector x, final SkillVector y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Get the X values in the waveform
   * 
   * @return x vector
   */
  public SkillVector getX() {
    return this.x;
  }

  /**
   * Get the Y values in the waveform
   * 
   * @return y vector
   */
  public SkillVector getY() {
    return this.y;
  }

  /**
   * Build a {@link SkillSingleWave} from a XML representaion
   * 
   * @param session Session
   * @param element XML element
   * @return wave when valid, <code>null</code> otherwise
   */
  public static SkillDataobject build(final SkillSession session,
      final Element element) {

    final SkillList xList = SkillList.build(session,
        (Element) element.getElementsByTagName("x").item(0));

    final SkillList yList = SkillList.build(session,
        (Element) element.getElementsByTagName("y").item(0));

    return new SkillSingleWave(SkillVector.getVectorFromList(xList),
        SkillVector.getVectorFromList(yList));
  }

  @Override
  public boolean canBeUsedInSession(final SkillSession session) {
    // waveform cannot be backannotated to Virtuoso
    return false;
  }

  @Override
  public boolean isTrue() {
    return true;
  }

  @Override
  public boolean equals(final Object o) {
    if (o instanceof SkillSingleWave) {
      final SkillSingleWave wave = (SkillSingleWave) o;
      return this.x.equals(wave.x) && this.y.equals(wave.y);
    } else {
      return false;
    }
  }

  @Override
  String toSkillHierarchical(final int depth) {
    return "nil";
  }

  @Override
  Element traverseSkillDataobjectForXMLGeneration(final String name,
      final Document document) {

    final Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, SkillSingleWave.TYPE_ID);

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