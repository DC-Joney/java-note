####Rabbit MQ模型

```
Sever
	又称为Broker，接受客户端的链接，实现AMQP协议
```

```
connection
	应用程序与Broker之间的连接
```

```
channel
	网络信道，几乎所有的操作都在channel中运行，channel是进行消息读写的通道，客户端可以建立多个channel，每个channel代表一次会话任务

```

```
Message
	消息，服务器和应用程序之间传送的数据，由Properties和Body组成，Properties可以对消息进行修饰，比如消息的优先级、延迟等高级特性；body则就是消息体内容

```

```
Vurtual host
	虚拟地址，用于进行逻辑隔离，最上层的消息路由
```

```
Exchangge
	交换机，接受消息，根据路由键转发消息到绑定的队列
```

```
routing key
	一个路由规则，虚拟机可以用它来确定如何路由一个特定消息
```

```
Queue
	也称为 Message Queue，消息队列，保存消息并将它转发给消费者
```

![1572386667222](assets\1572386667222.png)

![1572386685161](assets\1572386685161.png)





![1572386955868](assets\1572386955868.png)

![1572386968863](assets\1572386968863.png)





####java api 相关设置

```
Binding

```



