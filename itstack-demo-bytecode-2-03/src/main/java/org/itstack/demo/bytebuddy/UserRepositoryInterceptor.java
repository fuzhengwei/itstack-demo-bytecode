package org.itstack.demo.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;

import java.lang.reflect.Method;

public class UserRepositoryInterceptor {

    public static String intercept(@Origin Method method, @AllArguments Object[] arguments) {
        return "小傅哥博客，查询文章数据：https://bugstack.cn/?id=" + arguments[0];
    }

}
