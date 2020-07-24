## Mysql 实现 Oracle merge into 语句

### Oracle 

```sql
merge into emp a
using (select '111' as id,'周文军' as name from dual) b
on (a.id= b.id)
when matched then
  update set a.name= b.name
when not matched then
  insert (id,name) values(b.id,b.name);
```

--emp表中有id和name字段，当插入一个id=‘111’，name=‘周文军’的数据时，进行判断

1、如果原表存在id=111的人，则运行update语句。

2、如果表中不存在id=111的人，则运行insert语句。

> 解释

将插入的数据放置到b表中，和a表进行匹配，匹配规则Wieusing中的规则，如果匹配成功，则运行uodate，否则运行insert，此写法比先查询再做判断的效率提高很多。



### mysql

1、mysql中要求该表存在主键
MySQL中存在 insert ignore into 语句，用法和插入语句相同，但如果表中已存在该记录的主键，则忽略此插入语句。
MySQL中的存在更新否则插入的语句为replace into，语法同insert into，如

```sql
replace into emp (id,name) values('111','周文静');
```

> 解释
> 前提是id为emp的主键
> 如果存在id=‘111’的记录，则运行更新语句，否则插入。

2、A表和B表的结构一样，
当A表中的数据在B表中不存在时，把B表的数据添加到A表中；
当A表中的数据在B表中存在时（即唯一索引项相同时），把B表中的数据累加到A表中。

```sql
insert into tableA 
select
from tableB
on duplicate key update tableA.column= +tableB.column...;

insert into tableA(key1,key2,col1,col2)
select key1,key2,col1,col2
from tableB
on duplicate key update col1=col1+tableB.col1,col2=col2+tableB.col2;

insert into tableA(key1,key2,col1,col2)
select key1,key2,col1,col2
from (here :it also can be a temp table)tableB
on duplicate key update col1=col1+tableB.col1,col2=col2+tableB.col2;
```