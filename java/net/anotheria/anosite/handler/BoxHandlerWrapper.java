package net.anotheria.anosite.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.asg.exception.ASGRuntimeException;
import net.anotheria.maf.util.ModelObjectMapper;

public class BoxHandlerWrapper implements BoxHandler{

	private BoxHandlerProducer producer;
	private BoxHandler handler;

	public BoxHandlerWrapper(BoxHandlerProducer aProducer, BoxHandler aHandler){
		producer = aProducer;
		handler = aHandler;
	}
	
	public BoxHandlerResponse process(HttpServletRequest req,
			HttpServletResponse res, Box box, BoxBean bean)
			throws ASGRuntimeException {
		return producer.process(req, res, box, bean, handler);
	}

	public BoxHandlerResponse submit(HttpServletRequest req,
			HttpServletResponse res, Box box) throws ASGRuntimeException {
		ModelObjectMapper.map(req, handler);
		return producer.submit(req, res, box, handler);
	}
	
}
