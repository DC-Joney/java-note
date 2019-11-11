```
AQS 三个核心
	state 表示当前锁是否有线程获取
	CHL队列 将需要等待的线程加入该队列
	CAS 乐观锁

AQS实现了两种类型的锁机制

	公平锁 非公平锁	独占锁 和共享锁


实现子类如下：
    ReetrantLock	
    ReentrantReadWriteLock	
    CountDownLatch	
    CyclicBarrier	
    SemPhare
    Exchanger
    StampedLock//忽略
```







####AQS核心类型

> 状态，队列，CAS 

```
状态：一般是一个state属性，它基本是整个工具的核心，通常整个工具都是在设置和修改状态，很多方法的操作都依赖于当前状态是什么。由于状态是全局共享的，一般会被设置成volatile类型，以保证其修改的可见性；

CLH队列：队列通常是一个等待的集合，大多数以链表的形式实现。队列采用的是悲观锁的思想，表示当前所等待的资源，状态或者条件短时间内可能无法满足。因此，它会将当前线程包装成某种类型的数据结构，扔到一个等待队列中，当一定条件满足后，再从等待队列中取出。

CAS: CAS操作是最轻量的并发处理，通常我们对于状态的修改都会用到CAS操作，因为状态可能被多个线程同时修改，CAS操作保证了同一个时刻，只有一个线程能修改成功，从而保证了线程安全，CAS操作基本是由Unsafe工具类的compareAndSwapXXX来实现的；CAS采用的是乐观锁的思想，因此常常伴随着自旋，如果发现当前无法成功地执行CAS，则不断重试，直到成功为止，自旋的的表现形式通常是一个死循环for(;;)。
```



##### State

在AQS中，状态是由state属性来表示的，不出所料，它是volatile类型的：

```
private volatile int state; 
```

该属性的值即表示了锁的状态，state为0表示锁没有被占用，state大于0表示当前已经有线程持有该锁，这里之所以说大于0而不说等于1是因为可能存在可重入的情况。你可以把state变量当做是当前持有该锁的线程数量。

由于本篇我们分析的是独占锁，同一时刻，锁只能被一个线程所持有。通过state变量是否为0，我们可以分辨当前锁是否被占用，但光知道锁是不是被占用是不够的，我们并不知道占用锁的线程是哪一个。在监视器锁中，我们用ObjectMonitor对象的_owner属性记录了当前拥有监视器锁的线程，而在AQS中，我们将通过exclusiveOwnerThread属性：

```
private transient Thread exclusiveOwnerThread; //继承自AbstractOwnableSynchronizer
```

`exclusiveOwnerThread`属性的值即为当前持有锁的线程



##### 队列

AQS中，队列的实现是一个双向链表，被称为`sync queue`，它表示**所有等待锁的线程的集合**，有点类似于我们前面介绍synchronized原理的时候说的`entry list`。 

并发编程中使用队列通常是**将当前线程包装成某种类型的数据结构扔到等待队列中** 

```
static final class Node {
    /** Marker to indicate a node is waiting in shared mode */
    static final Node SHARED = new Node();
    /** Marker to indicate a node is waiting in exclusive mode */
    static final Node EXCLUSIVE = null;

    /** waitStatus value to indicate thread has cancelled */
    static final int CANCELLED =  1;
    /** waitStatus value to indicate successor's thread needs unparking */
    static final int SIGNAL    = -1;
    /** waitStatus value to indicate thread is waiting on condition */
    static final int CONDITION = -2;
    /**
     * waitStatus value to indicate the next acquireShared should
     * unconditionally propagate
     */
    static final int PROPAGATE = -3;

    volatile int waitStatus;

    volatile Node prev;

    volatile Node next;

    volatile Thread thread;

    Node nextWaiter;

    final boolean isShared() {
        return nextWaiter == SHARED;
    }

    final Node predecessor() throws NullPointerException {
        Node p = prev;
        if (p == null)
            throw new NullPointerException();
        else
            return p;
    }

    Node() {    // Used to establish initial head or SHARED marker
    }

    Node(Thread thread, Node mode) {     // Used by addWaiter
        this.nextWaiter = mode;
        this.thread = thread;
    }

    Node(Thread thread, int waitStatus) { // Used by Condition
        this.waitStatus = waitStatus;
        this.thread = thread;
    }
}

// 节点所代表的线程
volatile Thread thread;

// 双向链表，每个节点需要保存自己的前驱节点和后继节点的引用
volatile Node prev;
volatile Node next;

// 线程所处的等待锁的状态，初始化时，该值为0
volatile int waitStatus;

//线程是否已经退出（中断，或者有延时的获取锁）
static final int CANCELLED =  1;

//当前线程的下一个线程是否需要被唤醒
static final int SIGNAL    = -1;

//针对的是condition类型
static final int CONDITION = -2;
static final int PROPAGATE = -3;

// 该属性用于条件队列或者共享锁
Node nextWaiter;

在这个Node类中也有一个状态变量waitStatus，它表示了当前Node所代表的线程的等待锁的状态，在独占锁模式下，我们只需要关注CANCELLED SIGNAL两种状态即可。这里还有一个nextWaiter属性，它在独占锁模式下永远为null，仅仅起到一个标记作用，没有实际意义
```

 

