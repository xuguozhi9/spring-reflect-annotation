package com.pcloud;

import com.pcloud.start.SpringAppliction;
import com.plcoud.enums.Component;
import com.plcoud.enums.Controller;

public class StartAction {
    public static void run() {
        SpringAppliction.run(StartAction.class);
    }

    public static void main(String[] args) {
        Class<Controller> clz = Controller.class;
        System.out.println(clz.isAnnotation());
        System.out.println(clz.isAnnotationPresent(Component.class));
    }


}
