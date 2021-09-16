package edlab.eda.cadence.rc.data;

import java.math.BigDecimal;

public class SingleWave extends Wave {

  private BigDecimal[] wave;

  public SingleWave(BigDecimal[] wave) {
    this.wave = wave;
  }

  public BigDecimal[] getWave() {
    return this.wave;
  }

  public int getPoints() {
    return this.wave.length;
  }

  @Override
  public SkillDataobject toSkill() {

    SkillList retval = new SkillList();

    for (BigDecimal value : this.wave) {
      retval.append(new SkillFlonum(value));
    }

    return retval;
  }

  @Override
  public String toString() {
    String retval = "[";

    for (int i = 0; i < wave.length; i++) {
      if (i > 0) {
        retval += " ";
      }
      retval += wave[i].toEngineeringString();
    }

    retval += "]";

    return retval;
  }
}