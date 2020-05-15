package org.itstack.demo.test;

public class ApiTest {

    public String queryUserInfo(String uid) {
        long var2 = System.nanoTime();
        System.out.println("xxxx");
        System.out.println("xxxx");
        System.out.println("xxxx");
        System.out.println("xxxx");
        System.out.println("方法执行耗时(纳秒)->queryUserInfo：" + (System.nanoTime() - var2));
        return uid;
    }

}
