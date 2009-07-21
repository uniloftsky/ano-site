package net.anotheria.anosite.api.common;

/**
 * Base exception class for all exceptions thrown by the api.
 * @author lrosenberg
 * 
 */
//TODO this exception have to be a checked exception.
public class APIException extends RuntimeException{

	private static final long serialVersionUID = -8378944843859795925L;

	public APIException(){
		
	}
	
	public APIException(String message){
		super(message);
	}
	
	public APIException(String message, Exception cause){
		super(message, cause);
	}
}
