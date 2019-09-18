package com.huan.spring.utils;

import java.lang.reflect.Method;

public class HandleMap {
	
	private Object obj;
	
	private Method method;

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	@Override
	public String toString() {
		return "HandleMap [obj=" + obj + ", method=" + method + "]";
	}
	
}
