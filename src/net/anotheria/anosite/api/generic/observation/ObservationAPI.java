package net.anotheria.anosite.api.generic.observation;

import net.anotheria.anosite.api.common.API;

public interface ObservationAPI extends API{
	
	public void registerObserver(Observer observer, String... subject);

	public void unRegisterObserver(Observer observer, String... subject);

	/**
	 * Fire an update event for the current user. Current-User updates are important for clearing of session-bound caching in 
	 * "My" methods. 
	 * @param subject the subject which was updated.
	 * @param originator the originator of the event, typically the class name, but feel free.
	 */
	public void fireSubjectUpdateForCurrentUser(String subject, String originator);
	
	/**
	 * Fire an update event for a user. Those kind of updates are important for clearing of not-session-bound caching in
	 * the api (or whenever). An example is profilepage caching, or favorite caching.
	 * @param subject the subject which was updated.
	 * @param originator the originator of the event, typically the class name, but feel free.
	 * @param userId the userId of the updated user.
	 */
	public void fireSubjectUpdateForUser(String subject, String originator, String userId);

}
