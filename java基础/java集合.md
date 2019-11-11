##java集合考题

### java 集合 下的 架构图是什么样子的？

集合分为两大类：

 - Colletion（一组对象，每一个对象都是独立的）

   - List（有序的，可重复的）

     List接口的实现类主要有：ArrayList、LinkedList、Stack以及Vector等

   - Set（无序的，不可重复的）

     Set接口的实现类主要有：HashSet、TreeSet、LinkedHashSet等

   - Queue

   - 

![img](https://img-blog.csdn.net/20180825111638257?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dhbmd3ZWlfNjIw/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

- Map （一组映射关系，映射关系是成对的额）

 

![1571932708774](assets\1571932708774.png) 



1. Map接口的实现类主要有：HashMap、TreeMap、Hashtable、ConcurrentHashMap以及Properties等



####大体的图示

![1571932535750](assets\1571932535750.png)

#### 具体的比较 ：

![1571932589150](C:\Users\Administrator\Desktop\面试\面试题\java基础\assets\1571932589150.png)





###Collection 和 Map的区别是什么？



###HashMap和HashTable的区别是什么？

HashMap没有考虑同步，是线程不安全的；Hashtable使用了synchronized关键字，是线程安全的；

HashMap 允许K/V都为null；后者K/V都不允许为null；

HashMap 继承自AbstractMap类；而Hashtable继承自Dictionary类；

 HashMap 在1.8以后引入了红黑树，而 HashTable 则还是数组+链表来实现的



 



###java 集合的fail-fast机制 是什么？

**是java集合的一种错误检测机制，当多个线程对集合进行结构上的改变的操作时，有可能会产生 fail-fast 机制。**

例如：假设存在两个线程（线程1、线程2），线程1通过Iterator在遍历集合A中的元素，在某个时候线程2修改了集合A的结构（是结构上面的修改，而不是简单的修改集合元素的内容），那么这个时候程序就会抛出 ConcurrentModificationException 异常，从而产生fail-fast机制。

**原因：迭代器在遍历时直接访问集合中的内容，并且在遍历过程中使用一个 modCount 变量。集合在被遍历期间如果内容发生变化，就会改变modCount的值。每当迭代器使用hashNext()/next()遍历下一个元素之前，都会检测modCount变量是否为expectedmodCount值，是的话就返回遍历；否则抛出异常，终止遍历。**

**解决办法：**

**1. 在遍历过程中，所有涉及到改变modCount值得地方全部加上synchronized。**

**2. 使用CopyOnWriteArrayList来替换ArrayList**



### ArrayList 和 Vector 的区别是什么？

这两个类都实现了 List 接口（List 接口继承了 Collection 接口），他们都是有序集合，即存储在这两个集合中的元素位置都是有顺序的，相当于一种动态的数组，我们以后可以按位置索引来取出某个元素，并且其中的数据是允许重复的，这是与 HashSet 之类的集合的最大不同处，HashSet 之类的集合不可以按索引号去检索其中的元素，也不允许有重复的元素。

ArrayList 与 Vector 的区别主要包括两个方面：

1. 同步性：
    Vector 是线程安全的，也就是说它的方法之间是线程同步（加了synchronized 关键字）的，而 ArrayList 是线程不安全的，它的方法之间是线程不同步的。如果只有一个线程会访问到集合，那最好是使用 ArrayList，因为它不考虑线程安全的问题，所以效率会高一些；如果有多个线程会访问到集合，那最好是使用 Vector，因为不需要我们自己再去考虑和编写线程安全的代码。
2. 数据增长：
    ArrayList 与 Vector 都有一个初始的容量大小，当存储进它们里面的元素的个人超过了容量时，就需要增加 ArrayList 和 Vector 的存储空间，每次要增加存储空间时，不是只增加一个存储单元，而是增加多个存储单元，每次增加的存储单元的个数在内存空间利用与程序效率之间要去的一定的平衡。Vector 在数据满时（加载因子1）增长为原来的两倍（扩容增量：原容量的 2 倍），而 ArrayList 在数据量达到容量的一半时（加载因子 0.5）增长为原容量的 (0.5 倍 + 1) 个空间。



###ArrayList的扩展？

 ArrayList内部维护了一个final 类型的 DEFAULTCAPACITY_EMPTY_ELEMENTDATA 变量默认是一个空的数组

当 我们 new一个ArrayList集合时，如过不指定容量的话，默认是就会将当前类变量element数组 设置为 上面的空数组，当第一次添加元素的时候判断，当前数组是否为空数组，如过是空数组的话，就会开辟一个10空间大小的数组

如过 指定初始化大小的时候，在初始化的手就会构建一个指定大小的element数组，

如过当前实际存储元素数量 + 1 >  在创建时候指定的大小，

则 oldCapacity >> 1    右移运算符   原来长度的一半 再加上原长度也就是每次扩容是原来的1.5倍 



###ArrayList和 LinkedList的不同点是什么？ 各有什么优缺点？



1. LinkedList 实现了 List 和 Deque 接口，一般称为双向链表；ArrayList 实现了 List 接口，动态数组；
2. LinkedList 在插入和删除数据时效率更高，ArrayList 在查找某个 index 的数据时效率更高；
3. LinkedList 比 ArrayList 需要更多的内存；

**面试官：Array 和 ArrayList 有什么区别？什么时候该应 Array 而不是 ArrayList 呢？**

答：它们的区别是：

1. Array 可以包含基本类型和对象类型，ArrayList 只能包含对象类型。
2. Array 大小是固定的，ArrayList 的大小是动态变化的。
3. ArrayList 提供了更多的方法和特性，比如：addAll()，removeAll()，iterator() 等等。

 

 ### 谈谈HashSet 与 Treeset 的区别 以及如何实现去重？

答：**HashSet的底层其实就是HashMap**，只不过我们**HashSet是实现了Set接口并且把数据作为K值，而V值一直使用一个相同的虚值来保存**，我们可以看到源码：

```
public boolean add(E e) {
    return map.put(e, PRESENT)==null;// 调用HashMap的put方法,PRESENT是一个至始至终都相同的虚值
}
```

由于HashMap的K值本身就不允许重复，并且在HashMap中如果K/V相同时，会用新的V覆盖掉旧的V，然后返回旧的V，那么在HashSet中执行这一句话始终会返回一个false，导致插入失败，这样就保证了数据的不可重复性；

**而TreeSet底层则使用TreeMap来实现的**



###ConcurrentHashMap和Hashtable的区别？

实现map安全的几种方法？

Colletions.synchorizedMap()  

HashTable

ConcurrentHashMap



HashTable是在方法上加锁来实现的，而ConcurrentHashMap是将整锁拆解分成多个锁进行优化

![1571945239905](assets\1571945239905.png)







jdk8之后 

![1571945283040](assets\1571945283040.png)



ConcurretHashMap是不允许加入null key的

![1571945490229](assets\1571945490229.png)



![1571945517003](assets\1571945517003.png)





###HashMap的put方法的具体流程？



###HashMap的扩容操作是怎么实现的？



###HashMap在JDK1.7和JDK1.8中有哪些不同？



 

###**Collection与Collections的区别是什么？**

Collection是Java集合框架中的基本接口；

Collections是Java集合框架提供的一个工具类，其中包含了大量用于操作或返回集合的静态方法。



###**Collections.sort排序内部原理**

在Java 6中Arrays.sort()和Collections.sort()使用的是MergeSort，而在Java 7中，内部实现换成了TimSort，其对对象间比较的实现要求更加严格

  

###有没有有顺序的Map实现类，如果有，他们是怎么保证有序的。

 



###JAVA8的ConcurrentHashMap为什么放弃了分段锁，有什么问题吗，如果你来设计，你如何设计。



###谈谈 Queue的实现有哪些？ 那些是阻塞的，那些事非阻塞的？哪些是线程安全的？

Queue

```
Queue的实现默认分为三种
	// AbstractQueue的作用就是实现了AbstractCollection类，将一些常用的 集合方法带入了Queue中
	AbstractQueue<E> extends AbstractCollection<E> implements Queue<E>
		PriorityQueue
		LinkedTransferQueue
		PriorityBlockingQueue
		DelayQueue
		SynchronousQueue
		LinkedBlockingQueue
		ArrayBlockingQueue
		Colletions.AsLIFOQueue
		ConcurrentLinkedQueue（使用了cas算法来添加元素）
	BlockingQueue
		ArrayBlockingQueue （一个由数组支持的有界队列）
		SynchronousQueue
		DelayQueue
		TransferQueue（接口）
			LinkedTransferQueue（内部使用的是无锁操作）
		LinkedBlockingQueue（一个由链接节点支持的可选有界队列。）
		PriorityBlockingQueue（个由优先级堆支持的无界优先级队列）
	Deque
		BlockingDeque（接口，BlockingDeque<E> extends BlockingQueue<E>, Deque<E>）
			LinkedBlockingDeque
		ArrayDeque（Deque实现类）
		

Deque 表示 队列为双端队列，
BlockingQueue 队列表示队列为阻塞队列
BlockingDeque 表示队列为双端阻塞队列
PriorityQueue 表示队列为优先级队列

阻塞队列的操作：

　　add        增加一个元索                     如果队列已满，则抛出一个IIIegaISlabEepeplian异常
　　remove   移除并返回队列头部的元素    如果队列为空，则抛出一个NoSuchElementException异常
　　element  返回队列头部的元素             如果队列为空，则抛出一个NoSuchElementException异常
　　offer       添加一个元素并返回true       如果队列已满，则返回false
　　poll         移除并返问队列头部的元素    如果队列为空，则返回null
　　peek       返回队列头部的元素             如果队列为空，则返回null
　　put         添加一个元素                      如果队列满，则阻塞
　　take        移除并返回队列头部的元素     如果队列为空，则阻塞

 

remove、element、offer 、poll、peek 其实是属于Queue接口。

LinkedBlockingQueue的容量是没有上限的（说的不准确，在不指定时容量为Integer.MAX_VALUE，不要然的话在put时怎么会受阻呢），但是也可以选择指定其最大容量，它是基于链表的队列，此队列按 FIFO（先进先出）排序元素。


ArrayBlockingQueue在构造时需要指定容量， 并可以选择是否需要公平性，如果公平参数被设置true，等待时间最长的线程会优先得到处理（其实就是通过将ReentrantLock设置为true来 达到这种公平性的：即等待时间最长的线程会先操作）。通常，公平性会使你在性能上付出代价，只有在的确非常需要的时候再使用它。它是基于数组的阻塞循环队 列，此队列按 FIFO（先进先出）原则对元素进行排序。


PriorityBlockingQueue是一个带优先级的 队列，而不是先进先出队列。元素按优先级顺序被移除，该队列也没有上限（看了一下源码，PriorityBlockingQueue是对 PriorityQueue的再次包装，是基于堆数据结构的，而PriorityQueue是没有容量限制的，与ArrayList一样，所以在优先阻塞 队列上put时是不会受阻的。虽然此队列逻辑上是无界的，但是由于资源被耗尽，所以试图执行添加操作可能会导致 OutOfMemoryError），但是如果队列为空，那么取元素的操作take就会阻塞，所以它的检索操作take是受阻的。另外，往入该队列中的元 素要具有比较能力。


DelayQueue（基于PriorityQueue来实现的）是一个存放Delayed 元素的无界阻塞队列，只有在延迟期满时才能从中提取元素。该队列的头部是延迟期满后保存时间最长的 Delayed 元素。如果延迟都还没有期满，则队列没有头部，并且poll将返回null。当一个元素的 getDelay(TimeUnit.NANOSECONDS) 方法返回一个小于或等于零的值时，则出现期满，poll就以移除这个元素了。此队列不允许使用 null 元素。
```



![1571932253305](assets\1571932253305.png)

 

####**poll() 方法和 remove() 方法的区别？**

poll() 和

remove() 都是从队列中取出一个元素，但是 poll() 在获取元素失败的时候会返回空，但是 remove() 失败的时候会抛出异常。

 

 

 

 

 