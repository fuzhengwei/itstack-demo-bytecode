package org.itstack.demo.test;


public class ApiTest {

    private String queryUserInfo(String uid) {
        System.out.println(uid);
        String res = "查询信息：" + uid;
        System.out.println(res);
        return res;
    }

}


