package net.anotheria.anosite.api.generic.login;

/**
 * Login PostProcessors are called by the login api after each login attempt.
 * @author another
 *
 */
public interface LoginPostProcessor {
	void postProcessLogin(String userId);

}
