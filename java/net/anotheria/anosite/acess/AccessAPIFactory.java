package net.anotheria.anosite.acess;

import net.anotheria.anoplass.api.APIFactory;

/**
 * Factory for instantiating {@link AccessAPIImpl}
 * 
 * @author Alexandr Bolbat
 */
public class AccessAPIFactory implements APIFactory<AccessAPI> {

	@Override
	public AccessAPI createAPI() {
		return new AccessAPIImpl();
	}

}
