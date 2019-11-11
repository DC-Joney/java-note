#### mysql的事物特性 ?

```
Mysql事物的四大特性 ACID特性

A： 原子性 
一个事物总的所有操作，要么全部完成要不全部不完成

C： 一致性
在事物开始之前和事物结束之后，数据的完整性没有破坏

I： 隔离性
事物的隔离性要求每个读写事物的对象与其他事物的操作对象能相互隔离，既该事物提交前对于其他事物是不可见的

D： 持久性


数据库的四大隔离级别：

读未提交（read uncommitted）是指，一个事务还没提交时，它做的变更就能被别的事务看到。

读已提交（Read_Commited） 是指 一个事务提交之后，它做的变更才会被其他事务看到

可重复读（RR） 是指 一个事务执行过程中看到的数据，总是跟这个事务在启动时看到的数据是一致的。当然在可重复读隔离级别下，未提交变更对其他事务也是不可见的。

串行化(serializable) 顾名思义是对于同一行记录，“写”会加“写锁”，“读”会加“读锁”。当出现读写锁冲突的时候，后访问的事务必须等前一个事务执行完成，才能继续执行
```

![1572323721621](C:/Users/Administrator/Desktop/%E9%9D%A2%E8%AF%95/%E9%9D%A2%E8%AF%95%E9%A2%98/mysql/assets/1572323721621.png)





####在RR隔离级别下的事物

![1572966818171](assets\1572966818171.png)



#### 事物启动的方式

![1572966564970](assets\1572966564970.png)



#### Innodb是如何来实现事物的？

```
依赖于 Undo log 和 redo log

undo log（回滚日志）： 用于记录数据修改前的状态 （原子性）

redo log（重做日志）：用于记录数据修改后的状态	（一致性）

隔离性 ： 使用的是锁机制，分为共享锁和排他锁

持久性： 依赖于 redo log 和 undo log 来实现的
```

![1572487812404](C:/Users/Administrator/Desktop/%E9%9D%A2%E8%AF%95/%E9%9D%A2%E8%AF%95%E9%A2%98/mysql/assets/1572487812404.png)



####RR的事物隔离

MVCC的实现

```
InnoDB 在实现 MVCC 时用到的一致性读视图，即 consistent read view，用于支持 RC（Read Committed，读提交）和 RR（Repeatable Read，可重复度）隔离级别的实现

InnoDB 里面每个事务有一个唯一的事务 ID，叫作 transaction id。它是在事务开始的时候向InnoDB 的事务系统申请的，是按申请顺序严格递增的

而每行数据也都是有多个版本的。每次事务更新数据的时候，都会生成一个新的数据版本，并且把 transaction id 赋值给这个数据版本的事务 ID，记为 row trx_id。同时，旧的数据版本要保留，并且在新的数据版本中，能够有信息可以直接拿到它

也就是说，数据表中的一行记录，其实可能有多个版本 (row), 每个版本有自己的 row trx_id。
```

![1572973118828](assets\1572973118828.png)

```
因此，InnoDB 代码实现上，一个事务只需要在启动的时候，找到所有已经提交的事务 ID 的最大值，记为 up_limit_id；然后声明说，“如果一个数据版本的 row trx_id 大于 up_limit_id，我就不认，我必须要找到它的上一个版本”。当然，如果一个事务自己更新的数据，它自己还是要认的。

因此，InnoDB 代码实现上，一个事务只需要在启动的时候，找到所有已经提交的事务 ID 的最大值，记为 up_limit_id；然后声明说，“如果一个数据版本的 row trx_id 大于 up_limit_id，我就不认，我必须要找到它的上一个版本”。当然，如果一个事务自己更新的数据，它自己还是要认的
```

![1572973365529](assets\1572973365529.png)

```
语句 Q1 在事务 B 中，更新了行之后查询 ; Q2 在只读事务A 中查询，并且时间顺序上是在 Q1 的后面
Q1 返回的 k 的值是 3，而语句 Q2 返回的 k 的值是 1

这个历史版本，什么时候可以被删除掉呢？
当没有事务再需要它的时候，就可以删掉。
```



#### mysql的当前读与快照读

```
当前读 : 表示 读取的是数据的最新版本
select .... lock in share mode, select ... for update;
update,delete,insert 

快照读 : 不加锁的非阻塞读取
select 基于 mvcc的实现

SELECT title FROM film WHERE film_id = 1;
输出 demo


BEGIN ;
UPDATE film SET title = '123' WHERE film_id =1;
COMMIT ;


BEGIN ;
SELECT title FROM film WHERE film_id = 1;
SELECT title FROM film WHERE film_id = 1 LOCK IN SHARE MODE ;

输出结果 分别为 demo 和 123
```

![1572500223121](C:/Users/Administrator/Desktop/%E9%9D%A2%E8%AF%95/%E9%9D%A2%E8%AF%95%E9%A2%98/mysql/assets/1572500223121.png)









####事物执行的开始时间点

```
MySQL 中事务开始的时间

一般我们会认为 begin/start transaction 是事务开始的时间点，也就是一旦我们执行了 start transaction，就认为事务已经开始了，其实这是错误的。上面的实验也说明了这一点。事务开始的真正的时间点(LSN)，是 start transaction 之后执行的第一条语句，不管是什么语句，不管成功与否。

但是如果你想要达到将 start transaction 作为事务开始的时间点，那么我们必须使用：
START TRANSACTION WITH consistent snapshot
```

