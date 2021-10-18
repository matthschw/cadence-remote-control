package edlab.eda.cadence.rc.session;

/**
 * Interface that represents object that can be evaluated in a
 * {@link SkillInteractiveSession}
 */
public interface EvaluableToSkill {

  /**
   * Evaluate to a SKILL representation
   * 
   * @return Skill Code
   */
  public String toSkill();

  /**
   * Checks whether a SKILL-construct can be used in a given
   * {@link SkillSession}
   * 
   * @param session SkillSession
   * @return <code>true</code> when a Skill expression can be used in a session,
   *         <code>false</code> otherwise
   */
  public boolean canBeUsedInSession(SkillSession session);
}