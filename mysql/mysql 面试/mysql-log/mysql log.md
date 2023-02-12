# Mysql Log

Mysql 的日志分类包括error log 、 slow quer log，relay log ，undo log，redo log、binlog等等

其中关于Undo log 用于回滚数据，relay log 则用于主从同步数据，redo log 以及 binlog 用于持久化数据以及备份数据 到磁盘并且提供了crash 机制，下面我们只针对 binlog、redo log、error log 进行讲解

## Error Log

主要用来保存执行的错误信息





## Change Buffer





## Redo Log 与 Binlog

Redo log 是我们修改的数据值





### 二阶段提交



