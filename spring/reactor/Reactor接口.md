Reactor 与 RxJava 使用规范为 org.stream

**Publisher**

产生一个数据流（可能包含无限数据）, Subscriber 们可以根据它们的需要消费这些数据。

```text
public interface Publisher<T> {    
  public void subscribe(Subscriber<? super T> s);
}
```

**Subscriber**

Publisher 创建的元素的接收者。监听指定的事件，例如 OnNext, OnComplete, OnError 等。

```text
publicinterface Subscriber<T> {
  public void onSubscribe(Subscription s);
  public void onNext(T t);
  public void onError(Throwable t);
  public void onComplete();
}
```

**Subscription**

是 Publisher 和 Subscriber 一对一的协调对象。Subscriber 可以通过它来向 Publisher 取消数据发送或者 request 更多数据。

```text
public interface Subscription {  
  public void request(long n);  
  public void cancel();
}
```

**Processor**

同时具备 Publisher 和 Subscriber 特征。代码1中 FluxProcessor 既可以发送数据(OnNext)，也可以接收数据 (doOnNext)。

```text
public interface Processor<T, R> extends Subscriber<T>, Publisher<R> {}
```