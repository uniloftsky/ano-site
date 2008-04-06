package net.anotheria.anosite.api.common;

import java.util.Locale;

import net.anotheria.anosite.api.session.APISession;
import net.anotheria.anosite.api.session.PolicyHelper;

import org.apache.log4j.Logger;

public abstract class AbstractAPIImpl implements API{
	
	protected Logger log;
	
	private String ATTRIBUTE_PREFIX = getClass().getName()+".";
	private static APIConfig apiConfig;

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
	
	protected APIContext getContext(){
		return APIContext.getCallContext();
	}
	
	protected APISession getSession(){
		return getContext().getCurrentSession();
	}
	
	protected Locale getCurrentLocale(){
		return getContext().getCurrentLocale();
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
}
