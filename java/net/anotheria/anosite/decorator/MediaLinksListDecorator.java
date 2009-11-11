package net.anotheria.anosite.decorator;

import net.anotheria.anosite.gen.assitedata.service.ASSiteDataServiceFactory;
import net.anotheria.anosite.gen.assitedata.service.IASSiteDataService;
import net.anotheria.asg.exception.ASGRuntimeException;



/**
 * Decorator for linked scripts.
 * @author denis
 */
public class MediaLinksListDecorator extends LinksListDecorator{
	
	/**
	 * As web data service for MediaLinks retrieval.
	 */
	private static IASSiteDataService service = ASSiteDataServiceFactory.createASSiteDataService();

	@Override
	protected String getLinkTargetName(String targetId) throws ASGRuntimeException {
		return service.getMediaLink(targetId).getName();
	}
	

}
