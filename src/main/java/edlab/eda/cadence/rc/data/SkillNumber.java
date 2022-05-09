package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;

/**
 * Representation of a native Skill number
 */
public abstract class SkillNumber extends SkillBoolean {

  public SkillNumber() {
    super(true);
  }

  /**
   * Get the number
   * 
   * @return number
   */
  public abstract BigDecimal getNumber();

  /**
   * Identify whether an object is an instance of this class
   *
   * @param o Object to be checked
   * @return <code>true</code> when the object is an instance of this class,
   *         <code>false</code> otherwise
   */
  public static boolean isInstanceOf(final Object o) {
    return o instanceof SkillNumber;
  }
}