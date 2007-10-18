package net.anotheria.anosite.content.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;

import net.anotheria.anodoc.util.context.ContextManager;
import net.anotheria.anosite.gen.shared.service.AnoDocConfigurator;

import static net.anotheria.anosite.util.AnositeConstants.PARAM_LANGUAGE;
import static net.anotheria.anosite.util.AnositeConstants.SA_LANGUAGE;

public class LanguageFilter implements Filter{
	
	public void destroy() {
	}

	public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain chain) throws IOException, ServletException {
		if (!(rq instanceof HttpServletRequest)){
			chain.doFilter(rq, rs);
			return;
		}
		
		
		HttpServletRequest req = (HttpServletRequest)rq;
		HttpSession session = req.getSession();
		
		String pLang = req.getParameter(PARAM_LANGUAGE); 
		if (pLang!=null){
			session.setAttribute(SA_LANGUAGE, pLang);
		}
		
		String lang = (String)session.getAttribute(SA_LANGUAGE);
		if (lang != null){
			ContextManager.getCallContext().setCurrentLanguage(lang);
			//System.out.println("Setting current language to : "+lang);
		}else{
			lang = ContextManager.getCallContext().getCurrentLanguage();
		}
		
		Locale toSet = new Locale(lang);
		session.setAttribute(Action.LOCALE_KEY, toSet);
		session.setAttribute(SA_LANGUAGE, lang);
		
		chain.doFilter(rq, rs);
	}
	
	public void init(FilterConfig arg0) throws ServletException {
		AnoDocConfigurator.configure();
	}
}