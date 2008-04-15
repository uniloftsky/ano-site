package net.anotheria.anosite.api.session;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;


public class APISessionImpl implements APISession {

	private String id;
	private Map<String, AttributeWrapper> attributes;
	private String ipAddress;
	private String userAgent;
	private String referenceId;
	
	private String currentUserId;
	private String currentEditorId;
	
	private Map<String, Object> actionScope;
	
	private static Logger log;
	static {
		log = Logger.getLogger(APISessionImpl.class);
	}
	
	APISessionImpl(String anId){
		id = anId;
		attributes = new ConcurrentHashMap<String,AttributeWrapper>();
		actionScope = new ConcurrentHashMap<String, Object>();
	}

	public Object getAttribute(String key) {
		AttributeWrapper wrapper = attributes.get(key);
		return wrapper == null ? null : wrapper.getValue();
	}

	public String getId() {
		return id;
	}

	public void setAttribute(String key, int policy, Object value) {
		attributes.put(key, new AttributeWrapper(key, value, policy));
	}
	
	public void setAttribute(String key, int policy, Object value, long expiresWhen) {
		attributes.put(key, new AttributeWrapper(key, value, policy, expiresWhen));
	}

	public void setAttribute(String key, Object value) {
		setAttribute(key, APISession.POLICY_DEFAULT, value);
	}
	
	public void removeAttribute(String key){
		attributes.remove(key);
	}
	
	@Override
	public String toString() {
		return "Id: "+id+", attributes: "+attributes.size();
	}
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String anIpAddress) {
		String oldIpAdress = ipAddress;
		ipAddress = anIpAddress;
		if (oldIpAdress!=null && !(oldIpAdress.equals(ipAddress)))
			log.warn(this + " session switches ip from "+oldIpAdress+", to: "+ipAddress);
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String anUserAgent) {
		userAgent = anUserAgent;
	}

	void clear(){
		attributes.clear();
	}
	
	public void cleanupOnLogout() {
		for (AttributeWrapper w : getAttributes()){
			if (!PolicyHelper.isPolicySet(w.getPolicy(), POLICY_SURVIVE_LOGOUT))
				attributes.remove(w.getKey());
		}
	}
	
	Collection<AttributeWrapper> getAttributes(){
		return attributes.values();
	}

	void setAttributeWrapper(AttributeWrapper w){
		attributes.put(w.getKey(), w);
	}
		
	public void dumpSession(PrintStream out){
		out.println("API Session with id: "+getId()+", Attributes: "+attributes);
		for (AttributeWrapper a : attributes.values())
			System.out.println(a.getKey()+" = "+a.getValue());
	}

	public void dumpSession(Logger log){
		log.debug("API Session with id: "+getId()+", Attributes: "+attributes);
	}

	public String getReferenceId() {
    	return referenceId;
    }

	public void setReferenceId(String referenceId) {
    	this.referenceId = referenceId;
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
	
	public void addAttributeToActionScope(String name, Object attribute){
		actionScope.put(name, attribute);
	}
	
	public Map<String,Object> getActionScope(){
		return actionScope;
	}
	
	public void resetActionScope(){
		actionScope.clear();
	}

}

