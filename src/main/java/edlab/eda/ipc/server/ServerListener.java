package rbz.eda.generic.core.ipc.server;

import java.util.Scanner;

import rbz.eda.generic.core.ipc.protocol.MessageProtocol;

public abstract class ServerListener extends Thread{
	
	protected volatile boolean running = true;
	protected Server server;
	protected Scanner scanner;
	
	public ServerListener(Server server, Scanner scanner){
		this.server=server;
		this.scanner=scanner;
	}
	
	public void terminate(){
		this.running=false;
	}
	
	public abstract void run();
	
	protected void waitForTimeout(){
		
		synchronized (this) {
			try {
				this.wait(MessageProtocol.TIMEOUT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
		}
	}
}
