 https://houbb.github.io/tags/#netty 

## Netty核心接口

Channel、EventLoop、ChannelFuture 合在一起，可以被认为是Netty 网络抽象的代表： 

1. Channel—Socket；
2. EventLoop—控制流、多线程处理、并发；
3. ChannelFuture—异步通知。

Netty 是对 Socket 的封装，所有一切都是为了更高效的编写 Socket 程序准备的。

Channel 对应 Socket，EventLoop 可以理解为线程池（EventLoopGroup）中的线程。ChannelFuture 就是多线程中的 Future，用于返回异步的结果。



### Channel

基本的I/O 操作（bind()、connect()、read()和write()）依赖于底层网络传输所提供的原语。

在基于Java 的网络编程中，其基本的构造是class Socket。Netty 的Channel 接口所提供的API，大大地降低了直接使用Socket 类的复杂性。

此外，Channel 也是拥有许多预定义的、专门化实现的广泛类层次结构的根，下面是一个简短的部分清单：

- EmbeddedChannel；
- LocalServerChannel；
- NioDatagramChannel；
- NioSctpChannel；
- NioSocketChannel。



### EventLoop 接口

