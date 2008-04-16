package net.anotheria.anosite.api.generic.login;

public interface LoginPreProcessor {
	public void preProcessLogin(String userId) throws ProcessorException;
}
