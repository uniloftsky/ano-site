package net.anotheria.anosite.api.generic.login;

import net.anotheria.anosite.api.common.APIException;
import net.anotheria.anosite.api.common.AbstractAPIImpl;
import net.anotheria.anosite.api.session.APISessionImpl;

public class LoginAPIImpl extends AbstractAPIImpl implements LoginAPI{
	
	
	

	public void addLoginPostprocessor(LoginPostProcessor postProcessor) {
		// TODO Auto-generated method stub
		
	}

	public void addLoginPreprocessor(LoginPreProcessor preProcessor) {
		// TODO Auto-generated method stub
		
	}

	public void logInUser(String userId) throws APIException {
		//call preprocessors
		((APISessionImpl)getSession()).setCurrentUserId(userId);
		getCallContext().setCurrentUserId(userId);
		
		//call post processors
	}

	public void logoutMe() throws APIException {
		//call preprocessors
		
		((APISessionImpl)getSession()).setCurrentUserId(null);
		getCallContext().setCurrentUserId(null);
		
		//call postprocessors
		
	}

	public void addLogoutPostprocessor(LogoutPostProcessor preProcessor) {
		// TODO Auto-generated method stub
		
	}

	public void addLogoutPreprocessor(LogoutPreProcessor preProcessor) {
		// TODO Auto-generated method stub
		
	}

}
