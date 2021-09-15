package edlab.eda.cadence.rc.data;

import java.util.ArrayList;

public class WaveFamiliy extends Wave {

  private ArrayList<Wave> waves;

  public WaveFamiliy(ArrayList<Wave> waves) {
    this.waves = waves;
  }

  public SkillDataobject toSkill() {

    SkillList retval = new SkillList();

    for (Wave wave : this.waves) {
      retval.addAtLast(wave.toSkill());
    }

    return retval;
  }

  @Override
  public String toString() {

    String retval = "[";

    System.out.println(waves.size());
    for (int i = 0; i < waves.size(); i++) {
      if (i > 0) {
        retval += " ";
      }
      retval += waves.get(i).toString();
    }

    retval += "]";

    return retval;
  }
}