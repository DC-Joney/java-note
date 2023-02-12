## Mysql 优化



### 参数 优化



### 问题定位

1、通过 show processlist 查看当前数据库的连接， Query time >  指定阈值的

2、通过设置profiling对某个sql语句进行排查，如下：

```

# 设置记录 sql 语句
set profiling = 1;

#查看当前执行的sql
show profiles;
//查看语句执行的整个过程
show profile for query query_id;

//查看语句执行的cpu占比
show profile cpu for query query_id


//查看语句执行的io占比
show profile block io for query query_id
```

3、通过查询 information_schema  下的 profiling 与 processlist 来定位，

4、通过查询 information_shcema 下的 innodb_trx 与 innodb_lock 与 innodb_lock_wait 来发现长事务以及 sql占用锁时间过长问题





### Slow Query Log

我们可以通过开启 slow query log 来记录超过阀值的sql语句

配置文件：

```
slow_query_log 启动停止记录慢查日志
slow_query_log_file 指定慢查询日志的存储路径以及文件
long_query_time 指定记录慢查询sql的时间阈值
log_queries_not_using_indexs 是否记录未使用索引的sql
```





### QueryCache

我们可以通过开启查询缓存来进行优化执行，但是 查询缓存有一定的弊端

```
查询缓存
	优先检查这个查询是否命中缓存中的数据。
	通过一个队大小写敏感的哈希查找来实现的
	Hash查找只能进行全值匹配

条件：
	查询sql和缓存中的sql是完全一致的

query_cache_type
	设置查询缓存是否可用
	ON | OFF
	DEMAND : 在查询语句中使用SQL_CACHE 和 SQL_NO_CACHE 来控制是否需要缓存
query_cache_size
	设置查询缓存的大小
query_cache_limit
	设置查询缓存可用存储的最大值
query_cache_wlock_invalidate
	设置数据表被锁定后是否返回缓存中的数据
query_cache_min_res_unit
	设置查询缓存分配的内存块的最小单位
```



### 优化表空间与索引数据

analyze table table_name; (分析表状态)

optimize table table_name;  进行碎片化整理







### Sort buffer



### Join buffer



### count(1) 与 count (*)