package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edlab.eda.cadence.rc.session.SkillSession;

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
    this.list = new LinkedList<>();
  }

  /**
   * Create a Skill list
   *
   * @param data list of Skill data-objects
   */
  public SkillList(List<SkillDataobject> data) {

    super(true);

    this.list = new LinkedList<>(data);

    if (this.list.isEmpty()) {
      super.bool = false;
    }
  }

  /**
   * Create a Skill list from a string array
   *
   * @param data array of {@link String}
   */
  public SkillList(String[] data) {

    super(true);
    this.list = new LinkedList<>();

    for (String string : data) {
      this.list.add(new SkillString(string));
    }

    if (this.list.isEmpty()) {
      super.bool = false;
    }
  }

  /**
   * Create a Skill list from an integer array
   *
   * @param data array of {@link Integer}
   */
  public SkillList(int[] data) {

    super(true);
    this.list = new LinkedList<>();

    for (int element : data) {
      this.list.add(new SkillFixnum(element));
    }

    if (this.list.isEmpty()) {
      super.bool = false;
    }
  }

  /**
   * Create a Skill list from a {@link BigDecimal} array
   *
   * @param data array of {@link BigDecimal}
   */
  public SkillList(BigDecimal[] data) {

    super(false);
    this.list = new LinkedList<>();

    for (BigDecimal element : data) {
      this.list.add(new SkillFlonum(element));
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
   * @param skillDataobjects List of Skill data-objects
   */
  public void prepend(List<SkillDataobject> skillDataobjects) {

    LinkedList<SkillDataobject> result = new LinkedList<>();

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

    LinkedList<SkillDataobject> result = new LinkedList<>();

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
   * @param list list to be appended
   */
  public void append(SkillList list) {
    this.append(list.list);
  }

  /**
   * Add a list of {@link SkillDataobject} as last list elements
   *
   * @param skillDataobject Skill data-object to be appended at the end
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

  /**
   * Get an array representation of the {@link SkillList}
   *
   * @return array
   */
  public SkillDataobject[] getArray() {

    SkillDataobject[] retval = new SkillDataobject[this.getLength()];

    int i = 0;

    for (SkillDataobject obj : this.list) {
      retval[i++] = obj;
    }

    return retval;
  }

  @Override
  public String toSkillHierarchical(int depth) {

    if (super.bool) {

      StringBuilder builder = new StringBuilder();

      boolean firstIteration = true;

      if (depth == 0) {
        builder.append("'");
      }

      builder.append("(");

      for (SkillDataobject skillDataobject : this.list) {

        if (!firstIteration) {
          builder.append(" ");
        } else {
          firstIteration = false;
        }

        builder.append(skillDataobject.toSkillHierarchical(++depth));
      }

      builder.append(")");

      return builder.toString();

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

  static SkillList build(SkillSession session, Element element) {

    SkillList list = new SkillList();

    NodeList nodeList = element.getChildNodes();

    for (int i = 0; i < nodeList.getLength(); i++) {

      Element sub;

      if ((sub = getElement(nodeList.item(i))) != null) {
        list.append1(traverseNode(session, sub));
      }
    }

    return list;
  }

  /**
   * Identify whether an object is an instance of this class
   *
   * @param o Object to be checked
   * @return <code>true</code> when the object is an instance of this class,
   *         <code>false</code> otherwise
   */
  public static boolean isInstanceOf(final Object o) {
    return o instanceof SkillList;
  }
}