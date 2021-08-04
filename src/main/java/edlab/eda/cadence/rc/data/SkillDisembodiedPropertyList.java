package edlab.eda.cadence.rc.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Representation of a native SKILL Disembodied Property List
 *
 */
public class SkillDisembodiedPropertyList extends SkillBoolean {

  /**
   * Type-Identifier in XML
   */
  public static final String TYPE_ID = "dpl";

  private Map<String, SkillDataobject> properties;

  public SkillDisembodiedPropertyList() {
    super(true);
    this.properties = new HashMap<String, SkillDataobject>();
  }

  public SkillDisembodiedPropertyList(Map<String, SkillDataobject> properties) {
    super(true);
    this.properties = properties;
  }

  public SkillDataobject getProperty(String key) {
    return this.properties.get(key);
  }

  public void addProperty(String key, SkillDataobject skillDataobject) {

    this.properties.put(key, skillDataobject);
  }

  public SkillDataobject removeKey(String key) {

    return this.properties.remove(key);
  }

  public Set<String> getKeys() {
    return this.properties.keySet();
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

  public Set<Entry<String, SkillDataobject>> entrySet() {
    return this.properties.entrySet();
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

}
