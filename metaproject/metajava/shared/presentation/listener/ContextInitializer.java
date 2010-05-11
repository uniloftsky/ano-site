package net.anotheria.@applicationName@.shared.presentation.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import net.anotheria.anosite.gen.shared.service.MetaFactoryConfigurator;

import net.anotheria.anosite.config.Config;


public class ContextInitializer implements ServletContextListener {

	public void contextInitialized(ServletContextEvent event) {
		configureMetaFactory();
		configureAPI();
		System.out.println("--- @applicationName@ "+ Config.getInstance().getSystemName() +" INITIALIZED --- ");
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
	
	private void configureMetaFactory(){
		MetaFactoryConfigurator.configure();
	}
	
}
