package edlab.eda.cadence.rc;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Watchdog which observes the {@link SkillSession} and terminates if the
 * specified timeout is exceeded
 *
 */
class SkillSessionWatchdog extends Thread {

  private SkillSession session;

  private static final long WATCHDOG_INIT_WAIT_TIME_MS = 1000;
  private static final long WATCHDOG_CHECK_INTERVAL_MS = 500;

  private long duration_ms;
  private boolean killed = false;

  /**
   * Create a Watchdog for {@link SkillSession} 
   * @param session
   * @param duration
   * @param unit
   */
  SkillSessionWatchdog(SkillSession session, long duration, TimeUnit unit) {
    this.duration_ms = unit.toMillis(duration);

    this.session = session;
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

      if (killed) {
        contineWatching = false;
      } else {

        now = new Date();
        lastActivity = this.session.getLastActivity();

        if ((lastActivity == null
            || now.getTime() - lastActivity.getTime() > this.duration_ms)) {

          if (!killed) {
            this.session.stop();
          }
        }
      }
    }
  }

  /**
   * Kill the Watchdog-Thread
   */
  public void kill() {
    this.killed = true;
  }
}
