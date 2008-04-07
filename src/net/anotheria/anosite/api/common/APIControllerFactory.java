package net.anotheria.anosite.api.common;

public class APIControllerFactory {
	private static APIController controller = new APIController();
	
	public static APIController getAPIController(){
		return controller;
	}
}
