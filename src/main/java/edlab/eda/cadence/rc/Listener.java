package edlab.eda.cadence.rc;

import java.util.Scanner;

public abstract class Listener extends Thread {

  protected volatile boolean running = true;
  protected Scanner scanner;
  protected SkillChildSession session;

  public Listener(Scanner scanner, SkillChildSession session) {
    this.scanner = scanner;
    this.session = session;
  }

  public void terminate() {
    this.running = false;
  }

  public abstract void run();

  protected void waitForTimeout() {

    synchronized (this) {
      try {
        this.wait(10);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
      }
    }
  }

}
