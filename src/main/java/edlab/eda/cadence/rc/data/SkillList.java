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
public final class SkillList extends SkillBoolean
    implements Iterable<SkillDataobject> {

  /**
   * Type identifier in XML
   */
  public static final String TYPE_ID = "list";

  /**
   * Name identifier in XML
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
   * @param list list of Skill objects
   */
  public SkillList(final List<SkillDataobject> list) {

    super(true);

    if (list == null) {
      this.list = new LinkedList<>();
    } else {
      this.list = new LinkedList<>(list);
    }

    if (this.list.isEmpty()) {
      super.bool = false;
    }
  }

  /**
   * Create a Skill list
   *
   * @param array array of Skill objects
   */
  public SkillList(final SkillDataobject[] array) {

    super(true);

    this.list = new LinkedList<>();

    if (array != null) {

      for (final SkillDataobject obj : array) {
        this.list.addLast(obj);
      }
    }

    if (this.list.isEmpty()) {
      super.bool = false;
    }
  }

  /**
   * Create a Skill list from a string array
   *
   * @param data array of {@link String}
   */
  public SkillList(final String[] data) {

    super(true);
    this.list = new LinkedList<>();

    for (final String string : data) {
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
  public SkillList(final int[] data) {

    super(true);
    this.list = new LinkedList<>();

    for (final int element : data) {
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
  public SkillList(final BigDecimal[] data) {

    super(true);

    this.list = new LinkedList<>();

    for (final BigDecimal element : data) {
      this.list.add(new SkillFlonum(element));
    }

    if (this.list.isEmpty()) {
      this.updateBool();
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
  public void addAtFirst(final SkillDataobject skillDataobject) {
    this.list.addFirst(skillDataobject);
    this.updateBool();
  }

  /**
   * Add a {@link SkillDataobject} as first list element
   *
   * @param skillDataobject Object to be added
   */
  public void prepend(final SkillDataobject skillDataobject) {
    this.list.addFirst(skillDataobject);
    this.updateBool();
  }

  /**
   * Add a list of {@link SkillDataobject} as first list elements
   *
   * @param skillDataobjects List of Skill data-objects
   */
  public void prepend(final List<SkillDataobject> skillDataobjects) {

    final LinkedList<SkillDataobject> result = new LinkedList<>();

    result.addAll(skillDataobjects);

    result.addAll(this.list);

    this.list = result;

    this.updateBool();
  }

  /**
   * Add a list of {@link SkillDataobject} as first list elements
   *
   * @param list to be added
   */
  public void prepend(final SkillList list) {
    this.prepend(list.list);
  }

  /**
   * Add a {@link SkillDataobject} as last list element
   *
   * @param skillDataobject Object to be added
   */
  @Deprecated
  public void addAtLast(final SkillDataobject skillDataobject) {
    this.list.addLast(skillDataobject);
    this.updateBool();
  }

  /**
   * Add a {@link SkillDataobject} as last list element
   *
   * @param skillDataobject Object to be added
   */
  public void append(final SkillDataobject skillDataobject) {
    this.list.addLast(skillDataobject);
    this.updateBool();
  }

  /**
   * Add a list of {@link SkillDataobject} as last list elements
   *
   * @param skillDataobjects Objects to be added
   */
  public void append(final List<SkillDataobject> skillDataobjects) {

    final LinkedList<SkillDataobject> result = new LinkedList<>();

    result.addAll(this.list);

    result.addAll(skillDataobjects);

    this.list = result;

    this.updateBool();
  }

  /**
   * Add a list of {@link SkillDataobject} as last list elements
   *
   * @param list list to be appended
   */
  public void append(final SkillList list) {
    this.append(list.list);
  }

  /**
   * Add a list of {@link SkillDataobject} as last list elements
   *
   * @param skillDataobject Skill data-object to be appended at the end
   */
  public void append1(final SkillDataobject skillDataobject) {
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
  public boolean remove(final SkillDataobject skillDataobject) {
    final boolean retval = this.list.remove(skillDataobject);
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
  public SkillDataobject getByIndex(final int index) {
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

    final SkillDataobject[] retval = new SkillDataobject[this.getLength()];

    int i = 0;

    for (final SkillDataobject obj : this.list) {
      retval[i++] = obj;
    }

    return retval;
  }

  @Override
  public String toSkillHierarchical(int depth) {

    if (super.bool) {

      final StringBuilder builder = new StringBuilder();

      boolean firstIteration = true;

      if (depth == 0) {
        builder.append("'");
      }

      builder.append("(");

      for (final SkillDataobject skillDataobject : this.list) {

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
  protected Element traverseSkillDataobjectForXMLGeneration(final String name,
      final Document document) {

    final Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);

    for (final SkillDataobject skillDataobject : this.list) {
      element.appendChild(skillDataobject
          .traverseSkillDataobjectForXMLGeneration("entry", document));
    }

    return element;
  }

  @Override
  public boolean equals(final Object o) {

    if (o instanceof SkillList) {

      final SkillList object = (SkillList) o;

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

  static SkillList build(final SkillSession session, final Element element) {

    final SkillList list = new SkillList();

    final NodeList nodeList = element.getChildNodes();

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