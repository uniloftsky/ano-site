package net.anotheria.anosite.content.variables;

import javax.servlet.http.HttpServletRequest;

import net.anotheria.anosite.api.common.APICallContext;
import net.anotheria.anosite.content.bean.AttributeMap;

public class AttributeProcessor implements VariablesProcessor {

	public String replace(String prefix, String variable, String defValue, HttpServletRequest req) {
		Object ret = null;
		if("NONE".equals(defValue))
			defValue = "";
		if (prefix.equals(DefinitionPrefixes.PREFIX_API_CALL_CONTEXT_ATTRIBUTE))
			ret = APICallContext.getCallContext().getAttribute(variable);
		if (prefix.equals(DefinitionPrefixes.PREFIX_API_SESSION_ATTRIBUTE))
			ret = APICallContext.getCallContext().getCurrentSession().getAttribute(variable);
		if (prefix.equals(DefinitionPrefixes.PREFIX_REQUEST_ATTRIBUTE))
			ret = req.getAttribute(variable);
		if (prefix.equals(DefinitionPrefixes.PREFIX_SESSION_ATTRIBUTE))
			ret = req.getSession().getAttribute(variable);
		if (prefix.equals(DefinitionPrefixes.PREFIX_SESSION_AND_DELETE_ATTRIBUTE)){
			ret = req.getSession().getAttribute(variable);
			req.getSession().removeAttribute(variable);
		}
		if (prefix.equals(DefinitionPrefixes.PREFIX_CONTEXT_ATTRIBUTE))
			ret = req.getSession().getServletContext().getAttribute(variable);
		
		if (DefinitionPrefixes.PREFIX_BOX_ATTRIBUTE.equals(prefix))
			ret = ((AttributeMap)APICallContext.getCallContext().getAttribute(AttributeMap.CALL_CONTEXT_SCOPE_NAME)).getAttribute(variable).getValue();
		
		return ret == null ? defValue : ret.toString();
	}
	
}
