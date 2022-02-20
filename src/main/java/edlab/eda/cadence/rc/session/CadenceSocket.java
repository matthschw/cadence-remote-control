package edlab.eda.cadence.rc.session;

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
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Socket Server that is started in a Cadence tool and will create a socket.
 */
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

  /**
   * Create a new Socket
   * 
   * @throws Exception when creating of the socket failed
   */
  private CadenceSocket() throws Exception {

    Constructor<FileDescriptor> ctor;
    FileDescriptor fd = null;

    ctor = FileDescriptor.class.getDeclaredConstructor(Integer.TYPE);
    ctor.setAccessible(true);
    fd = ctor.newInstance(VIRTUOSO_SKILL_OUT);
    ctor.setAccessible(false);

    this.cadenceInputStream = new FileInputStream(fd);

    fd = null;

    ctor = FileDescriptor.class.getDeclaredConstructor(Integer.TYPE);
    ctor.setAccessible(true);
    fd = ctor.newInstance(CADENCE_SKILL_IN);
    ctor.setAccessible(false);

    this.cadenceOutputStream = new FileOutputStream(fd);

    this.serverSocket = new ServerSocket(0, 1);
    // this.serverSocket.bind(null);

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

  /**
   * Get the port of the socket
   * 
   * @return port
   */
  private int getPort() {
    return this.serverSocket.getLocalPort();
  }

  /**
   * Wait for incoming connection
   * 
   * @return this
   * @throws IOException (occurs never)
   */
  public CadenceSocket start() throws IOException {

    this.socket = this.serverSocket.accept();

    this.frameworkInputStream = this.socket.getInputStream();

    this.frameworkOutputStream = this.socket.getOutputStream();

    return this;
  }

  /**
   * Clear the socket
   * 
   * @return this
   */
  private CadenceSocket clear() {

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

    return this;
  }

  /**
   * Read-Eval-Print-Loop
   * 
   * @return this
   * @throws IOException (occurs never)
   */
  public CadenceSocket runRepl() throws IOException {

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

      try {
        this.cadenceOutputStream.write(data);
      } catch (IOException e) {
        // Exit when Cadence crashed
        System.exit(-1);
      }

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
        // Exit when Cadence crashed
        System.exit(-1);
      }

      if (!this.socket.isConnected()) {
        break repl;
      } else {
        this.frameworkOutputStream.write(data);
      }
    }

    return this.clear();
  }

  /**
   * Method which is invoked from a Cadence tool
   *
   * @param args Array of strings
   * @throws Exception Is thrown when the connection cannot be established
   */
  public static void main(String[] args) throws Exception {

    CadenceSocket socket = new CadenceSocket();

    while (true) {
      System.out.print(socket.getPort());
      socket.start().runRepl();
    }
  }
}