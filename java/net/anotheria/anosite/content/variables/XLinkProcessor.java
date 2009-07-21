package net.anotheria.anosite.content.variables;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * Basic processor for links to external resources like files or images.
 * @author lrosenberg
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

	@Override public String replace(final String prefix,final  String variable,final  String defValue,final  HttpServletRequest req) {
		return req.getContextPath()+"/file/"+getFileName(variable);
	}
	/**
	 * Returns the filename corresponding with the variable.
	 * @param variable
	 * @return
	 */
	protected abstract String getFileName(String variable);
	
}
