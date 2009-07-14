package net.anotheria.anosite.api.generic.login;

import java.util.ArrayList;
import java.util.List;

import net.anotheria.anosite.api.common.APIException;
import net.anotheria.anosite.api.common.APIFinder;
import net.anotheria.anosite.api.common.AbstractAPIImpl;
import net.anotheria.anosite.api.common.NoLoggedInUserException;
import net.anotheria.anosite.api.generic.login.processors.SessionCleanupOnLogoutProcessor;
import net.anotheria.anosite.api.generic.observation.ObservationAPI;
import net.anotheria.anosite.api.generic.observation.ObservationSubjects;
import net.anotheria.anosite.api.session.APISessionImpl;
import net.anotheria.util.StringUtils;

/**
 * An implementation for the login api.
 * @author lrosenberg
 *
 */
public class LoginAPIImpl extends AbstractAPIImpl implements LoginAPI{
	
	private List<LoginPreProcessor>  loginPreProcessors;
	private List<LoginPostProcessor> loginPostProcessors;

	private List<LogoutPreProcessor>  logoutPreProcessors;
	private List<LogoutPostProcessor> logoutPostProcessors;
	
	private ObservationAPI observationAPI;
	
	public void init(){
		super.init();
		
		loginPreProcessors = new ArrayList<LoginPreProcessor>();
		loginPostProcessors = new ArrayList<LoginPostProcessor>();
		
		logoutPreProcessors = new ArrayList<LogoutPreProcessor>();
		logoutPostProcessors = new ArrayList<LogoutPostProcessor>();
		
		addLogoutPostprocessor(new SessionCleanupOnLogoutProcessor());
		
		observationAPI = APIFinder.findAPI(ObservationAPI.class);
		
	}


	public void addLoginPostprocessor(LoginPostProcessor postProcessor) {
		loginPostProcessors.add(postProcessor);
		
	}

	public void addLoginPreprocessor(LoginPreProcessor preProcessor) {
		loginPreProcessors.add(preProcessor);
		
	}

	public void logInUser(String userId) throws APIException {
		callLoginPreprocessors(userId);
		
		((APISessionImpl)getSession()).setCurrentUserId(userId);
		getCallContext().setCurrentUserId(userId);
		
		callLoginPostprocessors(userId);
		
		observationAPI.fireSubjectUpdateForCurrentUser(ObservationSubjects.LOGIN, this.getClass().getName());
	}

	public void logoutMe() throws APIException {
		try{
			String userId = getCallContext().getCurrentUserId();
			callLogoutPreprocessors(userId);
			
			((APISessionImpl)getSession()).setCurrentUserId(null);
			getCallContext().setCurrentUserId(null);
			
			callLogoutPostprocessors(userId);
			
			observationAPI.fireSubjectUpdateForCurrentUser(ObservationSubjects.LOGOUT, this.getClass().getName());
		}catch(NoLoggedInUserException ignored){};
	}
	
	@Override
	public String getLogedUserId() throws NoLoggedInUserException {
		if(!isLogedIn())
			throw new APIException("No loged in users!");
		return getCallContext().getCurrentUserId();
	}
	
	public boolean isLogedIn() throws APIException {
		try{
			return !StringUtils.isEmpty(getCallContext().getCurrentUserId());
		}catch(NoLoggedInUserException e){
			return false;
		}
	}

	public void addLogoutPostprocessor(LogoutPostProcessor postProcessor) {
		logoutPostProcessors.add(postProcessor);
	}

	public void addLogoutPreprocessor(LogoutPreProcessor preProcessor) {
		logoutPreProcessors.add(preProcessor);
		
	}
	
	//////////
	private void callLoginPreprocessors(String userId) throws APIException{
		for (LoginPreProcessor p : loginPreProcessors){
			try{
				p.preProcessLogin(userId);
			}catch(ProcessorException e){
				throw e;
			}catch(Exception e){
				log.error("Exception in loginpreprocessor: "+p,e);
			}
		}
	}
	
	private void callLoginPostprocessors(String userId) {
		for (LoginPostProcessor p : loginPostProcessors){
			try{
				p.postProcessLogin(userId);
			}catch(Exception e){
				log.error("Exception in loginpostprocessor: "+p,e);
			}
		}
	}

	private void callLogoutPreprocessors(String userId) throws APIException{
		for (LogoutPreProcessor p : logoutPreProcessors){
			try{
				p.preProcessLogout(userId);
			}catch(ProcessorException e){
				throw e;
			}catch(Exception e){
				log.error("Exception in logoutPreProcessor: "+p,e);
			}
		}
	}

	private void callLogoutPostprocessors(String userId) {
		for (LogoutPostProcessor p : logoutPostProcessors){
			try{
				p.postProcessLogout(userId);
			}catch(Exception e){
				log.error("Exception in logoutpostprocessor: "+p,e);
			}
		}
		
	}

	
	

}
