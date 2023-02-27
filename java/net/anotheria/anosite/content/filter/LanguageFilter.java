package net.anotheria.anosite.content.filter;

import static net.anotheria.anosite.util.AnositeConstants.PARAM_LANGUAGE;
import static net.anotheria.anosite.util.AnositeConstants.SA_LANGUAGE;

import java.io.IOException;
import java.util.Locale;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import net.anotheria.anodoc.util.context.ContextManager;
import net.anotheria.anosite.gen.shared.service.AnoDocConfigurator;

public class LanguageFilter implements Filter{
	
	@Override public void destroy() {
	}

	@Override public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain chain) throws IOException, ServletException {
		if (!(rq instanceof HttpServletRequest)){
			chain.doFilter(rq, rs);
			return;
		}
		
	
		HttpServletRequest req = (HttpServletRequest)rq;
		req.setCharacterEncoding(AnoDocConfigurator.getEncoding());
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
		//TODO remove this?
		//session.setAttribute(Action.LOCALE_KEY, toSet);
		session.setAttribute(SA_LANGUAGE, lang);
		
		chain.doFilter(rq, rs);
	}
	
	@Override public void init(FilterConfig arg0) throws ServletException {
		AnoDocConfigurator.configure();
	}
}