package net.anotheria.anosite.api.generic.login.processors;

import net.anotheria.anosite.api.common.APICallContext;
import net.anotheria.anosite.api.generic.login.LogoutPostProcessor;

public class SessionCleanupOnLogoutProcessor implements LogoutPostProcessor{

	public void postProcessLogout(String userId) {
		APICallContext.getCallContext().getCurrentSession().cleanupOnLogout();
	}
	
}
