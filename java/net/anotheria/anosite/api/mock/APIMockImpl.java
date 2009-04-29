package net.anotheria.anosite.api.mock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import net.anotheria.anosite.api.common.API;

import org.apache.log4j.Logger;

public class APIMockImpl<T extends API> implements API, InvocationHandler{
	
	private Class<T> mockedClazz;
	private static Logger log = Logger.getLogger(APIMockImpl.class);
	
	public APIMockImpl(Class<T> aMockedClazz){
		mockedClazz = aMockedClazz;
	}

	@Override
	public void deInit() {
	}

	@Override
	public void init() {
	}
	
	@SuppressWarnings("unchecked")
	public T createAPIProxy(){
		return (T)Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{mockedClazz}, this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		if (method.getDeclaringClass().equals(mockedClazz) || method.getDeclaringClass().equals(API.class))
			return invokeOnMock(proxy, method, args);
		
		return method.invoke(this, args);
	}

	private Object invokeOnMock(Object proxy, Method method, Object[] args){
		if (log.isDebugEnabled())
			log.debug("Called method: "+method+" in "+mockedClazz.getName());
		
		APIMockMethod meth = MockMethodRegistry.getMockMethod(method);
		if (meth==null)
			throw new IllegalArgumentException("Method "+method+" is not mocked");
		
		return meth.invoke(method, args);
		
	}

}
