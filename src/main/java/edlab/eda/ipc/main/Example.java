package rbz.eda.generic.core.ipc.main;

import java.util.LinkedList;
import rbz.eda.generic.core.ipc.client.VirtuosoCommunicationHandler;
import rbz.eda.generic.core.ipc.protocol.MaxMessageLengthException;
import rbz.eda.generic.core.ipc.protocol.MessageProtocol;
import rbz.eda.generic.core.ipc.protocol.NotConnectedException;

public class Example {

	public static void main(String[] args) throws MaxMessageLengthException, NotConnectedException, InterruptedException {
		
	
			VirtuosoCommunicationHandler virtuosoCommunicationHandler = new VirtuosoCommunicationHandler();
			
			LinkedList<Rectangle> rectangles = new LinkedList<Rectangle>();
			
			long startTime = System.currentTimeMillis();
			
			virtuosoCommunicationHandler.sendSkillCommand("getStringFromLayout(\"testLib\" \"simpleLayout\" \"layout\")");
			
			while(!virtuosoCommunicationHandler.hasSkillMessageFromVirtuoso()){
				Thread.sleep(1);
			}
			System.out.println("Answer from Virtuoso "+virtuosoCommunicationHandler.getSkillMessage());
			
			String stringMessage="";		//whole message
			String [] stringRectangles;		//s
			String [] stringRectangleArray; //single rectangle
			
			while(virtuosoCommunicationHandler.hasStringMessageFromVirtuoso()){
				
				stringMessage=virtuosoCommunicationHandler.getStringMessage();
				
				stringRectangles = stringMessage.split("[\\)\\(]");
				
				for (String stringRectangle : stringRectangles) {
					
					stringRectangleArray = stringRectangle.split(" ");
					
					if (stringRectangleArray.length==6) {
						rectangles.add(new Rectangle(new LayoutPoint(Integer.parseInt(stringRectangleArray[2]),
																	 Integer.parseInt(stringRectangleArray[3])), 
													 new LayoutPoint(Integer.parseInt(stringRectangleArray[4]), 
																	 Integer.parseInt(stringRectangleArray[5])), 
													 stringRectangleArray[1]));
					}
				}
			}
			
			long intermTime = System.currentTimeMillis();
			
			System.out.println("Numer of rectangles "+rectangles.size());
			System.out.println("Time for pulling rectangles "+(intermTime-startTime)+" ms");
			
			String sendSkill="writeRectanglesToLayout(\"testLib\" \"simpleLayout\" \"layout_rtn\" ";
			
			String transmit =sendSkill+"list(";
			
			for (Rectangle rectangle : rectangles) {
				
				if (transmit.length()>MessageProtocol.MAX_MSG_LENGTH-100) {
					
					virtuosoCommunicationHandler.sendSkillCommand(transmit+"))");
					
					while (!virtuosoCommunicationHandler.hasSkillMessageFromVirtuoso()) {
						Thread.sleep(10);
					}
					
					virtuosoCommunicationHandler.getSkillMessage();
					transmit =sendSkill+"list(";
				}
				
				transmit+=" "+rectangle.getSkillCode();
			}
			
			virtuosoCommunicationHandler.sendSkillCommand(transmit+"))");
			
			while (!virtuosoCommunicationHandler.hasSkillMessageFromVirtuoso()) {
				Thread.sleep(10);
			}
			
			System.out.println(virtuosoCommunicationHandler.getSkillMessage());
			
			long stopTime = System.currentTimeMillis();

			System.out.println("Time for pushing rectangles "+(stopTime-intermTime)+" ms");

			System.exit(0);
			
		} 
	}