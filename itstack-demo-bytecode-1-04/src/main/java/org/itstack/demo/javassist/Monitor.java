package org.itstack.demo.javassist;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class Monitor {

    public static final int MAX_NUM = 1024 * 32;
    private final static AtomicInteger index = new AtomicInteger(0);
    private final static AtomicReferenceArray<MethodDescription> methodTagArr = new AtomicReferenceArray<>(MAX_NUM);

    public static int generateMethodId(String clazzName, String methodName, List<String> parameterNameList, List<String> parameterTypeList, String returnType) {

        MethodDescription methodDescription = new MethodDescription();
        methodDescription.setClazzName(clazzName);
        methodDescription.setMethodName(methodName);
        methodDescription.setParameterNameList(parameterNameList);
        methodDescription.setParameterTypeList(parameterTypeList);
        methodDescription.setReturnType(returnType);

        int methodId = index.getAndIncrement();
        if (methodId > MAX_NUM) return -1;
        methodTagArr.set(methodId, methodDescription);
        return methodId;
    }

    public static void point(final int methodId, final long startNanos, Object[] parameterValues, Object returnValues) {
        MethodDescription method = methodTagArr.get(methodId);
        System.out.println("监控 - Begin");
        System.out.println("方法：" + method.getClazzName() + "." + method.getMethodName());
        System.out.println("入参：" + JSON.toJSONString(method.getParameterNameList()) + " 入参[类型]：" + JSON.toJSONString(method.getParameterTypeList()) + " 入数[值]：" + JSON.toJSONString(parameterValues));
        System.out.println("出参：" + method.getReturnType() + " 出参[值]：" + JSON.toJSONString(returnValues));
        System.out.println("耗时：" + (System.nanoTime() - startNanos) / 1000000 + "(s)");
        System.out.println("监控 - End\r\n");
    }

    public static void point(final int methodId, Throwable throwable) {
        MethodDescription method = methodTagArr.get(methodId);
        System.out.println("监控 - Begin");
        System.out.println("方法：" + method.getClazzName() + "." + method.getMethodName());
        System.out.println("异常：" + throwable.getMessage());
        System.out.println("监控 - End\r\n");
    }

}
