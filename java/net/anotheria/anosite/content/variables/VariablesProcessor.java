package net.anotheria.anosite.content.variables;

import javax.servlet.http.HttpServletRequest;

public interface VariablesProcessor {
	String replace(String prefix, String variable, String defValue, HttpServletRequest req);
}
