package com.pcloud.start;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassUtils {
	
	private static List<String> classNameList = new ArrayList<>();
	
	public static <T> List<Class<?>> getClassList(Class<T> clz){
		parseClass(clz);
		List<Class<?>> classList = new ArrayList<>(classNameList.size());
		classNameList.forEach(className -> {
			try {
				Class<?> forName = Class.forName(className);
				classList.add(forName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
		return classList;
	}
	
	private static <T> void parseClass(Class<T> clz){
		String packageName = clz.getPackage().getName();
		URL url = clz.getResource("");
		String filePath = url.getPath();
		File parentFile = new File(filePath);
		getFiles(parentFile, packageName);
	}
	
	/**
	 * 获得目录下所有的文件信息，并转化为类名+包名.
	 * @param parentfile 对应的目录或文件File对象
	 */
	private static void getFiles(File parentFile,String packageName){
		// 是否是目录
		if(parentFile.isDirectory()){
			File[] files = parentFile.listFiles();
			// 是否是目录
			for(File f : files){
				if(f.isDirectory()){
					// 递归
					getFiles(f,packageName);
				}else{
					// 文件.class
					String uri = f.toURI().toString().replaceAll("/", ".");
					int begin = uri.indexOf(packageName);
					int end = uri.lastIndexOf(".");
					// 进行处理获得类名+包名
					classNameList.add(uri.substring(begin, end));
				}
			}
		}
	}
}
