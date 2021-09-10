package edlab.eda.cadence.rc.data;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edlab.eda.cadence.rc.session.EvaluableToSkill;
import edlab.eda.cadence.rc.session.SkillInteractiveSession;
import edlab.eda.cadence.rc.session.SkillSession;

/**
 * Representation of a SKILL data-object
 *
 */
public abstract class SkillDataobject implements EvaluableToSkill {

  /**
   * Identifier of the type of the {@link SkillDataobject} in a XML
   */
  public static final String TYPE_ID = "type";

  /**
   * Returns when the {@link SkillDataobject} is logically true
   * 
   * @return <code>true</code> when the {@link SkillDataobject} is true wrt. to
   *         SKILL syntax, <code>false</code> otherwise
   */
  public abstract boolean isTrue();

  @Override
  public abstract boolean equals(Object o);

  @Override
  public String toSkill() {
    return this.toSkillHierarchical(0);
  }

  /**
   * Convert a {@link SkillDataobject} to a SKILL representation while taking
   * hierarchical syntax rules into account
   * 
   * @param depth Hierarchy in which the {@link SkillDataobject} is instantiated
   * @return SKILL representation of a {@link SkillDataobject}
   */
  abstract String toSkillHierarchical(int depth);

  /**
   * Create a XML element of a SKILL data-object
   * 
   * @param name     Name of the SKILl data-object
   * @param document D
   * @return XML Element
   */
  abstract Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document);

  /**
   * Create a {@link SkillDataobject} from a XML
   * 
   * @param session Corresponding {@link SkillInteractiveSession}
   * @param xml     XML as string to be parsed
   * @return SkillDataobject
   */
  public static SkillDataobject getSkillDataobjectFromXML(SkillSession session,
      String xml) {

    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder;

    try {
      dBuilder = dbFactory.newDocumentBuilder();

      // System.out.println(xml);

      Document doc = dBuilder.parse(new ByteArrayInputStream(xml.getBytes()));

      doc.getDocumentElement().normalize();

      Node node = doc.getDocumentElement();

      return traverseNode(session, node);

    } catch (Exception e) {
      return new SkillList();
    }

  }

  /**
   * Traverse a node from a session's return value for creating a
   * {@link SkillDataobject}
   * 
   * @param session Corresponding session
   * @param node    Node in the XML
   * @return SkillDataobject
   */
  private static SkillDataobject traverseNode(SkillSession session, Node node) {

    SkillDataobject skillDataobject;
    NamedNodeMap nodeMap = node.getAttributes();
    NodeList nodeList;

    switch (nodeMap.getNamedItem(TYPE_ID).getNodeValue()) {

    case SkillDisembodiedPropertyList.TYPE_ID:

      SkillDisembodiedPropertyList dpl = new SkillDisembodiedPropertyList();

      nodeList = node.getChildNodes();
      Node next;

      for (int i = 0; i < nodeList.getLength(); i++) {

        next = nodeList.item(i);
        if (isValidNode(nodeList.item(i))) {
          dpl.put(next.getNodeName(), traverseNode(session, next));
        }
      }

      skillDataobject = dpl;

      break;

    case SkillList.TYPE_ID:

      SkillList list = new SkillList();

      nodeList = node.getChildNodes();

      for (int i = 0; i < nodeList.getLength(); i++) {

        if (isValidNode(nodeList.item(i))) {
          list.addAtLast(traverseNode(session, nodeList.item(i)));
        }
      }

      skillDataobject = list;

      break;

    case SkillString.TYPE_ID:

      skillDataobject = new SkillString(node.getTextContent());

      break;

    case SkillFlonum.TYPE_ID:

      skillDataobject = new SkillFlonum(new BigDecimal(node.getTextContent()));

      break;

    case SkillFixnum.TYPE_ID:

      skillDataobject = new SkillFixnum(
          Integer.parseInt(node.getTextContent()));

      break;
    case SkillSymbol.TYPE_ID:

      skillDataobject = new SkillSymbol(node.getTextContent());

      break;
    case SkillComplexDataobject.TYPE_ID:

      skillDataobject = new SkillComplexDataobject(session,
          Integer.parseInt(node.getTextContent()));
      break;

    default:
      System.out.println("Unkown Datatype-will be ignored");
      skillDataobject = null;
      break;
    }

    return skillDataobject;
  }

  /**
   * Check whether a {@link Node} is valid with the here utilized protocol
   * 
   * @param node to be checked
   * @return <code>true</true> when the node is valid, <code>false</true>
   *         otherwise
   */
  private static boolean isValidNode(Node node) {

    if (node.hasAttributes()) {
      NamedNodeMap nodeMap = node.getAttributes();

      if (nodeMap.getNamedItem(TYPE_ID) != null) {
        return true;
      } else {
        return false;
      }
    } else
      return false;
  }

  public SkillDataobject getFalse() {
    return new SkillList();
  }

  public SkillDataobject getTrue() {
    return new SkillSymbol("t");
  }

}