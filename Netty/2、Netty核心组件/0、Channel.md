## Channel

Channel 是NIO的一个基本构造，一个Channel就代表一个通道，例如一个硬件设备、文件、网络等等。

Netty 中的Channel大致分为两类，分别是ServerChannel 与 ClientChannel，ServerChannel代表的是服务器端监听的Channel通道，而ClientChannel 代表 是客户端连接Server端时的通道

### Channel 接口

基本的 I/O 操作（bind()、connect()、read()和 write()）依赖于底层网络传输所提 供的原语。在基于 Java 的网络编程中，其基本的构造是 class Socket。Netty 的 Channel 接口所提供的 API，大大地降低了直接使用 Socket 类的复杂性。此外，Channel 也是拥有许多 预定义的、专门化实现的广泛类层次结构的根，下面是一个简短的部分清单： 

-  EmbeddedChannel； 
-  LocalServerChannel； 
-  NioDatagramChannel； 
-  NioSctpChannel； 
-  NioSocketChannel。