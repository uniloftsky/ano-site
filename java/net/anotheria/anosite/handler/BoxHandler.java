package net.anotheria.anosite.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.asg.exception.ASGRuntimeException;

/**
 * A boxhandler can be attached to any box. Upon processing of a request for each box, its box handler (if set) will be instantiated and the methods 
 * submit and process called on it. First all submit methods are called, than all process methods. 
 * Submit methods are only called if the request has been a form submission. In one request submit and process will be called on the same 
 * handler instance, so the handler itself is allowed to be non-reentrant and store data in instance variables for the duration of one request.
 * @author another
 *
 */
public interface BoxHandler {
	
	/**
	 * Called in first processing cycle if a form submit has been detected (post requst or special indicator parameters). Note: 
	 * Usage of submit in handlers is discouraged, use actions whenever possible.
	 * @param req
	 * @param res
	 * @param box
	 * @return
	 * @throws ASGRuntimeException
	 */
	BoxHandlerResponse submit(HttpServletRequest req, HttpServletResponse res, Box box) throws ASGRuntimeException;
	
	/**
	 * Called in second processing cycle.
	 * @param req the http servlet request
	 * @param res the http servler response
	 * @param box the box this handler has been associated with (and which was currently processed).
	 * @param bean the already created bean.
	 * @return
	 * @throws ASGRuntimeException
	 */
	BoxHandlerResponse process(HttpServletRequest req, HttpServletResponse res, Box box, BoxBean bean) throws ASGRuntimeException;
}
