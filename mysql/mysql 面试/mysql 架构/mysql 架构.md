# Mysql 架构



## mysql 的执行流程







## mysql 的日志

mysql 的日志大致分为 genrate log、error log、binlog、redo log、 undo log

其中我们只需要关注的只有后面四种

- error log：错误日志信息
- binlog：mysql server 端存储的执行的sql语句，一般用来备份、恢复、主从复制等等
- redo log：innodb 引擎端的数据持久化日志，用于 WAL 
- undo log ：主要保存了



## innodb 引擎



## 

