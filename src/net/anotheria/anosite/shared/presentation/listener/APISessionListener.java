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
package net.anotheria.anosite.shared.presentation.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import net.anotheria.anosite.api.session.APISessionManager;

import org.apache.log4j.Logger;

public class APISessionListener implements HttpSessionListener{

	public void sessionCreated(HttpSessionEvent event) {
		//System.out.println("Session CREATED: "+event.getSession().getId());
		
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		try{
			APISessionManager.getInstance().destroyAPISessionByReferenceId(event.getSession().getId());
		}catch(Exception e){
			Logger log = Logger.getLogger(this.getClass());
			log.error("APISessionManager.destroyAPISessionByReferenceId failed:", e);
		}
	}

}
