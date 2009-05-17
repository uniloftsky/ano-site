package net.anotheria.anosite.api.generic.login;

public interface LogoutPreProcessor {

	void preProcessLogout(String userId) throws ProcessorException;
}
