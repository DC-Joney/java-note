###mysql主从复制

####如何实现的主从复制？

![1572488480393](assets\1572488480393.png)



![1572488550767](assets\1572488550767.png)

![1572488633505](assets\1572488633505.png)











#### 基于日志点的复制

```
binlog主从复制

my.cnf文件
log-bin = master-bin
default_authentication_plugin = mysql_native_password
建立数据库账户

create user repl@'%' identifield by '123456';

grant replication slave on *.* to repl@'%'

备份数据库（记录备份的pos值）

mysqldump -ubackup -p --single-transcation --master-data=2
	-R --triggers -E  --databases db1 db2 > path.sql

把备份文件发送到slave中
mysql -uroot -p < master.sql




slave
开启log-bin 

恢复master备份数据
mysql -uroot -p < master.sql

使用change master配置链路（master-file，master pos）
change master to master_host = '',
    master_log_file = '',
    master_log_pos = pos_number;

start slave 启动复制链路

show slave status; 查看从库状态
```

![1572489001602](assets\1572489001602.png)

#### 基于gitd的主从复制

```
gtid主从复制

主库：
查看是否支持半同步复制（插件名称 rpl_semi_sync_master soname）
show plugins；

| partition                  | ACTIVE   | STORAGE ENGINE     | NULL               | GPL     |
| rpl_semi_sync_master       | ACTIVE   | REPLICATION        | semisync_master.so | GPL	    |

如过没有开启则开启下
install plugin rpl_semi_sync_master soname 'semisync_master.so';


查看gtid相关变量

show variables like 'rel%';

+------------------------------------+----------+
| Variable_name                      | Value    |
+------------------------------------+----------+
| rpl_semi_sync_master_enabled       | OFF      |
| rpl_semi_sync_master_timeout       | 10000    |
| rpl_semi_sync_master_trace_level   | 32       |
| rpl_semi_sync_master_wait_no_slave | ON       |
| rpl_stop_slave_timeout             | 31536000 |
+------------------------------------+----------+

rpl_semi_sync_master_timeout： 设置半同步日志的超时时间，如过超过该时间slave还没有回应ack则切换到异步模式，以造成写延迟

set persist rpl_semi_sync_master_enabled =on 启动半同步复制；

show globals status like 'rpl%';


slave 从库

show plugins；

| partition                  | ACTIVE   | STORAGE ENGINE     | NULL               | GPL     |
| rpl_semi_sync_slave       | ACTIVE   | REPLICATION        | semisync_slave.so | GPL	    |

如过没有开启则开启下
install plugin rpl_semi_sync_slave  soname 'semisync_slave.so';

mysql> show variables like 'rpl%';
+---------------------------------+----------+
| Variable_name                   | Value    |
+---------------------------------+----------+
| rpl_semi_sync_slave_enabled     | OFF      |
| rpl_semi_sync_slave_trace_level | 32       |
| rpl_stop_slave_timeout          | 31536000 |
+---------------------------------+----------+

rpl_semi_sync_slave_enabled： 启动从库上的半同步复制

stop slave io_thread; 停止复制的线程

start slave io_thread user='repl' password='123456'; 启动复制的线程

show globals status like 'rpl%';
```

![1572490209444](assets\1572490209444.png)







#### 两种的不同点

```
基于日志点的方式是传统的复制方式

slave请求master的增量日子好依赖于日志偏移量

配置链路需要制定 master_log_file 和 master_log_position参数

GTID = source_id:transaction_id

slave 增量同步master的数据依赖于其末同步的事物id

配置复制链路时，slave可以根据已经同步的事物id继续同步


```

![1572490515550](assets\1572490515550.png)



####问题：







#####如何减小主从复制的延迟？



#####一致性如何保证？



#####如何解决数据库 读\写负载过大的问题







