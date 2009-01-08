package net.anotheria.@applicationName@.shared.presentation.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class ContextInitializer implements ServletContextListener {

	public void contextInitialized(ServletContextEvent event) {

//		Config cfg = new Config();
		configureAPI();
		System.out.println("--- @applicationName@ INITIALIZED --- ");
//		System.out.println("System configured as "+cfg.getSystemName());
	}
	
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("--- @applicationName@ DESTROYED --- ");
		 
	}
	
	private void configureAPI(){
		/*	Register projects API here. 
			Example:
			APIFinder.addAPIFactory(SomeProjectAPI.class, new SomeProjectAPIFactory());
		*/
	}
	
}
