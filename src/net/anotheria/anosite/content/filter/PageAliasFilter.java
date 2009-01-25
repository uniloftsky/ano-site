package net.anotheria.anosite.content.filter;

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

import net.anotheria.anosite.gen.assitedata.data.PageAlias;
import net.anotheria.anosite.gen.assitedata.service.ASSiteDataServiceFactory;
import net.anotheria.anosite.gen.assitedata.service.IASSiteDataService;
import net.anotheria.anosite.gen.aswebdata.data.Pagex;
import net.anotheria.anosite.gen.aswebdata.service.ASWebDataServiceFactory;
import net.anotheria.anosite.gen.aswebdata.service.IASWebDataService;
import net.anotheria.asg.exception.ASGRuntimeException;

import org.apache.log4j.Logger;

public class PageAliasFilter implements Filter{

	private static Logger log = Logger.getLogger(PageAliasFilter.class);

	private IASWebDataService webDataService;
	private IASSiteDataService siteDataService;

	public void destroy() {
	}

	public void doFilter(ServletRequest sreq, ServletResponse sres, FilterChain chain) throws IOException, ServletException {
		if (!(sreq instanceof HttpServletRequest))
			return;
		HttpServletRequest req = (HttpServletRequest)sreq;

		
		String path = req.getServletPath();
		if (path==null || path.length()<2){
			chain.doFilter(sreq, sres);
			return ;
		}
		path = path.substring(1).toLowerCase();
		
		try{
			List<PageAlias> aliases = siteDataService.getPageAliass();
			for (PageAlias alias : aliases){
				if (alias.getPathes()!=null && alias.getPathes().indexOf(path)!=-1){
					log.info("found page alias hit "+path+" to page: "+alias.getTargetPage());
					Pagex target = webDataService.getPagex(alias.getTargetPage());
					((HttpServletResponse)sres).sendRedirect("/"+target.getName()+".html");
					return;
				}
			}
		}catch(ASGRuntimeException e){
			log.error("doFilter", e);
		}
		
		chain.doFilter(sreq, sres);


	}

	public void init(FilterConfig config) throws ServletException {
		webDataService = ASWebDataServiceFactory.createASWebDataService();
		siteDataService = ASSiteDataServiceFactory.createASSiteDataService();
	}

}
