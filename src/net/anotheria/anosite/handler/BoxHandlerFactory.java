package net.anotheria.anosite.handler;

import net.anotheria.anosite.gen.data.BoxHandlerType;
import net.anotheria.anosite.gen.service.ASMetaDataServiceFactory;
import net.anotheria.anosite.gen.service.IASMetaDataService;

public class BoxHandlerFactory {
	
	private static IASMetaDataService service = ASMetaDataServiceFactory.createASMetaDataService();
	
	public static BoxHandler createHandler(String id){
		try{
			BoxHandlerType type = service.getBoxHandlerType(id);
			return (BoxHandler)Class.forName(type.getClazz()).newInstance();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Handler instantiation failed - "+e.getMessage());
		}
		
	}
}
