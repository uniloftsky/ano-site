package net.anotheria.anosite.content.variables;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import net.anotheria.anoplass.api.APICallContext;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asresourcedata.data.TextResource;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import net.anotheria.anosite.tags.content.BaseResourceTag;
import net.anotheria.anosite.util.AnositeConstants;
import net.anotheria.asg.exception.ASGRuntimeException;
import net.anotheria.util.StringUtils;

/**
 * Variables processor for box translations.
 *
 * @author rpotopa
 */
public class LocalizationProcessor implements VariablesProcessor {

	/**
	 * Prefix.
	 */
	public static final String PREFIX = DefinitionPrefixes.PREFIX_LOCALIZATION_MESSAGE;
	
	/**
	 * Resource data service.
	 */
	private static IASResourceDataService service;
	/**
	 * log.
	 */
	private static Logger log = Logger.getLogger(BaseResourceTag.class);

	/**
	 * Init.
	 */
	static {
		try {
			service = MetaFactory.get(IASResourceDataService.class);
		} catch (MetaFactoryException e) {
			log.fatal("IASResourceDataService init failure",e);
		}
	}


	@Override
	public String replace(String prefix, String variable, String defValue, HttpServletRequest req) {
		if (PREFIX.equals(prefix) && !StringUtils.isEmpty(variable)) {
			String value = getLocalizationFromContext(variable);
			if(value != null)
				return value;
			value = getLocalizationFromResources(variable);
			return value != null ? value : "Missing translation: " + variable;
		}
		return defValue;
	}
	
	protected String getLocalizationFromContext(String key){
		return (String) APICallContext.getCallContext().getAttribute(AnositeConstants.ACA_LOCALIZATION_BUNDLE_PREFIX + key);
	}
	
	/**
	 * Returns a text resource by its name.
	 * @param key
	 * @return
	 * @throws JspException
	 */
	protected String getLocalizationFromResources(String key){
		try{
			List<TextResource> resources = service.getTextResourcesByProperty(TextResource.PROP_NAME, key);
			if (resources==null || resources.size()==0)
				return null;
			return resources.get(0).getValue();
		}catch(ASGRuntimeException e){
			log.error("getLocalizationFromResources("+key+")", e);
			return null;
		}
	}
}
