package rbz.eda.generic.core.ipc.protocol;


public final class MessageProtocol {
	
	public static final char DELIMITER='$';
	
	public static final int ID_LENGTH=3;
	
	public static final String ID_SKILL="$S$";
	public static final String ID_STRING="$T$";
	
	public static final String CMD_VIRTUOSO_DATA ="$D$";
	public static final String CMD_VIRTUOSO_AVAIL ="$A$";
	public static final String CMD_VIRTUOSO_LOG="$L$";
	
	public static final int MAX_MSG_LENGTH=8191;
	
	public static final int VIRTUOSO_SKILL_IN=3;
	public static final int VIRTUOSO_SKILL_OUT=4;
	
	public static final int TIMEOUT=1; // in milliseconds
	
	public static final int ATTEMPTS_TO_INSTANTIATE_CONNECTION=10;
	public static final int TIMEOUT_TO_INSTANTIATE_CONNECTION=1000;
	
	
	
	/**
	 * This class method is only implemented for development. It returns a vaild socketID for the unix-users schweikardt, leber and maroltd
	 * @return socketID
	 */
	public static int getSocketID(){
		
		int socketID=0;
		
		switch (System.getProperty("user.name")) {
			
			case "schweikardt": socketID=4001; break;
			case "leber": socketID=4002; break;
			case "maroltd": socketID=4003; break;
			case "uhlmannyd": socketID=4004; break;
			case "tekoua": socketID=4005; break;
			case "danilowm": socketID=4006; break;
			case "borisov": socketID=4007; break;
			case "wolfert": socketID=4008; break;
			default:break;
		}
		return socketID;
	}
	
	/**
	 * This escaspes the message String such that the delimiter sequence (twice the delimiter) never occurs in the message string
	 * @param message
	 * @return
	 */
	public static String escapeMessage(String message){

		if(message.contains(Character.toString(DELIMITER))){
			int i=0;
			char loopChar;
			boolean detect=false;
			String res="";
			
			while (i < message.length()) {
				
				loopChar=message.charAt(i);
				
				if(loopChar==DELIMITER){
					
					if(detect){
						res=res+" ";
						detect=false;
					}else{
						detect=true;
						i++;
						res=res+loopChar;
					}
					
				}else{
					
					if(!(detect&&loopChar==' ')){
						detect=false;
					}
					
					i++;
					res=res+loopChar;
				}
			}
			
			return res;
			
		}else{
			
			return message;
		}
	}
	
	/**
	 * This method is the inverse to the method escapeMessage(String message)
	 * @param message
	 * @return
	 */
	public static String unescapeMessage(String message){
		
		if(message.contains(Character.toString(DELIMITER))){
			
			int i=0;
			char loopChar;
			boolean detect=false;
			String res="";
			
			while (i < message.length()) {
				
				loopChar=message.charAt(i);
				
				if(loopChar==DELIMITER){
					
					if(detect){
						detect=false;
						res=res.substring(0,res.length()-1);
					}else{
						detect=true;
						i++;
						res=res+loopChar;
					}
					
				}else{
					
					if(!(detect&&loopChar==' ')){
						detect=false;
					}
					
					i++;
					res=res+loopChar;
				}
			}
			
			return res;
			
		}else{
			
			return message;
		}
	}
	
	/**
	 * This method returns the delimiter sequence
	 * @return 
	 */
	public static String getDelimiterSequence(){
		
		return Character.toString(DELIMITER)+Character.toString(DELIMITER);
	}
	
	/**
	 * This method returns the delimiter sequence in regex formatting
	 * @return
	 */
	public static String getDelimiterSequenceRegex(){
		
		return "\\Q"+Character.toString(DELIMITER)+Character.toString(DELIMITER)+"\\E";
	}
}