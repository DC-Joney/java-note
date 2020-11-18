## ChannelHandler

Netty的主要组件时ChannelHandler ，他充当了处理入站和出战数据的应用程序逻辑的容器，也就是说我们只需要观住ChannelHandler 以及相关实现即可，ChannelHandler 实际上可以分为出站和入站两种，Netty分别为我们提供了 出站和入站的接口，以及默认实现类，我们只需要继承并且扩展自己的逻辑即可, 如下:

ChannelHandler

-  ChannelInboundHandler
  - ChannelInboundHandlerAdptor
- ChannelOutboundHandler
- 

