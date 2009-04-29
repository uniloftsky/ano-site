package net.anotheria.anosite.api.validation;

import java.util.List;

import net.anotheria.anosite.api.common.APIException;

public class ValidationException extends APIException{
	
	private List<ValidationError> errors;
	
	public ValidationException(List<ValidationError> someErrors){
		errors = someErrors;
	}
	
	public List<ValidationError> getErrors(){
		return errors;
	}
	
}
