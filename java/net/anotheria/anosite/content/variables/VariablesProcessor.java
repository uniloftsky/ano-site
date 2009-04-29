package net.anotheria.anosite.content.variables;

import javax.servlet.http.HttpServletRequest;

public interface VariablesProcessor {
	public String replace(String prefix, String variable, String defValue, HttpServletRequest req);
}
