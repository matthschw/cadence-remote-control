package edlab.eda.cadence.rc;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import edlab.eda.cadence.rc.session.SkillSocketSession;
import edlab.eda.cadence.rc.session.UnableToStartSession;

class NonFreezeConnectedSocketTest {

  public static final File TST_DIR = new File("./");

  @Test
  void test() throws UnableToStartSession, IOException {

    Process process = null;

    try {
      process = Runtime.getRuntime()
          .exec("virtuoso -replay ./src/test/resources/replay.il");

      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
      }

      @SuppressWarnings("unused")
      SkillSocketSession session1 = SkillSocketSession.connect(TST_DIR);

      try {
        @SuppressWarnings("unused")
        SkillSocketSession session2 = SkillSocketSession.connect(TST_DIR);
      } catch (Exception e) {
      }

      process.destroy();

      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
      }
    } catch (Exception e) {
      
      process.destroyForcibly();
      
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e2) {
      }
      
      throw e;
    }
  }
}