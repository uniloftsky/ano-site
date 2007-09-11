package net.anotheria.anosite.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.gen.data.Box;

public interface BoxHandler {
	
	public void submit();
	
	public void process(HttpServletRequest req, HttpServletResponse res, Box box, BoxBean bean);
}
