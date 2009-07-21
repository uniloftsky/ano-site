package net.anotheria.anosite.content.variables;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * Basic processor for links to external resources like files or images.
 * @author another
 *
 */
public abstract class XLinkProcessor implements VariablesProcessor{

	private Logger log;
	
	public XLinkProcessor(){
		log = Logger.getLogger(this.getClass());
	}
	
	protected Logger getLog() {
		return log;
	}

	public String replace(String prefix, String variable, String defValue, HttpServletRequest req) {
		return req.getContextPath()+"/file/"+getFileName(variable);
	}
	
	protected abstract String getFileName(String variable);
	
}
