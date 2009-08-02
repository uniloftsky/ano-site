package net.anotheria.anosite.api.generic.login;

/**
 * Called before each logout. Technically can prevent a logout by throwing a processor exception. 
 * @author lrosenberg
 */
public interface LogoutPreProcessor {
	void preProcessLogout(String userId) throws ProcessorException;
}
