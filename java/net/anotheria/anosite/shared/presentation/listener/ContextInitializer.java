package net.anotheria.anosite.shared.presentation.listener;

import net.anotheria.anodoc.service.LockHolder;
import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.anosite.blog.api.BlogAPI;
import net.anotheria.anosite.blog.api.BlogAPIFactory;
import net.anotheria.anosite.cms.helper.BoxHelperUtility;
import net.anotheria.anosite.config.Config;
import net.anotheria.anosite.wizard.api.WizardAPI;
import net.anotheria.anosite.wizard.api.WizardAPIFactory;
import net.anotheria.util.Date;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;



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
		DOMConfigurator.configureAndWatch("webapps/ROOT/WEB-INF/classes/log4j.xml");
		log = Logger.getLogger(ContextInitializer.class);
	}
	
	public void contextDestroyed(ServletContextEvent event) {
		log.info("CONTEXT DESTROYED @ "+Date.currentDate());
		
	}

	public void contextInitialized(ServletContextEvent event) {
		
		String myname = event.getServletContext().getContextPath()+" context ";
		
		log.info(myname+"CONTEXT INITIALIZED @ "+Date.currentDate());
		CMSSelfTest.performSelfTest();
		BoxHelperUtility.setup();
		
		Config cfg = Config.getInstance();
		log.info(myname+"configured as "+cfg.getSystemName());

		//configure API!
		log.info(myname+"Configure api");
		APIFinder.addAPIFactory(WizardAPI.class, new WizardAPIFactory());
		APIFinder.addAPIFactory(BlogAPI.class,new BlogAPIFactory());
		log.info(myname+"API configured");
		LockHolder.addShutdownHook();
		log.info(myname+"added shutdown hook");

	}
	
}
