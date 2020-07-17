### ClassNotFoundException 与 NoClassDefFoundError

###### 问：简单说说 Java 中 ClassNotFoundException 与 NoClassDefFoundError 的区别？

答：当 JVM 或 ClassLoader 在加载类时找不到对应类就会引发 NoClassDefFoundError 和 ClassNotFoundException，它们的区别如下：

- NoClassDefFoundError 和 ClassNotFoundException 都是由于在 CLASSPATH 下找不到对应的类而引起的。**当应用运行时没有找到对应的引用类就会抛出 NoClassDefFoundError，当在代码中通过类名显式加载类（如使用 Class.forName()）时没有找到对应的类就会抛出 ClassNotFoundException。**
- 再稍微详细点说，**NoClassDefFoundError 表示该类在编译阶段可以找到，但在运行时找不到了，另外有时静态块的初始化过程也会导致 NoClassDefFoundError。而 ClassNotFoundException 一般发生在通过反射或者 ClassLoader 依据类名加载类时类不存在。**
- **此外 NoClassDefFoundError 是 Error，是不受检查类型的异常；而 ClassNotFoundException 是受检查类型的异常，需要进行异常捕获，否则会导致编译错误。**
- 再深入来说，**NoClassDefFoundError 是链接错误，发生在 JVM 类加载流程的链接阶段，当解析引用的时候找不到对应的类就会抛出 NoClassDefFoundError；而 ClassNotFoundException 一般发生在类加载流程的加载阶段。**

![img](https:////upload-images.jianshu.io/upload_images/7182360-1f16caeda2adce55?imageMogr2/auto-orient/strip|imageView2/2/w/627/format/webp)