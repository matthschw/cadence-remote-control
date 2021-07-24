package rbz.eda.generic.core.ipc.simpleClient;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import rbz.eda.generic.core.ipc.protocol.MessageProtocol;

public class SimpleClient {
	
	private String host;
	private int socketID;
	
	public static void main(String[] args) {
		
		new SimpleClient("127.0.0.1",MessageProtocol.getSocketID()).start();
	}
	
	public SimpleClient(String host, int socketID){
		this.socketID=socketID;
		this.host=host;
	}
	
	@SuppressWarnings("resource")
	public void start(){
		
		// connect client to server
		Socket client=null;
		try {
			client = new Socket(this.host, this.socketID);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// create a new thread for server message handling
		try {
			new SimpleClientListener(client.getInputStream()).start();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		Scanner scanner = new Scanner(System.in);
		
		PrintStream outputPrintStream = null;
		try {
			outputPrintStream = new PrintStream(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Client successfully connected to server!");
		
		String s;
		
		while (true) {	
			s=scanner.nextLine();
			outputPrintStream.println(s);
		}
	}
}