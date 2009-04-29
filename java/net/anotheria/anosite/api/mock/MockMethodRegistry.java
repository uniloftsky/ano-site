package net.anotheria.anosite.api.mock;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.anotheria.anosite.api.common.API;

import org.apache.log4j.Logger;

public class MockMethodRegistry {
	private static Logger log = Logger.getLogger(MockMethodRegistry.class);
	private static Map<Method, APIMockMethod> methods = new ConcurrentHashMap<Method, APIMockMethod>();
	
	static{
		try{
			Method init = API.class.getMethod("init");
			addMockMethod(init, new NoopMockMethod());
		}catch(NoSuchMethodException e){
			log.fatal("Someone changed the api signature!",e );
		}
	}
	
	public static void addMockMethod(Method m, APIMockMethod mock){
		methods.put(m, mock);
	}
	
	public static APIMockMethod getMockMethod(Method m){
		return methods.get(m);
	}
}
