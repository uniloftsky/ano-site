package net.anotheria.anosite.content.variables;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Variables processor for constants. Useful for html tags with colide with variables processor syntax for example &amp;nbsp; etc.
 * @author another
 *
 */
public class ConstantsProcessor implements VariablesProcessor{
	/**
	 * Map with constants.
	 */
	private static Map<String,String> constants = new HashMap<String, String>();
	static{
		constants.put("spacer", "&nbsp;");
		constants.put("euro", "&euro;");
		constants.put("copyright", "&#169;");
		constants.put("EUR", "&euro;");
		constants.put("CHF", "CHF");
		constants.put("form", "form");
		constants.put("input", "input");
		constants.put("textarea", "textarea");
		constants.put("lbrace", "{");
		constants.put("rbrace", "}");
		constants.put("greatThan", ">");
		constants.put("lessThan", "<");
		constants.put("gt", ">");
		constants.put("lt", "<");
		constants.put("rbrace", "}");
		constants.put("colon", ":");
		constants.put("semicolon", ";");
		constants.put("raquo", "&raquo;");
		constants.put("laquo", "&laquo;");
	}

	@Override public String replace(String prefix, String variable, String defValue, HttpServletRequest req) {
		String val = constants.get(variable);
		return val == null ? 
				"Unknown constants: "+variable : val;
	}
}
