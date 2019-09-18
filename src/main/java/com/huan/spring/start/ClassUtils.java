package com.huan.spring.start;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 字节码信息处理
 */
public class ClassUtils {

    /**
     * 包名+类名的集合
     */
    private static List<String> classNameList = new ArrayList<>();

    /**
     * 获取扫描包下的所有字节码信息
     * @param clz 扫描包的字节码信息
     * @param <T>
     * @return
     */
    public static <T> List<Class<?>> getClassList(Class<T> clz) {
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

    /**
     * 解析获取所有扫描包下的信息
     * @param clz 扫描包开始的类字节码
     * @param <T>
     */
    private static <T> void parseClass(Class<T> clz) {
        String basePackageName = clz.getPackage().getName();
        URL url = clz.getResource("");
        getFiles(new File(url.getFile()), basePackageName);
    }

    /**
     * 获得目录下所有的文件信息，并转化为包名+类名.
     * @param parentFile 对应的目录
     * @param basePackageName 扫描包的名称
     */
    private static void getFiles(File parentFile, String basePackageName) {
        File[] files = parentFile.listFiles();
        // 是否是目录
        for (File f : files) {
            if (f.isDirectory()) {
                // 递归
                getFiles(f, basePackageName);
            } else {
                // 文件名.class 截取包名+类名
                String uri = f.toURI().toString().replaceAll("/", ".");
                int begin = uri.indexOf(basePackageName);
                int end = uri.lastIndexOf(".");
                classNameList.add(uri.substring(begin, end));
            }
        }
    }
}
