package edlab.eda.cadence.rc.data;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import edlab.eda.cadence.rc.session.SkillSession;

/**
 * Representation of a native SKILL-Object
 *
 */
public abstract class SkillNativeDataobject extends SkillDataobject {

  /**
   * Create a XML of a {@link SkillNativeDataobject}
   *
   * @param path Path where the XML is created
   * @return <code>true</code> when the XML was created successfully,
   *         <code>false</code> otherwise
   */
  public boolean writeSkillDataobjectToXML(String path) {

    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder;
    try {
      dBuilder = dbFactory.newDocumentBuilder();
      Document document = dBuilder.newDocument();

      document.appendChild(
          this.traverseSkillDataobjectForXMLGeneration("root", document));

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      DOMSource domSource = new DOMSource(document);
      StreamResult streamResult = new StreamResult(new File(path));

      transformer.transform(domSource, streamResult);

      return true;

    } catch (ParserConfigurationException e) {
      return false;
    } catch (TransformerConfigurationException e) {
      return false;
    } catch (TransformerException e) {
      return false;
    }
  }

  @Override
  public boolean canBeUsedInSession(SkillSession session) {
    return true;
  }

  /**
   * Identify whether an object is an instance of this class
   *
   * @param o Object to be checked
   * @return <code>true</code> when the object is an instance of this class,
   *         <code>false</code> otherwise
   */
  public static boolean isInstanceOf(final Object o) {
    return o instanceof SkillNativeDataobject;
  }
}