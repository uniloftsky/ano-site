package net.anotheria.anosite.api.generic.login.processors;

import net.anotheria.anosite.api.common.APICallContext;
import net.anotheria.anosite.api.generic.login.LoginPostProcessor;

public class SessionCleanupOnLoginProcessor implements LoginPostProcessor{

	public void postProcessLogin(String userId) {
		APICallContext.getCallContext().getCurrentSession().cleanupOnLogin();
	}
	
}
