package edlab.eda.cadence.rc;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import edlab.eda.cadence.rc.api.IncorrectSyntaxException;
import edlab.eda.cadence.rc.session.CadenceSocket;
import edlab.eda.cadence.rc.session.EvaluationFailedException;
import edlab.eda.cadence.rc.session.InvalidDataobjectReferenceExecption;
import edlab.eda.cadence.rc.session.SkillSocketSession;
import edlab.eda.cadence.rc.session.UnableToStartSession;

public class SkillSocketSessionTest {

  @Test
  void test() throws EvaluationFailedException, UnableToStartSession,
      IncorrectSyntaxException, IOException,
      InvalidDataobjectReferenceExecption {

    Process process = Runtime.getRuntime()
        .exec("virtuoso -replay ./src/test/resources/replay.il");

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
    }
    
    
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        process.destroy();
      }
  });
    Scanner scanner = new Scanner(
        new File(CadenceSocket.SOCKET_PORT_FILE_NAME));

    int port = Integer.parseInt(scanner.nextLine());
    scanner.close();

    SkillSocketSession session = new SkillSocketSession(port);
    

    session.start();

    SkillSessionMethods.writeFile(session);

    session.stop();
    session = new SkillSocketSession(port);
    session.start();

    for (int i = 0; i < 100; i++) {
      SkillSessionMethods.addUpValuesInList(session);
    }
    
    session.stop();
    session = new SkillSocketSession(port);
    session.start();

    SkillSessionMethods.strcat(session);
    
    session.stop();
  }
}
