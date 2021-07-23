package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edlab.eda.cadence.rc.Session;

public abstract class SkillDataobject {

  public static final String TYPE_ID = "type";
  public static final String IS_NATIVE_ID = "isNative";
  public static final String BOOL_TRUE = "yes";

  public String toSkill() {

    return "(let () " + this.toSkillHierarchical(0) + ")";
  }

  public abstract boolean isTrue();

  protected abstract String toSkillHierarchical(int depth);

  protected abstract Element traverseSkillDataobjectForXMLGeneration(
      String name, Document document);

  public static SkillDataobject getSkillDataobjectFromXML(Session session,
      String xml) {

    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder;

    try {
      dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(xml);

      doc.getDocumentElement().normalize();

      Node node = doc.getDocumentElement();

      return traverseNode(session, node);

    } catch (Exception e) {

      return new SkillList();
    }

  }

  private static SkillDataobject traverseNode(Session session, Node node) {

    SkillDataobject skillDataobject;
    NamedNodeMap nodeMap = node.getAttributes();
    NodeList nodeList;

    if (nodeMap.getNamedItem(IS_NATIVE_ID) != null && nodeMap
        .getNamedItem(IS_NATIVE_ID).getNodeValue().equals(BOOL_TRUE)) {

      switch (nodeMap.getNamedItem(TYPE_ID).getNodeValue()) {

      case SkillDisembodiedPropertyList.TYPE_ID:

        SkillDisembodiedPropertyList dpl = new SkillDisembodiedPropertyList();

        nodeList = node.getChildNodes();
        Node next;

        for (int i = 0; i < nodeList.getLength(); i++) {

          next = nodeList.item(i);
          if (isValidNode(nodeList.item(i))) {
            dpl.addProperty(next.getNodeName(), traverseNode(session, next));
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

        skillDataobject = (SkillDataobject) list;

        break;

      case SkillString.TYPE_ID:

        skillDataobject = new SkillString(node.getTextContent());

        break;

      case SkillFlonum.TYPE_ID:

        skillDataobject = new SkillFlonum(
            new BigDecimal(node.getTextContent()));

        break;

      case SkillFixnum.TYPE_ID:

        skillDataobject = new SkillFixnum(
            Integer.parseInt(node.getTextContent()));

        break;

      default:
        System.out.println("Unkown Datatype-will be ignored");
        skillDataobject = null;
        break;
      }

    } else {
      skillDataobject = new SkillComplexDataobject(session,
          Integer.parseInt(node.getTextContent()));
    }

    return skillDataobject;
  }

  private static boolean isValidNode(Node node) {

    if (node.hasAttributes()) {
      NamedNodeMap nodeMap = node.getAttributes();

      if (nodeMap.getNamedItem("type") != null) {
        return true;
      } else {
        return false;
      }

    } else
      return false;
  }
}