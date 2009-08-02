package net.anotheria.anosite.api.mock;

import java.lang.reflect.Method;

/**
 * A mocking method, used to construct an api implementation on the fly (or at least as much of it, as needed by tests).
 * @author another
 *
 */
public interface APIMockMethod {
	Object invoke(Method method, Object[] args);
}
