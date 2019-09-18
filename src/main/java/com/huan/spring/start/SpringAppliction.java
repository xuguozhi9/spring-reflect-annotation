package com.huan.spring.start;

import java.util.List;

/**
 * Spring启动类
 */
public class SpringAppliction {
	
	public static <T> void run(Class<T> clz){
		List<Class<?>> classList = ClassUtils.getClassList(clz);
		ApplicationContext.handle(classList);
	}
}
