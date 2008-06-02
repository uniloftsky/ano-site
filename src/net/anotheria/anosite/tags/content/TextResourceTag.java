package net.anotheria.anosite.tags.content;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import net.anotheria.anosite.gen.asresourcedata.data.TextResource;
import net.anotheria.anosite.util.AnositeConstants;

public class TextResourceTag extends BaseResourceTag{
	
	
	private String key;
	
	public int doEndTag() throws JspException {
		
		String myKey = null;
		if (key!=null){
			myKey = key;
		}else{
			myKey = (String)lookup();
		}
		
		boolean editable = false;
		String txt = null;
		TextResource resource = getTextResourceByName(myKey);
		if (resource==null)
			txt = "Missing key: "+myKey;
		else
			txt = resource.getValue();

		HttpSession session = pageContext.getSession();
		if (session!=null && resource!=null && session.getAttribute(AnositeConstants.SA_EDIT_MODE_FLAG)!=null)
			editable = true;

		if (editable){
			String link = "<a href="+quote("cms/textresourceEdit?ts="+System.currentTimeMillis()+"&pId="+resource.getId())+" target="+quote("_blank")+">E</a>&nbsp;";
			write(link);
		}
		write(txt);
		
		
		return SKIP_BODY;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
