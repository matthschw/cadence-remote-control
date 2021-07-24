package rbz.eda.generic.core.ipc.simpleClient;

import java.io.InputStream;
import java.util.Scanner;

import rbz.eda.generic.core.ipc.protocol.MessageProtocol;

public class SimpleClientListener extends Thread{
	
	private InputStream inputStream;
	private boolean running=true;
	
	public SimpleClientListener(InputStream inputStream){
		this.inputStream=inputStream;
	}
	@Override
	public void run() {	
		
		Scanner scanner = new Scanner(this.inputStream);
		scanner.useDelimiter(MessageProtocol.getDelimiterSequenceRegex());
		
		while (this.running) {
			String s=scanner.next();
			System.out.println(s);
		}
		
		scanner.close();
	}
	
	public void terminate(){
		this.running=false;
	}
}