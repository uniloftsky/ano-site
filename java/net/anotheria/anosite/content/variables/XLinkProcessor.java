package net.anotheria.anosite.content.variables;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public abstract class XLinkProcessor implements VariablesProcessor{

	private Logger log;
	
	public XLinkProcessor(){
		log = Logger.getLogger(this.getClass());
	}
	
	public Logger getLog() {
		return log;
	}

	public String replace(String prefix, String variable, String defValue, HttpServletRequest req) {
		return req.getContextPath()+"/file/"+getFileName(variable);
	}
	
	protected abstract String getFileName(String variable);
	
}
