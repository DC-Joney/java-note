#### 相关文章

```
https://www.jianshu.com/u/f57de6e249f6


```



#### FutureTask解读

```

```



#### Juc中常用的线程池有哪些，分别有什么作用？

```
线程池分为三种
    Executor
    	ExecutorService
    		ScheduledExceptionService
    		AbstractExecutorService
    			ThreadPoolExecutor
    				ScheuldThreadPoolExecutor 
    			ForkJoinPool
 	
Executors.newFixedThreadPool();
Executors.newCacheThreadPool();
Executors.newSingleThreadPool();
Executors.newScheduledThreadPool()
Executors.newWorkStealingPool();

Executor：线程池的最上层接口，提供了任务提交的基础方法。
ExecutorService：提供了线程池管理的上层接口，如池销毁、任务提交、异步任务提交。
ScheduledExecutorService：提供任务定时或周期执行方法的 ExecutorService。
AbstractExecutorService：为 ExecutorService 的任务提交方法提供了默认实现。
ThreadPoolExecutor：大名鼎鼎线程池类，提供线程和任务的调度策略。
ScheduledThreadPoolExecutor：属于线程池的一种，它可以允许任务延迟或周期执行，类似java的Timer。
ForkJoinPool：JDK1.7加入的成员，也是线程池的一种。只允许执行 ForkJoinTask 任务，它是为那些能够被递归地拆解成子任务的工作类型量身设计的。其目的在于能够使用所有可用的运算资源来提升应用性能。
Executors：创建各种线程池的工具类
```









#### 线程池的参数有哪些？ 作用是什么？

```
corePoolSize、 maximumPoolSize。线程池会自动根据corePoolSize和maximumPoolSize去调整当前线程池的大小。当你通过submit或者execute方法提交任务的时候，如果当前线程池的线程数小于corePoolSize,那么线程池就会创建一个新的线程处理任务, 即使其他的core线程是空闲的。如果当前线程数大于corePoolSize并且小于

maximumPoolSize，那么只有在队列"满"的时候才会创建新的线程。因此这里会有很多的坑，比如你的core和max线程数设置的不一样，希望请求积压在队列的时候能够实时的扩容，但如果制定了一个无界队列，那么就不会扩容了，因为队列不存在满的概念。

keepAliveTime。如果当前线程池中的线程数超过了corePoolSize，那么如果在keepAliveTime时间内都没有新的任务需要处理，那么超过corePoolSize的这部分线程就会被销毁。默认情况下是不会回收core线程的，可以通过设置allowCoreThreadTimeOut改变这一行为。

workQueue。即实际用于存储任务的队列，这个可以说是最核心的一个参数了，直接决定了线程池的行为，比如说传入一个有界队列，那么队列满的时候，线程池就会根据core和max参数的设置情况决定是否需要扩容，如果传入了一个
SynchronousQueue，这个队列只有在另一个线程在同步remove的时候才可以put成功，对应到线程池中，简单来说就是如果有线程池任务处理完了，调用poll或者take方法获取新的任务的时候，新提交的任务才会put成功，否则如果当前的线程都在忙着处理任务，那么就会put失败，也就会走扩容的逻辑，如果传入了一个DelayedWorkQueue，顾名思义，任务就会根据过期时间来决定什么时候弹出，即为ScheduledThreadPoolExecutor的机制。

threadFactory。创建线程都是通过ThreadFactory来实现的，如果没指定的话，默认会使用
Executors.defaultThreadFactory()，一般来说，我们会在这里对线程设置名称、异常处理器等。

handler。即当任务提交失败的时候，会调用这个处理器，ThreadPoolExecutor内置了多个实现，比如抛异常、直接抛弃等。这里也需要根据业务场景进行设置，比如说当队列积压的时候，针对性的对线程池扩容或者发送告警等策略。
```

```
corePoolSize，maximumPoolSize，workQueue之间关系。

当线程池中线程数小于corePoolSize时，新提交任务将创建一个新线程执行任务，即使此时线程池中存在空闲线程。

当线程池中线程数达到corePoolSize时，新提交任务将被放入workQueue中，等待线程池中任务调度执行 。

当workQueue已满，且maximumPoolSize > corePoolSize时，新提交任务会创建新线程执行任务。

当workQueue已满，且提交任务数超过maximumPoolSize，任务由RejectedExecutionHandler处理。

当线程池中线程数超过corePoolSize，且超过这部分的空闲时间达到keepAliveTime时，回收这些线程。

当设置allowCoreThreadTimeOut(true)时，线程池中corePoolSize范围内的线程空闲时间达到keepAliveTime也将回收
```







#### 线程池的状态有哪些？

