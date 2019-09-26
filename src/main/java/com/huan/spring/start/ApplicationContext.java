package com.huan.spring.start;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.huan.spring.servlet.DispatchServlet;
import com.huan.spring.enums.Autowired;
import com.huan.spring.enums.Component;
import com.huan.spring.enums.Controller;

/**
 * 创建对象
 */
public class ApplicationContext {

    private static Map<String, Object> map = new HashMap<>();

    /**
     * 创建对象并对对注入
     * @param classList 字节码信息的集合
     */
    public static void handle(List<Class<?>> classList) {
        List<Object> successList = new ArrayList<>();
        classList.forEach(clz -> {
            Annotation[] annotations = clz.getAnnotations();
            for (Annotation annotation : annotations) {
                Object obj = null;
                if (annotation instanceof Component) {
                    obj = createObject(clz, ((Component) annotation).value());
                }
                if (annotation instanceof Controller) {
                    obj = createObject(clz, ((Controller) annotation).value());
                    DispatchServlet.handleController(obj, clz);
                }
                if (obj != null) {
                    successList.add(obj);
                }
            }
        });
        postClassList(successList);
    }


    /**
     * 注入
     * @param successList
     */
    private static void postClassList(List<Object> successList) {
        successList.forEach(obj -> {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    try {
                        field.set(obj, getMap(type));
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static Object getMap(Class<?> type) {
        Set<Entry<String, Object>> entrySet = map.entrySet();
        for (Entry<String, Object> entry : entrySet) {
            Object obj = entry.getValue();
            if (type.equals(obj.getClass())) {
                return obj;
            }
            Type[] genericInterfaces = obj.getClass().getGenericInterfaces();
            for (Type genericInterface : genericInterfaces) {
                if (type.equals(genericInterface)) {
                    return obj;
                }
            }
        }
        System.out.println(type + "没有找到对应的实例化对象");
        return null;
    }


    private static Object createObject(Class<?> clz, String name) {
        if (name == null || name.equals("")) {
            name = clz.getSimpleName();
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        try {
            Object newInstance = clz.newInstance();
            map.put(name, newInstance);
            return newInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getBean(String name) {
        return map.get(name);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name, Class<T> clz) {
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
