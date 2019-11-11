###Server层日志

server层的一般是在server层面进行写入

####常用查询日志

```
generate_log
记录所有发向mysql的请求;

generate_log = [on|off]

generate_log_file = $mysql/sql_log/genrate.log

log_output = [FILE|TABLE|NONE] 
FILE : 保存在文件中
TABLE: 保存在表中
```



####错误日志

```
error_log 记录mysql在启动或者停止出现的问题

分析排除mysql的运行错误

记录未经授权的访问

log_err = $mysql/sql_log/mysql-error.log

log_errpr_verbosity = [1,2,3]
```

![1572485998008](assets\1572485998008.png)





####慢日志

```
slow_query_log
记录所有存在性能的sql

slow_query_log = [ON|OFF]

slow_query_log_file = path 指定慢查询日志位置

long_query_time = xx秒 指定慢查的阈值

log_query_not_using_indexes = [ON|OFF]

log_slow_slave_statements = [ON|OFF] 指定binlog的日志格式
```







####二进制日志

```
binlog
记录所有对数据库中数据的修改的语句


开启二进制日志
log-bin = path/base_name 
base_name 表示二进制日志文件的前缀

log_bin = [ON|OFF] 是否打开和关闭 
+---------------------------------+--------------------------------+
| Variable_name                   | Value                          |
+---------------------------------+--------------------------------+
| log_bin                         | ON                             |
| log_bin_basename                | /var/lib/mysql/mysql-bin       |
| log_bin_index                   | /var/lib/mysql/mysql-bin.index |
| log_bin_trust_function_creators | OFF                            |
| log_bin_use_v1_row_events       | OFF                           


binlog_format = [ROW|STATEMENT|MIXED] 

sql : update table_name set col_name = '' where condition

ROW 记录的是每一行数据的修改,一共影响多少行数据就会记录多少条

STATEMNT: 只是记录的当前的sql语句

MIXED: 是根据情况，mysql内部来决定是记录具体sql还是每一行修改的信息


bin_log_row_image = [FULL|MINIMAL|NOBLOB]

FULL： 记录的是被修改的前后的所有值

MINIMAL : 记录的只是当前行被修改的前后的值

NOBLOB: 记录的是被修改后的行的所有值，除了BLOB 类型

####查看二进制文件
mysqlbinlog --no-defaults -vv --base64-output=DECODE-ROWS bin_log.log

binlog_rows_query_log_event [ON|OFF] 在row格式下是否记录当前执行的sql语句

log_slave_updates = [ON|OFF]

sync_binlog = [1|0] 用于如何刷新binlog到磁盘
1 代表写入一次则刷新二进制日志到磁盘

expire_log_days = days mysql设置 自动过期的二进制日志时间


```



####中继日志

```
relay_log：用于主从复制，临时存储从从主库同步的二进制日志

relay_log = filename

relay_log_purge = [ON|OFF] 是否对relay log自动清理
```



#### 存储引擎层日志

#### Redo Log

```
作用：
  具体来说，当有一条记录需要更新的时候，InnoDB 引擎就会先把记录写到 redo log（粉板）里面，并更新内存，这个时候更新就算完成了。同时，InnoDB 引擎会在适当的时候，将这个操作记录更新到磁盘里面，而这个更新往往是在系统比较空闲的时候做，这就像打烊以后掌柜做的事。
  

  InnoDB 的 redo log 是固定大小的，比如可以配置为一组 4 个文件，每个文件的大小是 1GB，那么这块“粉板”总共就可以记录 4GB 的操作。从头开始写，写到末尾就又回到开头循环写，如下面这个图所示。
```

![1572965037717](assets\1572965037717.png)

```
wirte pos 是当前记录的位置，一边写一边后移，写到第 3 号文件末尾后就回到 0 号文件开头。
checkpoint 是当前要擦除的位置，也是往后推移并且循环的，擦除记录前要把记录更新到数据文件。
write pos 和 checkpoint 之间的是“粉板”上还空着的部分，可以用来记录新的操作。如果write pos 追上checkpoint，表示“粉板”满了，这时候不能再执行新的更新，得停下来先擦掉一些记录，把 checkpoint 推进一下。

有了 redo log，InnoDB 就可以保证即使数据库发生异常重启，之前提交的记录都不会丢失，

这个能力称为crash-safe。

要理解 crash-safe 这个概念，可以想想我们前面赊账记录的例子。只要赊账记录记在了粉板上或写在了账本上，之后即使掌柜忘记了，比如突然停业几天，恢复生意后依然可以通过账本和粉板上的数据明确赊账账目。
```



##### RedoLog 和 Binlog的区别

```
这两种日志有以下三点不同。

1. redo log 是 InnoDB 引擎特有的；binlog 是 MySQL 的 Server 层实现的，所有引擎都可以使用。

2. redo log 是物理日志，记录的是“在某个数据页上做了什么修改”；binlog 是逻辑日志，记录的是这个语句的原始逻辑，比如“给 ID=2 这一行的 c 字段加 1 ”。

3. redo log 是循环写的，空间固定会用完；binlog 是可以追加写入的。“追加写”是指binlog 文件写到一定大小后会切换到下一个，并不会覆盖以前的日志。
```



```
update T set c=c+1 where id = 2；
流程如下：
	1.执行器先找引擎取 ID=2 这一行。ID 是主键，引擎直接用树搜索找到这一行。如果 ID=2这一行所在的数据页本来就在内存中，就直接返回给执行器；否则，需要先从磁盘读入内存，然后再返回。
	
	2. 执行器拿到引擎给的行数据，把这个值加上 1，比如原来是 N，现在就是 N+1，得到新的一行数据，再调用引擎接口写入这行新数据。
	
	3. 引擎将这行新数据更新到内存中，同时将这个更新操作记录到 redo log 里面，此时 redolog 处于 prepare 状态。然后告知执行器执行完成了，随时可以提交事务。

	4. 执行器生成这个操作的 binlog，并把 binlog 写入磁盘。
	
	5. 执行器调用引擎的提交事务接口，引擎把刚刚写入的 redo log 改成提交（commit）状态，更新完成。
	
 update 语句的执行流程图，图中浅色框表示是在 InnoDB 内部执行的，深色框表示是在执行器中执行的
```

![1572965470612](assets\1572965470612.png)



##### 两阶段提交

![1572965886032](assets\1572965886032.png)

![1572965909752](assets\1572965909752.png)

##### redo log参数

redo log 用于保证 crash-safe 能力。innodb_flush_log_at_trx_commit 这个参数设置成 1 的时候，表示每次事务的 redo log 都直接持久化到磁盘。这个参数我建议你设置成 1，这样可以保证 MySQL 异常重启之后数据不丢失。

#####

#####bin log参数

sync_binlog 这个参数设置成 1 的时候，表示每次事务的 binlog 都持久化到磁盘。这个参数我也建议你设置成 1，这样可以保证 MySQL 异常重启之后 binlog 不丢失。



#### Undo Log

在 MySQL 中，实际上每条记录在更新的时候都会同时记录一条回滚操作。记录上的最新值，通过回滚操作，都可以得到前一个状态的值。



参数

innodb_undo_tablespaces