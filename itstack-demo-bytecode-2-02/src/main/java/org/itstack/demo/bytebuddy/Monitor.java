package org.itstack.demo.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.Callable;

/**
 * 公众号：bugstack虫洞栈
 * 博客栈：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 * 本专栏是小傅哥多年从事一线互联网Java开发的学习历程技术汇总，旨在为大家提供一个清晰详细的学习教程，侧重点更倾向编写Java核心内容。如果能为您提供帮助，请给予支持(关注、点赞、分享)！
 */
public class Monitor {

    @RuntimeType
    public static Object intercept(@Origin Method method, @AllArguments Object[] objs, @Argument(0) Object obj1, @SuperCall Callable<?> callable) throws Exception {
        long start = System.currentTimeMillis();

        System.out.println(objs[0].toString());

        System.out.println(obj1);

        Parameter[] parameters = method.getParameters();
        System.out.println(parameters[0].getName());

        Class<?>[] parameterTypes = method.getParameterTypes();
        System.out.println(parameterTypes[0].getName());

        System.out.println("方法名称：" + method.getName());
        int parameterCount = method.getParameterCount();
        System.out.println("入参个数：" + parameterCount);
        Class<?> returnType = method.getReturnType();
        System.out.println("出参类型：" + returnType.getName());

        try {
            return callable.call();
        } finally {
            System.out.println("方法耗时：" + (System.currentTimeMillis() - start) + "ms");
        }
    }

}
