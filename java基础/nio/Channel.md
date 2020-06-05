## Channel

Channel 是 

Channel 模型图：

 ![Channelä½ç³»](https://raw.githubusercontent.com/zehonghuang/github_blog_bak/master/source/image/Channel%E4%BD%93%E7%B3%BB.png) 

`Channel`顶级接口，实际只提供一个`close()`。

`InterruptibleChannel`注释写了用于异步关闭or中断，大概说的是`AbstractInterruptibleChannel.begin()`的回调，中断后调用`implCloseChannel()`。

`SelectableChannel`这个就是多路复用提供的部分实现API。

`NetworkChannel`网络IO，绑定、设置socket选项等。

`ScatteringByteChannel` & `GatheringByteChannel`就是BufferByte读写了。

`SeekableByteChannel`知道`lseek()`就明白是跟文件IO相关的了。

