package net.anotheria.anosite.handler.def;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.anosite.handler.AbstractBoxHandler;
import net.anotheria.anosite.handler.BoxHandlerResponse;
import net.anotheria.anosite.handler.ResponseRedirectImmediately;

public class RedirectImmediatelyHandler extends AbstractBoxHandler{

	@Override
	public BoxHandlerResponse process(HttpServletRequest req, HttpServletResponse res, Box box, BoxBean bean) {
		String redirectTarget = box.getParameter1();
		return new ResponseRedirectImmediately(redirectTarget);
	}

}
