package net.anotheria.anosite.content.variables;

import javax.servlet.http.HttpServletRequest;

public abstract class XLinkProcessor implements VariablesProcessor{

	public String replace(String prefix, String variable, String defValue, HttpServletRequest req) {
		return req.getContextPath()+"/file/"+getFileName(variable);
	}
	
	protected abstract String getFileName(String variable);
	
}
