package net.anotheria.anosite.content.bean;

import java.util.HashMap;
import java.util.Map;

public class AttributeMap {
	private Map<String, AttributeBean> attributes;
	
	public static final String CALL_CONTEXT_SCOPE_NAME = AttributeMap.class.getName();
	
	public AttributeMap(){
		attributes = new HashMap<String, AttributeBean>();
	}
	
	public AttributeBean getAttribute(String key){
		return attributes.get(key);
	}
	
	public void setAttribute(AttributeBean b, boolean override){
		AttributeBean old = attributes.get(b.getKey());
		if (old == null || override)
			attributes.put(b.getKey(), b);
	}
	
	public void setAttribute(AttributeBean b){
		setAttribute(b, false);
	}
	
	public String toString(){
		return attributes.toString();
	}
}
