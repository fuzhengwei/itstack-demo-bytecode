package org.itstack.demo.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class SumOfTwo extends ClassLoader {

    public static void main(String[] args) throws Exception {
        // 生成二进制字节码
        byte[] bytes = generate();
        // 输出字节码
        outputClazz(bytes);
        // 加载AsmHelloWorld
        Class<?> clazz = new SumOfTwo().defineClass("org.itstack.demo.asm.AsmSumOfTwo", bytes, 0, bytes.length);
        // 反射获取 main 方法
        Method main = clazz.getMethod("main", String[].class);
        // 调用 main 方法
        main.invoke(null, new Object[]{new String[]{}});
    }

    private static byte[] generate() {

        ClassWriter classWriter = new ClassWriter(0);

        {
            MethodVisitor  mv = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        // 定义对象头；版本号、修饰符、全类名、签名、父类、实现的接口
        classWriter.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC, "org/itstack/demo/asm/AsmSumOfTwo", null, "java/lang/Object", null);
        // 添加方法；修饰符、方法名、描述符、签名、异常
        MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);

        methodVisitor.visitTypeInsn(Opcodes.NEW, "org/itstack/demo/asm/AsmSumOfTwo");
        methodVisitor.visitInsn(Opcodes.DUP);
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "org/itstack/demo/asm/AsmSumOfTwo", "<init>", "()V");
        methodVisitor.visitInsn(Opcodes.ICONST_1);
        methodVisitor.visitInsn(Opcodes.ICONST_2);
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/itstack/demo/asm/AsmSumOfTwo", "sum", "(II)I");
        methodVisitor.visitVarInsn(Opcodes.ISTORE, 1);
        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        methodVisitor.visitVarInsn(Opcodes.ILOAD, 1);
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V");
        // 返回
        methodVisitor.visitInsn(Opcodes.RETURN);

        // 设置操作数栈的深度和局部变量的大小
        methodVisitor.visitMaxs(3, 2);
        // 方法结束
        methodVisitor.visitEnd();

        /**
         * public int sum(int i, int m) {
         *     return i + m;
         * }
         */
        MethodVisitor methodVisitor_sum = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "sum", "(II)I", null, null);
        methodVisitor_sum.visitVarInsn(Opcodes.ILOAD, 1);
        methodVisitor_sum.visitVarInsn(Opcodes.ILOAD, 2);
        methodVisitor_sum.visitInsn(Opcodes.IADD);
        // 返回
        methodVisitor_sum.visitInsn(Opcodes.IRETURN);
        // 设置操作数栈的深度和局部变量的大小
        methodVisitor_sum.visitMaxs(2, 3);
        methodVisitor_sum.visitEnd();

        // 类完成
        classWriter.visitEnd();
        // 生成字节数组
        return classWriter.toByteArray();
    }

    private static void outputClazz(byte[] bytes) {
        // 输出类字节码
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("AsmSumOfTwo.class");
            System.out.println("ASM类输出路径：" + (new File("")).getAbsolutePath());
            out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != out) try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
