package net.anotheria.anosite.handler;

import net.anotheria.anosite.gen.data.BoxHandlerDef;
import net.anotheria.anosite.gen.service.ASFederatedDataServiceFactory;
import net.anotheria.anosite.gen.service.IASFederatedDataService;

public class BoxHandlerFactory {
	
	private static IASFederatedDataService service = ASFederatedDataServiceFactory.createASFederatedDataService();
	
	public static BoxHandler createHandler(String id){
		try{
			BoxHandlerDef def = service.getBoxHandlerDef(id);
			return (BoxHandler)Class.forName(def.getClazz()).newInstance();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Handler instantiation failed - "+e.getMessage());
		}
		
	}
}
