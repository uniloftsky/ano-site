package net.anotheria.anosite.tags.shared;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.ResponseUtils;

public class TagUtils {

	private static Logger log = Logger.getLogger(TagUtils.class);
	private static Map<String, Integer> scopes = new HashMap<String, Integer>();
	
	
	static {
		scopes.put("page", Integer.valueOf(PageContext.PAGE_SCOPE));
		scopes.put("request", Integer.valueOf(PageContext.REQUEST_SCOPE));
		scopes.put("session", Integer.valueOf(PageContext.SESSION_SCOPE));
		scopes.put("application", Integer.valueOf(PageContext.APPLICATION_SCOPE));
	}
	
	
	public static int getScope(String scopeName) throws JspException {
		Integer scope = scopes.get(scopeName.toLowerCase());

		if (scope == null) {
			throw new JspException("Unknown scope " + scopeName);
		}

		return scope.intValue();
    }
	
	
	public static Object lookup(PageContext pageContext, String scopeName, String beanName, String propertyName, String subPropertyName) throws JspException {
		
		Object bean = lookup(pageContext, scopeName, beanName);
		if (bean == null) {
			return null;
		}
        
		if (propertyName == null) {
			return bean;
		}

		try {
			Object property = PropertyUtils.getProperty(bean, propertyName);
			
			if(subPropertyName == null) {
				return property;
			}
			
			try {
				return PropertyUtils.getProperty(property, subPropertyName);
			} catch (Exception e) {
				log.error(e,e);
				throw new JspException("Could not read " + beanName + "." + propertyName + "." + subPropertyName, e);
			} 
		} catch (Exception e) {
			log.error(e,e);
			throw new JspException("Could not read " + beanName + "." + propertyName, e);
		} 
	}
	
	public static Object lookup(PageContext pageContext, String scopeName, String aBeanName) throws JspException {
		String beanName = aBeanName;
		if(beanName == null) {
			beanName = "box";
		}
		
		if (scopeName == null) {
			Object bean = pageContext.findAttribute(beanName);
			if(bean == null && log.isDebugEnabled()) {
				log.debug("Did not find " + beanName + " in any scope.");
			}
			return bean;
		}

		Object bean = pageContext.getAttribute(beanName, getScope(scopeName));
		if(bean == null && log.isDebugEnabled()) {
			log.debug("Did not find " + beanName + " in scope " + scopeName);
		}
		return bean;
	}
	
	public static void putAttribute(PageContext pageContext, String aScope, String anObjectName, Object anBean) throws JspException {
		if(aScope == null || aScope.length() == 0)
			aScope = "page";
		pageContext.setAttribute(anObjectName, anBean, getScope(aScope));
	}
	
	protected static void write(PageContext pageContext, String s) throws JspException{
		ResponseUtils.write(pageContext, s);		
	}
	
	protected static void writeLn(PageContext pageContext, String s) throws JspException{
		write(pageContext, s+"\n");		
	}

	protected static String quote(String s){
		return "\""+s+"\"";
	}

}

