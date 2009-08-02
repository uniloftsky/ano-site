package net.anotheria.anosite.content.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.anotheria.anosite.gen.shared.service.AnoDocConfigurator;
import net.anotheria.anosite.util.AnositeConstants;

public class EditModeSwitcherFilter implements Filter{
	
	public static final String PARAM_SWITCH_MODE = "pSwitchMode";
	public static final String PARAM_VALUE_EDIT_MODE = "editMode";
	public static final String PARAM_VALUE_VIEW_MODE = "viewMode";

	@Override public void destroy() {
		
	}

	@Override public void doFilter(ServletRequest sreq, ServletResponse sres, FilterChain chain) throws IOException, ServletException {
		if (!(sreq instanceof HttpServletRequest))
			return;
		HttpServletRequest req = (HttpServletRequest)sreq;
		req.setCharacterEncoding(AnoDocConfigurator.getEncoding());
		
		String p = req.getParameter(PARAM_SWITCH_MODE);
		if (p!=null && p.length()>0){
			if (p.equals(PARAM_VALUE_EDIT_MODE))
				req.getSession().setAttribute(AnositeConstants.SA_EDIT_MODE_FLAG, Boolean.TRUE);
			if (p.equals(PARAM_VALUE_VIEW_MODE))
				req.getSession().removeAttribute(AnositeConstants.SA_EDIT_MODE_FLAG);
		}
		
		chain.doFilter(sreq, sres);
	}

	@Override public void init(FilterConfig arg0) throws ServletException {
		
	}

}
