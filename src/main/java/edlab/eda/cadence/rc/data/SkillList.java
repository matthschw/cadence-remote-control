package edlab.eda.cadence.rc.data;

import java.util.Iterator;
import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Representation of a SKILL-List
 *
 */
public class SkillList extends SkillBoolean
    implements Iterable<SkillDataobject> {

  /**
   * Type-Identifier in XML
   */
  public static final String TYPE_ID = "list";

  private LinkedList<SkillDataobject> list;

  /**
   * Create an empty list
   */
  public SkillList() {
    super(false);
    this.list = new LinkedList<SkillDataobject>();
  }

  /**
   * Add a {@link SkillDataobject} as first list element
   * 
   * @param skillDataobject Object to be added
   */
  public void addAtFirst(SkillDataobject skillDataobject) {
    super.bool = true;
    this.list.addFirst(skillDataobject);
  }

  /**
   * Add a {@link SkillDataobject} as last list element
   * 
   * @param skillDataobject Object to be added
   */
  public void addAtLast(SkillDataobject skillDataobject) {
    super.bool = true;
    this.list.addLast(skillDataobject);
  }

  /**
   * Remove of a {@link SkillDataobject} from a list
   * 
   * @param skillDataobject Object to be removed
   * @return <code>true</code> when the object was removed successfully,
   *         <code>false</code> otherwise
   */
  public boolean remove(SkillDataobject skillDataobject) {
    boolean retval = this.list.remove(skillDataobject);
    if (this.list.isEmpty()) {
      super.bool = false;
    }
    return retval;
  }

  /**
   * Get a {@link SkillDataobject} from a list by index
   * 
   * @param index Position in list
   * @return list element
   */
  public SkillDataobject getByIndex(int index) {
    return this.list.get(index);
  }

  /**
   * Get number of list elements
   * 
   * @return number of list elements
   */
  public int getLength() {
    return this.list.size();
  }

  @Override
  public String toSkillHierarchical(int depth) {

    if (super.bool) {
      String s;
      Boolean firstIteration = true;

      if (depth == 0) {
        s = "'(";
      } else {
        s = "(";
      }

      for (SkillDataobject skillDataobject : this.list) {

        if (!firstIteration) {
          s += " ";

        } else {
          firstIteration = false;
        }

        s += skillDataobject.toSkillHierarchical(++depth);
      }

      s += ")";

      return s;

    } else {
      return "nil";
    }
  }

  @Override
  public Iterator<SkillDataobject> iterator() {

    return this.list.iterator();
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {

    Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);

    for (SkillDataobject skillDataobject : this.list) {
      element.appendChild(skillDataobject
          .traverseSkillDataobjectForXMLGeneration("entry", document));
    }

    return element;
  }

  @Override
  public boolean equals(Object o) {

    if (o instanceof SkillList) {

      SkillList object = (SkillList) o;

      if (this.list.size() != object.list.size()) {
        return false;
      }

      for (int i = 0; i < this.list.size(); i++) {
        if (!this.list.get(i).equals(object.list.get(i))) {
          return false;
        }
      }

      return true;
    } else {
      return false;
    }
  }
}