package net.anotheria.anosite.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An action is something which can be performed by the user. A typical action is sendMessage or deleteMessage in a messaging system, both actions which do not 
 * render an immediate result. Instead they delegate the user to the proper page. Same is happening here, the action returns an ActionCommand which is usually a 
 * redirect (throw browser redirect or direct forward) to another page.
 * @author lrosenberg
 *
 */
public interface Action {
	/**
	 * Executes the action.
	 * @param req the http request.
	 * @param responce the http response.
	 * @param mapping default mapping as configured in the CMS.
	 * @return
	 * @throws Exception
	 */
	ActionCommand execute(HttpServletRequest req, HttpServletResponse responce, ActionMapping mapping) throws Exception;
}
