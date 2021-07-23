package edlab.eda.cadence.rc.data;

import java.util.Iterator;
import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SkillList extends SkillBoolean
    implements Iterable<SkillDataobject> {
  public static final String TYPE_ID = "list";
  private LinkedList<SkillDataobject> list;

  public SkillList() {
    super(false);
    this.list = new LinkedList<SkillDataobject>();
  }

  public void addAtFirst(SkillDataobject skillDataobject) {
    super.bool = true;
    this.list.addFirst(skillDataobject);
  }

  public void addAtLast(SkillDataobject skillDataobject) {
    super.bool = true;
    this.list.addLast(skillDataobject);
  }

  public SkillDataobject getByIndex(int index) {

    return this.list.get(index);
  }

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

}
