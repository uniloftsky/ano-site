package net.anotheria.anosite.api.mock;

import java.lang.reflect.Method;

import net.anotheria.anosite.api.common.API;

public interface APIMaskMethod <T extends API>{
	/**
	 * Called to invoke the given method on a given api. An instance of underlying api implementation is also given for internal usage.
	 * @param method
	 * @param args
	 * @param maskedAPI
	 * @return
	 */
	public Object invoke(Method method, Object[] args, T maskedAPI);
}
