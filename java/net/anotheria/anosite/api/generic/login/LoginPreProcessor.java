package net.anotheria.anosite.api.generic.login;

public interface LoginPreProcessor {
	void preProcessLogin(String userId) throws ProcessorException;
}
