package edlab.eda.cadence.rc;

import java.util.Scanner;

import edlab.eda.cadence.rc.data.SkillDataobject;

public class StringListener extends Listener {

  public StringListener(Scanner scanner, SkillChildSession session) {
    super(scanner, session);
    this.scanner.useDelimiter("& ");
  }

  @Override
  public void run() {

    String xml;

    while (this.scanner.hasNext()) {

      xml = scanner.next();

      SkillDataobject obj = SkillDataobject.getSkillDataobjectFromXML(null,
          xml);

     // session.react(obj);

      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
      }
    }

    if (!this.running) {
      return;
    }

    try {
      Thread.sleep(1);
    } catch (InterruptedException e) {
    }
  }

}
