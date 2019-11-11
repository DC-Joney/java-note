####redis的主从配置

主从配置的问题：

​	容量瓶颈 和 QPS瓶颈（单机单节点）

![1573408800333](assets\1573408800333.png)



![1573408815683](assets\1573408815683.png)



#####作用：

​	作为master的节点的数据副本

​	可以提供读写分离

​	一个master可以有多个slave

​	一个slave只能有一个master

​	数据流向是单向的，从master到slave



#####主从复制配置

命令：

​	slave节点 -》 slaveof master ip

​	slave节点 -》 取消复制：slaveof no one



配置文件：

```
#slave节点同步的主节点
slaveof ip port

#slave 节点是只读的
slave-read-only yes
```



原理： runid

redis启动的时候都有一个runid

slave需要根据runid进行数据同步

```
127.0.0.1:6380> info server
# Server
redis_version:5.0.6
redis_git_sha1:00000000
redis_git_dirty:0
redis_build_id:c3d7ebb6b1a2844b
redis_mode:standalone
os:Linux 3.10.0-693.el7.x86_64 x86_64
arch_bits:64
multiplexing_api:epoll
atomicvar_api:atomic-builtin
gcc_version:4.8.5
process_id:2855
run_id:c68d5d38d871ef0fbb91425724672d65cd53da69
tcp_port:6380
uptime_in_seconds:438
uptime_in_days:0
hz:10
configured_hz:10
lru_clock:13128529
executable:/opt/redis/config/redis-server
config_file:/opt/redis/config/redis-6380.conf
```

第一次启动复制的时候需要进行全量同步



主节点增加数据的时候是增量同步

```
从节点需要根据主节点的偏移量来进行数据同步

master_repl_offset:873
```





#####全量复制 VS 部分复制

全量同步的原理：

![1573409911876](assets\1573409911876.png)



全量复制的开销：

​	bgsave的时间

​	rbd的文件网络传输时间

​	从节点清空数据的时间

​	从节点加载rdb的时间

​	可能的aof重写的时间





#####增量复制

![1573410059642](assets\1573410059642.png)





#####故障处理

master和slave挂掉之后无法做到高可用需要进行动态修改

![1573410126585](assets\1573410126585.png)



