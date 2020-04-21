package org.itstack.demo.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.util.HotSwapper;

/**
 * 公众号：bugstack虫洞栈
 * 博客栈：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 * 本专栏是小傅哥多年从事一线互联网Java开发的学习历程技术汇总，旨在为大家提供一个清晰详细的学习教程，侧重点更倾向编写Java核心内容。如果能为您提供帮助，请给予支持(关注、点赞、分享)！
 */
public class GenerateClazzMethod {

    public static void main(String[] args) throws Exception {

        ApiTest apiTest = new ApiTest();
        System.out.println("你到底几个前女友！！！");

        new Thread(() -> {
            while (true){
                System.out.println(apiTest.queryGirlfriendCount("谢飞机"));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        
        // 监听 8000 端口,在启动参数里设置
        // java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000
        HotSwapper hs = new HotSwapper(8000);

        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get(ApiTest.class.getName());

        // 获取方法
        CtMethod ctMethod = ctClass.getDeclaredMethod("queryGirlfriendCount");
        // 重写方法
        ctMethod.setBody("{ return $1 + \"的前女友数量：\" + (0L) + \" 个\"; }");

        // 加载新的类
        System.out.println(":: 执行HotSwapper热插拔，修改谢飞机前女友数量为0个！");
        hs.reload(ApiTest.class.getName(), ctClass.toBytecode());

    }

}
