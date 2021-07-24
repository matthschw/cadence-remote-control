package rbz.eda.generic.core.ipc.main;

import java.util.Scanner;

import rbz.eda.generic.core.ipc.client.VirtuosoCommunicationHandler;
import rbz.eda.generic.core.ipc.protocol.MaxMessageLengthException;
import rbz.eda.generic.core.ipc.protocol.NotConnectedException;

public class CIW {

	public static void main(String[] args) throws MaxMessageLengthException, NotConnectedException, InterruptedException {
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String res;
		
		
			VirtuosoCommunicationHandler myVirtuosoCommunicationHandler=new VirtuosoCommunicationHandler();
			
			while(true) {
				
				myVirtuosoCommunicationHandler.sendSkillCommand(scanner.nextLine());
				
				
				while(!myVirtuosoCommunicationHandler.hasSkillMessageFromVirtuoso()){
					Thread.sleep(1);
				}
			
				res=myVirtuosoCommunicationHandler.getSkillMessage();
				System.out.println(res.substring(1, res.length()-1));
			}
	}
}
