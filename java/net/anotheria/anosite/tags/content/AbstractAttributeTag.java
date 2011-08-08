package net.anotheria.anosite.tags.content;

import javax.servlet.jsp.JspException;

import net.anotheria.anosite.content.bean.AttributeBean;
import net.anotheria.anosite.content.bean.AttributeMap;
import net.anotheria.tags.BaseTagSupport;

import org.apache.log4j.Logger;

/**
 * Writes the value for the attribute "name" of the current CMS Object(box/page/etc). If no value is specified, writes out the defaultValue.
 * @author dzhmud
 */
public abstract class AbstractAttributeTag extends BaseTagSupport {

	/**
	 * Attribute name.
	 */
	private String name;
	/**
	 * Default value (same as in VariableProcessors).
	 */
	private String defaultValue = "UNSET";
	/**
	 * Log-
	 */
	private static Logger log = Logger.getLogger(AbstractAttributeTag.class);

	/**
	 * Retrieves attributes from the corresponding object.
	 * @return {@link AttributeMap}
	 */
	protected abstract AttributeMap getAttributeMap();
	
	@Override public int doEndTag() throws JspException {

		try{
			AttributeMap attributes = getAttributeMap();
			AttributeBean bean = (AttributeBean) attributes.getAttribute(name);
			String value = bean == null ? defaultValue : bean.getValue();
			write(value);
		}catch(Exception e){
			log.error(this.getClass().getName() + ".doEndTag(), name: "+name, e);
			write(e.getMessage());
		}

		return SKIP_BODY;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
