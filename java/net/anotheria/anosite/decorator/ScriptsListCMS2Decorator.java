package net.anotheria.anosite.decorator;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.assitedata.service.IASSiteDataService;
import net.anotheria.asg.exception.ASGRuntimeException;

import org.apache.log4j.Logger;


/**
 * Decorator for linked scripts.
 * @author denis
 */
public class ScriptsListCMS2Decorator extends LinksListCMS2Decorator{
	
	/**
	 * As site data service for script retrieval.
	 */
	private static IASSiteDataService service;

	/**
	 * Init.
	 */
	static {
		try {
			service = MetaFactory.get(IASSiteDataService.class);
		} catch (MetaFactoryException e) {
			Logger.getLogger(ScriptsListCMS2Decorator.class).fatal("IASSiteDataService  init failure", e);
		}
	}
	
	@Override
	protected String getLinkTargetName(String targetId) throws ASGRuntimeException {
		return service.getScript(targetId).getName();
	}
	

}
