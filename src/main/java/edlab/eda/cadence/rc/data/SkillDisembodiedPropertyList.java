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
public final class SkillDisembodiedPropertyList extends SkillBoolean
    implements Map<String, SkillDataobject> {

  /**
   * Type-Identifier in XML
   */
  public static final String TYPE_ID = "dpl";

  private final Map<String, SkillDataobject> properties;

  /**
   * Create an empty disembodied property list
   */
  public SkillDisembodiedPropertyList() {
    super(true);
    this.properties = new HashMap<>();
  }

  /**
   * Create an empty disembodied property list from a map
   *
   * @param properties Map of key-value pairs
   */
  public SkillDisembodiedPropertyList(final Map<String, SkillDataobject> properties) {
    super(true);
    this.properties = properties;
  }

  @Override
  protected String toSkillHierarchical(int depth) {

    final StringBuilder builder = new StringBuilder();

    if (depth == 0) {
      builder.append("'");
    }

    builder.append("(nil");

    for (final Map.Entry<String, SkillDataobject> entry : this.properties
        .entrySet()) {
      builder.append(" ").append(entry.getKey()).append(" ")
          .append(entry.getValue().toSkillHierarchical(++depth));
    }

    builder.append(")");

    return builder.toString();
  }

  @Override
  protected Element traverseSkillDataobjectForXMLGeneration(final String name,
      final Document document) {

    final Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);

    for (final Map.Entry<String, SkillDataobject> entry : this.properties
        .entrySet()) {
      element.appendChild(entry.getValue()
          .traverseSkillDataobjectForXMLGeneration(entry.getKey(), document));
    }

    return element;
  }

  @Override
  public boolean equals(final Object o) {

    if (o instanceof SkillDisembodiedPropertyList) {

      final SkillDisembodiedPropertyList object = (SkillDisembodiedPropertyList) o;

      if (this.properties.size() != object.properties.size()) {
        return false;
      }

      for (final String key : this.properties.keySet()) {

        if (!object.properties.containsKey(key)
            || !this.properties.get(key).equals(object.properties.get(key))) {
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
  public boolean containsKey(final Object arg0) {
    return this.properties.containsKey(arg0);
  }

  @Override
  public boolean containsValue(final Object arg0) {
    return this.properties.containsValue(arg0);
  }

  @Override
  public Set<Entry<String, SkillDataobject>> entrySet() {
    return this.properties.entrySet();
  }

  @Override
  public SkillDataobject get(final Object arg0) {
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

  /**
   * Get the keys in the dpl as array
   * 
   * @return keys as array
   */
  public String[] getKeysAsArray() {

    final String[] retval = new String[this.properties.size()];
    int i = 0;

    for (final String key : this.properties.keySet()) {
      retval[i++] = key;
    }

    return retval;
  }

  @Override
  public SkillDataobject put(final String arg0, final SkillDataobject arg1) {
    return this.properties.put(arg0, arg1);
  }

  @Override
  public void putAll(final Map<? extends String, ? extends SkillDataobject> arg0) {
    this.putAll(arg0);
  }

  @Override
  public SkillDataobject remove(final Object arg0) {
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
  
  /**
   * Identify whether an object is an instance of this class
   *
   * @param o Object to be checked
   * @return <code>true</code> when the object is an instance of this class,
   *         <code>false</code> otherwise
   */
  public static boolean isInstanceOf(final Object o) {
    return o instanceof SkillDisembodiedPropertyList;
  }
}