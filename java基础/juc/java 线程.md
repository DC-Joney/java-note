####多线程的几种实现方式，什么是线程安全。

```
继承Thread类

看jdk源码可以发现，Thread类其实是实现了Runnable接口的一个实例，继承Thread类后需要重写run方法并通过start方法启动线程。

继承Thread类耦合性太强了，因为java只能单继承，所以不利于扩展。

2、实现Runnable接口
通过实现Runnable接口并重写run方法，并把Runnable实例传给Thread对象，Thread的start方法调用run方法再通过调用Runnable实例的run方法启动线程。

所以如果一个类继承了另外一个父类，此时要实现多线程就不能通过继承Thread的类实现。



3、实现Callable接口
通过实现Callable接口并重写call方法，并把Callable实例传给FutureTask对象，再把FutureTask对象传给Thread对象。它与Thread、Runnable最大的不同是Callable能返回一个异步处理的结果Future对象并能抛出异常，而其他两种不能。
```



####Thread start方法和 Thread run方法

Run方法会在Thread.c文件中使用JVM_startThread 类启动线程然后执行 run方法

 

![img](C:\Users\Administrator\Desktop\面试\面试题\java基础\juc\assets/wpsE510.tmp.jpg)



####ForkJoin框架的作用？



#### 常用的lock有什么？

```
synchorized ，Lock 和 ReadWriteLock

Lock 实现 公平锁和非公平锁

ReadWriteLock来实现读写锁
```





####CompletionService的作用？

```
CompletionService 主要是为了大量相同任务的执行，
可以优先拿到已经执行完成的任务
a-b-c （三个相同任何）
CompletionService.submit();

CompletionService.take() 可以获取到最先执行完成的任务
```



####线程池分为几种？

```
线程池分为三种
Executor
	ExecutorService
		AbstractExecutorService
			ThreadPoolExecutor
				ScheuldThreadPoolExecutor
			ForkJoinPool
Executors.newFixedThreadPool();
Executors.newCacheThreadPool();
Executors.newSingleThreadPool();
Executors.newScheduledThreadPool()
Executors.newWorkStealingPool();
```





####Volitile 关键字和 jmm内存

实现多线程间的可见性

阻止指令的冲排序



jmm内存

![1572214633036](../assets\1572214633036.png)





#### java并发的安全集合

```
CopyOnWriteArraySet
CopyOnWriteArrayList（弊端 Object[] newElements = Arrays.copyOf(elements, len + 1);）
ConcurrentHashMap

ConcurrentSkipListSet (有序set)
ConcurrentSkipListMap(有序map)（底层是用cas来进行完成的）
```







####Synchornized 和 lock的区别

![1572211649903](../assets\1572211649903.png)



####volatile的原理，作用，能代替锁么。







####画一个线程的生命周期状态图。

![1572212260340](../assets\1572212260340.png)

![1572212293354](../assets\1572212293354.png)



####sleep和wait的区别。

```
1. Wait方法是object的方法,Sleep是Thread中的方法
2. sleep方法可以在任何地方使用,Wait方法只能在synchorized方法或者synchorized代码块中进行使用
3. Thread.sleep只会让出cpu，不会导致锁行为的改变
  Object.wait不仅让出cpu，还会释放已经占有的同步资源
```





####sleep和sleep(0)的区别。

```
当 timeout = 0， 即 Sleep(0)，如果线程调度器的可运行队列中有大于或等于当前线程优先级的就绪线程存在，操作系统会将当前线程从处理器上移除，调度其他优先级高的就绪线程运行；如果可运行队列中的没有就绪线程或所有就绪线程的优先级均低于当前线程优先级，那么当前线程会继续执行，就像没有调用 Sleep(0)一样。

当 timeout > 0 时，如：Sleep(1)，会引发线程上下文切换：调用线程会从线程调度器的可运行队列中被移除一段时间，这个时间段约等于 timeout 所指定的时间长度。为什么说约等于呢？是因为睡眠时间单位为毫秒，这与系统的时间精度有关。通常情况下，系统的时间精度为 10 ms，那么指定任意少于 10 ms但大于 0 ms 的睡眠时间，均会向上求值为 10 m
```

![1573124427944](assets\1573124427944.png)



![1573124439722](assets\1573124439722.png)





####synchronized的原理是什么，一般用在什么地方(比如加在静态方法和非静态方法的区别，静态方法和非静态方法同时执行的时候会有影响吗)，解释以下名词：重排序，自旋锁，偏向锁，轻量级锁，可重入锁，公平锁，非公平锁，乐观锁，悲观锁。

