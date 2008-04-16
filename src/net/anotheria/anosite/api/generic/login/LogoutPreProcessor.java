package net.anotheria.anosite.api.generic.login;

public interface LogoutPreProcessor {

	public void preProcessLogout(String userId) throws ProcessorException;
}
