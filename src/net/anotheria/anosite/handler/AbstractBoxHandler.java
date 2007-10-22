package net.anotheria.anosite.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.gen.aswebdata.data.Box;

public abstract class AbstractBoxHandler implements BoxHandler{

	public BoxHandlerResponse process(HttpServletRequest req, HttpServletResponse res, Box box, BoxBean bean) {
		return new ResponseContinue();
	}

	public BoxHandlerResponse submit(HttpServletRequest req, HttpServletResponse res, Box box) {
		return new ResponseContinue();
	}

}