说完队列中的节点，我们接着说回这个`sync queue`，AQS是怎么使用这个队列的呢，既然是双向链表，操纵它自然只需要一个头结点和一个尾节点：

```
// 头结点，不代表任何线程，是一个哑结点
private transient volatile Node head;

// 尾节点，每一个请求锁的线程会加到队尾
private transient volatile Node tail;
```



#####CAS操作

前面我们提到过，CAS操作大对数是用来改变状态的，在AQS中也不例外。我们一般在静态代码块中初始化需要CAS操作的属性的偏移量：

```
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long waitStatusOffset;
    private static final long nextOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset
                (AbstractQueuedSynchronizer.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset
                (AbstractQueuedSynchronizer.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                (AbstractQueuedSynchronizer.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset
                (Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset
                (Node.class.getDeclaredField("next"));

        } catch (Exception ex) { throw new Error(ex); }
    }
```

从这个静态代码块中我们也可以看出，CAS操作主要针对5个属性，包括AQS的3个属性`state`,`head`和`tail`, 以及Node对象的两个属性`waitStatus`,`next`。说明这5个属性基本是会被多个线程同时访问的。

定义完属性的偏移量之后，接下来就是CAS操作本身了：

```
protected final boolean compareAndSetState(int expect, int update) {
    return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
}
private final boolean compareAndSetHead(Node update) {
    return unsafe.compareAndSwapObject(this, headOffset, null, update);
}
private final boolean compareAndSetTail(Node expect, Node update) {
    return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
}
private static final boolean compareAndSetWaitStatus(Node node, int expect,int update) {
    return unsafe.compareAndSwapInt(node, waitStatusOffset, expect, update);
}
private static final boolean compareAndSetNext(Node node, Node expect, Node update) {
    return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
}
```

如前面所说，最终CAS操作调用的还是Unsafe类的compareAndSwapXXX方法。

最后就是自旋了，这一点就没有什么好说的了，我们在后面源码分析的时候再详细讲。



##### AQS核心属性

```
锁相关的属性有两个：
	private volatile int state; //锁的状态
	
	// 当前持有锁的线程，注意这个属性是从AbstractOwnableSynchronizer继承而来
	private transient Thread exclusiveOwnerThread; 

sync queue：
	private transient volatile Node head; // 队头，为dummy node
	private transient volatile Node tail; // 队尾，新入队的节点
	
队列中的Node中需要关注的属性有三组：
    
    // 节点所代表的线程
    volatile Thread thread;

    // 双向链表，每个节点需要保存自己的前驱节点和后继节点的引用
    volatile Node prev;
    volatile Node next;

    // 线程所处的等待锁的状态，初始化时，该值为0
    volatile int waitStatus;
    static final int CANCELLED =  1;
    static final int SIGNAL    = -1;
```





####AQS （公平锁）

```
ReentrantLock  

static final class FairSync extends Sync {
    private static final long serialVersionUID = -3000897897090466540L;
    //获取锁
    final void lock() {
        acquire(1);
    }
    ...
}

acquire 定义在AQS类中，描述了获取锁的流程

public final void acquire(int arg) {
    if (!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}

可以看出, 该方法中涉及了四个方法的调用:

（1）tryAcquire(arg)

如果锁没有被占用, 尝试以公平的方式获取锁（判断当前节点的前节点是否为head节点）
如果锁已经被占用, 检查是不是锁重入
获取锁成功返回true, 失败则返回false

（2）addWaiter(Node mode)

该方法由AQS实现, 负责在获取锁失败后调用, 将当前请求锁的线程包装成Node扔到CLH队列中，并返回这个Node。

（3）acquireQueued(final Node node, int arg)

该方法由AQS实现,这个方法比较复杂, 主要对上面刚加入队列的Node不断尝试以下两种操作之一:
	- 在前驱节点就是head节点的时候,继续尝试获取锁
	- 将当前线程挂起,使CPU不再调度它
	
（4）selfInterrupt

该方法由AQS实现, 用于中断当前线程。由于在整个抢锁过程中，我们都是不响应中断的。那如果在抢锁的过程中发生了中断怎么办呢，总不能假装没看见呀。AQS的做法简单的记录有没有有发生过中断，如果返回的时候发现曾经发生过中断，则在退出acquire方法之前，就调用selfInterrupt自我中断一下，就好像将这个发生在抢锁过程中的中断“推迟”到抢锁结束以后再发生一样。

从上面的简单介绍中可以看出，除了获取锁的逻辑 tryAcquire(arg)由子类实现外, 其余方法均由AQS实现。

接下来我们重点来看 FairSync 所实现的获取锁的逻辑:
```



