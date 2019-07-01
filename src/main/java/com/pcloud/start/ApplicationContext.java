package com.pcloud.start;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.pcloud.web.DispatchServlet;
import com.plcoud.enums.Autowired;
import com.plcoud.enums.Component;
import com.plcoud.enums.Controller;

public class ApplicationContext {
	
	private static Map<String,Object> map = new HashMap<>();
	
	public static void handle(List<Class<?>> classList){
		List<Class<?>> postClassList = new ArrayList<>();
		classList.forEach(clz -> {
			clz.getAnnotation(Controller.class);
			Annotation[] annotations = clz.getAnnotations();
			for(Annotation annotation : annotations){
				if(annotation instanceof Component){
					createObject(clz, ((Component) annotation).value());
					postClassList.add(clz);
				}
				if(annotation instanceof Controller){
					Object obj = createObject(clz, ((Controller) annotation).value());
					DispatchServlet.handleController(obj, clz);
					postClassList.add(clz);
				}
			}
		});
		postClassList(postClassList);
	}

	private static void postClassList(List<Class<?>> postClassList) {
		postClassList.forEach(clz ->{
			Field[] fields = clz.getDeclaredFields();
			for(Field field : fields){
				Autowired autowired = field.getAnnotation(Autowired.class);
				if(autowired != null){
					field.setAccessible(true);
					Class<?> type = field.getType();
					System.out.println(type+"3");
					System.out.println(getMap(clz));
					System.out.println(getMap(type));
					try {
						field.set(getMap(clz), getMap(type));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private static Object getMap(Class<?> type){
		Set<Entry<String, Object>> entrySet = map.entrySet();
		for(Entry<String, Object> entry : entrySet){
			Object obj = entry.getValue();
			if(type.equals(obj.getClass())){
				return obj;
			}
			Type[] genericInterfaces = obj.getClass().getGenericInterfaces();
			for (Type genericInterface : genericInterfaces) {
				if(type.equals(genericInterface)){
					return obj;
				}
			}
		}
		return null;
	}

	private static Object createObject(Class<?> clz, String key) {
		if(key == null || key.equals("")){
			key = clz.getSimpleName();
			key = key.substring(0,1).toLowerCase() + key.substring(1);
		}
		try {
			Object newInstance = clz.newInstance();
			map.put(key,newInstance);
			return newInstance;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object getBean(String name){
		return map.get(name);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name,Class<T> clz){
		return (T) getBean(name);
	}
	
	public static void main(String[] args) {
		Type[] genericInterfaces = String.class.getGenericInterfaces();
		System.out.println(genericInterfaces);
		
		for (Type type : genericInterfaces) {
			System.out.println(type);
		}
	}
}
