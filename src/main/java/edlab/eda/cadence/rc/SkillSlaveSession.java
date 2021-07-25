package edlab.eda.cadence.rc;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import edlab.eda.cadence.rc.data.SkillDataobject;

public abstract class SkillSlaveSession {

  private StringListener listener;
  private FileOutputStream instream;
  private FileOutputStream outstream;

  public SkillSlaveSession(Scanner stringScanner) {

    if (stringScanner != null) {
      this.listener = new StringListener(stringScanner, this);
      this.listener.start();
    }

    Constructor<FileDescriptor> ctor;
    FileDescriptor fd = null;

    try {
      ctor = FileDescriptor.class.getDeclaredConstructor(Integer.TYPE);
      ctor.setAccessible(true);
      fd = ctor.newInstance(4);
      ctor.setAccessible(false);

    } catch (NoSuchMethodException | SecurityException | InstantiationException
        | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e) {

      System.exit(-1);
    }
    
    FileInputStream instream = new FileInputStream(fd);

    fd = null;
    try {
      ctor = FileDescriptor.class.getDeclaredConstructor(Integer.TYPE);
      ctor.setAccessible(true);
      fd = ctor.newInstance(3);
      ctor.setAccessible(false);

    } catch (NoSuchMethodException | SecurityException | InstantiationException
        | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e) {

      System.exit(-1);
    }

    FileOutputStream outstream = new FileOutputStream(fd);

    PrintStream outprintstream = new PrintStream(outstream);
  }

  public SkillDataobject evaluateCommand(SkillCommand command) {

    return null;
  }

  public abstract void react(SkillDataobject obj);

}
