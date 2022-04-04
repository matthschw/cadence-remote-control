package edlab.eda.cadence.rc.session;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Watchdog which observes the {@link SkillInteractiveSession} and terminates if
 * the specified timeout is exceeded
 *
 */
class SkillSessionWatchdog extends Thread {

  private SkillInteractiveSession session;

  private static final long WATCHDOG_INIT_WAIT_TIME_MS = 1000;
  private static final long WATCHDOG_CHECK_INTERVAL_MS = 500;

  private long duration_ms;
  private boolean killed = false;
  private Thread parent;

  /**
   * Create a Watchdog for a {@link SkillInteractiveSession}
   *
   * @param session  Session
   * @param duration Timeout duration
   * @param unit     Timeout unit
   */
  SkillSessionWatchdog(SkillInteractiveSession session, long duration,
      TimeUnit unit, Thread thread) {

    this.duration_ms = unit.toMillis(duration);
    this.parent = thread;

    this.session = session;

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        try {
          killed = true;
        } catch (Exception e) {
        }
      }
    });
  }

  @Override
  public void run() {

    Date now, lastActivity;

    boolean contineWatching = true;

    try {
      Thread.sleep(SkillSessionWatchdog.WATCHDOG_INIT_WAIT_TIME_MS);
    } catch (InterruptedException e) {
    }

    while (contineWatching) {

      try {
        Thread.sleep(SkillSessionWatchdog.WATCHDOG_CHECK_INTERVAL_MS);
      } catch (InterruptedException e) {
      }

      if (this.killed) {
        contineWatching = false;
      } else {

        now = new Date();
        lastActivity = this.session.getLastActivity();

        if ((lastActivity == null
            || now.getTime() - lastActivity.getTime() > this.duration_ms)) {

          if (!this.killed) {
            this.session.stop();
            contineWatching = false;
          }
        }
      }

      if (this.parent != null && !this.parent.isAlive() & !this.killed) {
        this.session.stop();
        contineWatching = false;
      }
    }
  }

  /**
   * Kill the watchdog thread
   */
  public void kill() {
    this.killed = true;
  }
}
