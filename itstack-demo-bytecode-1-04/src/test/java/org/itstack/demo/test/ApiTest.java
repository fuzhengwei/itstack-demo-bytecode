package org.itstack.demo.test;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.itstack.demo.javassist.Monitor;

import java.util.ArrayList;
import java.util.List;

public class ApiTest {

    public static void main(String[] args) throws Exception {

        ClassPool pool = ClassPool.getDefault();

        // 获取类
        CtClass ctClass = pool.get(org.itstack.demo.javassist.ApiTest.class.getName());
        ctClass.replaceClassName("ApiTest", "ApiTest02");
        String clazzName = ctClass.getName();

        // 获取方法
        CtMethod ctMethod = ctClass.getDeclaredMethod("strToInt");
        String methodName = ctMethod.getName();

        // 方法信息
        MethodInfo methodInfo = ctMethod.getMethodInfo();

        // 方法类型；静态方法判断
        boolean isStatic = (methodInfo.getAccessFlags() & AccessFlag.STATIC) != 0;

        // 方法：入参信息{名称和类型}
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        CtClass[] parameterTypes = ctMethod.getParameterTypes();

        // 方法；出参信息
        CtClass returnType = ctMethod.getReturnType();
        String returnTypeName = returnType.getName();

        System.out.println("类名：" + clazzName);
        System.out.println("方法：" + methodName);
        System.out.println("类型：" + (isStatic ? "静态方法" : "非静态方法"));
        System.out.println("描述：" + methodInfo.getDescriptor());
        System.out.println("入参[名称]：" + attr.variableName(1) + "，" + attr.variableName(2));
        System.out.println("入参[类型]：" + parameterTypes[0].getName() + "，" + parameterTypes[1].getName());
        System.out.println("出参[类型]：" + returnTypeName);

        int parameterSize = isStatic ? attr.tableLength() : attr.tableLength() - 1; // 静态类型取值
        List<String> parameterNameList = new ArrayList<>(parameterSize);            // 入参名称
        List<String> parameterTypeList = new ArrayList<>(parameterSize);            // 入参类型
        StringBuilder parameters = new StringBuilder();                             // 参数组装；$1、$2...，$$可以获取全部，但是不能放到数组初始化

        for (int i = 0; i < parameterSize; i++) {
            parameterNameList.add(attr.variableName(i + (isStatic ? 0 : 1))); // 静态类型去掉第一个this参数
            parameterTypeList.add(parameterTypes[i].getName());
            if (i + 1 == parameterSize) {
                parameters.append("$").append(i + 1);
            } else {
                parameters.append("$").append(i + 1).append(",");
            }
        }

        // 方法：生成方法唯一标识ID
        int idx = Monitor.generateMethodId(clazzName, methodName, parameterNameList, parameterTypeList, returnTypeName);

        // 定义属性
        ctMethod.addLocalVariable("startNanos", CtClass.longType);
        // 方法前加强
        ctMethod.insertBefore("{ startNanos = System.nanoTime(); }");

        // 定义属性
        ctMethod.addLocalVariable("parameterValues", pool.get(Object[].class.getName()));
        
        // 方法前加强
        ctMethod.insertBefore("{ parameterValues = new Object[]{" + parameters.toString() + "}; }");

        // 方法后加强
        ctMethod.insertAfter("{ org.itstack.demo.javassist.Monitor.point(" + idx + ", startNanos, parameterValues, $_);}", false); // 如果返回类型非对象类型，$_ 需要进行类型转换

        // 方法；添加TryCatch
        ctMethod.addCatch("{ org.itstack.demo.javassist.Monitor.point(" + idx + ", $e); throw $e; }", ClassPool.getDefault().get("java.lang.Exception"));   // 添加异常捕获

        // 输出类的内容
        ctClass.writeFile();
    }

}
