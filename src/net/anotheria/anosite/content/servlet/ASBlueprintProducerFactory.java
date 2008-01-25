package net.anotheria.anosite.content.servlet;

import net.anotheria.anosite.util.AnositeConstants;
import net.java.dev.moskito.core.blueprint.BlueprintProducer;
import net.java.dev.moskito.core.blueprint.BlueprintProducersFactory;

public class ASBlueprintProducerFactory {
	public static BlueprintProducer getBlueprintProducer(String producerId){
		return BlueprintProducersFactory.getBlueprintProducer(producerId, "default", AnositeConstants.AS_MOSKITO_SUBSYSTEM);
	}

}
