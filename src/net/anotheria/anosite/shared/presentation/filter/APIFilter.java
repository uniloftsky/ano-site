package net.anotheria.anosite.shared.presentation.filter;

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

import net.anotheria.anosite.api.activity.ActivityAPI;
import net.anotheria.anosite.api.common.APICallContext;
import net.anotheria.anosite.api.common.APIFinder;
import net.anotheria.anosite.api.session.APISession;
import net.anotheria.anosite.api.session.APISessionManager;
import net.anotheria.anosite.util.AnositeConstants;

public class APIFilter implements Filter{

	public static final String PARAM_COPY_SESSION = "srcsession";
	
	private ActivityAPI activityAPI;
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest sreq, ServletResponse sres,
			FilterChain chain) throws IOException, ServletException {

		if (!(sreq instanceof HttpServletRequest))
			return; 
		
		HttpServletRequest req = (HttpServletRequest) sreq;
		
		
		String copySessionParam = req.getParameter(PARAM_COPY_SESSION); 
		if (copySessionParam!=null && copySessionParam.length()>0)
			copySession(req, copySessionParam);
		
		@SuppressWarnings("unused")
		APISession session = initSession(req);
		
		String url = req.getRequestURL().toString();
		String qs = req.getQueryString();
		if (qs!=null && qs.length()>0)
			url += qs;
		
		activityAPI.notifyMyActivity(url);
		
		chain.doFilter(sreq, sres);
		
	}
	
	private void copySession(HttpServletRequest req, String copySessionParameter){
		HttpSession session = req.getSession(true);
		APISession apiSession = APISessionManager.getInstance().createSessionCopy(copySessionParameter, session.getId());
		session.setAttribute("API_SESSION_ID", apiSession.getId());
		
	}

	public void init(FilterConfig config) throws ServletException {
		activityAPI = APIFinder.findAPI(ActivityAPI.class);
	}

	protected APISession initSession(HttpServletRequest req) {		

		
		APICallContext currentContext = APICallContext.getCallContext();
		currentContext.reset();
		
		
		//ok, wir erstellen erstmal per request ne neue session, spaeter optimieren (ein problem z.b. fuer lb abfragen).
		//durch das "unroot" sollte es eben nicht mehr so sein, dass pro request "unnnoeitg eine session" erzeugt wird.
		HttpSession session = req.getSession(true);
		String apiSessionId = session == null ? null : (String) session.getAttribute("API_SESSION_ID");
		APISession apiSession;
		if (apiSessionId==null){
			apiSession = createAPISession(session);
		}else{
			apiSession = APISessionManager.getInstance().getSession(apiSessionId);
			if(apiSession == null) {
				apiSession = createAPISession(session);
			} 
		}
		

		apiSession.setIpAddress(req.getRemoteAddr());
		apiSession.setUserAgent(req.getHeader("user-agent"));
		
		
		currentContext.setCurrentSession(apiSession);
		
//		currentContext.setCurrentLocale(req.getLocale());
		currentContext.setCurrentLocale(getLocale(req));
		currentContext.setCurrentUserId(apiSession.getCurrentUserId());
		return apiSession;
	}

	private APISession createAPISession(HttpSession session) {
		APISession apiSession = APISessionManager.getInstance().createSession(session.getId());
		session.setAttribute("API_SESSION_ID", apiSession.getId());
		return apiSession;
	}
	
	//TODO don't use http session anymore. Only use it to store api sessionid.
	private Locale getLocale(HttpServletRequest req){
		Locale ret = (Locale)req.getSession().getAttribute(AnositeConstants.SA_LOCALE);
		if (ret==null)
			ret = (Locale)APICallContext.getCallContext().getCurrentSession().getAttribute(AnositeConstants.SA_LOCALE);
		return ret != null? ret: req.getLocale();
	}
	
}
