package edlab.eda.cadence.rc;

/**
 * Interface that identifies whether an object is evaluable to SKILL
 * representation
 *
 */
public interface EvaluateableToSkill {

  /**
   * Evaluate to a SKILL representation
   * 
   * @return SKILL Code
   */
  public String toSkill();
}