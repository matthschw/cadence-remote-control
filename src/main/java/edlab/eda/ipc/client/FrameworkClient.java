package rbz.eda.generic.core.ipc.client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

import rbz.eda.generic.core.ipc.protocol.*;

public class FrameworkClient {

  public static enum ClientState {
    UNCONNECTED, CONNECTED
  };

  private ClientState clientState = ClientState.UNCONNECTED;

  private PrintStream printStream;

  private FrameworkClientListener frameworkClientListener;

  private LinkedList<String> stringMessagesFromVirtuoso = new LinkedList<String>();
  private LinkedList<String> skillMessagesFromVirtuoso = new LinkedList<String>();

  private int socketID;
  private String host;

  private Socket socket;

  /**
   * Constructor
   * 
   * @param socketID
   * @param host
   */
  public FrameworkClient(int socketID, String host) {

    this.socketID = socketID;
    this.host = host;
  }

  /**
   * Within this method a connection to the Socket will be established
   * 
   * @throws NotConnectedException
   */
  public void start() throws NotConnectedException {

    int i = 0;

    // Try to instantiate socket connection
    while (this.socket == null) {

      try {

        this.socket = new Socket(this.host, this.socketID);

      } catch (IOException e) {
        System.out.println("Error");
        // Do nothing
      }

      try {

        Thread.sleep(MessageProtocol.TIMEOUT_TO_INSTANTIATE_CONNECTION);

      } catch (InterruptedException e) {
        // Do nothing
      }

      if (i < MessageProtocol.ATTEMPTS_TO_INSTANTIATE_CONNECTION) {

        i++;

      } else {

        throw new NotConnectedException(
            "Unable to connect to port " + this.socketID + " @ " + this.host);
      }

    }

    // Try to instantiate FrameworkListener
    try {

      this.frameworkClientListener = new FrameworkClientListener(this,
          new Scanner(this.socket.getInputStream()));

    } catch (IOException e) {

      try {
        this.socket.close();
      } catch (IOException e1) {
        // Do nothing
      }

      throw new NotConnectedException(
          "Unable to instantiate the Framework Listener\n" + e.getMessage());
    }

    // Try to instantiate PrintStream
    try {

      this.printStream = new PrintStream(this.socket.getOutputStream());

    } catch (IOException e) {

      try {
        this.socket.close();
      } catch (IOException e1) {
        // Do nothing
      }

      throw new NotConnectedException(
          "Unable to instantiate a print stream\n" + e.getMessage());
    }

    this.frameworkClientListener.start();

    this.clientState = ClientState.CONNECTED;
  }

  /**
   * Using this method a skill command can be send to virtuoso
   * 
   * @param skillCmd
   * @throws MaxMessageLengthException
   * @throws NotConnectedException
   */
  public void sendSkillCommand(String skillCmd)
      throws MaxMessageLengthException, NotConnectedException {

    if (this.clientState == ClientState.UNCONNECTED) {

      throw new NotConnectedException("FrameworkClient is not connected");

    } else {

      String frame;

      frame = MessageProtocol.getDelimiterSequence() + MessageProtocol.ID_SKILL
          + MessageProtocol.escapeMessage(skillCmd)
          + MessageProtocol.getDelimiterSequence();

      if (frame.length() < MessageProtocol.MAX_MSG_LENGTH) {

        this.printStream.print(frame);
      } else {

        throw new MaxMessageLengthException(
            "Your message exceed the maximum message length of "
                + MessageProtocol.MAX_MSG_LENGTH + " characters");
      }
    }
  }

