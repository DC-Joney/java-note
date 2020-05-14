#### 网络IO的三种模型

Acceptor模型

Reactor模型

Prodoctor模型



<https://juejin.im/post/5b4570cce51d451984695a9b#heading-3> 



#### BIO

```
传统的网络通讯模型，就是BIO，同步阻塞IO

默认使用 BIO的话，会维护一个socket队列，当请求过来以后需要串行化一个一个处理

它其实就是服务端创建一个ServerSocket， 然后就是客户端用一个Socket去连接服务端的那个ServerSocket， ServerSocket接收到了一个的连接请求就创建一个Socket和一个线程去跟那个Socket进行通讯。

接着客户端和服务端就进行阻塞式的通信，客户端发送一个请求，服务端Socket进行处理后返回响应。

在响应返回前，客户端那边就阻塞等待，上门事情也做不了。

这种方式的缺点：每次一个客户端接入，都需要在服务端创建一个线程来服务这个客户端

这样大量客户端来的时候，就会造成服务端的线程数量可能达到了几千甚至几万，这样就可能会造成服务端过载过高，最后崩溃死掉。
```



BIO模型图：

 ![img](https://upload-images.jianshu.io/upload_images/7564501-8f684afe5b241ccd?imageMogr2/auto-orient/strip|imageView2/2/w/583/format/webp) 

 

**Acceptor：**

传统的IO模型的网络服务的设计模式中有俩种比较经典的设计模式：一个是多线程， 一种是依靠线程池来进行处理。

如果是基于多线程的模式来的话，就是这样的模式，这种也是Acceptor线程模型。

 ![img](https://upload-images.jianshu.io/upload_images/7564501-001440a59f6fde83?imageMogr2/auto-orient/strip|imageView2/2/w/562/format/webp) 









####nio的reactor模型









Nio 的 Buffer





ByteBuffer CharBuffer



Nio中的Channel

```
Channel 

	ReadChannl、WriteChannel
		ByteChannel
	
```







Nio中的 Selector



aio 基于 File



aio 基于 Socket



#### Files的常用方法都有哪些？

- Files.exists()：检测文件路径是否存在。
- Files.createFile()：创建文件。
- Files.createDirectory()：创建文件夹。
- Files.delete()：删除一个文件或目录。
- Files.copy()：复制文件。
- Files.move()：移动文件。
- Files.size()：查看文件个数。
- Files.read()：读取文件。
- Files.write()：写入文件。