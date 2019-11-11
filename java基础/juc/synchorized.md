```
synchorized
	原理
		synchorized关键字提供了一种锁的机制，能够确保共享变量的互斥访问，从而防止数据不一致的问题的出现
		synchorized关键字包括monitor enter和 monitorexit 两个jvm指令，他能够保证 在如何时候任何线程 执行到minitor enter成功之前都必须从主内存中获取数据，而不是从缓存中，在monitor enter运行成功之后共享变量被更新后的值必须刷入主内存
		synchorized的指令严格遵守java hanppens-before原则，一个monitor exit指令之前必须有一个monitor enter指令
	底层原理
		实现基础
			java对象头
			monitor
				竞争、获取和释放
				字节码
					monitorenter
					monitorexit
				方法字节码
		重入锁
		synchorized的改变
			早期
			jdk1.6以后
				自旋锁
					如过自旋到一定的次数仍然没有拿到锁的话
					使用jvm参数PreBlockSpin
				自适应自选锁
					自旋次数不再固定
					由前一次在同一个锁上的自旋时间以及锁的拥有者的状态来决定
						如过正在运行的线程通过自旋获取到锁那么jvm就认为这个锁通过自旋获取的几率会很大，就会自动增加自旋次数
						如过自旋很少成功获取到锁，那么jvm会省略自旋过程
				锁消除
					JIT编译时，对运行上下分进行分析，消除不可能存在竞争的锁
				锁粗化
					通过扩大加锁的范围来避免加锁和解锁
			synchorized四种状态
				锁膨胀方向
					无锁-》偏向锁-》轻量级锁-》重量级锁
				状态
					无锁
					偏向锁
					轻量级锁
					重量级锁
			锁降级
	语法
		[default | public | private | protected ] synchorized [static ] type methodName( )
		synchorized ( monitor ) 执行块
	java对象在内存中的布局
		对象头
			Mark Word
				默认存储对象的hashCode，分代年龄，锁类型，锁标志位等信息
			Class Metadata Address
				类型指针指向对象的类元数据，jvm通过这个指针确定该对象是哪个类的数据
		实例数据
		对齐填充
		补充
			轻量级锁和偏向锁 是jdk6以后新增加的
	object monitor（hpp）
		每一个对象都与一个monitor相关联，一个monitor的 lock 的锁只能被一个线程在同一时间获得
		synchorized使用的锁对象是存储在java对象头中的，每个对象都存在一个monitor与之关联，monitor可以和对象一起创建销毁，或者当线程试图获取对象锁时生成
		锁池和等待池
			锁池(EntryList)
				为获取对象锁的线程会进入锁池中
			等待池（waitSet）
		owner
			代表当前的ObjectMonitor锁被哪个线程所拥有
		count
			锁是可重入的，重入一次锁count就会加1
			指令
				monitor enter
					如过monitor的计数器为0，意味着这个monitor的lock还没有被获得，某个线程获取之后会对该计数器加1，那么该线程就是 这个monitor的所有者了
					如过一个拥有该monitor所有权的线程重入，则导致该monitor的计数器再次累加
					如过monitor已经被其他线程拥有，则当前线程获取monitor所有全的时候会被阻塞直到monitor的计数器为0的时候才能再次获取monitor的所有权
				monitor exit
					释放对monitor的所有权，想要释放某个对象关联的monitor的所有权的前提是，你曾经获取了所有权，当计数器为0的时候将不再拥有该monitor的所有权，如过有重入的话每次都会减1，当减为0的时候被monitor block的线程将再次尝试获取该monitor
		monitor type
			this monitor
				public synchorized void test1()
			class monitor
				public synchorized static void test1()
			注意
				DemoClass.class 本身就是一个对象
				在非static方法也可以使用当前对象所属的class对象作为monitor，因为他本身也是对象
	特性
		互斥性
		可见性
		synchorized锁的不是代码而是对象
	注意
		synchorized 不是锁，而是某线程在执行到 synchorized方法或者代码块的时候获取到了 对象头中的monitor锁
		synchorized 提供的代码块或者方法只是保证了互斥性，也就是说只会有一个线程获取到mutex 的monitor锁
		synchorized的作用域要尽量小一点，因为synchorized修饰的方法或者代码块是互斥的
		monitor锁必须保证是同一个
		避免多个锁的交叉导致死锁
```



####synchorized的原理

```
	synchorized关键字包括monitor enter和 monitorexit 两个jvm指令，他能够保证 在如何时候任何线程 执行到minitor enter成功之前都必须从主内存中获取数据，而不是从缓存中，在monitor enter运行成功之后共享变量被更新后的值必须刷入主内存

	synchorized关键字包括monitor enter和 monitorexit 两个jvm指令，他能够保证 在如何时候任何线程 执行到minitor enter成功之前都必须从主内存中获取数据，而不是从缓存中，在monitor enter运行成功之后共享变量被更新后的值必须刷入主内存
	
	synchorized的指令严格遵守java hanppens-before原则，一个monitor exit指令之前必须有一个monitor enter指令
	
```



####synchorized的实现基础

