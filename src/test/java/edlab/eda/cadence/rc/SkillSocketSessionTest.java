package edlab.eda.cadence.rc;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import edlab.eda.cadence.rc.api.IncorrectSyntaxException;

public class SkillSocketSessionTest {

  @Test
  void test() throws EvaluationFailedException, UnableToStartSkillSession,
      IncorrectSyntaxException, IOException,
      InvalidDataobjectReferenceExecption {

    Process process = Runtime.getRuntime()
        .exec("virtuoso -replay ./src/test/resources/replay.il");

    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
    }

    Scanner scanner = new Scanner(
        new File(CadenceSocket.SOCKET_PORT_FILE_NAME));
    int port = Integer.parseInt(scanner.nextLine());
    scanner.close();

    System.err.println(port);
    
    SkillSocketSession session = new SkillSocketSession(port);
    
    session.start();

    /*
     * SkillInteractiveSession session = new SkillInteractiveSession();
     * 
     * try { session.start(); } catch (UnableToStartSkillSession e) {
     * session.stop(); fail("Unable to start session"); }
     * 
     * SkillSessionMethods.writeFile(session);
     * 
     * for (int i = 0; i < 1000; i++) {
     * SkillSessionMethods.addUpValuesInList(session); }
     * 
     * SkillSessionMethods.strcat(session);
     * 
     * session.stop();
     */
    
    session.stop();

    process.destroy();
   // process.destroyForcibly();
  }
}
