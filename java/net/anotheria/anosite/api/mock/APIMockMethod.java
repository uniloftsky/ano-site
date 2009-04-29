package net.anotheria.anosite.api.mock;

import java.lang.reflect.Method;

public interface APIMockMethod {
	public Object invoke(Method method, Object[] args);
}
