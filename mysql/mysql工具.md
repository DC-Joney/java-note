#### mysql 分析二进制日志文件工具

```
mysqlbinlog
```





#### 修改表结构工具

```
pt-online-schema-change
```



#### 删除冗余索引

```
pt-duplicate-key-checker h = 127.0.0.1 检查冗余索引
```







#### 慢日志查询工具

```
mysqldumpslow slow_query.log

options:
	-s order
	order [c,t,l,r,at,al,ar]
	c: 总次数
	t: 总时间
	l:锁的时间
	r:总数据行
	at，al，ar：t，l，r平均数 
	例如 at = 总时间/总次数
	-t 指定返回多少条信息
	
pt-query-digest
	--explain h[host] u[user] p[passwd] slow_query_log
	
explain 对每个慢查询进行explain执行计划    
```



#### 备份数据

##### （待续）增量备份（物理备份，ibd文件）

```
xtrabackup
待续
```



##### 全量备份（逻辑备份 sql）

```
需要备份的数据库需要以下权限：
    SELECT,
    RELOAD,
    LOCK TABLES,
    REPLICATION CLIENT,
    SHOW VIEW,
    PROCESS

mysqldump(会导致锁表)
[] 可以是多个

备份多个表
mysqldump [options] database [tables] 

备份多个库
mysqldump [options] --databases [options] DB1 [DB2..]

所有数据库备份
mysqldump [options] -all-databases [options]

options:
	-u,--user=name
	-p,--password=passwd
	--single-transaction 在数据库备份前先开启一个事物，保证数据一致性
	-l，--lock-tables myisam（锁表）只能保证一个db下表的数据一致性
	-x,--lock-all-tales 可以保证整个实例下的数据库都是一致的
	--master-data=[1/2]
	值为1的话 change master语句会出现在备份表中
	值为2的时候，change master默认是会被注释的
	
	-R,--routines 表示数据库中的存储过程
	
	--triggers 数据库中的触发器
	
	-E,--events 表示数据库中的调度事件
	
	-hex-blob 为 blob类型 备份为16进制文件
	
	--tab = path 切分备份文件为ddl语句与数据sql
	
	-w,--where = '过滤条件' where 支支持单表数据条件的导出
	
mysqldump -ubackup -p --single-transcation --master-data=2
	-R --triggers -E  database_name > path.sql

mysqldump -ubackup -p --single-transcation --master-data=2
	-R --triggers -E --tab='ddl_path' database_name 
生成的文件默认为ddl与txt数据文件	
```



##### mysql sql语句备份

```
create table table_new_name as select * from table_old_name;

create table table_new_name like table_old_name
```



##### 二进制文件备份

![1572436410351](assets\1572436410351.png)





#### 引入数据文件命令

##### mysqlimport

```
create tablespace zyl_data  logging  datafile '/oracle/data/data_temp.dbf' size 100m  autoextend on  next 50m maxsize UNLIMITED  extent management local;
```



```
create tablespace zyl_data logging datafile '/oracle/data/zyl_data.dbf' size 100m autoextend on  next 50m maxsize UNLIMITED extent management local;
```





##### 命令行引入

```
mysql -uroot -p db_name < backup.sql
```



##### mysql内部引入

```
mysql>  source /tmp/backup.sql
```



##### 引入 csv类型的 txt文件

```
load file infile '/path/sql.txt'
```





#### mysql 客户端执行sql

```
mysql -uroot -p -e'create database db_name'
```



#### 比较mysql动态参数和配置文件参数的区别

```
pt-config-diff

set persist 
```

![1572484965433](assets\1572484965433.png)



#### 迁移数据库账号

![1572494273135](assets\1572494273135.png)