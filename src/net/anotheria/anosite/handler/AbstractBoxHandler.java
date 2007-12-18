package net.anotheria.anosite.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.gen.asresourcedata.service.ASResourceDataServiceFactory;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.asg.generator.meta.IMetaType;

public abstract class AbstractBoxHandler implements BoxHandler{

	
	private static IASResourceDataService resourceService = ASResourceDataServiceFactory.createASResourceDataService();
	
	public BoxHandlerResponse process(HttpServletRequest req, HttpServletResponse res, Box box, BoxBean bean) {
		return new ResponseContinue();
	}

	public BoxHandlerResponse submit(HttpServletRequest req, HttpServletResponse res, Box box) {
		return new ResponseContinue();
	}
	
	protected static IASResourceDataService getResourceDataService(){
		return resourceService;
	}

}
