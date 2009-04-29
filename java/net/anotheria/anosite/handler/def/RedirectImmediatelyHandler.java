package net.anotheria.anosite.handler.def;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.anosite.handler.AbstractBoxHandler;
import net.anotheria.anosite.handler.BoxHandlerResponse;
import net.anotheria.anosite.handler.ResponseRedirectImmediately;
import net.anotheria.asg.exception.ASGRuntimeException;

public class RedirectImmediatelyHandler extends AbstractBoxHandler{

	@Override
	public BoxHandlerResponse process(HttpServletRequest req, HttpServletResponse res, Box box, BoxBean bean) {
		return getRedirection(req, res, box);
	}
	
	@Override
	public BoxHandlerResponse submit(HttpServletRequest req, HttpServletResponse res, Box box)  throws ASGRuntimeException{
		return getRedirection(req, res, box);
	}
	
	private BoxHandlerResponse getRedirection(HttpServletRequest req, HttpServletResponse res, Box box){
		String urlQuery = req.getQueryString();
		urlQuery = urlQuery != null && urlQuery.length() > 0? "?" + urlQuery:"";
		String redirectTarget = box.getParameter1() + urlQuery;
		return new ResponseRedirectImmediately(redirectTarget);
	}
}
