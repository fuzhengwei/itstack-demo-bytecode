package org.itstack.demo.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.*;

public class GenerateClazzMethod {

    public static void main(String[] args) throws Exception {

        ClassPool pool = ClassPool.getDefault();
        // 类、注解
        CtClass ctClass = pool.get(ApiTest.class.getName());
        // 通过集合获取自定义注解
        Object[] clazzAnnotations = ctClass.getAnnotations();
        RpcGatewayClazz rpcGatewayClazz = (RpcGatewayClazz) clazzAnnotations[0];
        System.out.println("RpcGatewayClazz.clazzDesc：" + rpcGatewayClazz.clazzDesc());
        System.out.println("RpcGatewayClazz.alias：" + rpcGatewayClazz.alias());
        System.out.println("RpcGatewayClazz.timeOut：" + rpcGatewayClazz.timeOut());

        // 方法、注解
        CtMethod ctMethod = ctClass.getDeclaredMethod("queryInterestFee");
        RpcGatewayMethod rpcGatewayMethod = (RpcGatewayMethod) ctMethod.getAnnotation(RpcGatewayMethod.class);
        System.out.println("RpcGatewayMethod.methodName：" + rpcGatewayMethod.methodName());
        System.out.println("RpcGatewayMethod.methodDesc：" + rpcGatewayMethod.methodDesc());

        // 获取指令码
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        CodeIterator iterator = codeAttribute.iterator();
        while (iterator.hasNext()) {
            int idx = iterator.next();
            int code = iterator.byteAt(idx);
            System.out.println("指令码：" + idx + " > " + Mnemonic.OPCODE[code]);
        }

        // 通过指令码改写方法
        ConstPool cp = methodInfo.getConstPool();
        Bytecode bytecode = new Bytecode(cp);
        bytecode.addDconst(0);
        bytecode.addReturn(CtClass.doubleType);
        methodInfo.setCodeAttribute(bytecode.toCodeAttribute());

        // 输出字节码
        ctClass.writeFile();

    }

}
