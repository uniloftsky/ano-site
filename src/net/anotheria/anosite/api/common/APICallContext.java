package net.anotheria.anosite.api.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.anotheria.anosite.api.session.APISession;
import net.anotheria.anosite.api.validation.ValidationError;


/**
 * A context in which the api is executed. The context is assigned to the Thread (as ThreadLocal) and therefore don't need to be synchronized. 
 * However, the context is killed (or lost) as soon as the thread finishes execution.
 * Important: If you start a new thread it does NOT share the same context, and hence same session.
 * @author another
 *
 */
public class APICallContext {
	
	public static final Locale DEFAULT_LOCALE = new Locale("de", "DE");
	
	private Locale currentLocale;
	private APISession currentSession;
	private Map<String, Object> scope = new HashMap<String, Object>();
	
	private String currentUserId;
	private String currentEditorId;
	
	private List<ValidationError> validationErrors;
	
	private APICallContext(){
		reset();
	}
	
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

	/**
	 * Called upon association with a Thread, to remove all tracks of the previous service.
	 */
	public void reset(){
		currentLocale = DEFAULT_LOCALE;
		currentSession = null;
		scope = new HashMap<String, Object>();
		currentUserId = null;
		currentEditorId = null;
		validationErrors = new ArrayList<ValidationError>();
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
		return "User: " +
			(isMember() ? getCurrentUserId() : "guest")+
			" session: "+currentSession+", locale: "+currentLocale+
			", scope contains "+scope.size()+" elements.";
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
	
	public void addValidationError(ValidationError error){
		validationErrors.add(error);
	}
	
	public void addValidationErrors(List<ValidationError> errors){
		validationErrors.addAll(errors);
	}
	
	public List<ValidationError> getValidationErrors() {
		return validationErrors;
	}

	public void setValidationErrors(List<ValidationError> validationErrors) {
		this.validationErrors = validationErrors;
	}

	public boolean hasValidationErrors(){
		return validationErrors.size()>0;
	}
	
	/**
	 * Used by the api if you want to span a new thread but share the values with that thread. As soon as the parameters are shared 
	 * each thread works on its own copy which is not shared.
	 * @param anotherContext the APICallContext to copy from
	 */
	void copyFromAnotherContext(APICallContext anotherContext){
		System.out.println("copying from "+anotherContext+" into "+this);
		currentLocale = anotherContext.currentLocale;
		currentSession = anotherContext.currentSession;
		scope = new HashMap<String, Object>(); scope.putAll(anotherContext.scope);
		currentUserId = anotherContext.currentUserId;
		currentEditorId = anotherContext.currentEditorId;
		validationErrors = new ArrayList<ValidationError>(); validationErrors.addAll(anotherContext.validationErrors);
	}
	
	
	////////////// END ////////////////////
	

	private static InheritableThreadLocal<APICallContext> apiCallContext = new InheritableThreadLocal<APICallContext>(){
		@Override
		protected synchronized APICallContext initialValue(){
			return new APICallContext();
		}

		@Override
		protected APICallContext childValue(APICallContext parentValue) {
			APICallContext ret = new APICallContext();
			ret.copyFromAnotherContext(parentValue);
			return ret;
		}
	};
	
	public static APICallContext getCallContext(){
		return apiCallContext.get();
	}



}
