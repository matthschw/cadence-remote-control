package edlab.eda.cadence.rc;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.junit.jupiter.api.Test;

import edlab.eda.cadence.rc.data.SkillDisembodiedPropertyList;
import edlab.eda.cadence.rc.data.SkillFixnum;
import edlab.eda.cadence.rc.data.SkillList;
import edlab.eda.cadence.rc.data.SkillString;
import edlab.eda.cadence.rc.session.FileSkillSession;

class FileSkillSessionTest {

  @Test
  void test() {

    final SkillDisembodiedPropertyList dpl = new SkillDisembodiedPropertyList();

    dpl.put("string1", new SkillString(SkillSessionMethods.STR1));
    dpl.put("string2", new SkillString(SkillSessionMethods.STR2));
    dpl.put("string3", new SkillString(SkillSessionMethods.STR3));

    final SkillList list = new SkillList();

    for (int i = 0; i < 101; i++) {
      list.append(new SkillFixnum(i));
    }

    dpl.put("list", list);

    final FileSkillSession session = FileSkillSession
        .init(new File("./src/test/resources/test.xml"));

    if (!dpl.equals(session.getSkillData())) {
      fail("Data does not match");
    }
  }
}
