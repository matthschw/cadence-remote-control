package rbz.eda.generic.core.ipc.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import rbz.eda.generic.core.ipc.protocol.MessageProtocol;

public class VirtuosoTransmitter extends Thread {
	
	private Server server;
	private PrintStream virtuosoStringCmdPrintStream;
	private FileOutputStream virtuosoSkillCmdFileOutputStream;
	private VirtuosoSkillListener virtuosoSkillListener;
	
	private boolean running=true;
	
	public VirtuosoTransmitter(Server server, OutputStream virtuosoStringCmdOutputStream,FileOutputStream virtuosoSkillCmdFileOutputStream,VirtuosoSkillListener virtuosoSkillListener){
		this.server=server;
		this.virtuosoStringCmdPrintStream=new PrintStream(virtuosoStringCmdOutputStream);
		this.virtuosoSkillCmdFileOutputStream=virtuosoSkillCmdFileOutputStream;
		this.virtuosoSkillListener=virtuosoSkillListener;
		
	}
	
	public void run(){
		
		while(this.running){
			
			if( !this.server.isVirtuosoBusy() && this.server.isNewMessageAvailable()){
				
				this.server.setVirtuosoBusy();
				String s=this.server.popMessageForVirtuoso();
				
				switch (s.substring(0, MessageProtocol.ID_LENGTH)) {
					
				case MessageProtocol.ID_SKILL:
					
					try {
						this.virtuosoSkillListener.waitForAnswer();
						this.virtuosoSkillCmdFileOutputStream.write(MessageProtocol.unescapeMessage(s.substring(MessageProtocol.ID_LENGTH, s.length())).getBytes());
						this.server.pushInfoToLogfile("skill command transmitted to virtuoso:\n"+s.substring(MessageProtocol.ID_LENGTH, s.length()));
					} catch (IOException e) {
						// To be implemented...
						e.printStackTrace();
					}
					break;
				
				case MessageProtocol.ID_STRING:
					
					this.server.pushInfoToLogfile("string command transmitted to virtuoso:\n"+s.substring(MessageProtocol.ID_LENGTH, s.length()));
					this.virtuosoStringCmdPrintStream.println(s.substring(MessageProtocol.ID_LENGTH, s.length()));
					break;
				
				default:
					break;
				}
			}
			
			synchronized (this) {
				
				try {
					
					this.wait(MessageProtocol.TIMEOUT);
				} 
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
				}
			}
		}
		
	}
	
	public void terminate(){
		this.running=false;
	}
}
