### EXPLAIN 执行计划

```
+----+-------------+-------+------+---------------+------+---------+------+------+-------+
| id | select_type | table | type | possible_keys | key  | key_len | ref  | rows | Extra |
+----+-------------+-------+------+---------------+------+---------+------+------+-------+
|  1 | SIMPLE      | film  | ALL  | NULL          | NULL | NULL    | NULL | 1000 | NULL  |
+----+-------------+-------+------+---------------+------+---------+------+------+-------+
```



#### ID

```
id列中的数字为一组数字，表示执行select语句的顺序
id相同时，执行顺序由上至下
id值越大则优先级越大，越先被执行

每一个select都会使id自增
```



SELECT_TYPE

```
simple ： 不包含子查询或者是union操作的查询

primary: 查询中如过包含子查询，那么最外层的查询被标记为primary

subquery：select列表中的子查询

dependent subquery：依赖外部结果的子查询
```

![1572432903345](assets\1572432903345.png)



TABLE

```
输出数据所在的表的名称
<unionM,N> 由id为M，N查询union产生的结果集
derivedN，subqueryN 由ID为N的查询产生的结果
```

![1572433185573](assets\1572433185573.png)



#### partitions

```
数据是在哪个分区
```



type

```

```

![1572433302768](assets\1572433302768.png)



Extra 

![1572433443560](assets\1572433443560.png)



![1572433461571](assets\1572433461571.png)



POSSIBLE_KEYS

![1572433481603](assets\1572433481603.png)



KEY

```
查询优化器优化查询实际所使用的索引

如过没有可用的索引，则显示为null

如查询使用了覆盖索引，则索引仅出现在key列中

```



KEY_LEN

```
索引使用的字节数
表示索引字段的最大可能长度
```



Ref

```
表示哪些列或常量被用于查询索引列上的值
```



Rows

```
表示mysql通过索引统计信息，估算的所读取的行数

Rows 值的大小只是统计大小，并不一定正确
```



Filtered

```
表示返回结果的行数占需读取行数的百分比

Filtered 列的值越大越好

Filtered 列的值依赖说统计信息
```







