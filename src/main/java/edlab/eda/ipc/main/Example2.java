package rbz.eda.generic.core.ipc.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import rbz.eda.generic.core.ipc.client.VirtuosoCommunicationHandler;
import rbz.eda.generic.core.ipc.protocol.MaxMessageLengthException;
import rbz.eda.generic.core.ipc.protocol.NotConnectedException;

public class Example2 {

	public static void main(String[] args) throws MaxMessageLengthException, NotConnectedException, InterruptedException, NumberFormatException, IOException {
		
		VirtuosoCommunicationHandler virtuosoCommunicationHandler = new VirtuosoCommunicationHandler();
			
		LinkedList<Rectangle> rectangles = new LinkedList<Rectangle>();
			
		long startTime = System.currentTimeMillis();
			
		virtuosoCommunicationHandler.sendSkillCommand("getStringFromLayout2(\"testLib\" \"simpleLayout\" \"layout\")");
			
		while(!virtuosoCommunicationHandler.hasSkillMessageFromVirtuoso()){
			Thread.sleep(10);
		}
		
		String path=virtuosoCommunicationHandler.getSkillMessage();
		path=path.substring(2, path.length()-2);
		
		System.out.println("Rectangle file @ \n"+path+"\n");
			
			
		BufferedReader  br = new BufferedReader(new FileReader(path));
		String line;
		
        while ((line = br.readLine()) != null) {
                
        	String [] stringRectangleArray = line.split(" ");
				
        	if (stringRectangleArray.length==6) {
        		rectangles.add(new Rectangle(new LayoutPoint(Integer.parseInt(stringRectangleArray[2]), 
        													 Integer.parseInt(stringRectangleArray[3])), 
        									 new LayoutPoint(Integer.parseInt(stringRectangleArray[4]), 
        											         Integer.parseInt(stringRectangleArray[5])), 
        									 stringRectangleArray[1]));
        	}
        }
        br.close();

        System.out.println("Numer of rectangles "+rectangles.size());

        long intermTime = System.currentTimeMillis();

        System.out.println("Time for pulling rectangles "+(intermTime-startTime)+" ms");

        @SuppressWarnings("resource")
		PrintWriter out = new PrintWriter(path);

        for (Rectangle rectangle : rectangles) {
			out.println(rectangle.getSkillCode());
			out.flush();
		}
            
		virtuosoCommunicationHandler.sendSkillCommand("writeRectanglesToLayout2(\"testLib\" \"simpleLayout\" \"layout_rtn\" \""+path+"\")");
			
		while(!virtuosoCommunicationHandler.hasSkillMessageFromVirtuoso()){
				Thread.sleep(10);
		}
		
		long stopTime = System.currentTimeMillis();
		
		System.out.println("Time for pushing rectangles "+ (stopTime-intermTime)+" ms");
		
		System.exit(0);
			
	}
}
