####mysql的数据目录

```
my.cnf data_dir

max_connections 设置mysql允许访问的最大数量

interactve_time: 设置交互连接的time_out时间

wait_timeout：设置非交互连接的time_out时间

max_allowed_packet 控制mysql可以接受数据包的大小

syn_binlog ： 表示每写多少次缓冲会向磁盘同步一次 binlog数据


//每个连接
sort_buffer_size： 每个连接使用的排序缓冲区的大小

join_buffer_size: 每个连接使用join连接缓冲区的大小

read_buffer_size : 指定了对一个MYISAM进行表扫描的时候分配的读缓冲区的大小

read_rnd_buffer_size: 设置每个连接控制索引缓冲区的大小

binlog_cache_size : 设置每个回话用于缓存未提交事物的缓存大小



// 存储引擎参数

inndbo_flush_log_at_trx_commit:

事物日志缓冲区
	inndodb_log_buffer_size:   innodb事物日志缓冲区
	innodb_flush_log_at_trx_commit：事物日志刷新的频繁程度
		0：每秒进行一次log写入cache，并flush log到磁盘（丢失一秒钟事物）
		1：每次事物提交执行log写入cache，并刷新到磁盘中
		2：每次事物提交，执行log数据写入到cache，每秒执行一次flush log到磁盘（建议）
	innodb_flush_method=O_DIRECT innodb刷新的方式
	innodb_doublewrite:双写缓冲，避免数据不完整造成的数据损坏
```

![1572485435831](assets\1572485435831.png)





主从复制参数

redo log参数