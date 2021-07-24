package rbz.eda.generic.core.ipc.server;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.*;



import rbz.eda.generic.core.ipc.protocol.MessageProtocol;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author schweikardt
 * @version 1.0, 2017/11/21
 */
public class Server {
	
	public static enum ServerState {DOWN,UNCONNECTED,CONNECTED};
	
	private int socketID;
	
	private VirtuosoStringListener virtuosoStringListener;
	private VirtuosoSkillListener virtuosoSkillListener;
	private VirtuosoTransmitter virtuosoTransmitter;
	
	public volatile boolean virtuosoBusy=false;
	private volatile LinkedList<String> fifoMessagesForVirtuoso=new LinkedList<String>();
	
	private ServerSocket serverSocket;
	
	private Socket socket;
	private volatile PrintStream frameworkPrintStream;
	private volatile FrameworkListener frameworkListener;
	
	private Logger ipcLogger;
	private FileHandler fileHandler;
	
	private ServerState serverState=Server.ServerState.DOWN;
	
	/**
	 * Constructor for class server
	 * Within the constructor the connection to the parent process (Virtuoso) is established and the socket gets created.
	 * @param socketID - integer
	 * @param virtuosoStdOutScanner - typically System.in
	 */
	private Server(int socketID, Scanner virtuosoScanner,String pathToLogfile){
		
		this.socketID=socketID;
		
		//Creating logfile
		this.ipcLogger = Logger.getLogger("ipcLog");
		
		try {
			this.fileHandler = new FileHandler(pathToLogfile+"ServerLog");
			ipcLogger.addHandler(this.fileHandler);
			
			this.fileHandler .setFormatter(new SimpleFormatter());
			
			this.ipcLogger.info("Logfile had been created successfully");
			
		} 
		catch (SecurityException e) {
			
			this.ipcLogger.log(Level.SEVERE,"Logfile couldn't be created - no permission \n"+ e.getMessage());
			System.exit(-1);
			
		} 
		catch (IOException e) {
			
			this.ipcLogger.log(Level.SEVERE,"Logfile couldn't be created \n"+e.getMessage());
			System.exit(-1);
			
		}
		
		//Creating String Listener for Virtuoso
		this.virtuosoStringListener = new VirtuosoStringListener(virtuosoScanner, this);
		this.virtuosoStringListener.start();//start thread
		this.ipcLogger.info("Virtuoso Listener had been created successfully");
		
		
		//Creating SKill Listener for Virtuoso
		Constructor<FileDescriptor> ctor;
		FileDescriptor fd = null;
		
		try {
			ctor = FileDescriptor.class.getDeclaredConstructor(Integer.TYPE);
			ctor.setAccessible(true);
			fd = ctor.newInstance(MessageProtocol.VIRTUOSO_SKILL_OUT);
			ctor.setAccessible(false);
				
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			
			this.ipcLogger.log(Level.SEVERE,"File Descriptor for skill output @ port "+MessageProtocol.VIRTUOSO_SKILL_OUT+" couldn't been opended \n"+e.getMessage());
			System.exit(-1);
		} 
		
		this.virtuosoSkillListener=new VirtuosoSkillListener(new FileInputStream(fd),this);
		this.virtuosoSkillListener.start();
		
		
		//Creating Virtuoso Transmitter
		fd = null;
		try {
			ctor = FileDescriptor.class.getDeclaredConstructor(Integer.TYPE);
			ctor.setAccessible(true);
			fd = ctor.newInstance(MessageProtocol.VIRTUOSO_SKILL_IN);
			ctor.setAccessible(false);
				
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			
			this.ipcLogger.log(Level.SEVERE,"File Descriptor for skill return values @ port "+MessageProtocol.VIRTUOSO_SKILL_IN+" couldn't been opended \n"+e.getMessage());
			System.exit(-1);
		}
		this.virtuosoTransmitter=new VirtuosoTransmitter(this, System.out,new FileOutputStream(fd),this.virtuosoSkillListener);
		this.virtuosoTransmitter.start();
		
		//Creating socket
		try {
			
			this.serverSocket = new ServerSocket(this.socketID);
			
			this.ipcLogger.info("Server Socket had been created successfully @ port: "+ this.serverSocket.getLocalPort());
			this.serverState=Server.ServerState.UNCONNECTED;
			
		} 
		catch (IOException e) {
			
			this.ipcLogger.log(Level.SEVERE,"Server Socket couldn't be created"+ e.getMessage());
			System.exit(-1);
		}

	}
	
