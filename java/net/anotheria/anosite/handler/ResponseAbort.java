package net.anotheria.anosite.handler;

import net.anotheria.anosite.shared.InternalResponseCode;

public class ResponseAbort extends BoxHandlerResponse{
	private Exception cause;
	
	public ResponseAbort(){
		
	}
	
	public ResponseAbort(Exception aCause){
		cause = aCause;
	}

	@Override
	public InternalResponseCode getResponseCode() {
		return InternalResponseCode.ABORT;
	}
	
	public Exception getCause(){
		return cause;
	}
	
	public String getCauseMessage(){
		return cause == null ? null : cause.getMessage();
	}
	
	
	
}
