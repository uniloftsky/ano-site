package net.anotheria.anosite.api.common;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.anotheria.anosite.api.session.APISession;



public class APIContext {
	
	public static final Locale DEFAULT_LOCALE = new Locale("de", "DE");
	
	private Locale currentLocale;
	private APISession currentSession;
	private Map<String, Object> scope = new HashMap<String, Object>();
	
	public void reset(){
		currentLocale = null;
		currentSession = null;
		scope = new HashMap<String, Object>();
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
		return "session: "+currentSession+", locale: "+currentLocale.toString()+", scope contains "+scope.size()+" elements.";
	}

	
	////////////// END ////////////////////

	private static ThreadLocal<APIContext> apiContext = new ThreadLocal<APIContext>(){
		@Override
		protected synchronized APIContext initialValue(){
			return new APIContext();
		}
	};
	
	public static APIContext getCallContext(){
		return apiContext.get();
	}


}
