package rbz.eda.generic.core.ipc.server;

import java.io.FileInputStream;
import java.io.IOException;

public class VirtuosoSkillListener extends ServerListener {
	
	private boolean waitForAnswer=false;
	private String found="";
	private FileInputStream virtuosoSkillFileInputStream;
	
	public VirtuosoSkillListener(FileInputStream virtuosoSkillFileInputStream, Server server) {
		super(server, null);
		this.virtuosoSkillFileInputStream=virtuosoSkillFileInputStream;
	}
	
	public void run() {
		
		int content=-1;
		
		while(this.running){
			
			if(waitForAnswer){
				
				try {
					
					while(this.virtuosoSkillFileInputStream.available()>0||found.equals("")){
						
						try {
							
							content=this.virtuosoSkillFileInputStream.read();
							this.found=this.found+(char)content;
							
						} 
						catch (IOException e) {
							//TODO
						}
						
					}
				} 
				catch (IOException e) {
					
					this.waitForTimeout();
				}
				
				if(!found.equals("")){ //result of string command received
					
					waitForAnswer=false;//block listener
					
					this.server.virtuosoSkillToFramework(found);
					
					this.found="";//restore initial condition
				}
			}
			
			this.waitForTimeout();
		}
	}
	
	public void waitForAnswer(){
		this.waitForAnswer=true;
	}
}
