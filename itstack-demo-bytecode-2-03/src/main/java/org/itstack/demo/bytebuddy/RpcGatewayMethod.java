package org.itstack.demo.bytebuddy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RpcGatewayMethod {

    String methodName() default "";
    String methodDesc() default "";
    
}