	/**
	 * This methods gets called by the VirtuosoTransmitter to enqire if a new message can be received by virtuoso.
	 * @return
	 */
	public boolean isVirtuosoBusy(){
		
		return this.virtuosoBusy;
	}
	
	/**
	 * This methods gets called by the VirtuosoTransmitter when a command was send to virtuoso
	 */
	public void setVirtuosoBusy(){
		
		this.virtuosoBusy=true;
		this.ipcLogger.info("virtuoso is busy");
	}
	
	/**
	 * This methods gets called when
	 */
	private void setVirtuosoAvailable(){
		
		this.virtuosoBusy=false;
		this.ipcLogger.info("virtuoso is available");
	}
	
	/**
	 * This method gets called by the VirtuosoTransmitter to inquire if a message is available for transmission to virtuoso.
	 * @return
	 */
	public boolean isNewMessageAvailable(){
		
		return this.fifoMessagesForVirtuoso.size()>0;
	}
	
	/**
	 * This method gets called the FrameworkListener to push a new message to the server fifo
	 * @param s
	 */
	public void pushMessageForVirtuoso(String s){
		
		synchronized (this.fifoMessagesForVirtuoso) {
			this.fifoMessagesForVirtuoso.addLast(s);
		}
	}
	
	/**
	 * This methods returns the first element of the server fifo. Will be called by the Virtuoso Transmitter
	 * @return
	 */
	public String popMessageForVirtuoso(){
		
		synchronized (this.fifoMessagesForVirtuoso) {
			
			return this.fifoMessagesForVirtuoso.removeFirst();
		}
	}
	
	/**
	 * This method gets called when a new server gets created or the the previous connection was cancelled.
	 * Within the method the socket waits until a connection is established.
	 */
	private void start(){

		try {
			
			//Waiting for incoming connection
			this.socket=this.serverSocket.accept();
			this.ipcLogger.info("Connection established with client " + socket.getInetAddress().getHostAddress());
			
			this.frameworkPrintStream=new PrintStream(socket.getOutputStream());
			
			//Create Listener for the new connection
			this.frameworkListener=new FrameworkListener(this, new Scanner(socket.getInputStream()));
			this.frameworkListener.start();//start thread
			
			this.ipcLogger.info("Framework Listener had been created successfully");
			
			this.serverState=Server.ServerState.CONNECTED;
			
		} 
		catch (IOException e) {
			
			this.ipcLogger.log(Level.SEVERE,"Framework Listener couldn't be created: \n"+e.getMessage());
			e.printStackTrace();
			
			System.exit(-1);
		}
	}
	
	/**
	 * This Method gets called by the VirtuosoSKillListener and returns 
	 * @param s
	 */
	public void virtuosoSkillToFramework(String s){
		
		this.ipcLogger.info("Skill command had been evaluated - Result received:\n"+s);
		
		this.frameworkPrintStream.print(MessageProtocol.getDelimiterSequence()+MessageProtocol.ID_SKILL+MessageProtocol.escapeMessage(s)+MessageProtocol.getDelimiterSequence());
		
		this.setVirtuosoAvailable();
	}
	
