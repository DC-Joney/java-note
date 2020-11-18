## Netty 基本组件

# Channel、EventLoop 和ChannelFuture

接下来的各节将会为我们对于Channel、EventLoop 和ChannelFuture 类进行的讨论

增添更多的细节，这些类合在一起，可以被认为是Netty 网络抽象的代表：

1. Channel—Socket；
2. EventLoop—控制流、多线程处理、并发；
3. ChannelFuture—异步通知。

个人理解：Netty 是对 Socket 的封装，所有一切都是为了更高效的编写 Socket 程序准备的。

Channel 对应 Socket，EventLoop 可以理解为线程池（EventLoopGroup）中的线程。ChannelFuture 就是多线程中的 Future，用于返回异步的结果。



### Channel 接口

基本的I/O 操作（bind()、connect()、read()和write()）依赖于底层网络传输所提供的原语。

在基于Java 的网络编程中，其基本的构造是class Socket。Netty 的Channel 接口所提供的API，大大地降低了直接使用Socket 类的复杂性。

此外，Channel 也是拥有许多预定义的、专门化实现的广泛类层次结构的根，下面是一个简短的部分清单：

- EmbeddedChannel
- LocalServerChannel
- NioDatagramChannel
- NioSctpChannel
- NioSocketChannel

### EventLoop 接口

EventLoop 定义了Netty 的核心抽象，用于处理连接的生命周期中所发生的事件。

我们将在第7 章中结合Netty 的线程处理模型的上下文对EventLoop 进行详细的讨论。

在高层次上说明了Channel、EventLoop、Thread 以及EventLoopGroup 之间的关系。

Channel、EventLoop 和 EventLoopGroup 关系是： 

-  一个 EventLoopGroup 包含一个或者多个EventLoop； 
-  一个 EventLoop 在它的生命周期内只和一个Thread 绑定； 

- 所有由 EventLoop 处理的I/O 事件都将在它专有的 Thread 上被处理；
-  一个 Channel 在它的生命周期内只注册于一个 EventLoop； 
-  一个 EventLoop 可能会被分配给一个或多个Channel。 

 注意，在这种设计中，一个给定Channel 的I/O 操作都是由相同的Thread 执行的，实际上消除了对于同步的需要。 



### ChannelFuture 接口

 正如我们已经解释过的那样，Netty 中所有的I/O 操作都是异步的。 

因为一个操作可能不会立即返回，所以我们需要一种用于在之后的某个时间点确定其结果的方法。 

 为此，Netty 提供了ChannelFuture 接口，其addListener()方法注册了一个ChannelFutureListener，以便在某个操作完成时（无论是否成功）得到通知。 

 关于ChannelFuture 的更多讨论 可以将ChannelFuture 看作是将来要执行的操作的结果的占位符。它究竟什么时候被执行则可能取决于若干的因素，因此不可能准确地预测，但是可以肯定的是它将会被执行。 

此外，所有属于同一个Channel 的操作都被保证其将以它们被调用的顺序被执行。

ps: 在线程中，有时候线程池的资源是有限的。如果线程已经满了，一般会采用丢弃策略。Netty 肯定这方面做了相关的处理，比如维护一个内部的任务队列等等。



### ChannelHandler 和ChannelPipeline

 现在，我们将更加细致地看一看那些管理数据流以及执行应用程序处理逻辑的组件。 

#### ChannelHandler 接口

从应用程序开发人员的角度来看，Netty 的主要组件是ChannelHandler，它充当了所有处理入站和出站数据的应用程序逻辑的容器。

这是可行的，因为 ChannelHandler 的方法是由网络事件（其中术语“事件”的使用非常广泛）触发的。

 事实上，ChannelHandler 可专门用于几乎任何类型的动作，例如将数据从一种格式转换为另外一种格式，或者处理转换过程中所抛出的异常。 