package com.huan.web;

import com.huan.spring.start.SpringAppliction;

/**
 * 启动包扫描类(自动扫描该路径下的所有类)
 */
public class StartAction {
    public static void run() {
        SpringAppliction.run(StartAction.class);
    }
}