  /**
   * Using this method a string command can be send to virtuoso
   * 
   * @param message
   * @throws MaxMessageLengthException
   * @throws NotConnectedException
   */
  public void sendStringCommand(String message)
      throws MaxMessageLengthException, NotConnectedException {

    if (this.clientState == ClientState.UNCONNECTED) {

      throw new NotConnectedException("FrameworkClient is not connected");

    } else {

      String frame;

      frame = MessageProtocol.getDelimiterSequence() + MessageProtocol.ID_STRING
          + MessageProtocol.escapeMessage(message)
          + MessageProtocol.getDelimiterSequence();

      if (frame.length() < MessageProtocol.MAX_MSG_LENGTH) {

        this.printStream.print(frame);

      } else {

        throw new MaxMessageLengthException(
            "Your message exceed the maximum message length of "
                + MessageProtocol.MAX_MSG_LENGTH + " characters");
      }
    }
  }

  /**
   * This method returns whether a a skill message from virtuoso is avaiable
   * 
   * @return
   * @throws NotConnectedException
   */
  public boolean hasSkillMessageFromVirtuoso() throws NotConnectedException {

    if (this.clientState == ClientState.CONNECTED) {

      synchronized (this.skillMessagesFromVirtuoso) {

        return this.skillMessagesFromVirtuoso.size() > 0;

      }
    } else {

      throw new NotConnectedException("FrameworkClient is not connected");
    }
  }

  /**
   * This method returns whether a a string message from virtuoso is avaiable
   * 
   * @return
   * @throws NotConnectedException
   */
  public boolean hasStringMessageFromVirtuoso() throws NotConnectedException {

    if (this.clientState == ClientState.CONNECTED) {

      synchronized (this.stringMessagesFromVirtuoso) {

        return this.stringMessagesFromVirtuoso.size() > 0;

      }
    } else {

      throw new NotConnectedException("FrameworkClient is not connected");
    }
  }

  /**
   * This method returns the oldest available skill message
   * 
   * @return
   * @throws NotConnectedException
   */
  public String getSkillMessage() throws NotConnectedException {

    if (this.hasSkillMessageFromVirtuoso()) {

      synchronized (this.skillMessagesFromVirtuoso) {
        return this.skillMessagesFromVirtuoso.removeFirst();
      }
    } else {

      return null;
    }
  }

  /**
   * This method returns the oldest available string message
   * 
   * @return
   * @throws NotConnectedException
   */
  public String getStringMessage() throws NotConnectedException {

    if (this.hasStringMessageFromVirtuoso()) {

      synchronized (this.stringMessagesFromVirtuoso) {
        return this.stringMessagesFromVirtuoso.removeFirst();
      }

    } else {

      return null;
    }
  }

  /**
   * This method returns all available skill messages within a LinkedList
   * 
   * @return
   * @throws NotConnectedException
   */
  public LinkedList<String> getSkillMessages() throws NotConnectedException {

    LinkedList<String> res = new LinkedList<String>();

    while (this.hasSkillMessageFromVirtuoso()) {

      res.addLast(this.getSkillMessage());

    }

    return res;
  }

  /**
   * This method returns all available string messages within a LinkedList
   * 
   * @return
   * @throws NotConnectedException
   */
  public LinkedList<String> getStringMessages() throws NotConnectedException {

    LinkedList<String> res = new LinkedList<String>();

    while (this.hasStringMessageFromVirtuoso()) {

      res.addLast(this.getStringMessage());

    }

    return res;
  }

  /**
   * This method gets called by the listener and adds new messages to the lists
   * 
   * @param s
   */

  public void pushMessageFromVirtuoso(String s) {

    if (s.length() > MessageProtocol.ID_LENGTH) {

      switch (s.substring(0, MessageProtocol.ID_LENGTH)) {

      case MessageProtocol.ID_SKILL:

        synchronized (this.skillMessagesFromVirtuoso) {

          this.skillMessagesFromVirtuoso
              .addLast(MessageProtocol.unescapeMessage(
                  s.substring(MessageProtocol.ID_LENGTH, s.length())));
        }
        break;

      case MessageProtocol.ID_STRING:

        synchronized (this.stringMessagesFromVirtuoso) {

          this.stringMessagesFromVirtuoso
              .addLast(MessageProtocol.unescapeMessage(
                  s.substring(MessageProtocol.ID_LENGTH, s.length())));
        }
        break;

      default:
        break;
      }
    }
  }
}
