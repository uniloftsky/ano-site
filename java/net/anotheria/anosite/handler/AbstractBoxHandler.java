package net.anotheria.anosite.handler;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.api.common.APICallContext;
import net.anotheria.anosite.api.session.APISessionImpl;
import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.gen.asresourcedata.service.ASResourceDataServiceFactory;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.anosite.handler.exception.BoxProcessException;
import net.anotheria.anosite.handler.exception.BoxSubmitException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Adapter style implementation of a boxhandler.
 * @author another
 *
 */
public abstract class AbstractBoxHandler implements BoxHandler{
	/**
	 * Log. Each subclass has indirect access to this log via getLog. The log is named by the subclass and created in the constructor.
	 */
	private Logger log;
	
	/**
	 * A resource service instance.
	 */
	private static IASResourceDataService resourceService;
	/**
	 * Init.
	 */
	static {
		try {
			resourceService = MetaFactory.get(IASResourceDataService.class);
		} catch (MetaFactoryException e) {
			Logger.getLogger(AbstractBoxHandler.class).fatal("IASResourceDataService init failure",e);
		}
	}
	
	/**
	 * Constructor for extending classes.
	 */
	protected AbstractBoxHandler(){
		log = Logger.getLogger(this.getClass());
	}

	/**
	 * Returns the log instance. This way each handler has a useable log.
	 * @return
	 */
	protected Logger getLog(){
		return log;
	}
	/**
	 * Returns ResponseContinue.INSTANCE.
	 */
	@Override public BoxHandlerResponse process(HttpServletRequest req, HttpServletResponse res, Box box, BoxBean bean)  throws BoxProcessException{
		return ResponseContinue.INSTANCE;
	}

	/**
	 * Returns ResponseContinue.INSTANCE.
	 */
	@Override public BoxHandlerResponse submit(HttpServletRequest req, HttpServletResponse res, Box box)  throws BoxSubmitException{
		return ResponseContinue.INSTANCE;
	}
	
	protected static IASResourceDataService getResourceDataService(){
		return resourceService;
	}


	/**
	 * Used to put attribute to current request till next request
	 * 
	 * @param name attribute name
	 * @param attribute attribute value
	 */
	protected void sendAttributeToPage(String name, Object attribute){
					((APISessionImpl)APICallContext.getCallContext().getCurrentSession()).addAttributeToActionScope(name, attribute);
	}
}
