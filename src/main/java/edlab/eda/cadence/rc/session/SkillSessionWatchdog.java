package edlab.eda.cadence.rc.session;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Watchdog which observes the {@link SkillInteractiveSession} and terminates if
 * the specified timeout is exceeded
 */
final class SkillSessionWatchdog extends Thread {

  private final SkillInteractiveSession session;

  private static final long WATCHDOG_INIT_WAIT_TIME_MS = 1000;
  private static final long WATCHDOG_CHECK_INTERVAL_MS = 500;

  private final long duration_ms;
  private boolean killed = false;
  private final Thread parent;
  private final Thread shutdownHook;

  /**
   * Create a Watchdog for a {@link SkillInteractiveSession}
   *
   * @param session  Session
   * @param duration Timeout duration
   * @param unit     Timeout unit
   */
  SkillSessionWatchdog(final SkillInteractiveSession session,
      final long duration, final TimeUnit unit, final Thread thread) {

    this.duration_ms = unit.toMillis(duration);
    this.parent = thread;

    this.session = session;

    this.shutdownHook = new Thread() {
      @Override
      public void run() {
        try {
          SkillSessionWatchdog.this.killed = true;
        } catch (final Exception e) {
        }
      }
    };

    Runtime.getRuntime().addShutdownHook(this.shutdownHook);

  }

  @Override
  public void run() {

    Date now, lastActivity;

    boolean contineWatching = true;

    try {
      Thread.sleep(SkillSessionWatchdog.WATCHDOG_INIT_WAIT_TIME_MS);
    } catch (final InterruptedException e) {
    }

    while (contineWatching) {

      try {
        Thread.sleep(SkillSessionWatchdog.WATCHDOG_CHECK_INTERVAL_MS);
      } catch (final InterruptedException e) {
      }

      if (this.killed) {
        contineWatching = false;
      } else {

        now = new Date();
        lastActivity = this.session.getLastActivity();

        if (((lastActivity == null)
            || ((now.getTime() - lastActivity.getTime()) > this.duration_ms))) {

          if (!this.killed) {
            this.session.stop();
            contineWatching = false;
          }
        }
      }

      if ((this.parent != null) && (!this.parent.isAlive() & !this.killed)) {
        this.session.stop();
        contineWatching = false;
      }
    }
  }

  /**
   * Kill the watchdog thread
   */
  public void kill() {
    Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
    this.killed = true;
  }
}