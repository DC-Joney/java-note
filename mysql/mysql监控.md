#### 性能类监控

```
#######QPS 数据库每秒钟处理的请求数量
	show global status like 'com%'
	
	com_update : 执行update的次数
	com_insert 执行insert操作的数量

show global status where varaiable_name like in ('Queries','uptime'); 第一次取样

show global status where varaiable_name like in ('Queries','uptime'); 第二次取样

Queries：数据库操作的总的次数

uptime：实例启动的时间

select (12-11)/(265-252) :计算这段时间内的qps值


########TPS：数据库每秒钟处理的事物数量
show global status where varaiable_name like in ('com_inser','com_delete','com_update');
Tc = com_insert + com_delete + com_update
TPS = (Tc2-Tc1)/(time2-time1)


########并发量 数据库实例当前处理的回话数量
show global status like 'Thread_running' 查看mysql的并发数


########连接数：连接到数据库会话的数量（包括正在执行的线程和sleep的线程）
show global status like 'Threads_conneted'
报警阈值 ： Thread_connected / max_connections > 0.8


#######缓存命中率 innodb_buffer的命中率
(Innodb_buffer_pool_read_requests - Innodb_buffer_pool_reads) /  Innodb_buffer_pool_read_requests * 100%

Innodb_buffer_pool_read_requests: 从缓存池中读取的次数
Innodb_buffer_pool_reads：表示从物理磁盘读取的次数

+---------------------------------------+--------+
| Variable_name                         | Value  |
+---------------------------------------+--------+
| Innodb_buffer_pool_read_ahead_rnd     | 0      |
| Innodb_buffer_pool_read_ahead         | 0      |
| Innodb_buffer_pool_read_ahead_evicted | 0      |
| Innodb_buffer_pool_read_requests      | 155037 |
| Innodb_buffer_pool_reads              | 176    |
+---------------------------------------+--------+

```



功能类指标：

可用性：数据库是否可正常对外提供服务

```
select @@version;

mysqladmin -u(user_name) -p -h(host) ping;
```



阻塞：当前是否有阻塞的回话

```
多个线程对同一个资源加排他锁造成的？
```

![1572491854244](assets\1572491854244.png)





死锁：当前事物是否产生了死锁

mysql会在产生死锁的事物中间，选择资源较少的事物进行回滚

```
begin tx_id1;
update set id =1;
update set id =2;

begin tx_id2;
update set id =2;
update set id =1;


查询死锁状态
show engine innodb status;

```

![1572492522235](assets\1572492522235.png)



```
pt-deadlock-logger u=dba,p=xxx,h=127.0.0.1 //查看死锁信息
--create-dest-table //自动建立死锁信息的表
-dest u=dba,p=xxxx,h=host,D=crn,t=deadlock //将死锁信息存储到表中
```



```
set global innodb_print_all_deadlock = on; 死锁信息放入到error log中
```





慢查询：实时慢查询查询

```
慢查询日志

information_schema.processlist（实时性监控）
select * from processlist where time > 60 and command != 'Sleep';

show processlist 也可以实时查看；
```



主从延迟时间 ：数据库主从延迟的时间

```
主从延迟；
show slave status;
seconds_behind_master: 当前正在恢复的relay_log日志的时间与slave系统之间的差值
```

![1572492326695](assets\1572492326695.png)



主从状态：数据库主从复制链路是否正常

```
show slave status；

slave_IO_running : yes
slave_sql_running: yes

last_errno: 0
last_error: 
```





