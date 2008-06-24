package net.anotheria.anosite.content.variables;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ConstantsProcessor implements VariablesProcessor{
	
	private static Map<String,String> constants = new HashMap<String, String>();
	static{
		constants.put("spacer", "&nbsp;");
		constants.put("euro", "&euro;");
		constants.put("EUR", "&euro;");
		constants.put("CHF", "CHF");
		constants.put("form", "form");
		constants.put("input", "input");
		constants.put("textarea", "textarea");
		constants.put("lbrace", "{");
		constants.put("rbrace", "}");
		constants.put("colon", ":");
		constants.put("semicolon", ";");
	}

	public String replace(String prefix, String variable, String defValue, HttpServletRequest req) {
		String val = constants.get(variable);
		return val == null ? 
				"Unknown constants: "+variable : val;
	}

}
