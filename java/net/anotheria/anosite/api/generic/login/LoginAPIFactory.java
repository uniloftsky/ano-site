package net.anotheria.anosite.api.generic.login;

import net.anotheria.anosite.api.common.APIFactory;
/**
 * The factory for the current login api.
 */
public class LoginAPIFactory implements APIFactory<LoginAPI>{

	@Override public LoginAPI createAPI() {
		return new LoginAPIImpl();
	}

}
