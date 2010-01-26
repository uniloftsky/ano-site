package net.anotheria.anosite.handler.def;

import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.anosite.handler.AbstractBoxHandler;
import net.anotheria.anosite.handler.BoxHandlerResponse;
import net.anotheria.anosite.handler.ResponseRedirectImmediately;
import net.anotheria.anosite.handler.exception.BoxSubmitException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * This handler sends a redirect request back to the browser. The redirect target is stored in the parameter1 of the associated box. This is used as builtin redirect mechanism.
 * @author lrosenberg
 *
 */
public class RedirectImmediatelyHandler extends AbstractBoxHandler{

	@Override
	public BoxHandlerResponse process(HttpServletRequest req, HttpServletResponse res, Box box, BoxBean bean) {
		return getRedirect(req, res, box);
	}
	
	@Override
	public BoxHandlerResponse submit(HttpServletRequest req, HttpServletResponse res, Box box)  throws BoxSubmitException{
		return getRedirect(req, res, box);
	}
	
	/**
	 * Reads the redirect target from the box.
	 * @param req
	 * @param res
	 * @param box
	 * @return
	 */
	private BoxHandlerResponse getRedirect(HttpServletRequest req, HttpServletResponse res, Box box){
		String urlQuery = req.getQueryString();
		urlQuery = urlQuery != null && urlQuery.length() > 0? "?" + urlQuery:"";
		String redirectTarget = box.getParameter1() + urlQuery;
		return new ResponseRedirectImmediately(redirectTarget);
	}
}
