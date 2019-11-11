####redis-sentinal 

主从复制高可用

![1573410909989](assets\1573410909989.png)

redis-sentinal是实现了redis集群的故障转移功能

客户端获取节点信息是根据redis-sentinal来获取的



![1573411024164](assets\1573411024164.png)

redis-sentinal可以对多套 master-slave进行监控



安装配置

配置sentinal监控节点（sentinal是特殊的redis节点但是并不保存数据）



sentinal的配置文件

```
port ${port}

dir ./

logfile ${port}.log

daemonize no
pidfile /var/run/redis-sentinel.pid
logfile /var/log/redis/sentinel.log


# 主节点的名称 以及 ip地址以及多少个sentinal对主节点投票发现有问题就会进行故障转移
sentinal monitor mymaster 127.0.0.1 7000 2

##master被当前sentinel实例认定为“失效”的间隔时间  
##如果当前sentinel与master直接的通讯中，在指定时间内没有响应或者响应错误代码，那么  
##当前sentinel就认为master失效(SDOWN，“主观”失效)  
##<mastername> <millseconds>  
##默认为30秒  
sentinel down-after-milliseconds def_master 30000


#默认超过这个时间认为故障转移失败，需要人工进行故障转移  
sentinel failover-timeout mymaster 180000

##当前sentinel实例是否允许实施“failover”(故障转移)  
##no表示当前sentinel为“观察者”(只参与"投票".不参与实施failover)，  
##全局中至少有一个为yes  
sentinel can-failover def_master yes

#一次性修改几个slave指向新的new master，这个参数防止当master宕机后，多个sentinel同时
#一下子指向new master致使new master负载过重
sentinel parallel-syncs mymaster 1 
```







####出现的问题：

redis哨兵部署，哨兵之间不能相互通信，哨兵不能发现slave？

```
查看下各个哨兵的配置文件中的sentinel myid xxxx是不是一样的，如果是一样的就把所有配置文件中这行删除，然后重启，会自动生成唯一的。
```



redis-sentinal的客户端



实现原理：

