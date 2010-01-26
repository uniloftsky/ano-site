package net.anotheria.anosite.content.filter;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.assitedata.data.PageAlias;
import net.anotheria.anosite.gen.assitedata.service.IASSiteDataService;
import net.anotheria.anosite.gen.aswebdata.data.Pagex;
import net.anotheria.anosite.gen.aswebdata.service.IASWebDataService;
import net.anotheria.anosite.gen.shared.service.AnoDocConfigurator;
import net.anotheria.asg.exception.ASGRuntimeException;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * This filter checks the incoming urls whether they are matching predefined page aliases. In case it matches the redirect to the target of the alias is issued. 
 * @author lrosenberg
 */
public class PageAliasFilter implements Filter{

	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(PageAliasFilter.class);

	/**
	 * WebDataService for pages retrieval.
	 */
	private IASWebDataService webDataService;
	/**
	 * SiteDataService for page aliases.
	 */
	private IASSiteDataService siteDataService;

	@Override public void destroy() {
	}

	@Override public void doFilter(ServletRequest sreq, ServletResponse sres, FilterChain chain) throws IOException, ServletException {
		if (!(sreq instanceof HttpServletRequest))
			return;
		HttpServletRequest req = (HttpServletRequest)sreq;
		req.setCharacterEncoding(AnoDocConfigurator.getEncoding());

		
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
					log.debug("found page alias hit "+path+" to page: "+alias.getTargetPage());
					Pagex target = webDataService.getPagex(alias.getTargetPage());
					String urlQuery = req.getQueryString();
					urlQuery = urlQuery != null && urlQuery.length() > 0? "?" + urlQuery:"";
					((HttpServletResponse)sres).sendRedirect("/"+target.getName()+".html" + urlQuery);
					return;
				}
			}
		}catch(ASGRuntimeException e){
			log.error("doFilter", e);
		}
		
		chain.doFilter(sreq, sres);


	}

	@Override public void init(FilterConfig config) throws ServletException {
		try {
			webDataService = MetaFactory.get(IASWebDataService.class);
			siteDataService = MetaFactory.get(IASSiteDataService.class);
		} catch (MetaFactoryException e) {
			log.fatal("ASG services init failure",e);
			throw new ServletException("ASG services init failure",e);
		}
	}

}
