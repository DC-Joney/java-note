###RDB的持久方式

#### AOF存储

#####AOF的作用和原理

每次写入命令都会追加到AOF文件中

![1573406987879](assets\1573406987879.png)

![1573407004541](assets\1573407004541.png)

#####AOF的三种策略

######redis写入命令的流程

每次写入命令都是先写入到缓存，然后根据策略刷新到磁盘的AOF文件中

![1573407088484](assets\1573407088484.png)

- always	每条命令刷新到磁盘

- everysec	每秒刷新到磁盘

- no	操作系统自己来决定

![1573407140525](assets\1573407140525.png)



##### AOF的重写机制

AOF将过期的重复的一些命令整合成一条或者优化

![1573407329818](assets\1573407329818.png)

- 减少磁盘占用量
- 加速恢复速度

实现方式：

​	bgrewriteaof

![1573407580481](assets\1573407580481.png)

配置策略：

auto-aof-rewrite-min-size : AOF 文件重写需要的尺寸

auto-aof-rewrite-percentage：AOF文件的增长率

统计：

aof_current_size AOF当前尺寸（单位:字节）

aof_base_size：AOF上次启动和重写尺寸（字节）

![1573407767198](assets\1573407767198.png)



AOF的重写流程：

![1573407810788](assets\1573407810788.png)

AOF的配置文件：

```
appendonly: yes 开启aof

appendfilename： "name.aof" aof文件的名称

appendfsync erevrsec aof刷新到磁盘的策略

dir ./ aof存储的路径

no-appendfsync-no-rewrite yes ： aof重写的时候是否对正常的aof进行append操作（既正常aof文件是否从内存刷新到磁盘）

auto-aof-rewrite-min-size : AOF 文件重写需要的尺寸

auto-aof-rewrite-percentage：AOF文件的增长率

aof-load-truncated：当aof文件不完整或者有问题的时候，在重启加载aof的时候是否忽略这个错误
```



AOF的重写阻塞：

**会阻塞主线程**

![1573408668884](assets\1573408668884.png)







#### RDB

RDB（快照）持久化：保存某个时间点的全量数据快照

> save ：阻塞Redis的服务器进程，直到RBD文件被创建完毕

> bgsave：fork 出一个子进程来创建RDB文件，不阻塞服务器进程

##### 开启RDB的方式

- save（同步） 是阻塞的 (如过存在老的文件，则替换老的文件)
- bgsave（异步） 是非阻塞的

![1573405853995](assets\1573405853995.png)



fork如过执行慢的话 依旧会阻塞redis

save 与 bgsave的对比

![1573405942147](assets\1573405942147.png)



自动触发

```
save 900 1
save 300 10
save 60 10000

### 含义：
如过在60秒内改变了100000条数据则自动执行bgsave生成rdb文件
```

配置文件：

```
#rdb文件存储的名称
dbfilenam name.rdb

#rdb文件存储的目录
dir ./

# 如过bgsave发生了错误是否停止写入
stop-wirtes-on-bgsave-error yes

# rdb文件是否采压缩的方式存储
rdbcompression yes

# 对rdb文件是否进行校验和的检验
rdbchecksum yes 
```



触发机制：

全量复制（主从复制）

debug reload

shutdown 执行 命令的时候会生成



#####RDB的缺点

1、 耗时耗性能

![1573406909490](assets\1573406909490.png)

2、不可控、会丢失数据

![1573406932291](assets\1573406932291.png)



####AOF和 RBD的区别

当redis重启进行数据恢复时流程如下：

​	如过存在AOF文件则优先加载AOF文件



AOF与RDB的对比：

![1573408369659](assets\1573408369659.png)



RDB 尽量是关闭的



AOF 尽量是开启的 





















![1572374156264](assets\1572374156264.png)