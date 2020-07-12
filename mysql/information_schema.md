## information_schema 数据库信息表



#### Tables 存储表信息



![1572445613917](assets\1572445613917.png)



####  SCHEMATA

SCHEMATA表：提供了当前mysql实例中所有数据库的信息。是show databases的结果取之此表。 



####  TABLES 

 TABLES表：提供了关于数据库中的表的信息（包括视图）。详细表述了某个表属于哪个schema，表类型，表引擎，创建时间等信息。是show tables from schemaname的结果取之此表。 



####  COLUMNS 

COLUMNS表：提供了表中的列信息。详细表述了某张表的所有列以及每个列的信息。是show columns from schemaname.tablename的结果取之此表。 



#### STATISTICS

 STATISTICS表：提供了关于表索引的信息。是show index from schemaname.tablename的结果取之此表。 



### 用户权限表 

#### USER_PRIVILEGES（用户权限） 

 给出了关于全程权限的信息。该信息源自mysql.user授权表。是非标准表。 



 #### SCHEMA_PRIVILEGES（方案权限） 

SCHEMA_PRIVILEGES（方案权限）表：给出了关于方案（数据库）权限的信息。该信息来自mysql.db授权表。是非标准表。



#### TABLE_PRIVILEGES（表权限 )

 给出了关于表权限的信息。该信息源自mysql.tables_priv授权表。是非标准表。 



#### COLUMN_PRIVILEGES（列权限） 

 给出了关于列权限的信息。该信息源自mysql.columns_priv授权表。是非标准表。 



### 字符集表

#### CHARACTER_SETS（字符集） 

 提供了mysql实例可用字符集的信息。是SHOW CHARACTER SET结果集取之此表。 



####  COLLATIONS表 

 COLLATIONS表：提供了关于各字符集的对照信息。 



####  COLLATION_CHARACTER_SET_APPLICABILITY 

 指明了可用于校对的字符集。这些列等效于SHOW COLLATION的前两个显示字段。 



### 表、列约束

#### TABLE_CONSTRAINTS

表：描述了存在约束的表。以及表的约束类型。

#### KEY_COLUMN_USAGE

表：描述了具有约束的键列。



#### Processlist  

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

 

 

 

 

 

