package edlab.eda.cadence.rc.data;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Skill vector of integers
 */
public final class SkillIntegerVector extends SkillVector {

  private final int[] values;

  /**
   * Create vector of integers
   * 
   * @param values
   */
  SkillIntegerVector(final int[] values) {
    this.values = values;
  }

  /**
   * Create vector of integers
   * 
   * @param values
   */
  SkillIntegerVector(final List<Integer> values) {

    this.values = new int[values.size()];

    int i = 0;

    for (Integer integer : values) {
      this.values[i++] = integer;
    }
  }

  /**
   * Create a {@link SkillStringVector} from a {@link SkillList}
   * 
   * @param list List
   * @return vector when the list consists uniquely of integers. Non intgeger
   *         elements are omitted
   */
  static SkillIntegerVector getVectorFromList(final SkillList list) {

    List<Integer> values = new ArrayList<Integer>();

    SkillFixnum fixnum;

    for (SkillDataobject obj : list) {

      try {

        fixnum = (SkillFixnum) obj;
        values.add(fixnum.getFixnum());

      } catch (Exception e) {
      }
    }

    return new SkillIntegerVector(values);
  }

  @Override
  public boolean equals(final Object o) {
    if (o instanceof SkillIntegerVector) {
      final SkillIntegerVector vector = (SkillIntegerVector) o;

      if (this.values.length != vector.values.length) {
        return false;
      }

      for (int i = 0; i < this.values.length; i++) {
        if (this.values[i] != vector.values[i]) {
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
  public int[] getValues() {
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

    for (final int value : this.values) {
      element.appendChild(new SkillFixnum(value)
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
    return o instanceof SkillIntegerVector;
  }
}