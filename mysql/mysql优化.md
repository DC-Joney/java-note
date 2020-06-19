#### 慢查询sql

·根据慢日志来定位慢sql查询

```
配置文件：
	slow_query_log 启动停止记录慢查日志
	slow_query_log_file 指定慢查询日志的存储路径以及文件
	long_query_time 指定记录慢查询sql的时间阈值
	log_queries_not_using_indexs 是否记录未使用索引的sql

```



#### 获取实时性能sql

![1572446438902](assets\1572446438902.png)



```
set profing = 1；

show profiles;

show profile for query query_id;

show profile cpu for query query_id
```



#### Query Cache

![1572446759004](assets\1572446759004.png)

```zh
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



sql优化

```
优化器和可优化的sql类型
	重新定义标的关联顺序
	将外连接转化为内连接
	等价变换规则
	优化count()、min()、max()
	子查询优化
	对in() 条件进行优化
	
特定sql的查询优化
	大表的数据修改
	大表的表结构修改
	not in 和 <> 查询 可以改写成 关联查询
	汇总表查询
```





#### in 和 exist的区别



#### select * 和 select col_names的区别



#### count(0) 和 count(*)的区别



#### 更新索引统计信息和减少索引碎片

```
analyze table table_name;(分析表状态)

optimize table table_name; 进行碎片化整理
```

