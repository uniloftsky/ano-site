package net.anotheria.anosite.api.validation;

public class ValidationError {
	private String field;
	private String message;
	private String cmsKey;
	
	public ValidationError(){
		
	}
	
	public ValidationError(String aField, String aCmsKey, String aMessage){
		field = aField;
		cmsKey = aCmsKey;
		message = aMessage;
	}
	
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCmsKey() {
		return cmsKey;
	}
	public void setCmsKey(String cmsKey) {
		this.cmsKey = cmsKey;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
}
