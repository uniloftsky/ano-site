package net.anotheria.anosite.api.validation;

public class ValidationError {
	private String message;
	private String cmsKey;
	
	public ValidationError(){
		
	}
	
	public ValidationError(String aCmsKey, String aMessage){
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
}
