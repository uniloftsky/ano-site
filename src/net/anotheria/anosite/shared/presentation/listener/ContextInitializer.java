/*
 *	$Id$
 *
 *      (C)2007 LOYALTY PARTNER GMBH, ALL RIGHTS RESERVED
 *
 * 	THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF LOYALTY PARTNER GMBH.
 *	THE COPYRIGHT NOTICE ABOVE DOES NOT EVIDENCE ANY ACTUAL OR INTENDED
 *	PUBLICATION OF SUCH SOURCE CODE.
 */
package net.anotheria.anosite.shared.presentation.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.anotheria.util.Date;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;



/**
 * This listener performs the webapp initialization upon webserver start (or webapp hot-re-deploy).
 * @author lrosenberg
 * @created Feb 16, 2007
 */
public class ContextInitializer implements ServletContextListener{
	
	

	private static Logger log;
	/**
	 * Initializes log4j, configures MetaFactory.
	 */
	static {
		//System.out.println("new file: "+new File(".").getAbsolutePath());
		//PropertyConfigurator.configureAndWatch("webapps/ROOT/WEB-INF/classes/log4j.properties");
		BasicConfigurator.configure();
		//DOMConfigurator.configureAndWatch("webapps/ROOT/WEB-INF/classes/log4j.xml");
		log = Logger.getLogger(ContextInitializer.class);
	}
	
	public void contextDestroyed(ServletContextEvent event) {
		log.info("CONTEXT DESTROYED @ "+Date.currentDate());
		
	}

	public void contextInitialized(ServletContextEvent event) {
		log.info("CONTEXT INITIALIZED @ "+Date.currentDate());
	}
	
}
