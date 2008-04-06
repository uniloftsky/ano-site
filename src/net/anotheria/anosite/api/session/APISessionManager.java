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
package net.anotheria.anosite.api.session;


import java.util.ArrayList;
import java.util.List;

import net.anotheria.util.IdCodeGenerator;
import net.java.dev.moskito.util.storage.Storage;
import net.java.dev.moskito.util.storage.StorageFactory;

import org.apache.log4j.Logger;


public class APISessionManager {
	
	private Storage<String, APISession> sessions;
	private Storage<String, String> referenceIds;
	
	private static APISessionManager instance;

	private List<APISessionManagerListener> listeners;
	protected Logger log;

	private APISessionManager(){
		sessions = new StorageFactory<String, APISession>().createConcurrentHashMapStorage("sessions");
		referenceIds = new StorageFactory<String, String>().createConcurrentHashMapStorage("session-refIds");
		log = Logger.getLogger(this.getClass());
		listeners = new ArrayList<APISessionManagerListener>();
	}
	
	public static synchronized APISessionManager getInstance() {
		if(instance == null) {
			instance = new APISessionManager();
		}
		return instance;
	}	
	public APISession createSession(String referenceId){
		APISession s = new APISessionImpl(IdCodeGenerator.generateCode(30));
		((APISessionImpl)s).setReferenceId(referenceId);
		sessions.put(s.getId(), s);
		referenceIds.put(referenceId, s.getId());
		log.debug("createSession, id=" + s.getId());
		return s;
	}
	
	public ArrayList<String> getReferenceIds() {
		ArrayList<String> ret = new ArrayList<String>(referenceIds.size());
		for ( String id : referenceIds.keySet() )
			ret.add(id);
		return ret;
	}
	
	public ArrayList<String> getSessionIds() {
		ArrayList<String> ret = new ArrayList<String>(sessions.size());
		for ( String id : sessions.keySet() )
			ret.add(id);
		return ret;
	}
	
	public APISession getSession(String id){
		return sessions.get(id);
	}
	
	public int getSessionCount(){
		return sessions.size();
	}
	
	public APISession getSessionByReferenceId(String aReferenceId){
		String sessionId; 
		sessionId = referenceIds.get(aReferenceId);
		if (sessionId==null)
			throw new RuntimeException("Can't find session for referenceId: "+aReferenceId);
		return getSession(sessionId);
	}
	
	public void destroyAPISessionByReferenceId(String referenceId){
		//System.out.println("DESTROY API SESSION BY REFERENCE CALLED: "+referenceId);
		APISession session = null;
		String sessionId = referenceIds.get(referenceId);
		if (sessionId!=null){
			session = sessions.remove(sessionId);			
			for (APISessionManagerListener listener : listeners)
				listener.apiSessionDestroyed(session);
		}
		if (session==null)
			log.info("HttpSession expired "+referenceId+", no api session connected");
		else
			log.info("HttpSession expired: "+referenceId+", ApiSessionId: "+session.getId());
		//System.out.println("DESTROY API SESSION FINISHED: "+referenceId+", "+sessionId+", "+session);
		/*if (session!=null)
			((APISessionImpl)session).dumpSession(System.out);*/
		if (session!=null) {
			((APISessionImpl)session).clear();
			referenceIds.remove(referenceId);
		}
	}
	
	public void destroyAPISessionBySessionId(String sessionId){
		APISession session = null;
		if (sessionId!=null){
			session = sessions.remove(sessionId);			
			for (APISessionManagerListener listener : listeners)
				listener.apiSessionDestroyed(session);
		}
		if (session!=null){
			((APISessionImpl)session).clear();
			referenceIds.remove(((APISessionImpl)session).getReferenceId());
		}
		
	}

	public void addAPISessionManagerListener(APISessionManagerListener listener){
		listeners.add(listener);
	}
}