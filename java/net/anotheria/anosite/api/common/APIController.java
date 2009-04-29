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
