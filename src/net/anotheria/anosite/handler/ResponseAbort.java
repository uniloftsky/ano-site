package net.anotheria.anosite.handler;

public class ResponseAbort extends BoxHandlerResponse{
	private Exception cause;
	
	public ResponseAbort(){
		
	}
	
	public ResponseAbort(Exception aCause){
		cause = aCause;
	}

	@Override
	public BoxHandlerResponseCode getResponseCode() {
		return BoxHandlerResponseCode.ABORT;
	}
	
	public Exception getCause(){
		return cause;
	}
	
	public String getCauseMessage(){
		return cause == null ? null : cause.getMessage();
	}
	
	
	
}
