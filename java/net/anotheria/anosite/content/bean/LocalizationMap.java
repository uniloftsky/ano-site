package net.anotheria.anosite.content.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LocalizationMap {

	/**
	 * The name under which the localization map is stored in the call context to allow access by the variable processors.
	 */
	public static final String CALL_CONTEXT_SCOPE_NAME = LocalizationMap.class.getName();
	
	private Map<String,String> localizationBundles = new HashMap<String, String>();
	
	private String getTemplatePrivateKey(String key){
		return "template_" + key;
	}
	
	private String getPagePrivateKey(String key){
		return "page_" + key;
	}
	
	private String getBoxPrivateKey(BoxBean box, String key){
		return "box" + box.getId() + "_" + key;
	}
	
	public String getMessage(BoxBean box, String key){
		String message = box != null? localizationBundles.get(getBoxPrivateKey(box, key)): null; 
		if(message != null)
			return message;
		message = localizationBundles.get(getPagePrivateKey(key));
		if(message != null)
			return message;
		return localizationBundles.get(getTemplatePrivateKey(key));
	}
	
	public void addTemplateLocalization(Map<String,String> localization){
		for(Entry<String,String> loc: localization.entrySet())
			localizationBundles.put(getTemplatePrivateKey(loc.getKey()), loc.getValue());
	}
	
	public void addPageLocalization(Map<String,String> localization){
		for(Entry<String,String> loc: localization.entrySet())
			localizationBundles.put(getPagePrivateKey(loc.getKey()), loc.getValue());
	}
	
	public void addBoxLocalization(BoxBean box, Map<String,String> localization){
		for(Entry<String,String> loc: localization.entrySet())
			localizationBundles.put(getBoxPrivateKey(box, loc.getKey()), loc.getValue());
	}
}
