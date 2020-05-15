package org.itstack.demo.asm;

public class ClassLoaderUtil extends ClassLoader {

    public Class<?> defineClass(String name, byte[] b) {
        return super.defineClass(name, b, 0, b.length);
    }

}
