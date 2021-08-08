package edlab.eda.cadence.rc.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Representation of a native SKILL disembodied property list
 *
 */
public class SkillDisembodiedPropertyList extends SkillBoolean
    implements Map<String, SkillDataobject> {

  /**
   * Type-Identifier in XML
   */
  public static final String TYPE_ID = "dpl";

  private Map<String, SkillDataobject> properties;

  /**
   * Create an empty disembodied property list
   */
  public SkillDisembodiedPropertyList() {
    super(true);
    this.properties = new HashMap<String, SkillDataobject>();
  }

  public SkillDisembodiedPropertyList(Map<String, SkillDataobject> properties) {
    super(true);
    this.properties = properties;
  }

  @Override
  protected String toSkillHierarchical(int depth) {

    String s;

    if (depth == 0) {
      s = "'(nil";
    } else {
      s = "(nil";
    }

    for (Map.Entry<String, SkillDataobject> entry : this.properties
        .entrySet()) {
      s += " " + entry.getKey() + " "
          + entry.getValue().toSkillHierarchical(++depth);
    }

    s += ")";

    return s;
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {

    Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);

    for (Map.Entry<String, SkillDataobject> entry : this.properties
        .entrySet()) {
      element.appendChild(entry.getValue()
          .traverseSkillDataobjectForXMLGeneration(entry.getKey(), document));
    }

    return element;
  }

  @Override
  public boolean equals(Object o) {

    if (o instanceof SkillDisembodiedPropertyList) {

      SkillDisembodiedPropertyList object = (SkillDisembodiedPropertyList) o;

      if (this.properties.size() != object.properties.size()) {
        return false;
      }

      for (String key : this.properties.keySet()) {

        if (!object.properties.containsKey(key)) {
          return false;
        }

        if (!this.properties.get(key).equals(object.properties.get(key))) {
          return false;
        }
      }

      return true;
    } else {
      return false;
    }
  }

  @Override
  public void clear() {
    this.properties.clear();
  }

  @Override
  public boolean containsKey(Object arg0) {
    return this.properties.containsKey(arg0);
  }

  @Override
  public boolean containsValue(Object arg0) {

    return this.properties.containsValue(arg0);
  }

  @Override
  public Set<Entry<String, SkillDataobject>> entrySet() {

    return this.properties.entrySet();
  }

  @Override
  public SkillDataobject get(Object arg0) {

    return this.properties.get(arg0);
  }

  @Override
  public boolean isEmpty() {

    return this.properties.isEmpty();
  }

  @Override
  public Set<String> keySet() {

    return this.properties.keySet();
  }

  @Override
  public SkillDataobject put(String arg0, SkillDataobject arg1) {

    return this.properties.put(arg0, arg1);
  }

  @Override
  public void putAll(Map<? extends String, ? extends SkillDataobject> arg0) {

    this.putAll(arg0);
  }

  @Override
  public SkillDataobject remove(Object arg0) {

    return this.properties.remove(arg0);
  }

  @Override
  public int size() {

    return this.properties.size();
  }

  @Override
  public Collection<SkillDataobject> values() {

    return this.properties.values();
  }

}