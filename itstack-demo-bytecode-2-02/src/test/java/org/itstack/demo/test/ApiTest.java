package org.itstack.demo.test;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.itstack.demo.bytebuddy.BizMethod;
import org.itstack.demo.bytebuddy.Monitor;
import org.itstack.demo.bytebuddy.MonitorDemo;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * 公众号：bugstack虫洞栈
 * 博客栈：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 * 本专栏是小傅哥多年从事一线互联网Java开发的学习历程技术汇总，旨在为大家提供一个清晰详细的学习教程，侧重点更倾向编写Java核心内容。如果能为您提供帮助，请给予支持(关注、点赞、分享)！
 */
public class ApiTest {

    @Test
    public void test_byteBuddy() throws Exception {

        DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
                .subclass(BizMethod.class)
                .method(ElementMatchers.named("queryUserInfo"))
                .intercept(MethodDelegation.to(MonitorDemo.class))
                .make();

        // 加载类
        Class<?> clazz = dynamicType.load(ApiTest.class.getClassLoader())
                .getLoaded();

        // 反射调用
        clazz.getMethod("queryUserInfo", String.class, String.class).invoke(clazz.newInstance(), "10001", "Adhl9dkl");

    }

}
