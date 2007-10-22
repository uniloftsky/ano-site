package net.anotheria.anosite.handler;

import net.anotheria.anosite.shared.InternalResponseCode;

public class ResponseContinue extends BoxHandlerResponse{
	public InternalResponseCode getResponseCode(){
		return InternalResponseCode.CONTINUE;
	}
}
