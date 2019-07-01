package com.pcloud.start;

import java.util.List;

public class SpringAppliction {
	
	public static <T> void run(Class<T> clz){
		List<Class<?>> classList = ClassUtils.getClassList(clz);
		ApplicationContext.handle(classList);
	}
}
