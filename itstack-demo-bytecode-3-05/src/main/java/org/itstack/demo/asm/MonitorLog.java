package org.itstack.demo.asm;

public class MonitorLog {

    public static void info(String name, int... parameters) {
        System.out.println("方法：" + name);
        System.out.println("参数：" + "[" + parameters[0] + "," + parameters[1] + "]");
    }

}
