package org.itstack.demo.bytebuddy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcGatewayClazz {

    String clazzDesc() default "";
    String alias() default "";
    long timeOut() default 350;

}
