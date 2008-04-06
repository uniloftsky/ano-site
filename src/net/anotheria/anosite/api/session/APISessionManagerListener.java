/* ------------------------------------------------------------------------- *
$HeadURL$
$Author$
$Date$
$Revision$


Copyright 2004-2007 by FriendScout24 GmbH, Munich, Germany.
All rights reserved.

This software is the confidential and proprietary information
of FriendScout24 GmbH. ("Confidential Information").  You
shall not disclose such Confidential Information and shall use
it only in accordance with the terms of the license agreement
you entered into with FriendScout24 GmbH.
See www.friendscout24.de for details.
** ------------------------------------------------------------------------- */
package net.anotheria.anosite.api.session;

/**
 * Implementations of this interface may are notified of changes to the list of 
 * active sessions APISessionManager. 
 * 
 * @author azent
 * @created on Aug 27, 2007
 */
public interface APISessionManagerListener {

	/**
	 * Notification that an api session will be invalidated.
	 * 
	 * @param session
	 */
	void apiSessionDestroyed(APISession session);
	
}