	/**
	 * This method is called from the VirtuosoListener and transmits the String to the framework
	 * Furthermore the String will be written in the logfile.
	 * @param String s
	 */
	public void virtuosoStringToFramework(String s){
		
		if(s.length()>MessageProtocol.ID_LENGTH){
				
			switch (s.substring(0, MessageProtocol.ID_LENGTH)) {
					
				case MessageProtocol.CMD_VIRTUOSO_AVAIL:
						
					this.setVirtuosoAvailable();
					
					break;
						
				case MessageProtocol.ID_STRING:
						
					if(this.frameworkPrintStream!=null){
						
						this.ipcLogger.info("data received from virtuoso:\n " +s.substring(MessageProtocol.ID_LENGTH, s.length()));
						
						//Sending message to Framework using the print stream
						this.frameworkPrintStream.print(MessageProtocol.getDelimiterSequence()+s+MessageProtocol.getDelimiterSequence());
							
					}else {
						
						this.ipcLogger.warning("string command received from Virtuoso - not connected to framework:\n"+s.substring(MessageProtocol.ID_LENGTH, s.length()));
					}
					
					break;
					
				case MessageProtocol.CMD_VIRTUOSO_LOG:
						
					//Writing string to logfile
					this.ipcLogger.info("logfile message received from virtuoso:\n " +s.substring(MessageProtocol.ID_LENGTH, s.length()));
						
					break;
					
				default:
					break;
					
			}
		}
	}
	
	
	/**
	 * This method is called from the FrameworkListener and transmits the String to Virtuoso
	 * Furthermore the String will be written in the logfile.
	 * @param s
	 */
	public void frameworkToVirtuoso(String s){
		
		if(s.length()>MessageProtocol.ID_LENGTH){
			
			switch (s.substring(0, MessageProtocol.ID_LENGTH)) {
				
				case MessageProtocol.ID_SKILL:
					this.ipcLogger.info("skill command received from framework: \n"+s.substring(MessageProtocol.ID_LENGTH,s.length())+"\nwill push to fifo");
					this.pushMessageForVirtuoso(s);
					break;
					
				case MessageProtocol.ID_STRING:
					this.ipcLogger.info("string command received from framework: \n"+s.substring(MessageProtocol.ID_LENGTH,s.length())+"\nwill push to fifo");
					this.pushMessageForVirtuoso(s);
					break;
					
				default:
					break;
			}
		}
	}
	
	/**
	 * 
	 * @param s
	 */
	public void pushInfoToLogfile(String s){
		this.ipcLogger.info(s);
	}
	
	/**
	 * This method gets called from the FrameworkListener if the connection to the client gets lost.
	 * It will terminate the corresponding thread and reset the associated attributes.
	 * Afterwards it will call the method start, which waits for a new connection.
	 */
	public void connectionToFrameworkLost(){
		
		this.ipcLogger.info("Client closed connection");
		
		this.frameworkListener.terminate();
		this.frameworkListener=null;
		this.frameworkPrintStream=null;
		this.fifoMessagesForVirtuoso.clear();
		this.virtuosoBusy=false;
		
		this.serverState=ServerState.UNCONNECTED;
		
		//Wait for new connection
		this.start();
	}
	
	/**
	 * Returns an enum value which describes the internal state of the server
	 * @return server state
	 */
	public ServerState getServerState(){
		
		return this.serverState;
	}
	
	/**
	 * This method will called from Virtuoso. The main method expects a string where to save the logfile.
	 * If no string is delivered the local directory will be used. 
	 * No valid check is performed, i.e. if the argument is not a valid path the program will crash.
	 * For development the socketID is derived from the username (works only on rbz server elmo)
	 * @param args
	 */
	public static void main(String[] args) {
		
		String path="./";
		
		if(args !=null&&args.length>0){
			
			path=args[0];
		}
		
		if(MessageProtocol.getSocketID()!=0){
			new Server(MessageProtocol.getSocketID(),new Scanner(System.in), path).start();
		}
	}
}
