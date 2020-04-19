## 资料

[http://www.javassist.org/](http://www.javassist.org/)

Javassist中最为重要的是ClassPool，CtClass ，CtMethod 以及 CtField这几个类。
ClassPool：一个基于HashMap实现的CtClass对象容器，其中键是类名称，值是表示该类的CtClass对象。默认的ClassPool使用与底层JVM相同的类路径，因此在某些情况下，可能需要向ClassPool添加类路径或类字节。
CtClass：表示一个类，这些CtClass对象可以从ClassPool获得。
CtMethods：表示类中的方法。
CtFields ：表示类中的字段。

1. ClassPool：javassist的类池，使用ClassPool 类可以跟踪和控制所操作的类，它的工作方式与 JVM 类装载器非常相似
2. CtClass： CtClass提供了类的操作，如在类中动态添加新字段、方法和构造函数、以及改变类、父类和接口的方法。
3. CtField：类的属性，通过它可以给类创建新的属性，还可以修改已有的属性的类型，访问修饰符等
4. CtMethod：类中的方法，通过它可以给类创建新的方法，还可以修改返回类型，访问修饰符等， 甚至还可以修改方法体内容代码
5. CtConstructor：与CtMethod类似

- https://www.cnblogs.com/dooor/p/5289326.html