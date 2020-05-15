package org.itstack.demo.asm;

import com.alibaba.fastjson.JSON;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.ASM5;

public class MethodTest extends ClassLoader {

    // 测试的方法
    public Integer strToNumber(String str) {
        return Integer.parseInt(str);
    }

    public static void main(String[] args) throws Exception {
        byte[] bytes = new MethodTest().getBytes(MethodTest.class.getName());
        // 输出方法
        outputClazz(bytes, MethodTest.class.getSimpleName());

        // 测试方法
        Class<?> clazz = new MethodTest().defineClass("org.itstack.test.MethodTest", bytes, 0, bytes.length);
        Method queryUserInfo = clazz.getMethod("strToNumber", String.class);

        // 正确入参
        Object obj01 = queryUserInfo.invoke(clazz.newInstance(), "123");
        System.out.println("01 测试结果：" + obj01);

        // 异常入参
        Object obj02 = queryUserInfo.invoke(clazz.newInstance(), "abc");
        System.out.println("02 测试结果：" + obj02);
    }

    /**
     * 字节码增强获取新的字节码
     */
    private byte[] getBytes(String className) throws IOException {

        ClassReader cr = new ClassReader(className);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        cr.accept(new ClassVisitor(ASM5, cw) {

            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {

                // 方法过滤
                if (!"strToNumber".equals(name))
                    return super.visitMethod(access, name, descriptor, signature, exceptions);

                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

                return new AdviceAdapter(ASM5, mv, access, name, descriptor) {

                    private Label from = new Label(),
                            to = new Label(),
                            target = new Label();

                    @Override
                    protected void onMethodEnter() {
                        //标志：try块开始位置
                        visitLabel(from);
                        visitTryCatchBlock(from,
                                to,
                                target,
                                "java/lang/Exception");
                    }

                    @Override
                    public void visitMaxs(int maxStack, int maxLocals) {

                        //标志：try块结束
                        mv.visitLabel(to);
                        //标志：catch块开始位置
                        mv.visitLabel(target);

                        mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{"java/lang/Exception"});

                        // 异常信息保存到局部变量
                        int local = newLocal(Type.LONG_TYPE);
                        mv.visitVarInsn(ASTORE, local);

                        // 输出信息
                        mv.visitLdcInsn(className + "." + name);  // 类名.方法名
                        mv.visitVarInsn(ALOAD, local);
                        mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(MethodTest.class), "point", "(Ljava/lang/String;Ljava/lang/Throwable;)V", false);

                        // 抛出异常
                        mv.visitVarInsn(ALOAD, local);
                        mv.visitInsn(ATHROW);

                        super.visitMaxs(maxStack, maxLocals);
                    }

                    @Override
                    protected void onMethodExit(int opcode) {
                        if ((IRETURN <= opcode && opcode <= RETURN) || opcode == ATHROW) {
                            int nextLocal = this.nextLocal;
                            mv.visitVarInsn(ASTORE, nextLocal); // 将栈顶引用类型值保存到局部变量indexbyte中。
                            mv.visitVarInsn(ALOAD, nextLocal);  // 从局部变量indexbyte中装载引用类型值入栈。

                            mv.visitLdcInsn(className + "." + name);  // 类名.方法名
                            mv.visitVarInsn(ALOAD, nextLocal);
                            mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(MethodTest.class), "point", "(Ljava/lang/String;Ljava/lang/Object;)V", false);
                        }
                    }
                };
            }
        }, ClassReader.EXPAND_FRAMES);

        return cw.toByteArray();
    }

    /**
     * 输出字节码
     *
     * @param bytes     字节码
     * @param className 类名称
     */
    private static void outputClazz(byte[] bytes, String className) {
        // 输出类字节码
        FileOutputStream out = null;
        try {
            String pathName = MethodTest.class.getResource("/").getPath() + className + "SQM.class";
            out = new FileOutputStream(new File(pathName));
            System.out.println("ASM字节码增强后类输出路径：" + pathName + "\r\n");
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

    public static void point(String methodName, Throwable throwable) {
        System.out.println("系统监控 :: [方法名称：" + methodName + " 异常信息：" + throwable.getMessage() + "]\r\n");
    }

    public static void point(String methodName, Object response) {
        System.out.println("系统监控 :: [方法名称：" + methodName + " 输出信息：" + JSON.toJSONString(response) + "]\r\n");
    }

}