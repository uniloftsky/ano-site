package net.anotheria.anosite.handler;

import org.apache.log4j.Logger;

import net.anotheria.anosite.gen.asfederateddata.data.BoxHandlerDef;
import net.anotheria.anosite.gen.asfederateddata.service.ASFederatedDataServiceFactory;
import net.anotheria.anosite.gen.asfederateddata.service.IASFederatedDataService;

public class BoxHandlerFactory {
	
	private static Logger log = Logger.getLogger(BoxHandlerFactory.class);
	
	private static IASFederatedDataService service = ASFederatedDataServiceFactory.createASFederatedDataService();
	
	public static BoxHandler createHandler(String id){
		try{
			BoxHandlerDef def = service.getBoxHandlerDef(id);
			return (BoxHandler)Class.forName(def.getClazz()).newInstance();
		}catch(Exception e){
			log.error("createHandler("+id+")", e);
			throw new RuntimeException("Handler instantiation failed - "+e.getMessage());
		}
		
	}
}
