package net.anotheria.anosite.handler;

import net.anotheria.anosite.shared.InternalResponseCode;
/**
 * Commands the called to abort the execution. This usually happens if the execution encountered and exception.
 * @author lrosenberg
 */
public class ResponseAbort extends BoxHandlerResponse{
	/**
	 * The underlying exception (if any).
	 */
	private Exception cause;
	/**
	 * Creates a new abort response.
	 */
	public ResponseAbort(){
		
	}
	/**
	 * Creates a new abort response with the given code.
	 * @param aCause
	 */
	public ResponseAbort(Exception aCause){
		cause = aCause;
	}

	@Override
	public InternalResponseCode getResponseCode() {
		return InternalResponseCode.ABORT;
	}
	/**
	 * Returns the cause of the abort (i.e. the exception).
	 * @return
	 */
	public Exception getCause(){
		return cause;
	}
	/**
	 * Returns the exception's message or null.
	 * @return
	 */
	public String getCauseMessage(){
		return cause == null ? null : cause.getMessage();
	}
	
	
	
}
