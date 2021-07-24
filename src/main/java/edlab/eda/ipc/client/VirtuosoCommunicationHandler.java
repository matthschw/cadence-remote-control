package rbz.eda.generic.core.ipc.client;

import java.util.LinkedList;

import rbz.eda.generic.core.ipc.protocol.MaxMessageLengthException;
import rbz.eda.generic.core.ipc.protocol.MessageProtocol;
import rbz.eda.generic.core.ipc.protocol.NotConnectedException;

public class VirtuosoCommunicationHandler {
	
	private FrameworkClient frameworkClient;
	
	/**
	 * Constructor
	 * @throws NotConnectedException 
	 */
	public VirtuosoCommunicationHandler() throws NotConnectedException{
		
		this.frameworkClient= new FrameworkClient(MessageProtocol.getSocketID(), "127.0.0.1");
		this.frameworkClient.start();
	}
	
	/**
	 * Using this method a skill command can be send to virtuoso
	 * @param skillCmd
	 * @throws MaxMessageLengthException
	 * @throws NotConnectedException
	 */
	public void sendSkillCommand(String skillCmd) throws MaxMessageLengthException, NotConnectedException{
		
		this.frameworkClient.sendSkillCommand(skillCmd);
	}
	
	/**
	 * Using this method a string command can be send to virtuoso
	 * @param message
	 * @throws MaxMessageLengthException
	 * @throws NotConnectedException
	 */
	public void sendStringCommand(String message) throws MaxMessageLengthException, NotConnectedException{
		
		this.frameworkClient.sendStringCommand(message);
	}
	
	/**
	 * This method returns whether a a skill message from virtuoso is avaiable
	 * @return
	 * @throws NotConnectedException 
	 */
	public boolean hasSkillMessageFromVirtuoso() throws NotConnectedException{
		
		return this.frameworkClient.hasSkillMessageFromVirtuoso();
	}
	
	/**
	 * This method returns whether a a string message from virtuoso is avaiable
	 * @return
	 * @throws NotConnectedException 
	 */
	public boolean hasStringMessageFromVirtuoso() throws NotConnectedException{
		
		return this.frameworkClient.hasStringMessageFromVirtuoso();
	}
	
	/**
	 * This method returns the oldest available skill message
	 * @return
	 * @throws NotConnectedException
	 */
	public String getSkillMessage() throws NotConnectedException{
		
		return this.frameworkClient.getSkillMessage();
	}
	
	/**
	 * This method returns the oldest available string message
	 * @return
	 * @throws NotConnectedException
	 */
	public String getStringMessage() throws NotConnectedException{
		
		return this.frameworkClient.getStringMessage();
	}
	
	/**
	 * This method returns all available skill messages within a LinkedList
	 * @return
	 * @throws NotConnectedException
	 */
	public LinkedList<String> getSkillMessages() throws NotConnectedException{
		
		return this.frameworkClient.getSkillMessages();
	}
	
	/**
	 * This method returns all available string messages within a LinkedList
	 * @return
	 * @throws NotConnectedException
	 */
	public LinkedList<String> getStringMessages() throws NotConnectedException{
		
		return this.frameworkClient.getStringMessages();
	}
}
