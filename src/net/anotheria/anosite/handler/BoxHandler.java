package net.anotheria.anosite.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.asg.exception.ASGRuntimeException;

public interface BoxHandler {
	
	public BoxHandlerResponse submit(HttpServletRequest req, HttpServletResponse res, Box box) throws ASGRuntimeException;
	
	public BoxHandlerResponse process(HttpServletRequest req, HttpServletResponse res, Box box, BoxBean bean) throws ASGRuntimeException;
}
