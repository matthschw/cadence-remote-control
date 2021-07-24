package rbz.eda.generic.core.ipc.protocol;

public class MaxMessageLengthException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public MaxMessageLengthException(String message){
		super(message);
	}

}
