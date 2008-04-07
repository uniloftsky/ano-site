package net.anotheria.anosite.api.common;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.anotheria.anosite.api.session.APISession;



public class APICallContext {
	
	public static final Locale DEFAULT_LOCALE = new Locale("de", "DE");
	
	private Locale currentLocale;
	private APISession currentSession;
	private Map<String, Object> scope = new HashMap<String, Object>();
	
	private String currentUserId;
	private String currentEditorId;
	
	
	public String getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}

	public String getCurrentEditorId() {
		return currentEditorId;
	}

	public void setCurrentEditorId(String currentEditorId) {
		this.currentEditorId = currentEditorId;
	}

	public void reset(){
		currentLocale = null;
		currentSession = null;
		scope = new HashMap<String, Object>();
		currentUserId = null;
		currentEditorId = null;
	}

	public void setAttribute(String name, Object value) {
		scope.put(name, value);
	}
	
	public Object getAttribute(String name) {
		return scope.get(name);
	}
	
	public void removeAttribute(String name) {
		scope.remove(name);
	}
	
	public Locale getCurrentLocale() {
		return currentLocale == null ? DEFAULT_LOCALE : currentLocale;
	}

	public void setCurrentLocale(Locale aCurrentLocale) {
		currentLocale = aCurrentLocale;
	}
	

	public APISession getCurrentSession() {
		return currentSession;
	}

	public void setCurrentSession(APISession aCurrentSession) {
		currentSession = aCurrentSession;
	}
	
	
	@Override
	public String toString(){
		return "User: " +(isMember() ? getCurrentUserId() : "guest")+" session: "+currentSession+", locale: "+currentLocale.toString()+", scope contains "+scope.size()+" elements.";
	}
	
	public boolean isGuest(){
		return !isMember();
	}
	
	public boolean isMember(){
		return currentUserId!=null;
	}
	
	public boolean isEditor(){
		return currentEditorId != null;
	}

	
	////////////// END ////////////////////

	private static ThreadLocal<APICallContext> apiCallContext = new ThreadLocal<APICallContext>(){
		@Override
		protected synchronized APICallContext initialValue(){
			return new APICallContext();
		}
	};
	
	public static APICallContext getCallContext(){
		return apiCallContext.get();
	}


}
