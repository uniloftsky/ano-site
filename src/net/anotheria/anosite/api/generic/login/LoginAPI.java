package net.anotheria.anosite.api.generic.login;

import net.anotheria.anosite.api.common.API;
import net.anotheria.anosite.api.common.APIException;

public interface LoginAPI extends API{
	
	public void logInUser(String userId) throws APIException;
	
	public void logoutMe() throws APIException;

	public boolean isLogedIn() throws APIException;

	/**
	 * Adds a login preprocessor. Each login preprocessor is called before a user can actually login. LoginPreprocessor can prevent login by throwing an exception.
	 * @param preProcessor
	 */
	public void addLoginPreprocessor(LoginPreProcessor preProcessor);
	/**
	 * Adds a login post processor. Each login postprocessor is called _after_ a user logins. LoginPostProcessor can't prevent login.
	 * @param postProcessor
	 */
	public void addLoginPostprocessor(LoginPostProcessor postProcessor);
	
	public void addLogoutPreprocessor(LogoutPreProcessor preProcessor);
	
	public void addLogoutPostprocessor(LogoutPostProcessor preProcessor);
}
