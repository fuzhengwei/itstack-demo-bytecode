package org.itstack.demo.javassist;

public @interface RpcGatewayClazz {

    String clazzDesc() default "";
    String alias() default "";
    long timeOut() default 350;

}
