package net.anotheria.anosite.api.common;

public class NoSuchAPIException extends RuntimeException{

	public NoSuchAPIException(String apiName){
		super("API "+apiName+" not found.");
	}
}
