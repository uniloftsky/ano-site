package net.anotheria.anosite.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {
	public ActionCommand execute(HttpServletRequest req, HttpServletResponse responce, ActionMapping mapping) throws Exception;
}
