package net.anotheria.anosite.handler;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asfederateddata.data.BoxHandlerDef;
import net.anotheria.anosite.gen.asfederateddata.service.ASFederatedDataServiceFactory;
import net.anotheria.anosite.gen.asfederateddata.service.IASFederatedDataService;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class BoxHandlerFactory {
	
	private static Logger log = Logger.getLogger(BoxHandlerFactory.class);
	
	private static IASFederatedDataService service;

	static {
		try {
			service = MetaFactory.get(IASFederatedDataService.class);
		} catch (MetaFactoryException e) {
			log.fatal("IASFederatedDataService init failure",e);
		}
	}
	
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
