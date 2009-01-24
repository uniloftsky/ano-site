package net.anotheria.anosite.api.mock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import net.anotheria.anosite.api.common.API;

import org.apache.log4j.Logger;


public class APIMaskImpl <T extends API> implements API, InvocationHandler{
	
	private T maskedInstance;
	private Class<T> maskedClazz;
	private static Logger log = Logger.getLogger(APIMockImpl.class);
	
	public APIMaskImpl(T aMaskedInstance, Class<T> aMaskedClazz){
		maskedInstance = aMaskedInstance;
		maskedClazz = aMaskedClazz;
	}

	@Override
	public void deInit() {
		maskedInstance.deInit();
	}

	@Override
	public void init() {
		maskedInstance.init();
	}
	
	@SuppressWarnings("unchecked")
	public T createAPIProxy(){
		return (T)Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{maskedClazz}, this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		if (method.getDeclaringClass().equals(maskedClazz) || method.getDeclaringClass().equals(API.class))
			return invokeOnMasked(proxy, method, args);

		return method.invoke(this, args);
	}

	private Object invokeOnMasked(Object proxy, Method method, Object[] args) throws Throwable{
		if (log.isDebugEnabled())
			log.debug("Called method: "+method+" in "+maskedClazz.getName());
		
		@SuppressWarnings("unchecked")
		APIMaskMethod<T> meth = (APIMaskMethod<T>)MaskMethodRegistry.getMaskMethod(method);
		if (meth==null){
			//method not masked
			return method.invoke(maskedInstance, args);
		}
		//method masked
		return meth.invoke(method, args, maskedInstance);
		
	}

}
