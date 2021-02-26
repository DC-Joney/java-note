v

MySQL 数据类型中的 integer types 有点奇怪。你可能会见到诸如：int(3)、int(4)、int(8) 之类的 int 数据类型。刚接触 MySQL 的时候，我还以为 int(3) 占用的存储空间比 int(4) 要小， int(4) 占用的存储空间比 int(8) 小。

后来，参看 MySQL 手册，发现自己理解错了。

```
`int``(M): M indicates the maximum display width ``for` `integer` `types.`
```

在 integer 数据类型中，M 表示最大显示宽度。

原来，在 int(M) 中，M 的值跟 int(M) 所占多少存储空间并无任何关系。 int(3)、int(4)、int(8) 在磁盘上都是占用 4 btyes 的存储空间。说白了，除了显示给用户的方式有点不同外，int(M) 跟 int 数据类型是相同的。

另外，int(M) 只有跟 zerofill 结合起来，才能使我们清楚的看到不同之处。

```mysql
`mysql> ``drop` `table` `if exists t;``mysql> ``create` `table` `t(id ``int` `zerofill);``mysql> ``insert` `into` `t(id) ``values``(10);``mysql> ``select` `* ``from` `t;``+``------------+``| id  |``+``------------+``| 0000000010 |``+``------------+``mysql> ``alter` `table` `t change ``column` `id id ``int``(3) zerofill;``mysql> ``select` `* ``from` `t;``+``------+``| id |``+``------+``| 010 |``+``------+``mysql>``mysql> ``alter` `table` `t change ``column` `id id ``int``(4) zerofill;``mysql> ``select` `* ``from` `t;``+``------+``| id |``+``------+``| 0010 |``+``------+``mysql>``mysql> ``insert` `into` `t(id) ``values``(1000000);``mysql> ``select` `* ``from` `t;``+``---------+``| id |``+``---------+``| 0010 |``| 1000000 |``+``---------+`
```

从上面的测试可以看出，“(M)”指定了 int 型数值显示的宽度，如果字段数据类型是 int(4)，则：当显示数值 10 时，在左边要补上 “00”；当显示数值 100 是，在左边要补上“0”；当显示数值 1000000 时，已经超过了指定宽度“（4）”，因此按原样输出。

在使用 MySQL 数据类型中的整数类型（tinyint、smallint、 mediumint、 int/integer、bigint）时，非特殊需求下，在数据类型后加个“(M)”，我想不出有何意义。

**下面补充一下数据类型**

**1、整型**

```
`MySQL数据类型 含义（有符号）``tinyint(m) 1个字节 范围(-128~127)``smallint``(m) 2个字节 范围(-32768~32767)``mediumint(m) 3个字节 范围(-8388608~8388607)``int``(m) 4个字节 范围(-2147483648~2147483647)``bigint``(m) 8个字节 范围(+-9.22*10的18次方)`
```

取值范围如果加了unsigned，则最大值翻倍，如tinyint unsigned的取值范围为(0~256)。
int(m)里的m是表示SELECT查询结果集中的显示宽度，并不影响实际的取值范围，没有影响到显示的宽度，不知道这个m有什么用。

**2、浮点型(float和double)**

```
`MySQL数据类型 含义``float``(m,d) 单精度浮点型 8位精度(4字节) m总个数，d小数位``double``(m,d) 双精度浮点型 16位精度(8字节) m总个数，d小数位`
```

设一个字段定义为float(5,3)，如果插入一个数123.45678,实际数据库里存的是123.457，但总个数还以实际为准，即6位。

**3、定点数**

浮点型在数据库中存放的是近似值，而定点类型在数据库中存放的是精确值。



**4、字符串(char,varchar,_text)**

```
`MySQL数据类型 含义``char``(n) 固定长度，最多255个字符``varchar``(n) 可变长度，最多65535个字符``tinytext 可变长度，最多255个字符``text 可变长度，最多65535个字符``mediumtext 可变长度，最多2的24次方-1个字符``longtext 可变长度，最多2的32次方-1个字符`
```

**5、日期和时间数据类型**

```
`MySQL数据类型 含义``date` `3字节，日期，格式：2014-09-18``time` `3字节，时间，格式：08:42:30``datetime 8字节，日期时间，格式：2014-09-18 08:42:30``timestamp` `4字节，自动存储记录修改的时间``year` `1字节，年份`
```