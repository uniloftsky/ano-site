package net.anotheria.anosite.content.servlet;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import net.anotheria.anosite.gen.asfederateddata.data.BoxHandlerDef;
import net.anotheria.anosite.gen.asfederateddata.service.ASFederatedDataServiceFactory;
import net.anotheria.anosite.gen.asfederateddata.service.IASFederatedDataService;
import net.anotheria.anosite.util.AnositeConstants;

public class BlueprintProducersFactory {
	
	private static Logger log = Logger.getLogger(BlueprintProducersFactory.class);
	
	private static Map<String, BlueprintProducer> producers = new HashMap<String, BlueprintProducer>();
	
	public static BlueprintProducer getBlueprintProducer(String producerId){
		return getBlueprintProducer(producerId, "default", AnositeConstants.AS_MOSKITO_SUBSYSTEM);
	}
	
	public static BlueprintProducer getBlueprintProducer(String producerId, String category, String subsystem){
		try{
			//first find producer
			BlueprintProducer producer = producers.get(producerId);
			if (producer==null){
				synchronized(producers){
					producer = producers.get(producerId);
					if (producer==null){
						producer = new BlueprintProducer(producerId, category, subsystem);
						producers.put(producerId, producer);
					}
				}
			}
			//end producer lookup
			return producer;
		}catch(Exception e){
			log.error("getBlueprintProducer("+producerId+", "+category+", "+subsystem+")", e);
			throw new RuntimeException("Handler instantiation failed - "+e.getMessage());
		}
		
	}
}
