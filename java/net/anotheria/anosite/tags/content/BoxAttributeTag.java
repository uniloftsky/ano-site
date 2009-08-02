package net.anotheria.anosite.tags.content;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import net.anotheria.anosite.content.bean.AttributeBean;
import net.anotheria.anosite.content.bean.AttributeMap;
import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.tags.shared.BaseTagSupport;

/**
 * Writes the value for the attribute "name" of the current box. If no value is specified, writes out the defaultValue.
 * @author lrosenberg
 *
 */
public class BoxAttributeTag extends BaseTagSupport{
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
	private static Logger log = Logger.getLogger(BoxAttributeTag.class);

	@Override public int doEndTag() throws JspException {
		
		try{
			BoxBean box = (BoxBean) pageContext.getAttribute("box");
			AttributeMap attributes = box.getAttributes();
		
			AttributeBean bean = (AttributeBean) attributes.getAttribute(name);
			String value = bean == null ? defaultValue : bean.getValue();
			write(value);
		}catch(Exception e){
			log.error("doEndTag(), name: "+name, e);
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
