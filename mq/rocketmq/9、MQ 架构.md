# MQ 架构

## MQ 的工作方式 

RocketMQ 架构上主要分为四个部分：如图所示：

 ![img](assets/rocketmq_architecture_1.png) 



分别为 Producer、Broker、NameServer、Consumer

- Producer：消息发布的角色，支持分布式集群方式部署。Producer通过MQ的负载均衡模块选择相应的Broker集群队列进行消息投递，投递的过程支持快速失败并且低延迟。
- Consumer：消息消费的角色，支持分布式集群方式部署。支持以push推，pull拉两种模式对消息进行消费。同时也支持集群方式和广播方式的消费，它提供实时消息订阅机制，可以满足大多数用户的需求。
- 



## Broker 配置

```properties
#所属集群名字
brokerClusterName=rocketmq-cluster
#broker名字，注意此处不同的配置文件填写的不一样  例如：在a.properties 文件中写 broker-a  在b.properties 文件中写 broker-b
brokerName=broker-a

#0 表示 Master，>0 表示 Slave
brokerId=0


#nameServer地址，这里nameserver是单台，如果nameserver是多台集群的话，就用分号分割（即namesrvAddr=ip1:port1;ip2:port2;ip3:port3或者修改hosts文件不使用IP而是域名）
namesrvAddr=rocketmq-nameserver1:9876;rocketmq-nameserver2:9876;rocketmq-nameserver3:9876;rocketmq-nameserver4:9876

#在发送消息时，自动创建服务器不存在的topic，默认创建的队列数。由于是4个broker节点，所以设置为4
defaultTopicQueueNums=4

#是否允许 Broker 自动创建Topic，建议线下开启，线上关闭
autoCreateTopicEnable=true

#是否允许 Broker 自动创建订阅组，建议线下开启，线上关闭
autoCreateSubscriptionGroup=true

#Broker 对外服务的监听端口
listenPort=10911

#删除文件时间点，默认凌晨 4点
deleteWhen=04

#文件保留时间，默认 48 小时
fileReservedTime=120


#commitLog每个文件的大小默认1G
mapedFileSizeCommitLog=1073741824

#ConsumeQueue每个文件默认存30W条，根据业务情况调整
mapedFileSizeConsumeQueue=300000

#destroyMapedFileIntervalForcibly=120000
#redeleteHangedFileInterval=120000

#检测物理文件磁盘空间
diskMaxUsedSpaceRatio=88

#存储路径
storePathRootDir=/usr/local/rocketmq/store

#commitLog 存储路径
storePathCommitLog=/usr/local/rocketmq/store/commitlog

#消费队列存储路径存储路径
storePathConsumeQueue=/usr/local/rocketmq/store/consumequeue

#消息索引存储路径
storePathIndex=/usr/local/rocketmq/store/index

#checkpoint 文件存储路径
storeCheckpoint=/usr/local/rocketmq/store/checkpoint

#abort 文件存储路径
abortFile=/usr/local/rocketmq/store/abort

#限制的消息大小
maxMessageSize=65536

#flushCommitLogLeastPages=4
#flushConsumeQueueLeastPages=2
#flushCommitLogThoroughInterval=10000
#flushConsumeQueueThoroughInterval=60000

#Broker 的角色
#- ASYNC_MASTER 异步复制Master
#- SYNC_MASTER 同步双写Master
#- SLAVE
brokerRole=SYNC_MASTER

#刷盘方式
#- ASYNC_FLUSH 异步刷盘
#- SYNC_FLUSH 同步刷盘
flushDiskType=ASYNC_FLUSH
#checkTransactionMessageEnable=false

#发消息线程池数量
#sendMessageThreadPoolNums=128

#拉消息线程池数量
#pullMessageThreadPoolNums=128
```





## MQ 的 存储



## MQ 的高可用方式



## MQ 的权限管理



## MQ 消息轨迹

