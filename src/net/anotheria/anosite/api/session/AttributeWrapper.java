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

import java.io.Serializable;

public class AttributeWrapper implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2709101589774990085L;
	
	private Object value;
	private int policy;
	private String key;
	private long expiryTimestamp;
	
	public AttributeWrapper(String aKey, Object aValue, int aPolicy){
		this(aKey, aValue, aPolicy, PolicyHelper.isAutoExpiring(aPolicy) ? System.currentTimeMillis()+APISession.DEFAULT_EXPIRE_PERIOD : 0L);
	}
	
	public AttributeWrapper(String aKey, Object aValue, int aPolicy, long expiresWhen){
		key = aKey;
		value = aValue;
		policy = aPolicy;
		expiryTimestamp = expiresWhen;
	}
	
	public int getPolicy() {
		return policy;
	}
	public void setPolicy(int aPolicy) {
		policy = aPolicy;
	}
	public Object getValue() {
		/*System.out.println("---Get called");
		System.out.println("\tisExpiring: "+isExpiring());
		System.out.println("\tisExpired: "+isExpired());
		System.out.println("\twill return: "+(isExpired() ? null : value));
		System.out.println("---");*/
		return isExpired() ? null : value;
	}
	public void setValue(Object aValue) {
		value = aValue;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String aKey) {
		key = aKey;
	}
	
	public boolean isExpiring(){
		return (policy & APISession.POLICY_AUTOEXPIRE) != 0;
	}
	
	public boolean isExpired(){
		//System.out.println("Remains: "+(expiryTimestamp-System.currentTimeMillis()));
		return isExpiring() && (System.currentTimeMillis() > expiryTimestamp); 
	}
	
	@Override
	public String toString(){
		return new StringBuilder("Key: ").append(getKey()).append(", Value: ").append(getValue()).append(", Policy: ").append(getPolicy()).toString();
	}
	
	public boolean isSerializable(){
		return value instanceof Serializable;
	}
}
