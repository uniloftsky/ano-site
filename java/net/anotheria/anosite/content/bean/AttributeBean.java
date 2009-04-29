package net.anotheria.anosite.content.bean;

public class AttributeBean {
	private String key;
	private String name;
	private String value;
	
	public AttributeBean(){
		
	}
	
	public AttributeBean(String aKey, String aName, String aValue){
		key = aKey;
		name = aName;
		value = aValue;
	}
	
	public String toString(){
		return getName()+": "+getKey()+" = "+getValue();
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