```
tryAcquire
tryAcquire 获取锁的逻辑其实很简单——判断当前锁有没有被占用：

如果锁没有被占用, 尝试以公平的方式获取锁
如果锁已经被占用, 检查是不是锁重入
获取锁成功返回true, 失败则返回false

protected final boolean tryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    // 首先获取当前锁的状态
    int c = getState(); 
    
    // c=0 说明当前锁是avaiable的, 没有被任何线程占用, 可以尝试获取
    // 因为是实现公平锁, 所以在抢占之前首先看看队列中有没有排在自己前面的Node
    // 如果没有人在排队, 则通过CAS方式获取锁, 就可以直接退出了
    if (c == 0) {
        if (!hasQueuedPredecessors() 
        
        /* 为了阅读方便, hasQueuedPredecessors源码就直接贴在这里了, 这个方法的本质实际上是检测自己是不是head节点的后继节点，即处在阻塞队列第一位的节点
            public final boolean hasQueuedPredecessors() {
                Node t = tail; 
                Node h = head;
                Node s;
                return h != t && ((s = h.next) == null || s.thread != Thread.currentThread());
            }
        */
        
        && compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current); // 将当前线程设置为占用锁的线程
            return true;
        }
    }
    
    // 如果 c>0 说明锁已经被占用了
    // 对于可重入锁, 这个时候检查占用锁的线程是不是就是当前线程,是的话,说明已经拿到了锁, 直接重入就行
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0)
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        /* setState方法如下：
        protected final void setState(int newState) {
            state = newState;
        }
        */
        return true;
    }
    
    // 到这里说明有人占用了锁, 并且占用锁的不是当前线程, 则获取锁失败
    return false;
}
```



```
addWaiter

如果执行到此方法, 说明前面尝试获取锁的tryAcquire已经失败了, 既然获取锁已经失败了, 就要将当前线程包装成Node，加到等待锁的队列中去, 因为是FIFO队列, 所以自然是直接加在队尾。
方法调用为：

addWaiter(Node.EXCLUSIVE)
private Node addWaiter(Node mode) {
    Node node = new Node(Thread.currentThread(), mode); //将当前线程包装成Node
    // 这里我们用注释的形式把Node的构造函数贴出来
    // 因为传入的mode值为Node.EXCLUSIVE，所以节点的nextWaiter属性被设为null
    /*
        static final Node EXCLUSIVE = null;
        
        Node(Thread thread, Node mode) {     // Used by addWaiter
            this.nextWaiter = mode;
            this.thread = thread;
        }
    */
    Node pred = tail;
    // 如果队列不为空, 则用CAS方式将当前节点设为尾节点
    if (pred != null) {
        node.prev = pred;
        if (compareAndSetTail(pred, node)) {
            pred.next = node;
            return node;
        }
    }
    
    // 代码会执行到这里, 只有两种情况:
    //    1. 队列为空
    //    2. CAS失败
    // 注意, 这里是并发条件下, 所以什么都有可能发生, 尤其注意CAS失败后也会来到这里
    enq(node); //将节点插入队列
    return node;
}
可见，每一个处于独占锁模式下的节点，它的nextWaiter一定是null。
在这个方法中，我们首先会尝试直接入队，但是因为目前是在并发条件下，所以有可能同一时刻，有多个线程都在尝试入队，导致compareAndSetTail(pred, node)操作失败——因为有可能其他线程已经成为了新的尾节点，导致尾节点不再是我们之前看到的那个pred了。

如果入队失败了，接下来我们就需要调用enq(node)方法，在该方法中我们将通过自旋+CAS的方式，确保当前节点入队。
```









####AQS非公平锁

```

```





####AQS共享锁

```

```











网址：<https://segmentfault.com/a/1190000015739343> 