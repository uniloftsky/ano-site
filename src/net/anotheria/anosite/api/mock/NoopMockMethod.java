package net.anotheria.anosite.api.mock;

import java.lang.reflect.Method;

public class NoopMockMethod implements APIMockMethod{

	@Override
	public Object invoke(Method method, Object[] args) {
		return null;
	}

}