```

```





####用过哪些原子类，他们的原理是什么。

````
AtomicInteger(针对 int类型)
AtomicBoolean（针对boolean类型）
AtomicLong（针对long类型）
AtomicReference（针对引用类型）
AtomicIntegerFieldUpdater
AtomicLongFieldUpdater
AtomicReferenceFieldUpdater
````









####JUC下研究过哪些并发工具，讲讲原理。

Semphare, Countdowlatch, Cyclicbarrier





####用过线程池吗，如果用过，请说明原理，并说说newCache和newFixed有什么区别，构造函数的各个参数的含义是什么，比如coreSize，maxsize等。

```

```





####线程池的关闭方式有几种，各自的区别是什么。

两种

```
shutdown 平滑关闭，如过线程池中还有正在工作的任务，则等待工作任务完成后关闭
shutdownNow ： 不管线程池中是否有任务，直接关闭
```





####假如有一个第三方接口，有很多个线程去调用获取数据，现在规定每秒钟最多有10个线程同时调用它，如何做到。

```
Semaphore : 是一个计数信号量,可以设置许可证的数量
```





####spring的controller是单例还是多例，怎么保证并发的安全。

```
spring的controller是单例的
```



####用三个线程按顺序循环打印abc三个字母，比如abcabcabc。

```
lock synchorized 依赖于 锁的排他性
```





####ThreadLocal用过么，用途是什么，原理是什么，用的时候要注意什么。

```
ThreaLocal 是将自定义存储的数据和当前线程进行绑定

```





####如果让你实现一个并发安全的链表，你会怎么做。

使用cas算法来进行识别





####有哪些无锁数据结构，他们实现的原理是什么。

```
ConcurrentListQueue、LinkedTrsformQueue
```





####讲讲java同步机制的wait和notify。

```
wait方法会让当前线程进入waiting状态，并且将当前线程加入到对象锁关联的ObjectMonitor中的waitSet中，在waitSet中的线程不允许再去争抢 该锁，而notify则是随机的从waitSet中 获取一条等待的线程放入到entryList（锁池）中
```



####CAS机制是什么，如何解决ABA问题。

```
AtomicStampedReference（内置为int类型版本号）
AtomicMarkableReference（内置为boolean类型版本号）
```









####countdowlatch和cyclicbarrier的内部原理和用法，以及相互之间的差别(比如countdownlatch的await方法和是怎么实现的)。







####对AbstractQueuedSynchronizer了解多少，讲讲加锁和解锁的流程，独占锁和公平所加锁有什么不同。

```
AQS核心点：
	
```











####使用synchronized修饰静态方法和非静态方法有什么区别。

synchorized 修饰的静态方法 是使用当前类对象的lock monitor，非静态方法使用的是 当前对象的lock monitor



####简述ConcurrentLinkedQueue和LinkedBlockingQueue的用处和不同之处。

ConcurrentLinkedQueue不是阻塞的队列，内部并没有使用到锁，而是使用的cas来更新添加队列节点



而LinkedBlockingQueue是阻塞队列，内部使用的是lock的Condition来进行队列阻塞，而且 再添加删除节点的手都是使用的lock锁来操作的，内置两种锁一种是 takeLock 一种是offerLock





####导致线程死锁的原因？怎么解除线程死锁。

导致线程的死锁的原因可能是A拥有lockA 想竞争lockB  ，而 线程B 拥有lockB 想竞争lockA

可以使用ThreadMXBean来监控线程，或者使用jconsole 、 jvm可视化工具、jstack 来 查看



####非常多个线程（可能是不同机器），相互之间需要等待协调，才能完成某种工作，问怎么设计这种协调方案。



####用过读写锁吗，原理是什么，一般在什么场景下用。

```
使用的是 ReadWriteLock来实现的读写锁，内部默认是使用的 
```



####开启多个线程，如果保证顺序执行，有哪几种实现方式，或者如何保证多个线程都执行完再拿到结果。

```
使用公平锁来实现
使用Cycivaaire实现

```



#### Thread.interrupt()

![img](assets/wps8C90.tmp.jpg) 

![img](C:\Users\Administrator\Desktop\面试\面试题\java基础\assets/wps8CCF.tmp.jpg) 





####延迟队列的实现方式，delayQueue和时间轮算法的异同。

