# JDK8新特性之CompletableFuture详解

  在JDK1.5已经提供了Future和Callable的实现,可以用于阻塞式获取结果,如果想要异步获取结果,通常都会以轮询的方式去获取结果,如下:



```java
//定义一个异步任务
Future<String> future = executor.submit(()->{
       Thread.sleep(2000);
       return "hello world";
});
//轮询获取结果
while (true){
    if(future.isDone()) {
         System.out.println(future.get());
         break;
     }
 }
```

  从上面的形式看来轮询的方式会耗费无谓的CPU资源，而且也不能及时地得到计算结果.所以要实现真正的异步,上述这样是完全不够的,在Netty中,我们随处可见异步编程



```java
ChannelFuture f = serverBootstrap.bind(port).sync();
f.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    System.out.println("complete");
                }
            });
```

  而JDK1.8中的`CompletableFuture`就为我们提供了异步函数式编程,`CompletableFuture`提供了非常强大的`Future`的扩展功能，可以帮助我们简化异步编程的复杂性，提供了函数式编程的能力，可以通过回调的方式处理计算结果，并且提供了转换和组合`CompletableFuture`的方法。

### 1. 创建CompletableFuture对象

  `CompletableFuture`提供了四个静态方法用来创建CompletableFuture对象：



```java
public static CompletableFuture<Void>   runAsync(Runnable runnable)
public static CompletableFuture<Void>   runAsync(Runnable runnable, Executor executor)
public static <U> CompletableFuture<U>  supplyAsync(Supplier<U> supplier)
public static <U> CompletableFuture<U>  supplyAsync(Supplier<U> supplier, Executor executor)
```

  `Asynsc`表示异步,而`supplyAsync`与`runAsync`不同在与前者异步返回一个结果,后者是void.第二个函数第二个参数表示是用我们自己创建的线程池,否则采用默认的`ForkJoinPool.commonPool()`作为它的线程池.其中`Supplier`是一个函数式接口,代表是一个生成者的意思,传入0个参数,返回一个结果.(更详细的可以看我另一篇文章)



```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(()->{
            return "hello world";
  });
System.out.println(future.get());  //阻塞的获取结果  ''helllo world"
```

### 2. 主动计算

  以下4个方法用于获取结果



```java
//同步获取结果
public T    get()
public T    get(long timeout, TimeUnit unit)
public T    getNow(T valueIfAbsent)
public T    join()
```

`getNow`有点特殊，如果结果已经计算完则返回结果或者抛出异常，否则返回给定的valueIfAbsent值。`join()`与`get()`区别在于`join()`返回计算的结果或者抛出一个unchecked异常(CompletionException)，而`get()`返回一个具体的异常.

- 主动触发计算.



```java
public boolean complete(T  value)
public boolean completeExceptionally(Throwable ex)
```

上面方法表示当调用`CompletableFuture.get()`被阻塞的时候,那么这个方法就是结束阻塞,并且`get()`获取设置的value.



```java
 public static CompletableFuture<Integer> compute() {
        final CompletableFuture<Integer> future = new CompletableFuture<>();
        return future;
    }
    public static void main(String[] args) throws Exception {
        final CompletableFuture<Integer> f = compute();
        class Client extends Thread {
            CompletableFuture<Integer> f;
            Client(String threadName, CompletableFuture<Integer> f) {
                super(threadName);
                this.f = f;
            }
            @Override
            public void run() {
                try {
                    System.out.println(this.getName() + ": " + f.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        new Client("Client1", f).start();
        new Client("Client2", f).start();
        System.out.println("waiting");
        //设置Future.get()获取到的值
        f.complete(100);
        //以异常的形式触发计算
        //f.completeExceptionally(new Exception());
        Thread.sleep(1000);
    }
```

### 3. 计算结果完成时的处理



```java
public CompletableFuture<T>     whenComplete(BiConsumer<? super T,? super Throwable> action)
public CompletableFuture<T>     whenCompleteAsync(BiConsumer<? super T,? super Throwable> action)
public CompletableFuture<T>     whenCompleteAsync(BiConsumer<? super T,? super Throwable> action, Executor executor)
public CompletableFuture<T>     exceptionally(Function<Throwable,? extends T> fn)
```

