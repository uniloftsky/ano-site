package net.anotheria.anosite.content.variables;

import javax.servlet.http.HttpServletRequest;

import net.anotheria.anoplass.api.APICallContext;
import net.anotheria.anosite.util.AnositeConstants;
import net.anotheria.util.StringUtils;

/**
 * Variables processor for box translations.
 *
 * @author rpotopa
 */
public class BoxTranslationProcessor implements VariablesProcessor {

	/**
	 * Prefix.
	 */
	public static final String PREFIX = DefinitionPrefixes.PREFIX_BOX_TRANSLATIONS;


	@Override
	public String replace(String prefix, String variable, String defValue, HttpServletRequest req) {
		if (PREFIX.equals(prefix) && !StringUtils.isEmpty(variable)) {
			String value = (String) APICallContext.getCallContext().getAttribute(AnositeConstants.ACA_BOX_TRANSLATION_PREFIX + variable);
			return value != null ? value : "Missing translation: " + variable;
		}
		return defValue;
	}
}
