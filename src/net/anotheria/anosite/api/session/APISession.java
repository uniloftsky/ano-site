package net.anotheria.anosite.api.session;

public interface APISession {
	
	public static final long DEFAULT_EXPIRE_PERIOD = 1000L*60*5; // 5minutes
	
	/**
	 * This policy states that the attribute is for local use only and shouldn't be distributed.
	 */
	public int POLICY_LOCAL = 1;
	/**
	 * This policy states that the attribute will be distributed to other servers. Only useable with 
	 * Serializable attributes. <b>This feature isn't yet implemented</b>
	 */
	public int POLICY_DISTRIBUTED = 2;

	/**
	 * This policy states that the attribute will survice system or server restart. Only useable with 
	 * Serializable attributes. <b>This feature isn't yet implemented</b>
	 */
	public int POLICY_PERSISTENT = 4;
	/**
	 * If set this policy defines that after the specified expire period the attribute will be reseted and not visible 
	 * to the caller. The attribute itself will not be deleted until explicitely overwritten or removed, so don't use this
	 * policy on timer-bound attributes, since that can lead to unexpected behaviour. However, you shouldn't put timer-bound
	 * attributes in the session either way.
	 */
	public int POLICY_AUTOEXPIRE = 8;
	
	/**
	 * This policy ensures that instead of making a new AttributeWrapper, the old one will be reused (if available).
	 * This is important if you want to keep some wrapper attributes like autoexpire. If you put an attribute with an 
	 * autoexpire policy on but without the reuse_wrapper policy, the autoexpiring will be reset on each setAttribute call.
	 * If you want to keep a 'global' autoexpiring.
	 */
	public int POLICY_REUSE_WRAPPER = 16;
	
	/**
	 * This policy enables the attribute to survive the session clean-up on logout.
	 */
	public int POLICY_SURVIVE_LOGOUT = 32;

	/**
	 * This policy enables the attribute to survive the session clean-up on login.
	 */
	public int POLICY_SURVIVE_LOGIN = 64;
	
	public int POLICY_DEFAULT = POLICY_LOCAL;
	
	public static final String ATTRIBUTE_GUEST_SESSION_ID = "__guestSessionId";
	
	public void setAttribute(String key, Object value);
	
	public void setAttribute(String key, int policy, Object value);
	
	public void setAttribute(String key, int policy, Object value, long expiresWhen);

	public Object getAttribute(String key);
	
	public void removeAttribute(String key);
	
	public String getId();
	
	public String getIpAddress();
	
	public void setIpAddress(String anIpAddress);
	
	public String getUserAgent();
	
	public void setUserAgent(String anUserAgent);
	
	public void cleanupOnLogout();
	
	public void cleanupOnLogin();
	

	public String getCurrentUserId(); 
	
	public String getCurrentEditorId();
}