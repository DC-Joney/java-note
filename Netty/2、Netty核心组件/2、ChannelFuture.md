### ChannelFuture

ChannelFuture 继承自Future类，在Netty中所有的IO操作都是异步操作的，而阻塞操作将返回ChannelFuture对象，ChannelFuture是对FutureTask的一种扩展，JDK 本身的Future并没有提供Listener机制，所以在Future执行完成后必须阻塞等待才知道是否完成，而ChannelFuture在JDK的基础上提供了 addListener 功能，可以在任务执行完成后进行回调处理

Netty 中所有的 I/O 操作都是异步的。因为一个操作可能不会 立即返回，所以我们需要一种用于在之后的某个时间点确定其结果的方法。为此，Netty 提供了 ChannelFuture 接口，其 addListener()方法注册了一个 ChannelFutureListener，以 便在某个操作完成时（无论是否成功）得到通知。 

> 关于 **ChannelFuture** 的更多讨论 可以将 ChannelFuture 看作是将来要执行的操作的结果的 占位符。它究竟什么时候被执行则可能取决于若干的因素，因此不可能准确地预测，但是可以肯 定的是它将会被执行。此外，所有属于同一个 Channel 的操作都被保证其将以它们被调用的顺序 被执行