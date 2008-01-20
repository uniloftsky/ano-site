package net.anotheria.anosite.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import net.anotheria.anosite.gen.asfederateddata.data.BoxHandlerDef;
import net.anotheria.anosite.gen.asfederateddata.service.ASFederatedDataServiceFactory;
import net.anotheria.anosite.gen.asfederateddata.service.IASFederatedDataService;

public class BoxHandlerFactory {
	
	private static Logger log = Logger.getLogger(BoxHandlerFactory.class);
	
	private static IASFederatedDataService service = ASFederatedDataServiceFactory.createASFederatedDataService();
	
	private static Map<String, BoxHandlerProducer> producers = new HashMap<String, BoxHandlerProducer>();
	
	public static BoxHandler createHandler(String id){
		try{
			BoxHandlerDef def = service.getBoxHandlerDef(id);
		
			//first find producer
			String producerId = def.getClazz()+"-"+def.getId();
			BoxHandlerProducer producer = producers.get(producerId);
			if (producer==null){
				synchronized(producers){
					producer = producers.get(producerId);
					if (producer==null){
						producer = new BoxHandlerProducer(producerId);
						producers.put(producerId, producer);
					}
				}
			}
			//end producer lookup
			
			BoxHandlerWrapper wrapper = new BoxHandlerWrapper(producer, (BoxHandler)Class.forName(def.getClazz()).newInstance());
			return wrapper;
		
		}catch(Exception e){
			log.error("createHandler("+id+")", e);
			throw new RuntimeException("Handler instantiation failed - "+e.getMessage());
		}
		
	}
}
