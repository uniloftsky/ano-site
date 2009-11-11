package net.anotheria.anosite.decorator;

import net.anotheria.anosite.gen.assitedata.service.ASSiteDataServiceFactory;
import net.anotheria.anosite.gen.assitedata.service.IASSiteDataService;
import net.anotheria.asg.exception.ASGRuntimeException;



/**
 * Decorator for linked scripts.
 * @author denis
 */
public class ScriptsListDecorator extends LinksListDecorator{
	
	/**
	 * As web data service for script retrieval.
	 */
	private static IASSiteDataService service = ASSiteDataServiceFactory.createASSiteDataService();

	@Override
	protected String getLinkTargetName(String targetId) throws ASGRuntimeException {
		return service.getScript(targetId).getName();
	}
	

}