```
状态这里主要分为下面几种：
RUNNING：表示当前线程池正在运行中，可以接受新任务以及处理队列中的任务
SHUTDOWN：不再接受新的任务，但会继续处理队列中的任务
STOP：不再接受新的任务，也不处理队列中的任务了，并且会中断正在进行中的任务
TIDYING：所有任务都已经处理完毕，线程数为0，转为为TIDYING状态之后，会调用terminated()回调
TERMINATED：terminated()已经执行完毕

1. RUNNING

(01) 状态说明：线程池处在RUNNING状态时，能够接收新任务，以及对已添加的任务进行处理。
(02) 状态切换：线程池的初始化状态是RUNNING。换句话说，线程池被一旦被创建，就处于RUNNING状态！
道理很简单，在ctl的初始化代码中(如下)，就将它初始化为RUNNING状态，并且"任务数量"初始化为0。

private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
 

2. SHUTDOWN

(01) 状态说明：线程池处在SHUTDOWN状态时，不接收新任务，但能处理已添加的任务。
(02) 状态切换：调用线程池的shutdown()接口时，线程池由RUNNING -> SHUTDOWN。

 

3. STOP

(01) 状态说明：线程池处在STOP状态时，不接收新任务，不处理已添加的任务，并且会中断正在处理的任务。
(02) 状态切换：调用线程池的shutdownNow()接口时，线程池由(RUNNING or SHUTDOWN ) -> STOP。

 

4. TIDYING
(01) 状态说明：当所有的任务已终止，ctl记录的"任务数量"为0，线程池会变为TIDYING状态。当线程池变为TIDYING状态时，会执行钩子函数terminated()。terminated()在ThreadPoolExecutor类中是空的，若用户想在线程池变为TIDYING时，进行相应的处理；可以通过重载terminated()函数来实现。
(02) 状态切换：当线程池在SHUTDOWN状态下，阻塞队列为空并且线程池中执行的任务也为空时，就会由 SHUTDOWN -> TIDYING。
当线程池在STOP状态下，线程池中执行的任务为空时，就会由STOP -> TIDYING。

 

5. TERMINATED
(01) 状态说明：线程池彻底终止，就变成TERMINATED状态。
(02) 状态切换：线程池处在TIDYING状态时，执行完terminated()之后，就会由 TIDYING -> TERMINATED。
```

