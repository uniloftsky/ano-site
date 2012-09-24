package net.anotheria.anosite.acess.storage;

import net.anotheria.access.storage.persistence.SecurityBoxPersistenceService;
import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * Factory for instantiating {@link ASGSecurityBoxPersistenceServiceImpl}.
 * 
 * @author Alexandr Bolbat
 */
public class ASGSecurityBoxPersistenceServiceFactory implements ServiceFactory<SecurityBoxPersistenceService> {

	@Override
	public SecurityBoxPersistenceService create() {
		return new ASGSecurityBoxPersistenceServiceImpl();
	}

}
