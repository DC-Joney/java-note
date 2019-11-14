### Throwables

传递异常的常用方法：

　　1.RuntimeException propagate(Throwable)：把throwable包装成RuntimeException，用该方法保证异常传递，抛出一个RuntimeException异常
　　2.void propagateIfInstanceOf(Throwable, Class<X extends Exception>) throws X：当且仅当它是一个X的实例时，传递throwable
　　3.void propagateIfPossible(Throwable)：当且仅当它是一个RuntimeException和Error时，传递throwable
　　4.void propagateIfPossible(Throwable, Class<X extends Throwable>) throws X：当且仅当它是一个RuntimeException和Error时，或者是一个X的实例时，传递throwable。



Guava的异常链处理方法：

　　1.Throwable getRootCause(Throwable)
　　2.List<Throwable> getCausalChain(Throwable)
　　3.String getStackTraceAsString(Throwable)