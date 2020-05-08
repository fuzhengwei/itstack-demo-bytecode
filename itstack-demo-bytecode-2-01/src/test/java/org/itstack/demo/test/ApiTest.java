package org.itstack.demo.test;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.itstack.demo.bytebuddy.GenerateClazzMethod;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static net.bytebuddy.matcher.ElementMatchers.named;

// FixedValue.value("Hello World!")

/**
 * https://notes.diguage.com/byte-buddy-tutorial/
 * https://blog.csdn.net/undergrowth/article/details/86493336
 * https://yanbin.blog/leverage-bytebuddy-generate-generic-subclass/#more-7792
 * https://bytebuddy.net/
 * http://www.liuhaihua.cn/archives/562927.html
 * https://my.oschina.net/valsong/blog/2988403
 * http://rui0.cn/archives/1063
 */
public class ApiTest {

    @Test
    public void t(){
        DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .name("org.itstack.demo.bytebuddy.HelloWorld")
                .defineMethod("main", void.class, Modifier.PUBLIC + Modifier.STATIC)
                .withParameter(String[].class, "args")
                .intercept(MethodDelegation.to(Hi.class))
                .make();

        // 输出类字节码
        outputClazz(dynamicType.getBytes());
    }


    @Test
    public void test_make() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // 创建类
        DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .name("org.itstack.demo.bytebuddy.HelloWorld")                                         // 类名信息
                .defineMethod("main", void.class, Modifier.PUBLIC + Modifier.STATIC) // 定义方法
                .withParameter(String[].class, "args")                                          // 设置参数
                .intercept(MethodDelegation.to(Hi.class))
                .defineField("str", String.class, Modifier.PUBLIC)
                .make();

        // 加载类
        Class<?> clazz = dynamicType.load(GenerateClazzMethod.class.getClassLoader())
                .getLoaded();

        // 输出类字节码
        outputClazz(dynamicType.getBytes());

        // 反射调用
        clazz.getMethod("main", String[].class).invoke(clazz.newInstance(), (Object) new String[1]);
    }

    public static class Hi {
        public static void main(String[] args) {
            System.out.println("helloWorld");
        }
    }

    @Test
    public void test_Hi() throws IllegalAccessException, InstantiationException {
        String helloWorld = new ByteBuddy()
                .subclass(Object.class)
                .method(named("toString"))
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance()
                .toString();

        System.out.println(helloWorld);
    }

    @Test
    public void test_helloWorld() throws IllegalAccessException, InstantiationException {
        DynamicType.Unloaded<Object> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .name("org.itstack.demo.bytebuddy.HelloWorld")
                .method(named("toString")).intercept(FixedValue.value("Hello World!"))
                .make();

        // 输出类字节码
        outputClazz(dynamicType.getBytes());

        String toString = dynamicType.load(getClass().getClassLoader())
                .getLoaded()
                .newInstance()
                .toString();

        System.out.println(toString);

    }

    private static void outputClazz(byte[] bytes) {
        FileOutputStream out = null;
        try {
            String pathName = ApiTest.class.getResource("/").getPath() + "ByteBuddyHelloWorld.class";
            out = new FileOutputStream(new File(pathName));
            System.out.println("类输出路径：" + pathName);
            out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != out) try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

