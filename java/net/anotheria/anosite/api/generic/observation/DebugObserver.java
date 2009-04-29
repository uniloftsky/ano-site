package net.anotheria.anosite.api.generic.observation;

import org.apache.log4j.Logger;

public class DebugObserver implements Observer{

	public void notifySubjectUpdatedForCurrentUser(SubjectUpdateEvent event) {
		debugOutEvent("currentUser", event);
	}

	public void notifySubjectUpdatedForUser(SubjectUpdateEvent event) {
		debugOutEvent("aUser", event);
		
	}
	
	private void debugOutEvent(String method, SubjectUpdateEvent event){
		Logger log = Logger.getLogger(this.getClass());
		log.debug("Subject Fired: "+method+", event: "+event);
	}
	
}
