package net.anotheria.anosite.content.variables;

import javax.servlet.http.HttpServletRequest;

import net.anotheria.anoplass.api.APICallContext;
import net.anotheria.anosite.shared.AttributeConstants;
import net.anotheria.util.StringUtils;

/**
 * Variables processor for constants. Useful for html tags with colide with variables processor syntax for example &amp;nbsp; etc.
 *
 * @author another
 */
public class BoxTranslationProcessor implements VariablesProcessor {

	/**
	 * Prefix.
	 */
	public static final String PREFIX = DefinitionPrefixes.PREFIX_BOX_TRANSLATIONS;


	@Override
	public String replace(String prefix, String variable, String defValue, HttpServletRequest req) {
		if (PREFIX.equals(prefix) && !StringUtils.isEmpty(variable)) {
			String value = (String) APICallContext.getCallContext().getAttribute(AttributeConstants.ATTR_TRANSLATION_PREFIX + variable);
			return value != null ? value : "Missing key: " + variable;
		}
		return defValue;
	}
}
