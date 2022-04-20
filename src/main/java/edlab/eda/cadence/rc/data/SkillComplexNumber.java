package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;

import org.apache.commons.math3.complex.Complex;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Complex number in Skill
 */
public final class SkillComplexNumber extends SkillNumber {

  private final Complex value;

  /**
   * Type-Identifier in XML
   */
  public static final String TYPE_ID = "doublecomplex";

  public static final String REAL_ID = "real";
  public static final String IMAG_ID = "imag";

  /**
   * Create a {@link SkillComplexNumber}
   * 
   * @param real Real value
   */
  public SkillComplexNumber(final BigDecimal real) {
    super();
    this.value = new Complex(real.doubleValue());
  }

  /**
   * Create a {@link SkillComplexNumber}
   * 
   * @param real Real value
   * @param imag Imaginary value
   */
  public SkillComplexNumber(final BigDecimal real, final BigDecimal imag) {
    super();
    this.value = new Complex(real.doubleValue(), imag.doubleValue());
  }

  /**
   * Create a {@link SkillComplexNumber}
   * 
   * @param real Real value
   */
  public SkillComplexNumber(final double real) {
    super();
    this.value = new Complex(real);
  }

  /**
   * Create a {@link SkillComplexNumber}
   * 
   * @param real Real value
   * @param imag Imaginary value
   */
  public SkillComplexNumber(final double real, final double imag) {
    super();
    this.value = new Complex(real, imag);
  }

  /**
   * Create a {@link SkillComplexNumber}
   * 
   * @param complex Complex value
   */
  public SkillComplexNumber(final Complex complex) {
    super();
    this.value = complex;
  }

  @Override
  protected String toSkillHierarchical(final int depth) {
    return "(complex " + this.value.getReal() + " " + this.value.getImaginary()
        + " )";
  }

  @Override
  public boolean equals(final Object o) {
    if (o instanceof SkillComplexNumber) {
      return ((SkillComplexNumber) o).value.equals(this.value);
    } else if (o instanceof Complex) {
      return ((Complex) o).equals(this.value);
    }
    return false;
  }

  /**
   * Get the complex value
   * 
   * @return complex value
   */
  public Complex getComplex() {
    return this.value;
  }

  @Override
  Element traverseSkillDataobjectForXMLGeneration(final String name,
      final Document document) {

    final Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);

    final Element real = document.createElement(REAL_ID);
    real.setTextContent(String.valueOf(this.value.getReal()));

    final Element imag = document.createElement(IMAG_ID);
    imag.setTextContent(String.valueOf(this.value.getImaginary()));

    element.appendChild(real);
    element.appendChild(imag);

    return element;
  }

  /**
   * Create a {@link SkillComplexNumber} from an XML element
   * 
   * @param element XML element
   * @return complex number
   */
  static SkillComplexNumber build(final Element element) {

    double x, y;
    NodeList nodeList;
    Node node;

    nodeList = element.getElementsByTagName(REAL_ID);

    if (nodeList.getLength() > 0) {
      node = nodeList.item(0);
      x = Double.parseDouble(node.getTextContent());
    } else {
      return null;
    }

    nodeList = element.getElementsByTagName(IMAG_ID);

    if (nodeList.getLength() > 0) {
      node = nodeList.item(0);
      y = Double.parseDouble(node.getTextContent());
    } else {
      return null;
    }

    return new SkillComplexNumber(x, y);
  }

  /**
   * Identify whether an object is an instance of this class
   *
   * @param o Object to be checked
   * @return <code>true</code> when the object is an instance of this class,
   *         <code>false</code> otherwise
   */
  public static boolean isInstanceOf(final Object o) {
    return o instanceof SkillComplexNumber;
  }
}