package rbz.eda.generic.core.ipc.server;

import java.util.Scanner;

import rbz.eda.generic.core.ipc.protocol.MessageProtocol;

public class VirtuosoStringListener extends ServerListener{
	
	public VirtuosoStringListener(Scanner scanner, Server server){
		super(server, scanner);
		this.scanner.useDelimiter(MessageProtocol.getDelimiterSequenceRegex());
	}
	/**
	 * Overridden method from superclass ServerListener
	 * This method should NEVER be called by the user 
	 */
	@Override
	public void run() {
		
		String s;
		
		while(this.running){
			
			while (this.scanner.hasNext()){
				
				s = scanner.next();
				
				if(s!=null&&s!=""){
					
					this.server.virtuosoStringToFramework(s);
				}
				
				try {
					Thread.sleep(MessageProtocol.TIMEOUT);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (!this.running) {
				return;
			}
			
			try {
				Thread.sleep(MessageProtocol.TIMEOUT);
			} catch (InterruptedException e) {
				// TODO
			}
		}
	}
}