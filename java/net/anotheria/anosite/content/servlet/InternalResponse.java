package net.anotheria.anosite.content.servlet;

import net.anotheria.anosite.handler.BoxHandlerResponse;
import net.anotheria.anosite.shared.InternalResponseCode;

public class InternalResponse {
	private InternalResponseCode code;
	
	public InternalResponse(){
		
	}
	
	public InternalResponse(InternalResponseCode aCode){
		code = aCode;
	}
	
	public InternalResponse(BoxHandlerResponse handlerResponse){
		code = handlerResponse.getResponseCode();
	}

	public InternalResponseCode getCode() {
		return code;
	}

	public void setCode(InternalResponseCode code) {
		this.code = code;
	}
	
	public boolean canContinue(){
		return code == InternalResponseCode.CONTINUE || code == InternalResponseCode.CONTINUE_AND_REDIRECT || code == InternalResponseCode.ERROR_AND_CONTINUE; 
	}
}
