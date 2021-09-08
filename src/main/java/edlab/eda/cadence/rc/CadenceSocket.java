package edlab.eda.cadence.rc;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CadenceSocket {

  public static final String SOCKET_PORT_FILE_NAME = ".ed_cds_rc_socket";
  public static final int CADENCE_SKILL_IN = 3;
  public static final int VIRTUOSO_SKILL_OUT = 4;

  private Socket socket;
  private ServerSocket serverSocket;

  private InputStream frameworkInputStream;
  private OutputStream frameworkOutputStream;
  private FileInputStream cadenceInputStream;
  private FileOutputStream cadenceOutputStream;

  private Logger logger;

  public CadenceSocket() throws IOException, NoSuchMethodException,
      SecurityException, InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    // Creating logfile
    this.logger = Logger.getLogger("ipcLog");

    FileHandler fileHandler = new FileHandler(
        "/home/schweikardt/github/cadence-remote-control/log");
    this.logger.addHandler(fileHandler);
    fileHandler.setFormatter(new SimpleFormatter());
    this.logger.info("Logfile had been created successfully");

    Constructor<FileDescriptor> ctor;
    FileDescriptor fd = null;

    ctor = FileDescriptor.class.getDeclaredConstructor(Integer.TYPE);
    ctor.setAccessible(true);
    fd = ctor.newInstance(VIRTUOSO_SKILL_OUT);
    ctor.setAccessible(false);

    this.cadenceInputStream = new FileInputStream(fd);

    this.logger.info("Created Cadence Input-Stream");

    fd = null;

    ctor = FileDescriptor.class.getDeclaredConstructor(Integer.TYPE);
    ctor.setAccessible(true);
    fd = ctor.newInstance(CADENCE_SKILL_IN);
    ctor.setAccessible(false);

    this.cadenceOutputStream = new FileOutputStream(fd);

    this.logger.info("Created Cadence Output-Stream");

    this.serverSocket = new ServerSocket();
    this.serverSocket.bind(null);

    this.logger
        .info("Created Server-Socket @ " + this.serverSocket.getLocalPort());

    // add shutdown hook for server socket
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        try {
          serverSocket.close();
        } catch (Exception e) {
        }
      }
    });

    File file = new File(SOCKET_PORT_FILE_NAME);

    if (file.isFile()) {
      file.delete();
    }

    FileWriter fileWriter = new FileWriter(file);
    PrintWriter printWriter = new PrintWriter(fileWriter);
    printWriter.print(getPort());
    printWriter.close();

    // add shutdown hook for file
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        try {
          file.delete();
        } catch (Exception e) {
        }
      }
    });

  }

  public int getPort() {
    return this.serverSocket.getLocalPort();
  }

  public void start() throws IOException {

    this.logger.info("Waiting for connection...");

    this.socket = this.serverSocket.accept();

    this.logger.info("Created Socket @ " + this.socket.getPort());

    this.frameworkInputStream = this.socket.getInputStream();

    this.logger
        .info("Created Framework Input-Stream @ " + this.socket.getPort());

    this.frameworkOutputStream = this.socket.getOutputStream();

    this.logger
        .info("Created Framework Output-Stream @ " + this.socket.getPort());
  }

  public void clear() {

    try {
      this.frameworkInputStream.close();
    } catch (IOException e) {
    }

    try {
      this.frameworkOutputStream.close();
    } catch (IOException e) {
    }

    try {
      this.socket.close();
    } catch (IOException e) {
    }

    this.socket = null;

    this.frameworkInputStream = null;
    this.frameworkOutputStream = null;

    this.logger
        .info("Cleared Server-Socket @ " + this.serverSocket.getLocalPort());
  }

  public void runRepl() throws IOException {

    byte[] data;

    repl: while (true) {

      while (this.frameworkInputStream.available() == 0) {

        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
        }
      }

      data = new byte[this.frameworkInputStream.available()];

      this.frameworkInputStream.read(data);

      if (data.length == 1) {
        break repl;
      }

      this.logger.info("Received \"" + new String(data) + "\" from framework");

      try {
        this.cadenceOutputStream.write(data);
      } catch (IOException e) {
        System.exit(-1);
      }

      this.logger.info("Sent \"" + new String(data) + "\" to cadence");

      while (this.cadenceInputStream.available() == 0) {
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
        }
      }

      try {
        data = new byte[this.cadenceInputStream.available()];
        this.cadenceInputStream.read(data);
      } catch (IOException e) {
        System.exit(-1);
      }

      this.logger.info("Received \"" + new String(data) + "\" from cadence");

      if (!this.socket.isConnected()) {
        break repl;
      } else {
        this.frameworkOutputStream.write(data);
      }

      this.logger.info("Sent \"" + new String(data) + "\" to framework");
    }

    this.clear();
  }

  public static void main(String[] args)
      throws IOException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException {

    CadenceSocket socket = new CadenceSocket();
    System.out.print(socket.getPort());

    while (true) {
      socket.start();
      socket.runRepl();
    }
  }
}
