package com.huan;
import	java.io.File;

import java.net.URL;

/**
 * @Author: wb_xugz
 * @CreateTime: 2019-09-18 15:34
 */
public class MyTest {

    public static void main(String[] args) {
        String packageName = MyTest.class.getPackage().getName();
        URL resource = MyTest.class.getResource("");
        File file = new File(resource.getFile());
    }
}
