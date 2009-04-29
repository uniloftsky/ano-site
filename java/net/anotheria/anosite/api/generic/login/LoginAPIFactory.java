package net.anotheria.anosite.api.generic.login;

import net.anotheria.anosite.api.common.APIFactory;

public class LoginAPIFactory implements APIFactory<LoginAPI>{

	public LoginAPI createAPI() {
		return new LoginAPIImpl();
	}

}
