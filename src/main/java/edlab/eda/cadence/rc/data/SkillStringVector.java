package edlab.eda.cadence.rc.data;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Skill vector of strings
 */
public final class SkillStringVector extends SkillVector {

  private final String[] values;

  /**
   * Create vector of strings
   * 
   * @param values
   */
  SkillStringVector(final String[] values) {
    this.values = values;
  }

  /**
   * Create vector of strings
   * 
   * @param values
   */
  SkillStringVector(final List<String> values) {

    this.values = new String[values.size()];

    int i = 0;

    for (String string : values) {
      this.values[i++] = string;
    }
  }

  /**
   * Create an empty vector
   */
  SkillStringVector() {
    this.values = new String[0];
  }

  /**
   * Create a {@link SkillStringVector} from a {@link SkillList}
   * 
   * @param list List
   * @return vector when the list consists uniquely of strings. Non string
   *         elements are omitted
   */
  static SkillVector getVectorFromList(final SkillList list) {

    List<String> values = new ArrayList<String>();

    SkillString skillString;

    for (SkillDataobject string : list) {

      try {

        skillString = (SkillString) string;
        values.add(skillString.getString());

      } catch (Exception e) {
      }
    }

    return new SkillStringVector(values);
  }

  @Override
  public boolean equals(final Object o) {
    if (o instanceof SkillStringVector) {
      final SkillStringVector vector = (SkillStringVector) o;

      if (this.values.length != vector.values.length) {
        return false;
      }

      for (int i = 0; i < this.values.length; i++) {
        if (!this.values[i].equals(vector.values[i])) {
          return false;
        }
      }

      return true;
    } else {
      return false;
    }
  }

  /**
   * Get all values in the vector as array
   * 
   * @return array
   */
  public String[] getValues() {
    return this.values;
  }

  @Override
  public int getLength() {
    return this.values.length;
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(final String name,
      final Document document) {

    final Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);

    for (final String value : this.values) {
      element.appendChild(new SkillString(value)
          .traverseSkillDataobjectForXMLGeneration("entry", document));
    }

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
    return o instanceof SkillStringVector;
  }
}