package edlab.eda.cadence.rc;

/**
 * Interface that represents object that can be evaluated in a
 * {@link SkillInteractiveSession}
 *
 */
public interface EvaluableToSkill {

  /**
   * Evaluate to a SKILL representation
   * 
   * @return SKILL Code
   */
  public String toSkill();

  /**
   * Checks whether a SKILL-construct can be used in a given
   * {@link SkillSession}
   * 
   * @param session SkillSession
   * @return 
   */
  public boolean canBeUsedInSession(SkillSession session);
}