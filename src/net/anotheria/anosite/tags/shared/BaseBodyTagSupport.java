package net.anotheria.anosite.tags.shared;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;


public class BaseBodyTagSupport extends BodyTagSupport{
	
	private String scope;
	private String id;
	private String name;
	private String property;
	private String subProperty;
	
	protected Object lookup() throws JspException{
		return TagUtils.lookup(pageContext, getScope(), getName(), getProperty(), getSubProperty());
	}
	
	protected void write(String s) throws JspException{
		TagUtils.write(pageContext, s);		
	}
	
	protected void writeLn(String s) throws JspException{
		TagUtils.write(pageContext, s+"\n");		
	}

	protected String quote(String s){
		return TagUtils.quote(s);
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getSubProperty() {
		return subProperty;
	}

	public void setSubProperty(String subProperty) {
		this.subProperty = subProperty;
	}

}
