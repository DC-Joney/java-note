## Mongodb 常用操作

### 查询

#### find()

说明：返回所有的集合中所有的文档

示例：

```
db.collection.find(query,projection)
```

可选参数：

- query： 表示集合中的字段比如： {_id: "1"} ,查询id 为1的文档

- projection：返回指定的字段 ，比如： {userName: 1}、{userName: true}



#### findOne()

说明： 返回一条数据，用法同上



#### findAndModify 



#### 



| 读取操作                                                     | 描述                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [`db.collection.find()`](https://mongodb.net.cn/manual/reference/method/db.collection.find/#db.collection.find) | ``在集合中找到符合条件的文档。如果``未指定标准或该标准为空（即`{}`），则读取操作将选择集合中的所有文档。下面的示例选择`users` 集合中`name`字段等于的文档`"Joe"`：`coll = db.users; coll.find( { name: "Joe" } ); ` |
| [`db.collection.find(, )`](https://mongodb.net.cn/manual/reference/method/db.collection.find/#db.collection.find) | 查找符合``条件的文档，然后仅返回中的特定字段``。以下示例从集合中选择所有文档，但仅返回该`name`字段和该`_id`字段。将 `_id`始终返回，除非明确指定有来无回。`coll = db.users; coll.find( { }, { name: true } ); `有关指定的更多信息``， |
| [`db.collection.find().sort()`](https://mongodb.net.cn/manual/reference/method/cursor.sort/#cursor.sort) | 返回指定结果。``以下示例从集合中选择所有文档，并返回按`name`字段升序（`1`）排序的结果。使用`-1`降序排序：`coll = db.users; coll.find().sort( { name: 1 } ); ` |
| [`db.collection.find().sort()`](https://mongodb.net.cn/manual/reference/method/cursor.sort/#cursor.sort) | 返回``与指定条件中的条件相符的文档。``                       |
| [`db.collection.find( ... ).limit( )`](https://mongodb.net.cn/manual/reference/method/cursor.limit/#cursor.limit) | 将结果限制为``行。如果只需要一定数量的行以获得最佳性能，则强烈建议使用。 |
| [`db.collection.find( ... ).skip( )`](https://mongodb.net.cn/manual/reference/method/cursor.skip/#cursor.skip) | 跳过``结果。                                                 |
| [`db.collection.count()`](https://mongodb.net.cn/manual/reference/method/db.collection.count/#db.collection.count) | 返回集合中的文档总数。                                       |
| [`db.collection.find().count()`](https://mongodb.net.cn/manual/reference/method/cursor.count/#cursor.count) | 返回与查询匹配的文档总数。在[`count()`](https://mongodb.net.cn/manual/reference/method/cursor.count/#cursor.count)忽略[`limit()`](https://mongodb.net.cn/manual/reference/method/cursor.limit/#cursor.limit)和[`skip()`](https://mongodb.net.cn/manual/reference/method/cursor.skip/#cursor.skip)。例如，如果有100条记录匹配，但限制为10， [`count()`](https://mongodb.net.cn/manual/reference/method/cursor.count/#cursor.count)则将返回100。这比迭代自己的速度更快，但仍然需要时间。 |
| [`db.collection.findOne()`](https://mongodb.net.cn/manual/reference/method/db.collection.findOne/#db.collection.findOne) | 查找并返回一个文档。如果找不到，则返回null。以下示例在`users` 集合中选择一个与`name`字段匹配的文档`"Joe"`：`coll = db.users; coll.findOne( { name: "Joe" } ); `在内部，该[`findOne()`](https://mongodb.net.cn/manual/reference/method/db.collection.findOne/#db.collection.findOne) 方法是[`find()`](https://mongodb.net.cn/manual/reference/method/db.collection.find/#db.collection.find)带有的方法[`limit(1)`](https://mongodb.net.cn/manual/reference/method/cursor.limit/#cursor.limit)。 |



### 插入操作

在插入文档时，如果存在_id 则 使用现在id的值，而不会重新生成，如果不存在则自动生成

#### insertOne

说明：将单个文档插入到集合中

语法：

```js
db.collection.insertOne(
   <document>,
   {
      writeConcern: <document>
   }
)
```

可选参数

 writeConcern : 待定



#### insertMany()

说明：插入多个文档

语法：

```
db.collection.insertMany(
   [ <document 1> , <document 2>, ... ],
   {
      writeConcern: <document>,
      ordered: <boolean>
   }
)
```

| 参数           | 类型   | 描述                                                         |
| :------------- | :----- | :----------------------------------------------------------- |
| `document`     | 文献   | 要插入到集合中的一组文档。                                   |
| `writeConcern` | 文献   | 可选的。表达[书面关切的](https://mongodb.net.cn/manual/reference/write-concern/)文件。省略使用默认的写关注。如果在事务中运行，则不要为操作明确设置写关注点。要对事务使用写关注，请参见 [事务和写关注](https://mongodb.net.cn/manual/core/transactions/#transactions-write-concern)。 |
| `ordered`      | 布尔值 | 可选的。一个布尔值，指定[`mongod`](https://mongodb.net.cn/manual/reference/program/mongod/#bin.mongod)实例应执行有序插入还是无序插入。默认为`true`。 |

可选参数 ：

- `document `： 要插入到集合中的一组文档
- `writeConcern`： 待定
- `ordered`: 可选的。一个布尔值，指定[`mongod`](https://mongodb.net.cn/manual/reference/program/mongod/#bin.mongod)实例应执行有序插入还是无序插入。默认为`true`。



#### insert()  

说明： 插入一个多多个文档

语法如下：

```
db.collection.insert(
   <document or array of documents>,
   {
     writeConcern: <document>,
     ordered: <boolean>
   }
)
```

可选参数

- document： 单个文档或者是文档集合
- writeConcern： 待定
- ordered：bool 值， 如果为`true`，请按顺序在数组中插入文档，并且如果其中一个文档发生错误，则MongoDB将返回而不处理数组中的其余文档。如果为`false`，请执行无序插入，并且如果其中一个文档发生错误，请继续处理数组中的其余文档



#### 其他可以插入到集合中的API

- [`db.collection.update()`](https://mongodb.net.cn/manual/reference/method/db.collection.update/#db.collection.update)与该 选项一起使用时。`upsert: true`
- [`db.collection.updateOne()`](https://mongodb.net.cn/manual/reference/method/db.collection.updateOne/#db.collection.updateOne)与该选项一起使用时。`upsert: true`
- [`db.collection.updateMany()`](https://mongodb.net.cn/manual/reference/method/db.collection.updateMany/#db.collection.updateMany)与该选项一起使用时。`upsert: true`
- [`db.collection.findAndModify()`](https://mongodb.net.cn/manual/reference/method/db.collection.findAndModify/#db.collection.findAndModify)与该选项一起使用时。`upsert: true`
- [`db.collection.findOneAndUpdate()`](https://mongodb.net.cn/manual/reference/method/db.collection.findOneAndUpdate/#db.collection.findOneAndUpdate)与该选项一起使用时 。`upsert: true`
- [`db.collection.findOneAndReplace()`](https://mongodb.net.cn/manual/reference/method/db.collection.findOneAndReplace/#db.collection.findOneAndReplace)与该选项一起使用时 。`upsert: true`
- [`db.collection.save()`](https://mongodb.net.cn/manual/reference/method/db.collection.save/#db.collection.save)。
- [`db.collection.bulkWrite()`](https://mongodb.net.cn/manual/reference/method/db.collection.bulkWrite/#db.collection.bulkWrite)。





### 批量插入

### 更新文件

#### update()

同下，根据 multi 参数来控制是修改一条还是多条



#### updateOne()

说明：更新某一行数据, 如果 query查询匹配多行参数，也只修改第一行

语法：

```
db.collection.updateOne(
   <filter>,
   <update>,
   {
     upsert: <boolean>,
     writeConcern: <document>,
     collation: <document>,
     arrayFilters: [ <filterdocument1>, ... ],
     hint:  <document|string>        // Available starting in MongoDB 4.2.1
   }
)
```

- query

  更新的选择条件。可以使用与fifind（）方法中相同的查询选择器，类似sql update查询内where后面的。。在3.0版中进行了更改：当使用upsert:true执行update（）时，

- update

  要应用的修改。该值可以是：包含更新运算符表达式的文档，或仅包含：对的替换文档,或在MongoDB 4.2中启动聚合管道。管道可以由以下阶段组成

  - [`$addFields`](https://mongodb.net.cn/manual/reference/operator/aggregation/addFields/#pipe._S_addFields) 及其别名 [`$set`](https://mongodb.net.cn/manual/reference/operator/aggregation/set/#pipe._S_set)
  - [`$project`](https://mongodb.net.cn/manual/reference/operator/aggregation/project/#pipe._S_project) 及其别名 [`$unset`](https://mongodb.net.cn/manual/reference/operator/aggregation/unset/#pipe._S_unset)
  - [`$replaceRoot`](https://mongodb.net.cn/manual/reference/operator/aggregation/replaceRoot/#pipe._S_replaceRoot)及其别名[`$replaceWith`](https://mongodb.net.cn/manual/reference/operator/aggregation/replaceWith/#pipe._S_replaceWith)。

  换句话说：它是update的对象和一些更新的操作符（如 inc...）等，

- upsert

  可选。如果设置为true，则在没有与查询条件匹配的文档时创建新文档。默认值为false，如果找不到匹配项，则不会插入新文档。

- multi

  可选。如果设置为true，则更新符合查询条件的多个文档。如果设置为false，则更新一个文档。默认值为false

-  arrayFilters

  可选的。筛选器文档数组，用于确定要对数组字段进行更新操作要修改的数组元素。

  在更新文档中，使用 [`$[\]`](https://mongodb.net.cn/manual/reference/operator/update/positional-filtered/#up._S_[]) 过滤后的位置运算符定义一个标识符，然后在数组过滤器文档中引用该标识符。如果该标识符未包含在更新文档中，则不能具有标识符的数组过滤器文档。

  注意

  在``必须以小写字母开头，并且只包含字母数字字符。

  您可以在更新文档中多次包含相同的标识符；但是，对于`$[identifier]`更新文档中的每个不同的标识符（），必须**精确地**指定**一个** 对应的数组过滤器文档。也就是说，您不能为同一标识符指定多个数组过滤器文档。例如，如果update语句包含标识符`x` （可能多次），则不能为以下内容指定以下内容 `arrayFilters`：包括2个单独的过滤器文档`x`：

  ```
  // INVALID
  
  [
    { "x.a": { $gt: 85 } },
    { "x.b": { $gt: 80 } }
  ]
  ```

  但是，您可以在单个过滤器文档中的相同标识符上指定复合条件，例如以下示例：

  ```
  // Example 1
  [
    { $or: [{"x.a": {$gt: 85}}, {"x.b": {$gt: 80}}] }
  ]
  // Example 2
  [
    { $and: [{"x.a": {$gt: 85}}, {"x.b": {$gt: 80}}] }
  ]
  // Example 3
  [
    { "x.a": { $gt: 85 }, "x.b": { $gt: 80 } }
  ]
  ```

提示： 

主要关注前四个参数即可。

（1）覆盖的修改 

如果我们想修改_id为1的记录，点赞量为1001，输入以下语句： 

执行后，我们会发现，这条文档除了likenum字段其它字段都不见了， 

（2）局部修改 

为了解决这个问题，我们需要使用修改器$set来实现，命令如下： 

我们想修改_id为2的记录，浏览量为889，输入以下语句： 

这样就OK啦。 

（3）批量的修改 

更新所有用户为 1003 的用户的昵称为 凯撒大帝 。 

```js
//默认只修改第一条数据 
db.comment.update({userid:"1003"},{$set:{nickname:"凯撒2"}}) 

//修改所有符合条件的数据 
db.comment.update({userid:"1003"},{$set:{nickname:"凯撒大帝"}},{multi:true})
```

提示：如果不加后面的参数，则只更新符合条件的第一条记录 



（3）列值增长的修改 

如果我们想实现对某列值在原有值的基础上进行增加或减少，可以使用 $inc 运算符来实现。 

需求：对3号数据的点赞数，每次递增1 

```js
db.comment.update({_id:"3"},{$inc:{likenum:NumberInt(1)}})
```





#### updateMany()

同上 默认是修改多条



#### replaceOne()

说明： 与 update 没指定 $set 的局部修改一样，默认为替换该 document 所有数据

```
{
	"_id" : "5",
	"articleid" : "100001",
	"content" : "研究表明，刚烧开的水千万不能喝，因为烫 嘴。",
	"userid" : "1003",
	"nickname" : "凯撒",
	"createdatetime" : ISODate("2019-08-06T11:01:02.521Z"),
	"likenum" : 3000,
	"state" : "2"
}

{ "_id" : "7", "state" : "2" }

> db.articleDetail.replaceOne({_id:"5"},{state:"3"})

{ "_id" : "5", "state" : "3" }
```



### 删除文档

#### deleteOne()

删除一条数据 

语法：

```
db.collection.deleteOne(
   <filter>,
   {
      writeConcern: <document>,
      collation: <document>
   }
)
```





#### deleteMany()

删除多条数据，语法同上



#### remove() 

说明：删除一个或者多个文档

语法：

```
db.collection.remove(
   <query>,
   {
     justOne: <boolean>,
     writeConcern: <document>,
     collation: <document>
   }
)
```

- `query`

   使用[查询运算符](https://mongodb.net.cn/manual/reference/operator/)指定删除条件。要删除集合中的所有文档，请传递一个空文档 （{}）

- justOne 

  可选的。要将删除限制为仅一个文档，请设置为`true`。忽略使用默认值，`false`并删除所有符合删除条件的文档.

- `writeConcern`

    默认的写关注 ,比如设置 操作的超时时间等

   ```
   db.products.remove(
       { qty: { $gt: 20 } },
       { writeConcern: { w: "majority", wtimeout: 5000 } }
   )
   ```



### 批量写入

#### bulkWrite()

说明：批量操作可以有序或无序

在分片集合上执行操作的有序列表通常比执行的无序列表要慢，因为对于有序列表。每个操作必须等待上一个操作完成

- ordered ： true代表有序操作，false代表无序

语法：

```
db.collection.bulkWrite(
	[document...],
	{ordered: true | false}
)
```



示例：

```json
{ "_id" : 1, "char" : "Brisbane", "class" : "monk", "lvl" : 4 },
{ "_id" : 2, "char" : "Eldon", "class" : "alchemist", "lvl" : 3 },
{ "_id" : 3, "char" : "Meldane", "class" : "ranger", "lvl" : 3 }
```

```js
try {
   db.characters.bulkWrite(
      [
         { insertOne :
            {
               "document" :
               {
                  "_id" : 4, "char" : "Dithras", "class" : "barbarian", "lvl" : 4
               }
            }
         },
         { insertOne :
            {
               "document" :
               {
                  "_id" : 5, "char" : "Taeln", "class" : "fighter", "lvl" : 3
               }
            }
         },
         { updateOne :
            {
               "filter" : { "char" : "Eldon" },
               "update" : { $set : { "status" : "Critical Injury" } }
            }
         },
         { deleteOne :
            { "filter" : { "char" : "Brisbane"} }
         },
         { replaceOne :
            {
               "filter" : { "char" : "Meldane" },
               "replacement" : { "char" : "Tanys", "class" : "oracle", "lvl" : 4 }
            }
         }
      ]
   );
}
catch (e) {
   print(e);
}
```

结果：

```json
{
   "acknowledged" : true,
   "deletedCount" : 1,
   "insertedCount" : 2,
   "matchedCount" : 2,
   "upsertedCount" : 0,
   "insertedIds" : {
      "0" : 4,
      "1" : 5
   },
   "upsertedIds" : {

   }
}
```



#### 批量对象操作

| 名称                                                         | 描述                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [`db.collection.initializeOrderedBulkOp()`](https://mongodb.net.cn/manual/reference/method/db.collection.initializeOrderedBulkOp/#db.collection.initializeOrderedBulkOp) | 初始化[`Bulk()`](https://mongodb.net.cn/manual/reference/method/Bulk/#Bulk)操作构建器以获取操作的有序列表。 |
| [`db.collection.initializeUnorderedBulkOp()`](https://mongodb.net.cn/manual/reference/method/db.collection.initializeUnorderedBulkOp/#db.collection.initializeUnorderedBulkOp) | 初始化[`Bulk()`](https://mongodb.net.cn/manual/reference/method/Bulk/#Bulk)操作构建器以获取无序的操作列表。 |
| [`Bulk()`](https://mongodb.net.cn/manual/reference/method/Bulk/#Bulk) | 批量操作生成器。                                             |
| [`Bulk.execute()`](https://mongodb.net.cn/manual/reference/method/Bulk.execute/#Bulk.execute) | 批量执行操作列表。                                           |
| [`Bulk.find()`](https://mongodb.net.cn/manual/reference/method/Bulk.find/#Bulk.find) | 指定更新或删除操作的查询条件。                               |
| [`Bulk.find.arrayFilters()`](https://mongodb.net.cn/manual/reference/method/Bulk.find.arrayFilters/#Bulk.find.arrayFilters) | 指定用于确定要为`update`或`updateOne`操作更新数组中哪些元素的过滤器。 |
| [`Bulk.find.collation()`](https://mongodb.net.cn/manual/reference/method/Bulk.find.collation/#Bulk.find.collation) | 指定查询条件的[排序规则](https://mongodb.net.cn/manual/reference/bson-type-comparison-order/#collation)。 |
| [`Bulk.find.hint()`](https://mongodb.net.cn/manual/reference/method/Bulk.find.hint/#Bulk.find.hint) | 指定用于更新/替换操作的索引。                                |
| [`Bulk.find.remove()`](https://mongodb.net.cn/manual/reference/method/Bulk.find.remove/#Bulk.find.remove) | 将多文档删除操作添加到操作列表中。                           |
| [`Bulk.find.removeOne()`](https://mongodb.net.cn/manual/reference/method/Bulk.find.removeOne/#Bulk.find.removeOne) | 将单个文档删除操作添加到操作列表中。                         |
| [`Bulk.find.replaceOne()`](https://mongodb.net.cn/manual/reference/method/Bulk.find.replaceOne/#Bulk.find.replaceOne) | 将单个文档替换操作添加到操作列表中。                         |
| [`Bulk.find.updateOne()`](https://mongodb.net.cn/manual/reference/method/Bulk.find.updateOne/#Bulk.find.updateOne) | 将单个文档更新操作添加到操作列表中。                         |
| [`Bulk.find.update()`](https://mongodb.net.cn/manual/reference/method/Bulk.find.update/#Bulk.find.update) | 将`multi`更新操作添加到操作列表中。                          |
| [`Bulk.find.upsert()`](https://mongodb.net.cn/manual/reference/method/Bulk.find.upsert/#Bulk.find.upsert) | 指定更新操作。`upsert: true`                                 |
| [`Bulk.getOperations()`](https://mongodb.net.cn/manual/reference/method/Bulk.getOperations/#Bulk.getOperations) | 返回在[`Bulk()`](https://mongodb.net.cn/manual/reference/method/Bulk/#Bulk)操作对象中执行的写操作数组。 |
| [`Bulk.insert()`](https://mongodb.net.cn/manual/reference/method/Bulk.insert/#Bulk.insert) | 将插入操作添加到操作列表中。                                 |
| [`Bulk.tojson()`](https://mongodb.net.cn/manual/reference/method/Bulk.tojson/#Bulk.tojson) | 返回一个JSON文档，其中包含[`Bulk()`](https://mongodb.net.cn/manual/reference/method/Bulk/#Bulk)操作对象中的操作和批处理的数量。 |
| [`Bulk.toString()`](https://mongodb.net.cn/manual/reference/method/Bulk.toString/#Bulk.toString) | 返回                                                         |







### 分页查询






### 读取隔离







