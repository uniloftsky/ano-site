package net.anotheria.anosite.handler;

import net.anotheria.anosite.shared.InternalResponseCode;

public abstract class BoxHandlerResponse {
	public abstract InternalResponseCode getResponseCode();
	
	public String toString(){
		return getResponseCode().toString();
	}
}
