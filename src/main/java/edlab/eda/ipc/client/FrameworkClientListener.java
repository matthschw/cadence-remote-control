package rbz.eda.generic.core.ipc.client;

import java.util.Scanner;

import rbz.eda.generic.core.ipc.protocol.MessageProtocol;

public class FrameworkClientListener extends Thread{
	
	private FrameworkClient frameworkClient;
	private Scanner scanner;
	private volatile boolean running=true;
	
	public FrameworkClientListener(FrameworkClient frameworkClient,Scanner scanner){
		
		this.frameworkClient=frameworkClient;
		this.scanner=scanner;
		scanner.useDelimiter(MessageProtocol.getDelimiterSequenceRegex());
	}
	
	public void run(){
		
		String s;
		
		while (running) {
			
			s=this.scanner.next();
			
			if(s!=null&&s!=""){
				
				this.frameworkClient.pushMessageFromVirtuoso(s);
			}
			
			try {
				Thread.sleep(MessageProtocol.TIMEOUT);
			} catch (InterruptedException e) {
				// TODO
			}
		}
	}
	
	public void terminate(){
		this.running=false;
	}
}