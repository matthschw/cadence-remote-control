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
   * 
   * @see SkillSession#setPrompt(String)
   */
  public static final int MSG_CODE_1 = 1;

  /**
   * 
   * @see SkillSession#setTimeout(long, java.util.concurrent.TimeUnit)
   */
  public static final int MSG_CODE_2 = 2;

  /**
   * 
   * @see SkillSession#isSkillFunctionCallable(String)
   */
  public static final int MSG_CODE_3 = 3;

  /**
   * 
   * @see SkillSession#loadSkillCode(File)
   */
  public static final int MSG_CODE_4 = 4;

  /**
   * 
   * @see SkillSession#loadCodeFromJarWithReferenceFun(String, String)
   */
  public static final int MSG_CODE_5 = 5;

  /**
   * 
   * @see SkillSession#loadCodeFromJarWithReferenceFun(String, String)
   */
  public static final int MSG_CODE_6 = 6;

  /**
   * 
   * @see SkillSession#getResourcePathFromBinary(String, String)
   */
  public static final int MSG_CODE_7 = 7;

  /**
   * 
   * @see SkillSession#getResourcePathFromAscii(String, String)
   */
  public static final int MSG_CODE_8 = 8;

  /**
   * 
   * @see SkillSession#getResourcePathFromAscii(String, String)
   */
  public static final int MSG_CODE_9 = 9;

  /**
   * 
   * @see SkillSession#getResourcePathFromAscii(String, String)
   */
  public static final int MSG_CODE_10 = 10;

  /**
   * 
   * @see SkillSession#getResourceFromAscii(String)
   */
  public static final int MSG_CODE_11 = 11;

  /**
   * 
   * @see SkillInteractiveSession
   */
  public static final int MSG_CODE_12 = 12;

  /**
   * 
   * @see SkillInteractiveSession#setCommand(String)
   */
  public static final int MSG_CODE_13 = 13;

  /**
   * 
   * @see SkillInteractiveSession#setWatchdogTimeout(long,
   *      java.util.concurrent.TimeUnit)
   */
  public static final int MSG_CODE_14 = 14;

  /**
   * 
   * @see SkillInteractiveSession#start(Thread)
   */
  public static final int MSG_CODE_15 = 15;

  /**
   * 
   * @see SkillInteractiveSession#start(Thread)
   */
  public static final int MSG_CODE_16 = 16;

  /**
   * 
   * @see SkillInteractiveSession#start(Thread)
   */
  public static final int MSG_CODE_17 = 17;

  /**
   * 
   * @see SkillInteractiveSession#start(Thread)
   */
  public static final int MSG_CODE_18 = 18;

  /**
   * 
   * @see SkillInteractiveSession#start(Thread)
   */
  public static final int MSG_CODE_19 = 19;

  /**
   * 
   * @see SkillInteractiveSession#start(Thread)
   */
  public static final int MSG_CODE_20 = 20;

  /**
   * 
   * @see SkillInteractiveSession#start(Thread)
   */
  public static final int MSG_CODE_21 = 21;

  /**
   * 
   * @see SkillInteractiveSession#start(Thread)
   */
  public static final int MSG_CODE_22 = 22;

  /**
   * 
   * @see SkillInteractiveSession#start(Thread)
   */
  public static final int MSG_CODE_23 = 23;

  /**
   * 
   * @see SkillInteractiveSession#start(Thread)
   */
  public static final int MSG_CODE_24 = 24;

  /**
   * 
   * @see SkillInteractiveSession#start(Thread)
   */
  public static final int MSG_CODE_25 = 25;

  /**
   * 
   * @see SkillInteractiveSession#start(Thread)
   */
  public static final int MSG_CODE_26 = 26;

  /**
   * 
   * @see SkillInteractiveSession#start(Thread)
   */
  public static final int MSG_CODE_27 = 27;

  /**
   * 
   * @see SkillInteractiveSession#start(Thread)
   */
  public static final int MSG_CODE_28 = 28;

  /**
   * 
   * @see SkillInteractiveSession#start(Thread)
   */
  public static final int MSG_CODE_29 = 29;

  /**
   * 
   * @see SkillInteractiveSession#start(Thread)
   */
  public static final int MSG_CODE_30 = 30;

  /**
   * 
   * @see SkillInteractiveSession#evaluate(edlab.eda.cadence.rc.api.SkillCommand,
   *      Thread)
   */
  public static final int MSG_CODE_31 = 31;

  /**
   * 
   * @see SkillInteractiveSession#evaluate(edlab.eda.cadence.rc.api.SkillCommand,
   *      Thread)
   */
  public static final int MSG_CODE_32 = 32;

  /**
   * 
   * @see SkillInteractiveSession#evaluate(edlab.eda.cadence.rc.api.SkillCommand,
   *      Thread)
   */
  public static final int MSG_CODE_33 = 33;

  /**
   * 
   * @see SkillInteractiveSession#evaluate(edlab.eda.cadence.rc.api.SkillCommand,
   *      Thread)
   */
  public static final int MSG_CODE_34 = 34;

  /**
   * 
   * @see SkillInteractiveSession#evaluate(edlab.eda.cadence.rc.api.SkillCommand,
   *      Thread)
   */
  public static final int MSG_CODE_35 = 35;

  /**
   * 
   * @see SkillInteractiveSession#evaluate(edlab.eda.cadence.rc.api.SkillCommand,
   *      Thread)
   */
  public static final int MSG_CODE_36 = 36;

  /**
   * 
   * @see SkillInteractiveSession#evaluate(edlab.eda.cadence.rc.api.SkillCommand,
   *      Thread)
   */
  public static final int MSG_CODE_37 = 37;

  /**
   * 
   * @see SkillInteractiveSession#evaluate(edlab.eda.cadence.rc.api.SkillCommand,
   *      Thread)
   */
  public static final int MSG_CODE_38 = 38;

  /**
   * 
   * @see SkillInteractiveSession#evaluate(edlab.eda.cadence.rc.api.SkillCommand,
   *      Thread)
   */
  public static final int MSG_CODE_39 = 39;

  /**
   * 
   * @see SkillInteractiveSession#evaluate(edlab.eda.cadence.rc.api.SkillCommand,
   *      Thread)
   */
  public static final int MSG_CODE_40 = 40;

  /**
   * 
   * @see SkillInteractiveSession#evaluate(edlab.eda.cadence.rc.api.SkillCommand,
   *      Thread)
   */
  public static final int MSG_CODE_41 = 41;

  /**
   * 
   * @see SkillInteractiveSession#evaluate(edlab.eda.cadence.rc.api.SkillCommand,
   *      Thread)
   */
  public static final int MSG_CODE_42 = 42;

  /**
   * 
   * @see SkillInteractiveSession#evaluate(edlab.eda.cadence.rc.api.SkillCommand,
   *      Thread)
   */
  public static final int MSG_CODE_43 = 43;

  /**
   * 
   * @see SkillInteractiveSession#evaluate(edlab.eda.cadence.rc.api.SkillCommand,
   *      Thread)
   */
  public static final int MSG_CODE_44 = 44;

  /**
   * 
   * @see SkillInteractiveSession#communicate(String)
   */
  public static final int MSG_CODE_45 = 45;

  /**
   * 
   * @see SkillInteractiveSession#communicate(String)
   */
  public static final int MSG_CODE_46 = 46;

  /**
   * 
   * @see SkillInteractiveSession#communicate(String)
   */
  public static final int MSG_CODE_47 = 47;

  /**
   * 
   * @see SkillInteractiveSession#stop()
   */
  public static final int MSG_CODE_48 = 48;

  /**
   * 
   * @see SkillInteractiveSession#stop()
   */
  public static final int MSG_CODE_49 = 49;

  /**
   * 
   * @see SkillInteractiveSession#stop()
   */
  public static final int MSG_CODE_50 = 50;

  /**
   * 
   * @see SkillSocketSession
   */
  public static final int MSG_CODE_51 = 51;

  /**
   * 
   * @see SkillSocketSession#start()
   */
  public static final int MSG_CODE_52 = 52;

  /**
   * 
   * @see SkillSocketSession#start()
   */
  public static final int MSG_CODE_53 = 53;

  /**
   * 
   * @see SkillSocketSession#start()
   */
  public static final int MSG_CODE_54 = 54;

  /**
   * 
   * @see SkillSocketSession#start()
   */
  public static final int MSG_CODE_55 = 55;

  /**
   * 
   * @see SkillSocketSession#start()
   */
  public static final int MSG_CODE_56 = 56;

  /**
   * 
   * @see SkillSocketSession#start()
   */
  public static final int MSG_CODE_57 = 57;

  /**
   * 
   * @see SkillSocketSession#start()
   */
  public static final int MSG_CODE_58 = 58;

  /**
   * 
   * @see SkillSocketSession#start()
   */
  public static final int MSG_CODE_59 = 59;

  /**
   * 
   * @see SkillSocketSession#start()
   */
  public static final int MSG_CODE_60 = 60;

  /**
   * 
   * @see SkillSocketSession#start()
   */
  public static final int MSG_CODE_61 = 61;

  /**
   * 
   * @see SkillSession#isClassCallable(String)
   */
  public static final int MSG_CODE_62 = 62;

  /**
   * 
   * @see SkillSession#loadCodeFromJarWithReferenceClass(String, String)
   */
  public static final int MSG_CODE_63 = 63;

  /**
   * 
   * @see SkillSession#loadCodeFromJarWithReferenceClass(String, String)
   */
  public static final int MSG_CODE_64 = 64;

  /**
   * Create a new {@link Logger}
   * 
   * @return logger
   */
  static Logger create() {

    File logfile = null;

    try {
      logfile = File.createTempFile("cdsrc_", ".log");
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
    System.err.println("Logfile @ " + logfile);
  }

  /**
   * Add a message
   * 
   * @param id      Identifier
   * @param message Message
   * @return this
   */
  public Logger add(final int messageCode, final String id,
      final String message[]) {

    final StringBuilder retval = new StringBuilder().append("[").append(id)
        .append("|").append(messageCode).append("] @ ")
        .append(DATE_FORMAT.format(new Date()));

    String lines[];

    for (final String element : message) {

      lines = element.split("\\r?\\n");

      for (int i = 0; i < lines.length; i++) {

        if (lines[i].length() > 0) {
          retval.append("\n  ").append(lines[i]);
        }
      }
    }

    retval.append("\n");

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
  public Logger add(final int messageCode, final String id,
      final String message) {
    return this.add(messageCode, id, new String[] { message });
  }

  /**
   * Add a message
   * 
   * @param message Message
   * @return this
   */
  public Logger add(final int messageCode, final String message[]) {
    return this.add(messageCode, "" + this.iter, message);
  }

  /**
   * Add a message
   * 
   * @param message Message
   * @return this
   */
  public Logger add(final int messageCode, final String message) {
    return this.add(messageCode, "" + this.iter, new String[] { message });
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