上面4个方法是当计算阶段结束的时候触发,`BiConsumer`有两个入参,分别代表计算返回值,另外一个是异常.无返回值.方法不以Async结尾，意味着Action使用相同的线程执行，而Async可能会使用其它的线程去执行(如果使用相同的线程池，也可能会被同一个线程选中执行)。



```java
future.whenCompleteAsync((v,e)->{
       System.out.println("return value:"+v+"  exception:"+e);
 });
```

- handle()



```java
public <U> CompletableFuture<U>     handle(BiFunction<? super T,Throwable,? extends U> fn)
public <U> CompletableFuture<U>     handleAsync(BiFunction<? super T,Throwable,? extends U> fn)
public <U> CompletableFuture<U>     handleAsync(BiFunction<? super T,Throwable,? extends U> fn, Executor executor)
```

与`whenComplete()`不同的是这个函数返回`CompletableFuture`并不是原始的`CompletableFuture`返回的值,而是`BiFunction`返回的值.

### 4. CompletableFuture的组合

- thenApply
  当计算结算完成之后,后面可以接继续一系列的thenApply,来完成值的转化.



```java
public <U> CompletableFuture<U>     thenApply(Function<? super T,? extends U> fn)
public <U> CompletableFuture<U>     thenApplyAsync(Function<? super T,? extends U> fn)
public <U> CompletableFuture<U>     thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)
```

它们与handle方法的区别在于handle方法会处理正常计算值和异常，因此它可以屏蔽异常，避免异常继续抛出。而thenApply方法只是用来处理正常值，因此一旦有异常就会抛出。



```java
  CompletableFuture<String> future = CompletableFuture.supplyAsync(()->{
            
            return "hello world";
        });

CompletableFuture<String> future3 = future.thenApply((element)->{
            return element+"  addPart";
        }).thenApply((element)->{
            return element+"  addTwoPart";
        });
        System.out.println(future3.get());//hello world  addPart  addTwoPart
```

### 5. CompletableFuture的Consumer

只对`CompletableFuture`的结果进行消费,无返回值,也就是最后的`CompletableFuture`是void.



```java
public CompletableFuture<Void>  thenAccept(Consumer<? super T> action)
public CompletableFuture<Void>  thenAcceptAsync(Consumer<? super T> action)
public CompletableFuture<Void>  thenAcceptAsync(Consumer<? super T> action, Executor executor)
```



```java
//入参为原始的CompletableFuture的结果.
CompletableFuture future4 = future.thenAccept((e)->{
            System.out.println("without return value");
        });
future4.get();
```

- thenAcceptBoth

这个方法用来组合两个`CompletableFuture`,其中一个`CompletableFuture`等待另一个`CompletableFuture`的结果.



```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(()->{
            return "hello world";
        });
CompletableFuture future5 =  future.thenAcceptBoth(CompletableFuture.completedFuture("compose"),
                (x, y) -> System.out.println(x+y));//hello world compose
```

### 6. Either和ALL

  `thenAcceptBoth`是当两个`CompletableFuture`都计算完成，而我们下面要了解的方法`applyToEither`是当任意一个CompletableFuture计算完成的时候就会执行。



```java
Random rand = new Random();
        CompletableFuture<Integer> future9 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000 + rand.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        });
        CompletableFuture<Integer> future10 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000 + rand.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 200;
        });
        //两个中任意一个计算完成,那么触发Runnable的执行
        CompletableFuture<String> f =  future10.applyToEither(future9,i -> i.toString());
        //两个都计算完成,那么触发Runnable的执行
        CompletableFuture f1 = future10.acceptEither(future9,(e)->{
            System.out.println(e);
        });
        System.out.println(f.get());
```

  如果想组合超过2个以上的`CompletableFuture`,`allOf`和`anyOf`可能会满足你的要求.`allOf`方法是当所有的`CompletableFuture`都执行完后执行计算。`anyOf`方法是当任意一个`CompletableFuture`执行完后就会执行计算，计算的结果相同。

### 总结

  有了`CompletableFuture`之后,我们自己实现异步编程变得轻松很多,这个类也提供了许多方法来组合`CompletableFuture`.结合Lambada表达式来用,变得很轻松.