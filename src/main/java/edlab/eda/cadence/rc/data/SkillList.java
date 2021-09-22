package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Representation of a Skill list
 *
 */
public class SkillList extends SkillBoolean
    implements Iterable<SkillDataobject> {

  /**
   * Type-Identifier in XML
   */
  public static final String TYPE_ID = "list";

  /**
   * Name-Identifier in XML
   */
  public static final String NAME_ID = "name";

  private LinkedList<SkillDataobject> list;

  /**
   * Create an empty list
   */
  public SkillList() {
    super(false);
    this.list = new LinkedList<SkillDataobject>();
  }

  /**
   * Create a Skill list
   */
  public SkillList(List<SkillDataobject> data) {

    super(true);

    this.list = new LinkedList<SkillDataobject>(data);

    if (this.list.isEmpty()) {
      super.bool = false;
    }
  }

  /**
   * Create a Skill list from a string array
   */
  public SkillList(String[] data) {

    super(true);
    this.list = new LinkedList<SkillDataobject>();

    for (String string : data) {
      this.list.add(new SkillString(string));
    }

    if (this.list.isEmpty()) {
      super.bool = false;
    }
  }
  
  
  /**
   * Create a Skill list from an integer array
   */
  public SkillList(int[] data) {

    super(true);
    this.list = new LinkedList<SkillDataobject>();

    for (int i = 0; i < data.length; i++) {
      this.list.add(new SkillFixnum(data[i]));
    }

    if (this.list.isEmpty()) {
      super.bool = false;
    }
  }

  /**
   * Create a Skill list from a {@link BigDecimal} array
   */
  public SkillList(BigDecimal[] data) {

    super(false);
    this.list = new LinkedList<SkillDataobject>();

    for (int i = 0; i < data.length; i++) {
      this.list.add(new SkillFlonum(data[i]));
    }
  }

  private void updateBool() {
    if (this.list != null) {

      if (this.list.isEmpty()) {
        super.bool = false;
      } else {
        super.bool = true;
      }
    } else {
      super.bool = false;
    }
  }

  /**
   * Add a {@link SkillDataobject} as first list element
   * 
   * @param skillDataobject Object to be added
   */
  @Deprecated
  public void addAtFirst(SkillDataobject skillDataobject) {
    this.list.addFirst(skillDataobject);
    this.updateBool();
  }

  /**
   * Add a {@link SkillDataobject} as first list element
   * 
   * @param skillDataobject Object to be added
   */
  public void prepend(SkillDataobject skillDataobject) {
    this.list.addFirst(skillDataobject);
    this.updateBool();
  }

  /**
   * Add a list of {@link SkillDataobject} as first list elements
   * 
   * @param skillDataobject Object to be added
   */
  public void prepend(List<SkillDataobject> skillDataobjects) {

    LinkedList<SkillDataobject> result = new LinkedList<SkillDataobject>();

    for (SkillDataobject skillDataobject : skillDataobjects) {
      result.add(skillDataobject);
    }

    for (SkillDataobject skillDataobject : this.list) {
      result.add(skillDataobject);
    }

    this.list = result;

    this.updateBool();
  }

  /**
   * Add a list of {@link SkillDataobject} as first list elements
   * 
   * @param list to be added
   */
  public void prepend(SkillList list) {
    this.prepend(list.list);
  }

  /**
   * Add a {@link SkillDataobject} as last list element
   * 
   * @param skillDataobject Object to be added
   */
  @Deprecated
  public void addAtLast(SkillDataobject skillDataobject) {
    this.list.addLast(skillDataobject);
    this.updateBool();
  }

  /**
   * Add a {@link SkillDataobject} as last list element
   * 
   * @param skillDataobject Object to be added
   */
  public void append(SkillDataobject skillDataobject) {
    this.list.addLast(skillDataobject);
    this.updateBool();
  }

  /**
   * Add a list of {@link SkillDataobject} as last list elements
   * 
   * @param skillDataobjects Objects to be added
   */
  public void append(List<SkillDataobject> skillDataobjects) {

    LinkedList<SkillDataobject> result = new LinkedList<SkillDataobject>();

    for (SkillDataobject skillDataobject : this.list) {
      result.add(skillDataobject);
    }

    for (SkillDataobject skillDataobject : skillDataobjects) {
      result.add(skillDataobject);
    }

    this.list = result;

    this.updateBool();
  }

  /**
   * Add a list of {@link SkillDataobject} as last list elements
   * 
   * @param list to be added
   */
  public void append(SkillList list) {
    this.append(list.list);
  }

  /**
   * Add a list of {@link SkillDataobject} as last list elements
   * 
   * @param list to be added
   */
  public void append1(SkillDataobject skillDataobject) {
    this.list.addLast(skillDataobject);
    this.updateBool();
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

  /**
   * Build a Skill list from an array of integers
   * 
   * @param values Array of integers
   * @return Skill list
   */
  public static SkillList get(int[] values) {

    SkillList list = new SkillList();

    for (int i = 0; i < values.length; i++) {
      list.addAtLast(new SkillFixnum(values[i]));
    }

    return list;
  }

}