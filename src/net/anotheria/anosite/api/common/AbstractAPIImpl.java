package net.anotheria.anosite.api.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.anotheria.anosite.api.session.APISession;
import net.anotheria.anosite.api.session.ContentAwareAttribute;
import net.anotheria.anosite.api.session.PolicyHelper;
import net.anotheria.anosite.api.validation.ValidationError;
import net.anotheria.anosite.api.validation.ValidationException;

import org.apache.log4j.Logger;

public abstract class AbstractAPIImpl implements API{
	
	protected Logger log;
	
	private String ATTRIBUTE_PREFIX = getClass().getName()+".";
	private static APIConfig apiConfig;
	
	public static final long HOUR = 1000L*60*60;
	public static final long DAY = HOUR*24;
	
	public static final long MINUTE = 1000L*60;

	protected AbstractAPIImpl(){
		log = Logger.getLogger(this.getClass());
		apiConfig = new APIConfig();
	}
	
	public void deInit() {
	}

	public void init() {
	}

	protected void setAttributeInSession(String name, Object attribute){
		getSession().setAttribute(getPrivateAttributeName(name), attribute);
	}
	
	protected void setAttributeInSession(String name, int policy, Object attribute){
		if (PolicyHelper.isAutoExpiring(policy))
			setAttributeInSession(name, policy, attribute, System.currentTimeMillis()+getExpirePeriodForAttribute(name));
		else
			getSession().setAttribute(getPrivateAttributeName(name), policy, attribute);
	}

	protected void setAttributeInSession(String name, int policy, Object attribute, long expiresWhen){
		getSession().setAttribute(getPrivateAttributeName(name), policy, attribute, expiresWhen);
	}

	protected void setAttributeInSession(String name, Object attribute, long expiresWhen){
		setAttributeInSession(name, APISession.POLICY_AUTOEXPIRE, attribute, expiresWhen);
	}

	protected Object getAttributeFromSession(String name){
		return getSession().getAttribute(getPrivateAttributeName(name));
	}
	
	public void removeAttributeFromSession(String key) {
		getSession().removeAttribute(getPrivateAttributeName(key));
	}
	
	private String getSessionAttributePrefix(){
		return ATTRIBUTE_PREFIX;
	}
	
	protected String getPrivateAttributeName(String name){
		return new StringBuilder(getSessionAttributePrefix()).append(name).toString();
	}
	
	protected APICallContext getCallContext(){
		return APICallContext.getCallContext();
	}
	
	protected APISession getSession(){
		return getCallContext().getCurrentSession();
	}
	
	protected Locale getCurrentLocale(){
		return getCallContext().getCurrentLocale();
	}
	
	protected String getCurrentUserId(){
		return getCallContext().getCurrentUserId();
	}
	
	
	protected long getExpirePeriodForAttribute(@SuppressWarnings("unused") String name){
		return getDefaultExpirePeriodForAttribute();
	}
	
	protected long getDefaultExpirePeriodForAttribute(){
		return APISession.DEFAULT_EXPIRE_PERIOD;
	}
	

	
	protected static APIConfig getApiConfig() {
		return apiConfig;
	}
	
	//////// VALIDATION /////
	protected void addValidationError(String field, String cmsKey, String description){
		addValidationError(new ValidationError(field, cmsKey, description));
	}
	
	protected void addValidationError(ValidationError error){
		APICallContext.getCallContext().addValidationError(error);
	}
	
	protected void checkValidationAndThrowException(){
		if (APICallContext.getCallContext().hasValidationErrors())
			throw new ValidationException(APICallContext.getCallContext().getValidationErrors());
	}
	
	/////////////
	public static<T> List<T> createContentCachingList(){
		return new ContentCachingList<T>();
	}
	
	
	
}

class ContentCachingList<T> extends ArrayList<T> implements ContentAwareAttribute{

	public boolean deleteOnChange() {
		return true;
	}

	public void notifyContentChange(String documentName, String documentId) {
		throw new RuntimeException("Not supported");
	}
	
}