```
synchorzied 的锁操作要依赖于Java对象头来进行设置

synchorized使用的锁对象是存储在java对象头中的，每个对象都存在一个monitor与之关联，monitor可以和对象一起创建销毁，也可以在线程进入时创建

每一个对象都与一个monitor相关联，一个monitor的 lock 的锁只能被一个线程在同一时间获得

synchorized 关键字依赖于 字节码 monitorenter 和 monitorexist关键字

synchorized方法 依赖于 ACC_SYNCHORIZED 字节指令


```



####ObjectMonitor对象

```
synchorized使用的锁对象是存储在java对象头中的，每个对象都存在一个monitor与之关联，monitor可以和对象一起创建销毁，也可以在线程进入时创建

每一个对象都与一个monitor相关联，一个monitor的 lock 的锁只能被一个线程在同一时间获得

锁池和等待池
	锁池(EntryList)
		为获取对象锁的线程会进入锁池中
	等待池（waitSet）

owner
	代表当前的ObjectMonitor锁被哪个线程所拥有

count
	锁是可重入的，重入一次锁count就会加1
	指令
		monitor enter
			如过monitor的计数器为0，意味着这个monitor的lock还没有被获得，某个线程获取之后会对该计数器加1，那么该线程就是 这个monitor的所有者了
			如过一个拥有该monitor所有权的线程重入，则导致该monitor的计数器再次累加
			如过monitor已经被其他线程拥有，则当前线程获取monitor所有全的时候会被阻塞直到monitor的计数器为0的时候才能再次获取monitor的所有权
		monitor exit
			释放对monitor的所有权，想要释放某个对象关联的monitor的所有权的前提是，你曾经获取了所有权，当计数器为0的时候将不再拥有该monitor的所有权，如过有重入的话每次都会减1，当减为0的时候被monitor block的线程将再次尝试获取该monitor
```





####java对象在内存中的布局

```
synchorized使用的锁对象是存储在java对象头中的，每个对象都存在一个monitor与之关联，monitor可以和对象一起创建销毁，也可以在线程进入时创建
```

![img](https://user-gold-cdn.xitu.io/2019/3/21/169a038f9bb25549?imageView2/0/w/1280/h/960/format/webp/ignore-error/1) 

![img](https://user-gold-cdn.xitu.io/2019/3/21/169a04a52bd56c3f?imageView2/0/w/1280/h/960/format/webp/ignore-error/1) 

#####对象头

对象头的数据长度在`32`位和`64`位(未开启压缩指针)的虚拟机中分别为`32bit`和`64bit`。对象头由以下三个部分组成：

- Mark Word：记录了对象和锁的有关信息，储存对象自身的运行时数据，如哈希码(HashCode)、`GC`分代年龄、锁标志位、线程持有的锁、偏向线程`ID`、偏向时间戳、对象分代年龄等。**注意这个Mark Word结构并不是固定的，它会随着锁状态标志的变化而变化，而且里面的数据也会随着锁状态标志的变化而变化，这样做的目的是为了节省空间**。
- 类型指针：指向对象的类元数据的指针，虚拟机通过这个指针来确定这个对象是哪个类的实例。
- 数组长度：这个属性只有数组对象才有，储存着数组对象的长度。

在`32`位虚拟机下，`Mark Word`的结构和数据可能为以下`5`种中的一种。

![img](https://user-gold-cdn.xitu.io/2019/3/22/169a410864053e99?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

在`64`位虚拟机下，`Mark Word`的结构和数据可能为以下`2`种中的一种。

![img](https://user-gold-cdn.xitu.io/2019/3/22/169a42661fe25a80?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

这里重点注意**是否偏向锁**和**锁标志位**，这两个标识和`synchronized`的锁膨胀息息相关。



##### **自旋**

当有个线程`A`去请求某个锁的时候，这个锁正在被其它线程占用，但是线程`A`并不会马上进入阻塞状态，而是循环请求锁(自旋)。这样做的目的是因为很多时候持有锁的线程会很快释放锁的，线程`A`可以尝试一直请求锁，没必要被挂起放弃`CPU`时间片，因为线程被挂起然后到唤醒这个过程开销很大,当然如果线程`A`自旋指定的时间还没有获得锁，仍然会被挂起。

#####**自适应性自旋**

自适应性自旋是自旋的升级、优化，自旋的时间不再固定，而是由前一次在同一个锁上的自旋时间及锁的拥有者的状态决定。例如**线程如果自旋成功了，那么下次自旋的次数会增多**，因为`JVM`认为既然上次成功了，那么这次自旋也很有可能成功，那么它会允许自旋的次数更多。反之，如果**对于某个锁，自旋很少成功**，那么在以后获取这个锁的时候，自旋的次数会变少甚至忽略，避免浪费处理器资源。有了自适应性自旋，随着程序运行和性能监控信息的不断完善，`JVM`对程序锁的状况预测就会变得越来越准确，`JVM`也就变得越来越聪明。

#####锁消除

锁消除是指虚拟机即时编译器在运行时，**对一些代码上要求同步，但是被检测到不可能存在共享数据竞争的锁进行消除**。

#####锁粗化

在使用锁的时候，需要让同步块的作用范围尽可能小，这样做的目的是**为了使需要同步的操作数量尽可能小，如果存在锁竞争，那么等待锁的线程也能尽快拿到锁**

![img](assets\20190323140321501.png) 