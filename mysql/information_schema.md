####Tables 存储表信息



![1572445613917](assets\1572445613917.png)





####Processlist 

当前连接到mysql的客户端信息



#### innodb_trx

用于记录当前正在执行事物的sql语句

```
你可以在 information_schema 库的 innodb_trx 这个表中查询长事务，比如下面这个语句，用于查找持续时间超过 60s 的事务。

select * from information_schema.innodb_trx where TIME_TO_SEC(timediff(now(),trx_started))>60

select id as thread_id, info as sql_info  from information_schema.processlist join information_schema.innodb_trx on id = trx_mysql_thread_id and trx_state = 'RUNNING';
```



#### INNODB_TRX的结构

| 字段名                | 说明                                                         |
| --------------------- | ------------------------------------------------------------ |
| trx_id                | InnoDB存储引擎内部唯一的事务ID                               |
| trx_state             | 当前事务的状态                                               |
| trx_started           | 事务的开始时间                                               |
| trx_requested_lock_id | 等待事务的锁id.如果trx_state的状态为LOCK_WAIT,那么该值代表当前的事务等待之前事务占用锁资源的id.若trx_state不是LOCK WAIT,该值为NULL |
| trx_wait_started      | 事务等待开始的时间                                           |
| trx_weight            | 事务的权重,反映了一个事务修改和锁住的行数.在Innodb存储引擎中,当发生死锁需要回滚时,innodb存储引擎会选择该值最小的进行回滚 |
| trx_mysql_thread_id   | mysql中的线程id,show processlist显示的结果                   |
| trx_query             | 事务运行的SQL语句                                            |

```
SELECT * from information_schema.INNODB_TRX;
```

------



#### INNODB_LOCKS的结构

| 字段名      | 说明                                       |
| ----------- | ------------------------------------------ |
| lock_id     | 锁的ID                                     |
| lock_trx_id | 事务的id                                   |
| lock_mode   | 锁的模式                                   |
| lock_type   | 锁的类型,表锁还是行锁                      |
| lock_table  | 要加锁的表                                 |
| lock_index  | 锁住的索引                                 |
| lock_space  | 锁对象的space id                           |
| lock_page   | 事务锁定页的数量.若是表锁,则该值为null     |
| lock_rec    | 事务锁定行的数量,若是表锁,则该值为null     |
| lock_data   | 事务锁定记录的主键值,若是表锁,则该值为null |

```
SELECT * from information_schema.INNODB_LOCKS;
```

------



#### INNODB_LOCK_WAITS的结构

| 字段名             | 说明             |
| ------------------ | ---------------- |
| requesting_trx_id  | 申请资源的事务id |
| requesting_lock_id | 申请的锁id       |
| blocking_trx_id    | 阻塞的事务id     |
| blocking_trx_id    | 阻塞的锁的id     |

```
SELECT * from information_schema.INNODB_LOCK_WAITS;
```

 

 

 

 

 

