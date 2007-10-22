package net.anotheria.anosite.handler;

public enum BoxHandlerResponseCode {
	/**
	 * Continue execution
	 */
	CONTINUE,
	/**
	 * Abort the exception, an exception should be thrown 
	 */
	ABORT,
	/**
	 * Send the redirect to the client instead of page rendering. The Redirect will be executed at the END of the 
	 * processing of all Handler. 
	 */
	CONTINUE_AND_REDIRECT,
	/**
	 * Cancel further execution and redirect immediately.
	 */
	CANCEL_AND_REDIRECT,
	
	
}
