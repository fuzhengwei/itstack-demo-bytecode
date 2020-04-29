package org.itstack.demo.javassist;

public @interface RpcGatewayMethod {

    String methodName() default "";
    String methodDesc() default "";
    
}
