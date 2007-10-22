package net.anotheria.anosite.handler;

public abstract class BoxHandlerResponse {
	public abstract BoxHandlerResponseCode getResponseCode();
	
	public String toString(){
		return getResponseCode().toString();
	}
}