![img](https://images0.cnblogs.com/blog/497634/201401/08000847-0a9caed4d6914485b2f56048c668251a.jpg) 



#### 线程池的拒绝策略有哪些？

```
AbortPolicy：直接抛出异常，默认策略；
CallerRunsPolicy：用调用者所在的线程来执行任务；
DiscardOldestPolicy：丢弃阻塞队列中靠最前的任务，并执行当前任务；
DiscardPolicy：直接丢弃任务； 

当然也可以根据应用场景实现RejectedExecutionHandler接口，自定义饱和策略，如记录日志或持久化存储不能处理的任务
```





#### CompletionService的使用方法？

 ```
CompletionService 接口的功能是以异步的方式一边生产新的任务，一边处理已完成任务的结果，这样就可以将执行任务与处理任务分离开。

CompletionService 仅有一个实现类 ExecutorCompletionService，需要依赖Executor对象，其大部分实现是使用线程池 ThreadPoolExecutor实现的。

一般用于处理批量相同的任务

// Linkedblockingqueue作为任务完成队列
ExecutorCompletionService(Executor executor)    


// 将所提供队列作为任务完成队列
ExecutorCompletionService(Executor executor, BlockingQueue<Future<V>> completionQueue)

原理：
private class QueueingFuture extends FutureTask<Void> {
        QueueingFuture(RunnableFuture<V> task) {
            super(task, null);
            this.task = task;
        }
        protected void done() { completionQueue.add(task); }
        private final Future<V> task;
    }
 ```



#### CompletionFunture的使用方法？



#### ForkJoinPool的原理以及使用方法？

```
Java7 提供了ForkJoinPool来支持将一个任务拆分成多个“小任务”并行计算，再把多个“小任务”的结果合并成总的计算结果。

ForkJoinPool是ExecutorService的实现类，因此是一种特殊的线程池。

使用方法：创建了ForkJoinPool实例之后，就可以调用ForkJoinPool的submit(ForkJoinTask<T> task) 或invoke(ForkJoinTask<T> task)方法来执行指定任务了。

其中ForkJoinTask代表一个可以并行、合并的任务。ForkJoinTask是一个抽象类，它还有两个抽象子类：RecusiveAction和RecusiveTask。其中RecusiveTask代表有返回值的任务，而RecusiveAction代表没有返回值的任务。
```



#### 线程池的工作原理是什么？

重点看execute的实现

```
    public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();
        //第一步，获取ctl
        int c = ctl.get();
        //检查当前线程数是否达到核心线程数的限制，注意线程本身是不区分核心还是非核心，后面会进一步验证
        if (workerCountOf(c) < corePoolSize) {
            //如果核心线程数未达到，会直接添加一个核心线程，也就是说在线程池刚启动预热阶段，
            //提交任务后，会优先启动核心线程处理
            if (addWorker(command, true))
                return;
            //如果添加任务失败，刷新ctl，进入下一步
            c = ctl.get();
        }
        //检查线程池是否是运行状态，然后将任务添加到等待队列，注意offer是不会阻塞的
        if (isRunning(c) && workQueue.offer(command)) {
           //任务成功添加到等待队列，再次刷新ctl
            int recheck = ctl.get();
           //如果线程池不是运行状态，则将刚添加的任务从队列移除并执行拒绝策略
            if (! isRunning(recheck) && remove(command))
                reject(command);
            //判断当前线程数量，如果线程数量为0，则添加一个非核心线程，并且不指定首次执行任务
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        }
       //添加非核心线程，指定首次执行任务，如果添加失败，执行异常策略
        else if (!addWorker(command, false))
            reject(command);
    }
    
    /*
     * addWorker方法申明
     * @param core if true use corePoolSize as bound, else
     * maximumPoolSize. (A boolean indicator is used here rather than a
     * value to ensure reads of fresh values after checking other pool
     * state).
     * @return true if successful
     */
    private boolean addWorker(Runnable firstTask, boolean core) {
    //.....
    }
```

**这里有2个细节，可以深挖一下。**

1. 可以看到execute方法中没有用到重量级锁，ctl虽然可以保证本身变化的原子性，但是不能保证方法内部的代码块的原子性，是否会有并发问题？
2. 上面提到过，addWorker方法可以添加工作线程（核心或者非核心），线程本身没有核心或者非核心的标识，core参数只是用来确定 当前线程数的比较对象是线程池设置的核心线程数还是最大线程数，真实情况是不是这样？

#### addWorker

添加线程的核心方法，直接看源码

```
private boolean addWorker(Runnable firstTask, boolean core) {
       //相当于goto，虽然不建议滥用，但这里使用又觉得没一点问题
        retry:
        for (;;) {
            int c = ctl.get();
            int rs = runStateOf(c);
            //如果线程池的状态到了SHUTDOWN或者之上的状态时候，只有一种情况还需要继续添加线程，
            //那就是线程池已经SHUTDOWN，但是队列中还有任务在排队,而且不接受新任务（所以firstTask必须为null）
           //这里还继续添加线程的初衷是，加快执行等待队列中的任务，尽快让线程池关闭
            // Check if queue empty only if necessary.
            if (rs >= SHUTDOWN &&
                ! (rs == SHUTDOWN &&
                   firstTask == null &&
                   ! workQueue.isEmpty()))
                return false;

            for (;;) {
                int wc = workerCountOf(c);
               //传入的core的参数，唯一用到的地方，如果线程数超过理论最大容量，如果core是true跟最大核心线程数比较，否则跟最大线程数比较
                if (wc >= CAPACITY ||
                    wc >= (core ? corePoolSize : maximumPoolSize))
                    return false;
                //通过CAS自旋，增加线程数+1，增加成功跳出双层循环，继续往下执行
                if (compareAndIncrementWorkerCount(c))
                    break retry;
               //检测当前线程状态如果发生了变化，则继续回到retry，重新开始循环
                c = ctl.get();  // Re-read ctl
                if (runStateOf(c) != rs)
                    continue retry;
                // else CAS failed due to workerCount change; retry inner loop
            }
        }
        //走到这里，说明我们已经成功的将线程数+1了，但是真正的线程还没有被添加
        boolean workerStarted = false;
        boolean workerAdded = false;
        Worker w = null;
        try {
           //添加线程，Worker是继承了AQS，实现了Runnable接口的包装类
            w = new Worker(firstTask);
            final Thread t = w.thread;
            if (t != null) {
               //到这里开始加锁
                final ReentrantLock mainLock = this.mainLock;
                mainLock.lock();
                try {
                    // Recheck while holding lock.
                    // Back out on ThreadFactory failure or if
                    // shut down before lock acquired.
                    int rs = runStateOf(ctl.get());
                    //检查线程状态，还是跟之前一样，只有当线程池处于RUNNING，或者处于SHUTDOWN并且firstTask==null的时候，这时候创建Worker来加速处理队列中的任务
                    if (rs < SHUTDOWN ||
                        (rs == SHUTDOWN && firstTask == null)) {
                       //线程只能被start一次
                        if (t.isAlive()) // precheck that t is startable
                            throw new IllegalThreadStateException();
                      //workers是一个HashSet，添加我们新增的Worker
                        workers.add(w);
                        int s = workers.size();
                        if (s > largestPoolSize)
                            largestPoolSize = s;
                        workerAdded = true;
                    }
                } finally {
                    mainLock.unlock();
                }
                if (workerAdded) {
                  //启动Worker
                    t.start();
                    workerStarted = true;
                }
            }
        } finally {
            if (! workerStarted)
                addWorkerFailed(w);
        }
        return workerStarted;
    }
```

分析完addWorker的源码实现，我们可以回答上面留下的二个疑问，

1. execute方法虽然没有加锁，但是在addWorker方法内部，加锁了，这样可以保证不会创建超过我们预期的线程数，大师在设计的时候，做到了在最小的范围内加锁，尽量减少锁竞争，
2. 可以看到，core参数，只是用来判断当前线程数是否超量的时候跟corePoolSize还是maxPoolSize比较，Worker本身无核心或者非核心的概念。

#### 继续看Worker是怎么工作的

```
//Worker的run方法调用的是ThreadPoolExecutor的runWorker方法
    public void run() {
          runWorker(this);
    }


    final void runWorker(Worker w) {
        Thread wt = Thread.currentThread();
        //取出需要执行的任务，
        Runnable task = w.firstTask;
        w.firstTask = null;
        w.unlock(); // allow interrupts
        boolean completedAbruptly = true;
        try {
            //如果task不是null，或者去队列中取任务，注意这里会阻塞，后面会分析getTask方法
            while (task != null || (task = getTask()) != null) {
               //这个lock在这里是为了如果线程被中断，那么会抛出InterruptedException，而退出循环，结束线程
                w.lock();
                //判断线程是否需要中断
                if ((runStateAtLeast(ctl.get(), STOP) ||
                     (Thread.interrupted() &&
                      runStateAtLeast(ctl.get(), STOP))) &&
                    !wt.isInterrupted())
                    wt.interrupt();
                try {
                   //任务开始执行前的hook方法
                    beforeExecute(wt, task);
                    Throwable thrown = null;
                    try {
                        task.run();
                    } catch (RuntimeException x) {
                        thrown = x; throw x;
                    } catch (Error x) {
                        thrown = x; throw x;
                    } catch (Throwable x) {
                        thrown = x; throw new Error(x);
                    } finally {
                       ////任务开始执行后的hook方法
                        afterExecute(task, thrown);
                    }
                } finally {
                    task = null;
                    w.completedTasks++;
                    w.unlock();
                }
            }
            completedAbruptly = false;
        } finally {
           //Worker退出
            processWorkerExit(w, completedAbruptly);
        }
    }

   private Runnable getTask() {
        boolean timedOut = false; // Did the last poll() time out?

        for (;;) {
            int c = ctl.get();
            int rs = runStateOf(c);

            // Check if queue empty only if necessary.
           //检查线程池的状态，如果已经是STOP及以上的状态，或者已经SHUTDOWN，队列也是空的时候，直接return null，并将Worker数量-1
            if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
                decrementWorkerCount();
                return null;
            }

            int wc = workerCountOf(c);

           // 注意这里的allowCoreThreadTimeOut参数，字面意思是否允许核心线程超时，即如果我们设置为false，那么只有当线程数wc大于corePoolSize的时候才会超时
           //更直接的意思就是，如果设置allowCoreThreadTimeOut为false，那么线程池在达到corePoolSize个工作线程之前，不会让闲置的工作线程退出
            boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;
          //确认超时，将Worker数-1，然后返回
            if ((wc > maximumPoolSize || (timed && timedOut))
                && (wc > 1 || workQueue.isEmpty())) {
                if (compareAndDecrementWorkerCount(c))
                    return null;
                continue;
            }

            try {
                //从队列中取任务，根据timed选择是有时间期限的等待还是无时间期限的等待
                Runnable r = timed ?
                    workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                    workQueue.take();
                if (r != null)
                    return r;
                timedOut = true;
            } catch (InterruptedException retry) {
                timedOut = false;
            }
        }
    }
```

现在我们可以回答文章一开始提出的三个问题中的前2个了

1.  **线程池的线程是如何做到复用的。**
    线程池中的线程在循环中尝试取任务执行，这一步会被阻塞，如果设置了allowCoreThreadTimeOut为true，则线程池中的所有线程都会在keepAliveTime时间超时后还未取到任务而退出。或者线程池已经STOP，那么所有线程都会被中断，然后退出。
2.  **线程池是如何做到高效并发的。**
    看整个线程池的工作流程，有以下几个需要特别关注的并发点.
    ①: 线程池状态和工作线程数量的变更。这个由一个AtomicInteger变量 ctl来解决原子性问题。
    ②: 向工作Worker容器workers中添加新的Worker的时候。这个线程池本身已经加锁了。
    ③: 工作线程Worker从等待队列中取任务的时候。这个由工作队列本身来保证线程安全，比如LinkedBlockingQueue等。

 

 

 

https://www.jianshu.com/p/9a8c81066201

 

 

