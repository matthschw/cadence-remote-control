package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;

import org.apache.commons.math3.complex.Complex;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SkillComplexNumber extends SkillNumber {

  private Complex value;

  /**
   * Type-Identifier in XML
   */
  public static final String TYPE_ID = "doublecomplex";

  public static final String REAL_ID = "real";
  public static final String IMAG_ID = "imag";

  public SkillComplexNumber(BigDecimal real) {
    super();
    this.value = new Complex(real.doubleValue());
  }

  public SkillComplexNumber(BigDecimal real, BigDecimal imag) {
    super();
    this.value = new Complex(real.doubleValue(), imag.doubleValue());
  }

  public SkillComplexNumber(double real) {
    super();
    this.value = new Complex(real);
  }

  public SkillComplexNumber(double real, double imag) {
    super();
    this.value = new Complex(real, imag);
  }

  public SkillComplexNumber(Complex complex) {
    super();
    this.value = complex;
  }

  @Override
  protected String toSkillHierarchical(int depth) {
    return "(complex " + this.value.getReal() + " " + this.value.getImaginary()
        + " )";
  }

  @Override
  public boolean equals(Object o) {
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
  Element traverseSkillDataobjectForXMLGeneration(String name,
      Document document) {

    Element element = document.createElement(name);
    element.setAttribute(SkillDataobject.TYPE_ID, TYPE_ID);

    Element real = document.createElement(REAL_ID);
    real.setTextContent(String.valueOf(this.value.getReal()));

    Element imag = document.createElement(IMAG_ID);
    imag.setTextContent(String.valueOf(this.value.getImaginary()));

    element.appendChild(real);
    element.appendChild(imag);

    return element;
  }

  /**
   * @param element
   * @return
   */
  static SkillComplexNumber build(Element element) {

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