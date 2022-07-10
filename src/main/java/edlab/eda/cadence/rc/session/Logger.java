package edlab.eda.cadence.rc.session;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger for sessions
 */
final class Logger {

  static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
      "yyyy-MM-dd HH:mm:ss");

  static final String START_STEP = "START";
  static final String STOP_STEP = "STOP";
  static final String CONFIG_STEP = "CONFIG";
  static final String INFO_STEP = "INFO";

  private final File logfile;
  private int iter = 0;

  /**
   * Create a new {@link Logger}
   * 
   * @return logger
   */
  static Logger create() {

    File logfile = null;

    try {
      logfile = File.createTempFile("cdsrc", ".log");
    } catch (final IOException e) {
    }

    if (logfile instanceof File) {
      return new Logger(logfile);
    } else {
      return null;
    }
  }

  private Logger(final File logfile) {
    this.logfile = logfile;
  }

  /**
   * Add a message
   * 
   * @param id      Identifier
   * @param message Message
   * @return this
   */
  public Logger add(final String id, final String message[]) {

    final StringBuilder retval = new StringBuilder().append("[").append(id)
        .append("] @ ").append(DATE_FORMAT.format(new Date()));

    for (final String element : message) {
      retval.append("\n  ").append(element);
    }

    if (!this.logfile.exists()) {

      try {
        this.logfile.createNewFile();
      } catch (final IOException e) {
      }
    }

    try {
      Files.write(Paths.get(this.logfile.getAbsolutePath()),
          retval.toString().getBytes(), StandardOpenOption.APPEND);
    } catch (final IOException e) {
    }

    return this;
  }

  /**
   * Add a message
   * 
   * @param id      Identifier
   * @param message Message
   * @return this
   */
  public Logger add(final String id, final String message) {
    return this.add(id, new String[] { message });
  }

  /**
   * Add a message
   * 
   * @param message Message
   * @return this
   */
  public Logger add(final String message[]) {
    return this.add("" + this.iter, message);
  }

  /**
   * Add a message
   * 
   * @param message Message
   * @return this
   */
  public Logger add(final String message) {
    return this.add("" + this.iter, new String[] { message });
  }

  /**
   * Go to next communication step
   * 
   * @return
   */
  public Logger next() {
    this.iter++;
    return this;
  }
}