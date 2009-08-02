package net.anotheria.anosite.api.session;

/**
 * Implementations of this interface may are notified of changes to the list of 
 * active sessions APISessionManager. 
 */
public interface APISessionManagerListener {

	/**
	 * Notification that an api session will be invalidated.
	 * @param session
	 */
	void apiSessionDestroyed(APISession session);
	
}
