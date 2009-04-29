package net.anotheria.anosite.api.generic.login;

import net.anotheria.anosite.api.common.APIException;

public class ProcessorException extends APIException{
	public ProcessorException(){
		
	}
	
	public ProcessorException(String message){
		super(message);
	}
}
