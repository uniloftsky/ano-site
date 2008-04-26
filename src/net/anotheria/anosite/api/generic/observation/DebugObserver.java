/* ------------------------------------------------------------------------- *
$Source$
$Author$
$Date$
$Revision$


Copyright 2004-2005 by FriendScout24 GmbH, Munich, Germany.
All rights reserved.

This software is the confidential and proprietary information
of FriendScout24 GmbH. ("Confidential Information").  You
shall not disclose such Confidential Information and shall use
it only in accordance with the terms of the license agreement
you entered into with FriendScout24 GmbH.
See www.friendscout24.de for details.
** ------------------------------------------------------------------------- */
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
