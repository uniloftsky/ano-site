package net.anotheria.anosite.api.generic.login;

import net.anotheria.anosite.api.common.APIException;
/**
 * Can be thrown by a login/logout preprocessor.
 * @author lrosenberg
 */
public class ProcessorException extends APIException{
	/**
	 * Creates a new ProcessorException.
	 */
	public ProcessorException(){
		
	}
	
	/**
	 * Creates a new ProcessorException.
	 */
	public ProcessorException(String message){
		super(message);
	}
}
