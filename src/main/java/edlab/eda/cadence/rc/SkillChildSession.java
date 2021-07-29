package edlab.eda.cadence.rc;

import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import edlab.eda.cadence.rc.api.GenericSkillCommandTemplates;
import edlab.eda.cadence.rc.api.GenericSkillCommands;
import edlab.eda.cadence.rc.api.SkillCommandTemplate;
import edlab.eda.cadence.rc.data.SkillDataobject;

public class SkillChildSession {

  public StringListener listener;
  public FileInputStream instream;
  public PrintStream outstream;

  private SkillCommandTemplate controlCommand = new SkillCommandTemplate(
      "EDcdsRCfmtCmd", 1);

  public SkillChildSession(Scanner stringScanner) {

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

    instream = new FileInputStream(fd);

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

    outstream = new PrintStream(new FileOutputStream(fd));

    Scanner sc = new Scanner(instream);
  }

  public SkillDataobject evaluate(SkillCommand command) throws IOException {

    SkillCommand outer = SkillCommand.buildCommand(controlCommand,
        SkillCommand.buildCommand(GenericSkillCommandTemplates
            .getTemplate(GenericSkillCommands.ERRSET), command));

    outstream.println(outer.toSkill());

    BufferedWriter writer = new BufferedWriter(new FileWriter(
        "/home/schweikardt/github/cadence-remote-control/fuubar.txt"));

    writer.write("Hello");
    writer.flush();

    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    writer.write("\n");

    writer.write("" + this.instream.available());
    writer.flush();

    String s = "";

    while (this.instream.available() > 0) {

      s = s + (char) this.instream.read();

    }

    writer.write("\n");
    writer.write(s);

    writer.close();

    return null;

  }

}
