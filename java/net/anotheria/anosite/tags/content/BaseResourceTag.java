package net.anotheria.anosite.tags.content;

import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import net.anotheria.anosite.gen.asresourcedata.data.TextResource;
import net.anotheria.anosite.gen.asresourcedata.service.ASResourceDataServiceFactory;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import net.anotheria.anosite.tags.shared.BaseTagSupport;
import net.anotheria.asg.exception.ASGRuntimeException;

/**
 * Base tag for handling resources in the jsp.
 * @author lrosenberg
 *
 */
public abstract class BaseResourceTag extends BaseTagSupport{
	/**
	 * Resource data service.
	 */
	private static IASResourceDataService service = ASResourceDataServiceFactory.createASResourceDataService();
	/**
	 * log.
	 */
	private static Logger log = Logger.getLogger(BaseResourceTag.class);
	
	protected static IASResourceDataService getResourceDataService(){
		return service;
	}
	/**
	 * Returns a text resource by its name.
	 * @param key
	 * @return
	 * @throws JspException
	 */
	protected TextResource getTextResourceByName(String key) throws JspException{
		try{
			List<TextResource> resources = getResourceDataService().getTextResourcesByProperty(TextResource.PROP_NAME, key);
			if (resources==null || resources.size()==0)
				return null;
			return resources.get(0);
		}catch(ASGRuntimeException e){
			log.error("getTextResourceByName("+key+")", e);
			throw new JspException(e);
		}
	}
}
