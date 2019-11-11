```
InnoDB引擎
	结构
		change buffer
		buffer pool
		自适应hash索引
		内存结构
			缓冲池：LRU算法
			change buffer：针对增删改
				innodb_change_buffering 
					all
					none
					inserts
					deletes
					changes
					purges
			自适应 hash索引
			日志缓冲区
	特性
		Inndodb是一种事物性存储引擎
		完全支持事物的ACID特性
		Redo Log 和 Undo Log 来实现事物的特性
	事物
	锁
		定义
		分类
			共享锁
				称为读锁
			独占锁
				写锁
		锁的粒度
			表级锁
				lock table tablename write
			行级锁
		阻塞和死锁
			阻塞
			死锁
		支持行锁
		行级锁可以最大从程度的支持并发
		行级锁可是由存储引擎层实现的
		死锁
			mysql死锁会自动解决
	表空间（存储数据）
		frm文件
			用于存储表结构
		系统表空间
			名称
				ibdataX
			缺点
				系统表空间无法简单的收缩文件大小
				产生IO瓶颈
				大量磁盘脆片
			存储内容
				Innodb数据字典
				undo回滚段
		独立表空间
			名称
				table_name.ibd
			优点
				可以单独对某个已经清理的表空间进行空间回收，并且重建
					optimeize table
		参数
			innodb_file_per_table:决定是存储在系统表空间还是独立表空间
				ON
					tableName.ibd
				OFF
					ibdataX
		系统表空间转移到独立表空间
	日志（实现事物的ACID特性）
		Undo Log：存储的是未提交的事物, 帮助未提交事物进行回滚（当对某一行数据进行修改，事物并未提交时，那么另一个链接读取的是undo log日志中的数据）
		Redo log：实现事物的持久性,存储的是已经提交的事物
			innodb_log_buffer_size：决定redo log缓存区大小
			innodb_log_files_in_group：redo log文件数量
			路径 
				ib_logfile0
				ib_logfile1
		参数
	innodb 引擎内存状态
		show ennine innodb status
```

