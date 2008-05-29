package net.anotheria.anosite.handler;

import net.anotheria.anosite.shared.InternalResponseCode;

public class ResponseContinue extends BoxHandlerResponse{
	
	public static final BoxHandlerResponse INSTANCE = new ResponseContinue();
	
	public InternalResponseCode getResponseCode(){
		return InternalResponseCode.CONTINUE;
	}
}
