package net.anotheria.anosite.api.generic.login;

/**
 * Logout preprocessors are used to adopt the behaviour of the login api. They get called before each login. If a LoginPreProcessor throws a Processor exception
 * the login is aborted.
 * @author lrosenberg
 *
 */
public interface LoginPreProcessor {
	void preProcessLogin(String userId) throws ProcessorException;
}
