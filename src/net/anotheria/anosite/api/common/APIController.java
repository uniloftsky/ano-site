/*******************************************************************************
 * -------------------------------------------------------------------------
 * $Source$ $Author$ $Date$ $Revision$
 * 
 * 
 * Copyright 2004-2005 by FriendScout24 GmbH, Munich, Germany. All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of
 * FriendScout24 GmbH. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with FriendScout24 GmbH. See
 * www.friendscout24.de for details.
 * ------------------------------------------------------------------------- *
 ******************************************************************************/
package net.anotheria.anosite.api.common;

import java.util.Collection;

import org.apache.log4j.Logger;

public class APIController {

	private static Logger	log;
	static {
		log = Logger.getLogger(APIController.class);
	}

	APIController() {
	}

	public void notifyShutdown() {
		info("API is going down ... ");
		Collection<API> apis = APIFinder.getAPIs();
		for (API api : apis) {
			try {
				api.deInit();
				info("API " + api + " is down.");
			} catch (Throwable t) {
				log.error("notifyShutdown", t);
			}
		}

	}

	public void notifyStart() {
	}

	private void info(String message) {
		if (log != null) {
			log.info(message);
		}
	}

}
