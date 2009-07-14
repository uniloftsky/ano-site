package net.anotheria.anosite.api.common;

/**
 * Signals that an attempt to get Current User ID or any other data has failed cause no logged in User.
 * This exception will be thrown by the APISession.getCurrentUserId() and by the APICallContext.getCurrentUserId() and spread 
 * through the called APIs' "my" methods.
 *  
 * @author denis
 *
 */
public class NoLoggedInUserException extends APIException {

	private static final long serialVersionUID = 2952929048749260860L;

	public NoLoggedInUserException() {
		this("No logged in User found!");
	}

	public NoLoggedInUserException(String message) {
		super(message);
	}

	
}
