## Innodb space 工具



### 安装



### 命令



### 查看 undo log 日志

第一步：查看 某个表 index 的 offset

```sh
innodb_space -s ibdata1 -T test/t -I PRIMARY index-record-offsets
```



第二步：根据 主键索引的offset 查看 offset 关联的 undo log

```sh
innodb_space -s ibdata1 -T test/t -p 3 -R 309 record-history
```



第三步：查看undo log的具体信息

```sh

```





### 扩展连接

- https://blog.csdn.net/lc11535/article/details/115709072
- https://juejin.cn/post/6844903844107780103





