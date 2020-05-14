package org.itstack.demo.test;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.itstack.demo.bytebuddy.UserRepositoryInterceptor;
import org.itstack.demo.bytebuddy.Repository;
import org.itstack.demo.bytebuddy.RpcGatewayClazz;
import org.itstack.demo.bytebuddy.RpcGatewayMethod;
import org.junit.Test;

import java.io.File;

public class ApiTest {

    @Test
    public void test_byteBuddy() throws Exception {

        // 生成含有注解的泛型实现字类
        DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
                .subclass(TypeDescription.Generic.Builder.parameterizedType(Repository.class, String.class).build()) // 创建复杂类型的泛型注解
                .name(Repository.class.getPackage().getName().concat(".").concat("UserRepository"))                  // 添加类信息包括地址
                .method(ElementMatchers.named("queryData"))                                                          // 匹配处理的方法
                .intercept(MethodDelegation.to(UserRepositoryInterceptor.class))                                     // 交给委托函数
                .annotateMethod(AnnotationDescription.Builder.ofType(RpcGatewayMethod.class).define("methodName", "queryData").define("methodDesc", "查询数据").build())
                .annotateType(AnnotationDescription.Builder.ofType(RpcGatewayClazz.class).define("alias", "dataApi").define("clazzDesc", "查询数据信息").define("timeOut", 350L).build())
                .make();

        // 输出类信息到目标文件夹下
        dynamicType.saveIn(new File(ApiTest.class.getResource("/").getPath()));

        // 从目标文件夹下加载类信息
        Class<Repository<String>> repositoryClass = (Class<Repository<String>>) Class.forName("org.itstack.demo.bytebuddy.UserRepository");

        // 获取类注解
        RpcGatewayClazz rpcGatewayClazz = repositoryClass.getAnnotation(RpcGatewayClazz.class);
        System.out.println("RpcGatewayClazz.clazzDesc：" + rpcGatewayClazz.clazzDesc());
        System.out.println("RpcGatewayClazz.alias：" + rpcGatewayClazz.alias());
        System.out.println("RpcGatewayClazz.timeOut：" + rpcGatewayClazz.timeOut());

        // 获取方法注解
        RpcGatewayMethod rpcGatewayMethod = repositoryClass.getMethod("queryData", int.class).getAnnotation(RpcGatewayMethod.class);
        System.out.println("RpcGatewayMethod.methodName：" + rpcGatewayMethod.methodName());
        System.out.println("RpcGatewayMethod.methodDesc：" + rpcGatewayMethod.methodDesc());

        // 实例化对象
        Repository<String> repository = repositoryClass.newInstance();

        // 测试输出
        System.out.println(repository.queryData(10001));
    }

}
