package edlab.eda.cadence.rc;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

import edlab.eda.cadence.rc.session.SkillSession;

class SkillcdsRootTest {

  @Test
  void test() {

    File path = new File(SkillSession.cdsRoot("skill"));

    if (path instanceof File && path.exists()) {

    } else {
      fail("Cannot find skill cds_root");
    }
  }
}