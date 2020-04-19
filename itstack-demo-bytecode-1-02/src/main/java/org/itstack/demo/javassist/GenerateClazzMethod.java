package org.itstack.demo.javassist;

import javassist.*;

import java.io.IOException;

/**
 * 公众号：bugstack虫洞栈
 * 博客栈：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 * 本专栏是小傅哥多年从事一线互联网Java开发的学习历程技术汇总，旨在为大家提供一个清晰详细的学习教程，侧重点更倾向编写Java核心内容。如果能为您提供帮助，请给予支持(关注、点赞、分享)！
 */
public class GenerateClazzMethod {

    public static void main(String[] args) throws CannotCompileException, NotFoundException, IOException {

        ClassPool pool = ClassPool.getDefault();

        CtClass ctClass = pool.makeClass("org.itstack.demo.javassist.Circular");

        CtField ctField = new CtField(CtClass.doubleType, "π", ctClass);
        ctField.setModifiers(Modifier.PRIVATE);
        ctClass.addField(ctField);

        CtMethod ctMethod = new CtMethod(CtClass.doubleType, "calculateCircularArea", new CtClass[]{CtClass.doubleType}, ctClass);
        ctMethod.setModifiers(Modifier.PUBLIC);
        ctMethod.setBody("{return π * $1 * $1;}");

        ctClass.addMethod(ctMethod);

        // 输出类的内容
        ctClass.writeFile();

    }

}
