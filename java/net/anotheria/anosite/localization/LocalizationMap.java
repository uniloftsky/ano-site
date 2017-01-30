package net.anotheria.anosite.localization;

import net.anotheria.anoplass.api.APICallContext;
import net.anotheria.anosite.gen.asresourcedata.data.LocalizationBundle;
import net.anotheria.util.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Storage for localization in different environment.<br/>
 * Will be rewritten with new Localization Framework.
 * 
 * @author denis
 */

public class LocalizationMap {

    /**
     * {@code log4j} {@link Logger}.
     */
    private static final Logger LOGGER = Logger.getLogger(LocalizationMap.class);

	/**
	 * The name under which the localization map is stored in the call context
	 * to allow access by the variable processors.
	 */
	public static final String CALL_CONTEXT_SCOPE_NAME = LocalizationMap.class.getName();
	private Map<String, String> localizationBundles = new HashMap<String, String>();
	
	private LocalizationMap() {
	}

	private String getPrivateKey(LocalizationEnvironment environment, String key) {
		return environment.name() + "_" + key.trim();
	}

	public String getMessage(String key) {
		String message = localizationBundles.get(getPrivateKey(LocalizationEnvironment.ACTION, key));
		if (message != null)
			return message;
		message = localizationBundles.get(getPrivateKey(LocalizationEnvironment.BOX, key));
		if (message != null)
			return message;
		message = localizationBundles.get(getPrivateKey(LocalizationEnvironment.PAGE, key));
		if (message != null)
			return message;
		message = localizationBundles.get(getPrivateKey(LocalizationEnvironment.TEMPLATE, key));
		if (message != null)
			return message;
		message = localizationBundles.get(getPrivateKey(LocalizationEnvironment.SITE, key));
		if (message != null)
			return message;
		return localizationBundles.get(getPrivateKey(LocalizationEnvironment.RESOURCES, key));
	}

	public void addLocalization(LocalizationEnvironment scope, Map<String, String> localization) {
		for (Entry<String, String> loc : localization.entrySet())
			localizationBundles.put(getPrivateKey(scope, loc.getKey()), loc.getValue());
	}

	public void addLocalizationBundles(LocalizationEnvironment scope, List<LocalizationBundle> bundles){
		for (LocalizationBundle bundle : bundles)
			addLocalizationBundle(scope, bundle);
	}

	public void addLocalizationBundle(LocalizationEnvironment scope, LocalizationBundle bundle){
		String toParse = bundle.getMessages();
		if(toParse == null){
			LOGGER.error("LocalizationBundle " + bundle.getId() + ": message is null!");
            return;
        }
		toParse = StringUtils.removeChar(toParse, '\r');
		String[] lines = StringUtils.tokenize(toParse, '\n');
		for (String l : lines) {
			if(StringUtils.isEmpty(l) || l.trim().startsWith("#"))
				continue;

			String[] message = null;

			if (l.contains("\\=")) {
				String escapedString = l.replace("\\=", "&#61;");
				message = StringUtils.tokenize(escapedString, '=');

				if (message.length != 2){
					LOGGER.warn("Invalid format of LocalizationBundle with id " + bundle.getId() + " in line: <" + l + ">. Expected line format: <key=message>");
					continue;
				}

				message[1] = message[1].replace("&#61;", "=");
			} else {
				message = StringUtils.tokenize(l, '=');
				if (message.length != 2){
					LOGGER.warn("Invalid format of LocalizationBundle with id " + bundle.getId() + " in line: <" + l + ">. Expected line format: <key=message>");
					continue;
				}
			}

			localizationBundles.put(getPrivateKey(scope, message[0]), message[1]);
		}
		
	}

	public static LocalizationMap getCurrentLocalizationMap() {
		LocalizationMap loc = (LocalizationMap) APICallContext.getCallContext().getAttribute(LocalizationMap.CALL_CONTEXT_SCOPE_NAME);
		if (loc != null)
			return loc;
		loc = new LocalizationMap();
		APICallContext.getCallContext().setAttribute(LocalizationMap.CALL_CONTEXT_SCOPE_NAME, loc);
		return loc;
	}
}
