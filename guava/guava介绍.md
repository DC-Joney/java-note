### Guava 的基本工具使用

## 1. 基本工具 [Basic utilities]

让使用Java语言变得更舒适

1.1 Optionsl 类比于 java8的 Optional

1.2 [前置条件](http://ifeve.com/google-guava-preconditions/): 让方法中的条件检查更简单

1.3 Objects 工具类 类比于 java的 Objects

1.4 [排序: Guava强大的”流畅风格比较器”](http://ifeve.com/google-guava-ordering/)

1.5 [Throwables](http://ifeve.com/google-guava-throwables/)：简化了异常和错误的传播与检查

## 2. 集合[Collections]

Guava对JDK集合的扩展，这是Guava最成熟和为人所知的部分

2.1 [不可变集合](http://ifeve.com/google-guava-immutablecollections/): 用不变的集合进行防御性编程和性能提升。

2.2 [新集合类型](http://ifeve.com/google-guava-newcollectiontypes/): multisets, multimaps, tables, bidirectional maps等

2.3 [强大的集合工具类](http://ifeve.com/google-guava-collectionutilities/): 提供java.util.Collections中没有的集合工具

2.4 [扩展工具类](http://ifeve.com/google-guava-collectionhelpersexplained/)：让实现和扩展集合类变得更容易，比如创建`Collection的装饰器，或实现迭代器`

## 3. [缓存](http://ifeve.com/google-guava-cachesexplained)[Caches]

Guava Cache：本地缓存实现，支持多种缓存过期策略

## 4. [函数式风格](http://ifeve.com/google-guava-functional/)[Functional idioms]

Guava的函数式支持可以显著简化代码，但请谨慎使用它

## 5. 并发[Concurrency]

强大而简单的抽象，让编写正确的并发代码更简单

5.1 [ListenableFuture](http://ifeve.com/google-guava-listenablefuture/)：完成后触发回调的Future

5.2 [Service框架](http://ifeve.com/google-guava-serviceexplained/)：抽象可开启和关闭的服务，帮助你维护服务的状态逻辑

## 6. [字符串处理](http://ifeve.com/google-guava-strings/)[Strings]

非常有用的字符串工具，包括分割、连接、填充等操作

## 7. [原生类型](http://ifeve.com/google-guava-primitives/)[Primitives]

扩展 JDK 未提供的原生类型（如int、char）操作， 包括某些类型的无符号形式

## 8. [区间](http://ifeve.com/google-guava-ranges/)[Ranges]

可比较类型的区间API，包括连续和离散类型

## 9. [I/O](http://ifeve.com/google-guava-io/)

简化I/O尤其是I/O流和文件的操作，针对Java5和6版本

## 10. [散列](http://ifeve.com/google-guava-hashing/)[Hash]

提供比`Object.hashCode()`更复杂的散列实现，并提供布鲁姆过滤器的实现

## 11. [事件总线](http://ifeve.com/google-guava-eventbus/)[EventBus]

发布-订阅模式的组件通信，但组件不需要显式地注册到其他组件中

## 12. [数学运算](http://ifeve.com/google-guava-math/)[Math]

优化的、充分测试的数学工具类

## 13. [反射](http://ifeve.com/guava-reflection/)[Reflection]

Guava 的 Java 反射机制工具类

