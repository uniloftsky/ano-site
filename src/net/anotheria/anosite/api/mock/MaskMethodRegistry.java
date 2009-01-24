package net.anotheria.anosite.api.mock;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MaskMethodRegistry {
	private static Map<Method, APIMaskMethod<?>> methods = new ConcurrentHashMap<Method, APIMaskMethod<?>>();
	
	public static void addMaskMethod(Method m, APIMaskMethod<?> mock){
		methods.put(m, mock);
	}
	
	public static APIMaskMethod<?> getMaskMethod(Method m){
		return methods.get(m);
	}
}
