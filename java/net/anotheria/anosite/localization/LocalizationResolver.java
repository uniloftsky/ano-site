package net.anotheria.anosite.localization;

import java.util.List;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asresourcedata.data.TextResource;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import net.anotheria.asg.exception.ASGRuntimeException;

import org.apache.log4j.Logger;

/**
 * Not real resolver but some workaround as localizations adder from
 * LocalizationMap and TextResources.<br/>
 * Will be rewritten with new Localization Framework.
 * 
 * @author denis
 */

public enum LocalizationResolver {

	INSTANCE;

	private static Logger log = Logger.getLogger(LocalizationResolver.class);
	private static IASResourceDataService resourceService;

	static {
		try {
			resourceService = MetaFactory.get(IASResourceDataService.class);
		} catch (MetaFactoryException e) {
			log.fatal("IASResourceDataService init failure", e);
		}
	}

	public String getLocalizedMessage(String key) {
		// Try to find resource with given key in LocalizationMap
		
		String message = LocalizationMap.getCurrentLocalizationMap().getMessage(key);
		if (message != null)
			return message;

		// Ok, resource in LocalizationMap not found. Loading from TextResources
		TextResource resource = getTextResourceByName(key);
		if (resource != null)
			message = resource.getValue();
		return message != null? message: "Missing key: " + key;
	}

	/**
	 * Returns a text resource by its name.
	 * 
	 * @param key
	 * @return
	 * @throws RuntimeException
	 *             on ResourceService failure
	 */
	protected TextResource getTextResourceByName(String key) throws RuntimeException {
		try {
			List<TextResource> resources = resourceService.getTextResourcesByProperty(TextResource.PROP_NAME, key);
			if (resources == null || resources.size() == 0)
				return null;
			return resources.get(0);
		} catch (ASGRuntimeException e) {
			log.error("Error while loading localizations from ResourceDataService: ", e);
			throw new RuntimeException("Error while loading localizations from ResourceDataService: ", e);
		}
	}
}
