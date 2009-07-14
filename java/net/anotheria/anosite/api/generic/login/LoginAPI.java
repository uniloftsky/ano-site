package net.anotheria.anosite.api.generic.login;

import net.anotheria.anosite.api.common.API;
import net.anotheria.anosite.api.common.APIException;
import net.anotheria.anosite.api.common.NoLoggedInUserException;

/**
 * Basic API for login/out purposes.
 * @author another
 *
 */
public interface LoginAPI extends API{
	/**
	 * Logs the user with the given id in.
	 * @param userId the userId to log in.
	 * @throws APIException
	 */
	void logInUser(String userId) throws APIException;
	/**
	 * Logouts the current user.
	 * @throws APIException
	 */
	void logoutMe() throws APIException;
	
	/**
	 * Returns the id of the currently logged in user.
	 * @return
	 * @throws NoLoggedInUserException if no logged in user in current session
	 */
	String getLogedUserId() throws NoLoggedInUserException, APIException;

	/**
	 * Returns true if there is a current userid.
	 * @return
	 * @throws APIException
	 */
	boolean isLogedIn() throws APIException;

	/**
	 * Adds a login preprocessor. Each login preprocessor is called before a user can actually login. LoginPreprocessor can prevent login by throwing an exception.
	 * @param preProcessor
	 */
	void addLoginPreprocessor(LoginPreProcessor preProcessor);
	/**
	 * Adds a login post processor. Each login postprocessor is called _after_ a user logins. LoginPostProcessor can't prevent login.
	 * @param postProcessor
	 */
	void addLoginPostprocessor(LoginPostProcessor postProcessor);
	
	void addLogoutPreprocessor(LogoutPreProcessor preProcessor);
	
	void addLogoutPostprocessor(LogoutPostProcessor preProcessor);
}
