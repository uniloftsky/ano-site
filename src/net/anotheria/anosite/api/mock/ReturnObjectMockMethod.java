package net.anotheria.anosite.api.mock;

import java.lang.reflect.Method;

public class ReturnObjectMockMethod implements APIMockMethod{

	private Object value;
	
	public ReturnObjectMockMethod(Object aValue){
		value = aValue;
	}
	
	@Override
	public Object invoke(Method method, Object[] args) {
		return value;
	}
	

}
