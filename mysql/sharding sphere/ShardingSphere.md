# Sharding Sphere

ShardingSphere 由三部分组成，分别为sharding-jdbc、sharding-proxy以及sharding-sidecar三部分组成，我们现在需要关注的大多都是sharding-jdbc这一部分，sharding-jdbc是一款轻量级的分库、分表的框架

## 分表分库策略

### 分库分表

#### 分表的前提条件

首先我们要搞清楚为什么要分表、以及什么时候需要才需要进行分表，因为分表也有一些弊端，比如分表后可能产生的分布式事务以及主键自增问题都需要去解决

我们以mysql中的innodb引擎为例，我们知道mysql中的一张表在物理上是以一个文件的形式来进行存储的，一张表就是一个ibd文件，并且表相关的索引也是存放在ibd文件中，而表数据存储也是以B+树的形式来进行存放在ibd中的，当一个表的数据量越来越大的时候 B+树的高度也会越来越高，那么我们在进行数据查询时就会有多次磁盘IO，并且当并发量比较大时，单表IO性能就会比较差，而且当我们根据索引来进行筛选时同样也是基于单标io来决定的

#### 分库的前提条件

一般来说分表就已经可以解决我们大部门的问题了，为什么还需要分库呢

对于mysql来说，一个实例才被称为一个数据库，而数据库中又分为了不通的schema 与 schema下不同的表，而每个实例都对应不通的服务器，服务器本身也会有网路io、文件io、cpu等的一些限制，当并发量越来越大的时候我们单实例已经无法支撑的时候就需要进行所谓的分库，当然这是在表数据量比较大的前提下才需要做的，否则表的数据量比较小，而并发量比较大的话我们可以通过读写分离、多主多从的方式也可以减轻压力



### 分表分库的方案







## 分片

### 分片算法

Sharding 提供了四种分片算法以及两种分片策略

- 精确分片算法

对应PreciseShardingAlgorithm，用于处理使用单一键作为分片键的=与IN进行分片的场景。需要配合StandardShardingStrategy使用。

- 范围分片算法

对应RangeShardingAlgorithm，用于处理使用单一键作为分片键的BETWEEN AND、>、<、>=、<=进行分片的场景。需要配合StandardShardingStrategy使用。

- 复合分片算法

对应ComplexKeysShardingAlgorithm，用于处理使用多键作为分片键进行分片的场景，包含多个分片键的逻辑较复杂，需要应用开发者自行处理其中的复杂度。需要配合ComplexShardingStrategy使用。

- Hint分片算法

对应HintShardingAlgorithm，用于处理使用Hint行分片的场景。需要配合HintShardingStrategy使用。



### 分片策略

- 标准分片策略

对应StandardShardingStrategy。提供对SQL语句中的=, >, <, >=, <=, IN和BETWEEN AND的分片操作支持。StandardShardingStrategy只支持单分片键，提供PreciseShardingAlgorithm和RangeShardingAlgorithm两个分片算法。PreciseShardingAlgorithm是必选的，用于处理=和IN的分片。RangeShardingAlgorithm是可选的，用于处理BETWEEN AND, >, <, >=, <=分片，如果不配置RangeShardingAlgorithm，SQL中的BETWEEN AND将按照全库路由处理。

- 复合分片策略

对应ComplexShardingStrategy。复合分片策略。提供对SQL语句中的=, >, <, >=, <=, IN和BETWEEN AND的分片操作支持。ComplexShardingStrategy支持多分片键，由于多分片键之间的关系复杂，因此并未进行过多的封装，而是直接将分片键值组合以及分片操作符透传至分片算法，完全由应用开发者实现，提供最大的灵活度。

- 行表达式分片策略

对应InlineShardingStrategy。使用Groovy的表达式，提供对SQL语句中的=和IN的分片操作支持，只支持单分片键。对于简单的分片算法，可以通过简单的配置使用，从而避免繁琐的Java代码开发，如: `t_user_$->{u_id % 8}` 表示t_user表根据u_id模8，而分成8张表，表名称为`t_user_0`到`t_user_7`。

- Hint分片策略

对应HintShardingStrategy。通过Hint指定分片值而非从SQL中提取分片值的方式进行分片的策略。

- 不分片策略

对应NoneShardingStrategy。不分片的策略。





## 策略配置

### 分片策略配置

对于分片策略存有数据源分片策略和表分片策略两种维度。

- 数据源分片策略

对应于DatabaseShardingStrategy。用于配置数据被分配的目标数据源。

- 表分片策略

对应于TableShardingStrategy。用于配置数据被分配的目标表，该目标表存在与该数据的目标数据源内。故表分片策略是依赖与数据源分片策略的结果的。



### 自增逐渐生成策略

UUID + 雪花算法



### 数据节点配置

- 均匀分布

指数据表在每个数据源内呈现均匀分布的态势，例如：

```
db0
  ├── t_order0 
  └── t_order1 
db1
  ├── t_order0 
  └── t_order1
```

那么数据节点的配置如下：

```
db0.t_order0, db0.t_order1, db1.t_order0, db1.t_order1
```

- 自定义分布

指数据表呈现有特定规则的分布，例如：

```
db0
  ├── t_order0 
  └── t_order1 
db1
  ├── t_order2
  ├── t_order3
  └── t_order4
```

那么数据节点的配置如下：

```
db0.t_order0, db0.t_order1, db1.t_order2, db1.t_order3, db1.t_order4
```



## 流程剖析

### 解析引擎



### 路由引擎



### 改写引擎



### 执行引擎



### 归并引擎





