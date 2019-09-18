package com.huan.spring.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huan.web.StartAction;
import com.huan.spring.utils.HandleMap;
import com.huan.spring.enums.RequestMapping;

/**
 * 1.com.huan 转换成文件格式 扫描获取com/huan所有文件 再转换成com.huan.**形式
 * 2.加载所有的com.huan.**形式Class.forName(String name);得到所有的字节码文件
 * 3.遍历所有的字节码文件，有@Component注解则创建对象；
 * 	对有@Controller @RequestMapping注解的注册到DispatcherServlet中的map中(key->路径信息，value->method)
 * 4.遍历所有的对象，对有@Autowired注解的进行相关注入操作
 *
 * 5.请求首先匹配DispatcherServlet中的map,寻找对应的method方法进行相应处理。
 *
 * 请求分发类
 */
public class DispatchServlet extends HttpServlet{
	
	private static Map<String,HandleMap> handleMaps = new HashMap<>();

	@Override
	public void init() throws ServletException {
		System.out.println("Servlet初始化");
		StartAction.run();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String servletPath = req.getServletPath();
		HandleMap handleMap = handleMaps.get(servletPath);
		if(handleMap == null){
			resp.getWriter().write("error");
			return;
		}
		try {
			Object invoke = handleMap.getMethod().invoke(handleMap.getObj(), null);
			resp.getWriter().write(invoke.toString());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			resp.getWriter().write("error");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	public static void handleController(Object obj, Class<?> clz) {
		Method[] declaredMethods = clz.getDeclaredMethods();
		for(Method method : declaredMethods){
			RequestMapping annotation = method.getAnnotation(RequestMapping.class);
			if(annotation != null){
				String value = annotation.value();
				HandleMap handleMap = new HandleMap();
				handleMap.setMethod(method);
				handleMap.setObj(obj);
				handleMaps.put(value,handleMap);
			}
		}
	}
}
