package net.anotheria.anosite.shared.presentation.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anosite.gen.assitedata.data.RedirectUrl;
import net.anotheria.anosite.gen.assitedata.service.ASSiteDataServiceException;
import net.anotheria.anosite.gen.assitedata.service.ASSiteDataServiceFactory;
import net.anotheria.anosite.gen.assitedata.service.IASSiteDataService;

import org.apache.log4j.Logger;

public class RedirectFilter implements Filter{
	private IASSiteDataService siteDataService;
	
	private static Logger log = Logger.getLogger(RedirectFilter.class);

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest sreq, ServletResponse sres,	FilterChain chain) throws IOException, ServletException {
		if (!(sreq instanceof HttpServletRequest))
			return;
		HttpServletRequest req = (HttpServletRequest)sreq;
		
		//build url.
		String url = req.getRequestURL().toString();
		String qs = req.getQueryString();
		if (qs!=null && qs.length()>0)
			url += qs;
		
		String redirectUrl = resolveRedirectUrl(url);
		if (redirectUrl==null){
			chain.doFilter(sreq, sres);
			return;
		}
		
		//abort execution, send a redirect
		log.info("Redirecting "+url+" to "+redirectUrl);
		HttpServletResponse res = (HttpServletResponse)sres;
		res.setHeader("Location", redirectUrl);
		res.sendError(301, "Moved permanently");
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		siteDataService = ASSiteDataServiceFactory.createASSiteDataService();
	}
	
	private String resolveRedirectUrl(String lookupUrl){
		try{
			List<RedirectUrl> urls = siteDataService.getRedirectUrls();
			for (RedirectUrl u : urls){
				if (u.getIn()!=null && u.getIn().equals(lookupUrl))
					return u.getOut();
			}
		}catch(ASSiteDataServiceException e){
			log.error("resolveRedirectUrl("+lookupUrl+")", e);
		}
		return null;
	}
}
 