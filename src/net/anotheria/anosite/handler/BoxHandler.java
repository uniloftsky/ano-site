package net.anotheria.anosite.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.gen.aswebdata.data.Box;

public interface BoxHandler {
	
	public BoxHandlerResponse submit(HttpServletRequest req, HttpServletResponse res, Box box);
	
	public BoxHandlerResponse process(HttpServletRequest req, HttpServletResponse res, Box box, BoxBean bean);
}
