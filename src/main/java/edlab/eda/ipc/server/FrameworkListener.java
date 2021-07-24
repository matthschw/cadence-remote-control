package rbz.eda.generic.core.ipc.server;

import java.util.NoSuchElementException;
import java.util.Scanner;
import rbz.eda.generic.core.ipc.protocol.MessageProtocol;

public class FrameworkListener extends ServerListener{
	
	public FrameworkListener(Server server, Scanner scanner){
		super(server,scanner);
		this.scanner.useDelimiter(MessageProtocol.getDelimiterSequenceRegex());
	}
	
	/**
	 * Overridden method from superclass ServerListener
	 * This method should NEVER be called by the user
	 */
	@Override
	public void run() {
		
		String s;
		
		while (this.running){
			
			try{
				
				s=scanner.next();
				
				if(s!=null&&s!=""){
					
					this.server.frameworkToVirtuoso(s);
				}
			}
			catch(NoSuchElementException e){
				
				this.server.connectionToFrameworkLost();
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
		
	}
}