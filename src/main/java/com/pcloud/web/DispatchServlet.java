package com.pcloud.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pcloud.StartAction;
import com.pcloud.utils.HandleMap;
import com.plcoud.enums.RequestMapping;

public class DispatchServlet extends HttpServlet{
	
	private static Map<String,HandleMap> handleMaps = new HashMap<>();
	
	public void init() throws ServletException {
		System.err.println("Servlet初始化");
		StartAction.run();
	}

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

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	public static void handleController(Object obj, Class<?> clz) {
		Method[] declaredMethods = clz.getDeclaredMethods();
		System.out.println(clz);
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
