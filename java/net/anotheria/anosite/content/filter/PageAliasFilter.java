package net.anotheria.anosite.content.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.assitedata.data.PageAlias;
import net.anotheria.anosite.gen.assitedata.service.IASSiteDataService;
import net.anotheria.anosite.gen.aswebdata.data.Pagex;
import net.anotheria.anosite.gen.aswebdata.service.IASWebDataService;
import net.anotheria.anosite.gen.shared.data.PageAliasTypeEnum;
import net.anotheria.asg.exception.ASGRuntimeException;
import net.anotheria.util.StringUtils;

import org.apache.log4j.Logger;

/**
 * This filter checks the incoming urls whether they are matching predefined page aliases. 
 * In case it matches the redirect to the target of the alias is issued.
 *  
 * @author lrosenberg
 */
public class PageAliasFilter implements Filter{
	
	private static class PathEntity{
		String name;
		boolean convertable;
		
		public PathEntity(String aName, boolean aConvertable) {
			name = aName;
			convertable = aConvertable;
		}
		
		public String getName() {
			return name;
		}

		public boolean isConvertable() {
			return convertable;
		}

		public boolean conform(PathEntity other){
			return convertable || name.equals(other.name);
		}

		@Override
		public String toString() {
			return "PathEntity [name=" + name + ", convertable=" + convertable + "]";
		}
	}

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

	@Override public void init(FilterConfig config) throws ServletException {
		try {
			webDataService = MetaFactory.get(IASWebDataService.class);
			siteDataService = MetaFactory.get(IASSiteDataService.class);
		} catch (MetaFactoryException e) {
			log.fatal("ASG services init failure",e);
			throw new ServletException("ASG services init failure",e);
		}
	}
	
	@Override public void destroy() {
	}

	@Override public void doFilter(ServletRequest sreq, ServletResponse sres, FilterChain chain) throws IOException, ServletException {
		if (!(sreq instanceof HttpServletRequest))
			return;
		HttpServletRequest req = (HttpServletRequest)sreq;

		String pathStr = req.getServletPath();
		log.info("Resolving alias for path: " + pathStr);
		
		if (pathStr==null || pathStr.length()<2){
			chain.doFilter(sreq, sres);
			return ;
		}
		
		List<PathEntity> path = parsePath(pathStr);
		try{
			List<PageAlias> aliases = siteDataService.getPageAliass();
			for (PageAlias alias : aliases){
				for(String aliasPathStr : alias.getPathes()){
					List<PathEntity> aliasPath = parsePath(aliasPathStr);
					if(!matchPaths(aliasPath, path))
						continue;
					log.info("found page alias hit "+pathStr+" to page: "+alias.getTargetPage());
					Pagex target = webDataService.getPagex(alias.getTargetPage());
					
					String urlQuery = convertPathToQuery(aliasPath, path) + "&" + alias.getParameters().trim();
					log.info("Query: "+urlQuery);
					if(StringUtils.isEmpty(urlQuery))
						urlQuery = req.getQueryString();
					
					if(StringUtils.isEmpty(urlQuery)){
						urlQuery = "";
					}else{
						if(!urlQuery.startsWith("?"))
							urlQuery = "?" + urlQuery;						
					}
					
					
					
					String targetUrl = target.getName()+".html" + urlQuery;
					log.info("Alias: " + targetUrl);
					
					PageAliasTypeEnum command = PageAliasTypeEnum.getConstantByValue(alias.getType());
					switch (command) {
					case MASK:
						req.getRequestDispatcher("/" + targetUrl).forward(sreq, sres);
						return;
					case REDIRECT:
						((HttpServletResponse)sres).sendRedirect(req.getContextPath() + "/" + targetUrl);
						return;
					default:
						//Back compatibility with old versions
						((HttpServletResponse)sres).sendRedirect(req.getContextPath() + "/" + targetUrl);
						return;
					}
					
				}
			}
		}catch(ASGRuntimeException e){
			log.error("doFilter", e);
		}

		chain.doFilter(sreq, sres);
	}
	
	private List<PathEntity> parsePath(String pathStr){
		if(pathStr.startsWith("/"))
			pathStr = StringUtils.remove(pathStr, 0, 1);
		if(pathStr.endsWith("/"))
			pathStr = StringUtils.remove(pathStr, pathStr.length() - 1, 1);
		
		List<PathEntity> ret = new ArrayList<PathEntity>();
		List<String> pathTockens = StringUtils.tokenize2list(pathStr, '/');
		for(String tocken: pathTockens){
			boolean convertable = StringUtils.isSurroundedWith(tocken, '<', '>'); 
			String name = convertable? StringUtils.removeSurround(tocken): tocken;
			ret.add(new PathEntity(name, convertable));
		}
		
		return ret;
	}
	
	private boolean matchPaths(List<PathEntity> patternPath, List<PathEntity> path){
		if(patternPath.size() != path.size())
			return false;
		for(int i = 0; i < patternPath.size(); i++){
			if(!patternPath.get(i).conform(path.get(i)))
				return false;
		}
		return true;
	}
	
	private String convertPathToQuery(List<PathEntity> patternPath, List<PathEntity> path){
		List<String> parameters = new ArrayList<String>();
		for(int i = 0; i < patternPath.size(); i++){
			if(patternPath.get(i).isConvertable())
				parameters.add(patternPath.get(i).getName() + "=" + path.get(i).getName());
		}
		return StringUtils.concatenateTokens(parameters, "&");
	}
	
}
