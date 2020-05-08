package org.itstack.demo.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

public class GenerateClazzMethod {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {

        Class<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.named("toString"))
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(GenerateClazzMethod.class.getClassLoader())
                .getLoaded();

        String str = dynamicType.newInstance().toString();
        System.out.println(str);
    }

}
