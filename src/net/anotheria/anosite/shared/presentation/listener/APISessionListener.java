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
