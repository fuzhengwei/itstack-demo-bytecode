package org.itstack.demo.test;

import javassist.*;
import javassist.bytecode.*;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.LongMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

public class HelloWorld {

    public static void main(String[] args) throws Exception {

        ClassPool pool = ClassPool.getDefault();

        // 创建类信息
        CtClass ctClass = pool.makeClass("org.itstack.demo.javassist.HelloWorld");

        // 添加方法
        CtMethod mainMethod = new CtMethod(CtClass.doubleType, "queryInterestFee", new CtClass[]{pool.get(String.class.getName())}, ctClass);
        mainMethod.setModifiers(Modifier.PUBLIC);

        MethodInfo methodInfo = mainMethod.getMethodInfo();

        ConstPool cp = methodInfo.getConstPool();

        // 类添加注解
        AnnotationsAttribute clazzAnnotationsAttribute = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
        Annotation clazzAnnotation = new Annotation("org/itstack/demo/javassist/RpcGatewayClazz", cp);
        clazzAnnotation.addMemberValue("clazzDesc", new StringMemberValue("用户信息查询服务", cp));
        clazzAnnotation.addMemberValue("alias", new StringMemberValue("api", cp));
        clazzAnnotation.addMemberValue("timeOut", new LongMemberValue(500L, cp));
        clazzAnnotationsAttribute.setAnnotation(clazzAnnotation);
        ctClass.getClassFile().addAttribute(clazzAnnotationsAttribute);

        // 方法添加注解
        AnnotationsAttribute methodAnnotationsAttribute = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
        Annotation methodAnnotation = new Annotation("org/itstack/demo/javassist/RpcGatewayMethod", cp);
        methodAnnotation.addMemberValue("methodName", new StringMemberValue("查询息费", cp));
        methodAnnotation.addMemberValue("methodDesc", new StringMemberValue("interestFee", cp));
        methodAnnotationsAttribute.setAnnotation(methodAnnotation);
        methodInfo.addAttribute(methodAnnotationsAttribute);

        // 指令控制
        Bytecode bytecode = new Bytecode(cp);

        bytecode.addGetstatic("java/math/BigDecimal", "TEN", "Ljava/math/BigDecimal;");
        bytecode.addInvokevirtual("java/math/BigDecimal", "doubleValue", "()D");
        bytecode.addReturn(CtClass.doubleType);

        methodInfo.setCodeAttribute(bytecode.toCodeAttribute());

        ctClass.addMethod(mainMethod);

        ctClass.writeFile();

    }

}
