/* ------------------------------------------------------------------------- *
$Source$
$Author: another $
$Date: 2008-04-16 16:39:40 +0300 (Wed, 16 Apr 2008) $
$Revision: 1305 $


Copyright 2004-2005 by FriendScout24 GmbH, Munich, Germany.
All rights reserved.

This software is the confidential and proprietary information
of FriendScout24 GmbH. ("Confidential Information").  You
shall not disclose such Confidential Information and shall use
it only in accordance with the terms of the license agreement
you entered into with FriendScout24 GmbH.
See www.friendscout24.de for details.
** ------------------------------------------------------------------------- */
package net.anotheria.@applicationName@.shared.presentation.listener;

import java.util.Random;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionInitializer implements HttpSessionListener{

	public static final String ATTR_TRACKING_SESSION_ID = "TR_SESSIONID";
	
	private static Random rnd = new Random(System.currentTimeMillis());
	
	public void sessionCreated(HttpSessionEvent event) {
		event.getSession().setAttribute(ATTR_TRACKING_SESSION_ID, rnd.nextLong());
	}

	public void sessionDestroyed(HttpSessionEvent event) {
	}

}
