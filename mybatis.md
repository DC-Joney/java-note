## mybatis如何避免sql注入

数据库本身提供了预编译功能在执行sql的时候默认是不注入参数的，也就是说sql中是带了？ 这种占位符的，目的就是在数据库将sql预编译完成之后再将实际的数据填入进去

```
${} 和 #{}的区别

${} 是可以直接解析成字符串，然后将该sql语句直接在数据库运行

#{} 则是先将sql送至数据库进行预编译，预编译完成之后再进行数据的写入
```







## mybatis一级缓存和二级缓存

### mybatis一级缓存

```
每当我们使用MyBatis开启一次和数据库的会话，MyBatis会创建出一个SqlSession对象表示一次数据库会话。

      在对数据库的一次会话中，我们有可能会反复地执行完全相同的查询语句，如果不采取一些措施的话，每一次查询都会查询一次数据库,而我们在极短的时间内做了完全相同的查询，那么它们的结果极有可能完全相同，由于查询一次数据库的代价很大，这有可能造成很大的资源浪费。

      为了解决这一问题，减少资源的浪费，MyBatis会在表示会话的SqlSession对象中建立一个简单的缓存，将每次查询到的结果结果缓存起来，当下次查询的时候，如果判断先前有个完全一样的查询，会直接从缓存中直接将结果取出，返回给用户，不需要再进行一次数据库查询了。

     如下图所示，MyBatis会在一次会话的表示----一个SqlSession对象中创建一个本地缓存(local cache)，对于每一次查询，都会尝试根据查询的条件去本地缓存中查找是否在缓存中，如果在缓存中，就直接从缓存中取出，然后返回给用户；否则，从数据库读取数据，将查询结果存入缓存并返回给用户
```

![img](https://img-blog.csdn.net/20141121213425390) 



```
 由于MyBatis使用SqlSession对象表示一次数据库的会话，那么，对于会话级别的一级缓存也应该是在SqlSession中控制的。

      实际上, MyBatis只是一个MyBatis对外的接口，SqlSession将它的工作交给了Executor执行器这个角色来完成，负责完成对数据库的各种操作。当创建了一个SqlSession对象时，MyBatis会为这个SqlSession对象创建一个新的Executor执行器，而缓存信息就被维护在这个Executor执行器中，MyBatis将缓存和对缓存相关的操作封装成了Cache接口中。SqlSession、Executor、Cache之间的关系如下列类图所示：

```

   



![img](https://img-blog.csdn.net/20141120100824184) 



```
如上述的类图所示，Executor接口的实现类BaseExecutor中拥有一个Cache接口的实现类PerpetualCache，则对于BaseExecutor对象而言，它将使用PerpetualCache对象维护缓存。

综上，SqlSession对象、Executor对象、Cache对象之间的关系如下图所示：
```





![img](https://img-blog.csdn.net/20141119164906640) 



```
由于Session级别的一级缓存实际上就是使用PerpetualCache维护的，那么PerpetualCache是怎样实现的呢？

PerpetualCache实现原理其实很简单，其内部就是通过一个简单的HashMap<k,v> 来实现的，没有其他的任何限制。如下是PerpetualCache的实现代码：



一级缓存的生命周期有多长？

a. MyBatis在开启一个数据库会话时，会 创建一个新的SqlSession对象，SqlSession对象中会有一个新的Executor对象，Executor对象中持有一个新的PerpetualCache对象；当会话结束时，SqlSession对象及其内部的Executor对象还有PerpetualCache对象也一并释放掉。

b. 如果SqlSession调用了close()方法，会释放掉一级缓存PerpetualCache对象，一级缓存将不可用；

c. 如果SqlSession调用了clearCache()，会清空PerpetualCache对象中的数据，但是该对象仍可使用；

d.SqlSession中执行了任何一个update操作(update()、delete()、insert()) ，都会清空PerpetualCache对象的数据，但是该对象可以继续使用；
```





![img](https://img-blog.csdn.net/20141120104257906) 



SqlSession 一级缓存的工作流程：

```
1.对于某个查询，根据statementId,params,rowBounds来构建一个key值，根据这个key值去缓存Cache中取出对应的key值存储的缓存结果；

1. 判断从Cache中根据特定的key值取的数据数据是否为空，即是否命中；
2. 如果命中，则直接将缓存结果返回；
3. 如果没命中：

        4.1  去数据库中查询数据，得到查询结果；

        4.2  将key和查询到的结果分别作为key,value对存储到Cache中；

        4.3. 将查询结果返回；

1. 结束。
   [关于上述工作过程中 key值的构建，我们将在第下一节中重点探讨，这也是MyBatis缓存机制中非常重要的一个概念。]
```

![img](https://img-blog.csdn.net/20141120133247125) 



### mybatis 二级缓存

![img](https://img-blog.csdn.net/20141123125616381) 

```
 如上图所示，当开一个会话时，一个SqlSession对象会使用一个Executor对象来完成会话操作，MyBatis的二级缓存机制的关键就是对这个Executor对象做文章。如果用户配置了"cacheEnabled=true"，那么MyBatis在为SqlSession对象创建Executor对象时，会对Executor对象加上一个装饰者：CachingExecutor，这时SqlSession使用CachingExecutor对象来完成操作请求。CachingExecutor对于查询请求，会先判断该查询请求在Application级别的二级缓存中是否有缓存结果，如果有查询结果，则直接返回缓存结果；如果缓存中没有，再交给真正的Executor对象来完成查询操作，之后CachingExecutor会将真正Executor返回的查询结果放置到缓存中，然后在返回给用户。
```

![img](https://img-blog.csdn.net/20141123125640998) 

```
 CachingExecutor是Executor的装饰者，以增强Executor的功能，使其具有缓存查询的功能，这里用到了设计模式中的装饰者模式，
```



​       **CachingExecutor**和**Executor**的接口的关系如下类图所示：

​    ![img](https://img-blog.csdn.net/20141123130312715)

 MyBatis二级缓存的划分

```
MyBatis并不是简单地对整个Application就只有一个Cache缓存对象，它将缓存划分的更细，即是Mapper级别的，即每一个Mapper都可以拥有一个Cache对象，具体如下：

a.为每一个Mapper分配一个Cache缓存对象（使用<cache>节点配置）；

b.多个Mapper共用一个Cache缓存对象（使用<cache-ref>节点配置）；

b.多个Mapper共用一个Cache缓存对象（使用<cache-ref>节点配置）

如果你想让多个Mapper公用一个Cache的话，你可以使用<cache-ref namespace="">节点，来指定你的这个Mapper使用到了哪一个Mapper的Cache缓存。

```



![img](https://img-blog.csdn.net/20141123125741812) 



```
1. 使用二级缓存，必须要具备的条件
        MyBatis对二级缓存的支持粒度很细，它会指定某一条查询语句是否使用二级缓存。

     虽然在Mapper中配置了<cache>,并且为此Mapper分配了Cache对象，这并不表示我们使用Mapper中定义的查询语句查到的结果都会放置到Cache对象之中，我们必须指定Mapper中的某条选择语句是否支持缓存，即如下所示，在<select> 节点中配置useCache="true"，Mapper才会对此Select的查询支持缓存特性，否则，不会对此Select查询，不会经过Cache缓存。如下所示，Select语句配置了useCache="true"，则表明这条Select语句的查询会使用二级缓存。

 <select id="selectByMinSalary" resultMap="BaseResultMap" parameterType="java.util.Map" useCache="true">

总之，要想使某条Select查询支持二级缓存，你需要保证：

1. MyBatis支持二级缓存的总开关：全局配置变量参数   cacheEnabled=true
2. 该select语句所在的Mapper，配置了<cache> 或<cached-ref>节点，并且有效
3. 该select语句的参数 useCache=true



1. 一级缓存和二级缓存的使用顺序
        请注意，如果你的MyBatis使用了二级缓存，并且你的Mapper和select语句也配置使用了二级缓存，那么在执行select查询的时候，MyBatis会先从二级缓存中取输入，其次才是一级缓存，即MyBatis查询数据的顺序是：

               二级缓存    ———> 一级缓存——> 数据库

```



## mybatis 批处理

```
Excutor
	SimpleExecutor
	BatchExecutor
BatchExecutor则是为批处理操作，依赖于jdbc的

PreparedStatement ps = (PreparedStatement) statement;
ps.addBatch();

也可以使用forEeach标签来进行遍历获得
```









## mybatis的核心

mybatis的核心类组成

```
SqlSession            作为MyBatis工作的主要顶层API，表示和数据库交互的会话，完成必要数据库增删改查功能

Executor              MyBatis执行器，是MyBatis 调度的核心，负责SQL语句的生成和查询缓存的维护

StatementHandler   封装了JDBC Statement操作，负责对JDBC statement 的操作，如设置参数、将Statement结
果集转换成List集合。

ParameterHandler   负责对用户传递的参数转换成JDBC Statement 所需要的参数，

ResultSetHandler    负责将JDBC返回的ResultSet结果集对象转换成List类型的集合；

TypeHandler          负责java数据类型和jdbc数据类型之间的映射和转换

MappedStatement   MappedStatement维护了一条<select|update|delete|insert>节点的封装， 

SqlSource            负责根据用户传递的parameterObject，动态地生成SQL语句，将信息封装到BoundSql对象中，并返回

BoundSql             表示动态生成的SQL语句以及相应的参数信息

Configuration        MyBatis所有的配置信息都维持在Configuration对象之中。

```

![img](https://img-blog.csdn.net/20141028140852531?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbHVhbmxvdWlz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast) 



## mybatis的原理：

mybatis默认在创建的时候是先构建Confiuration对象，然后在初始化的时候根据扫描到的mapper接口生成一个代理类，而将SqlSession包装成了SqlSessionTemplate 内部又创建了一个代理类来进行管理，

为每个Mapper接口生成代理类的默认实现类如下：

```
生成代理类的名称  MapperProxy

Configuration 

public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
    return mapperRegistry.getMapper(type, sqlSession);
 }
 
 
 MapperRegistry
 
 public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
    final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
    if (mapperProxyFactory == null) {
      throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
    }
    try {
      return mapperProxyFactory.newInstance(sqlSession);
    } catch (Exception e) {
      throw new BindingException("Error getting mapper instance. Cause: " + e, e);
    }
  }
```



![1572835462335](assets\1572835462335.png)





## mybatis动态sql

![img](https://images2017.cnblogs.com/blog/1240732/201711/1240732-20171101153219685-1084206827.png) 

```
1.<if> 元素
在MyBatis中，<if>元素是最常用的判断语句，它类似于Java中的if语句，主要用于实现某些简单的条件选择。其基本使用示例如下：

    <select id="findCustomerByNameAndJobs" parameterType="com.ma.po.Customer" resultType="com.ma.po.Customer">
        select * from t_customer where 1=1
        <if test="username != null and username != ''">
            and username like '%${username}%'
        </if>
        <if test="jobs != null and jobs !=''">
            and jobs = #{jobs}
        </if>
    </select>


2.<choose>、<when>、<otherwise>元素
如果是java语言，这种情况就相当于switch...case...default语句。

      select * from t_customer where 1=1
      <choose>
           <when test="username !=null and username !=''">
                       and username like concat('%',#{username}, '%')
           </when>
           <when test="jobs !=null and jobs !=''">
                       and jobs= #{jobs}
           </when>
           <otherwise>
                   and phone is not null
           </otherwise>
      </choose>
      
3. <where>、<trim>元素
在前面案例中，映射文件中编写的SQL后面都加入了“where 1=1”的条件，那么到底为什么要这么写呢？如果将where后“1=1”的条件去掉，那么MyBatis所拼接出来的SQL将会如下所示

select * from t_customer where and username like '%'? '%'
可以看出上面SQL语句明显存在SQL语法错误，而加入了条件“1=1”后，既保证了where后面的条件成立，又避免了where后面第一个词是and或者or之类的关键词。
不过“where 1=1”这种写法对于初学者来将不容易理解，并且也不够雅观。
针对上述情况中“where 1=1”，在MyBatis的SQL中就可以使用或元素进行动态处理。

      select * from t_customer
      <where>
           <if test="username !=null and username !=''">
                 and username like concat('%',#{username}, '%')
           </if>
           <if test="jobs !=null and jobs !=''">
                 and jobs= #{jobs}
           </if>
      </where>
上述代码中，使用<where>元素对“where 1=1”条件进行了替换，<where>元素会自动判断组合条件下拼装的SQL语句，只有<where>内的条件成立时，才会在拼接SQL中加入where关键字，否则将不会添加；
即使where之后的内容有多余的“AND”或“OR”,<where>元素也会自动将它们去除。

    select * from t_customer
         <trim prefix="where" prefixOverrides="and">
                <if test="username !=null and username !=''">
                      and username like concat('%',#{username}, '%')
                </if>
                <if test="jobs !=null and jobs !=''">
                      and jobs= #{jobs}
                </if>
         </trim>
<trim>的作用是去除特殊的字符串，它的prefix属性代表语句的前缀，prefixOverrides属性代表需要去除的哪些特殊字符串，功能和<where>基本是等效的。


4.<set>元素
在Hibernate中，想要更新某个对象，就需要发送所有的字段给持久化对象，这种想更新的每一条数据都要将其所有的属性都更新一遍的方法，其执行效率是非常差的。为此，在MyBatis中可以使用动态SQL中的<set>元素进行处理：

<update id="updateCustomer"  parameterType="com.itheima.po.Customer">
        update t_customer 
        <set>
            <if test="username !=null and username !=''">
                  username=#{username},
            </if>
            <if test="jobs !=null and jobs !=''">
                  jobs=#{jobs},
            </if>
        </set>
        where id=#{id}
</update>
上述配置中，使用了<set>和<if>元素结合来组装update语句。其中<set>元素会动态前置SET关键字，同时也会消除SQL语句中最后一个多余的逗号；
<if>元素用于判断相应的字段是否为空，如果不为空，就将此字段进行动态SQL组装，并更新此字段，否则不执行更新。


5.<foreach>元素
<foreach>元素是一个循环语句，它的作用是遍历集合，它能够很好地支持数组和List、Set接口的集合，对此提供遍历功能。它往往用于SQL中的in关键字。其基本使用示例如下所示：

    <select id="findCustomerByIds" parameterType="List"
                         resultType="com.itheima.po.Customer">
           select * from t_customer where id in
            -- 判断为空可以用 list.size>0
            <foreach item="id" index="index" collection="list" 
                            open="(" separator="," close=")">
                   #{id}
            </foreach>
     </select>
关于上述示例中元素中使用的几种属性的描述具体如下：

item：配置的是循环中当前的元素。

index：配置的是当前元素在集合的位置下标。

collection：配置的list是传递过来的参数类型（首字母小写），它可以是一个array、list（或collection）、Map集合的键、POJO包装类中数组或集合类型的属性名等。

open和close：配置的是以什么符号将这些集合元素包装起来。

separator：配置的是各个元素的间隔符。

注意：
在使用<foreach>时最关键也是最容易出错的就是collection属性，该属性是必须指定的，而且在不同情况下，该属性的值是不一样的。主要有以下3种情况：
1.如果传入的是单参数且参数类型是一个数组或者List的时候，collection属性值分别为array和list（或collection）。

2.如果传入的参数是多个的时候，就需要把它们封装成一个Map了，当然单参数也可以封装成Map集合，这时候collection属性值就为Map的键。

3.如果传入的参数是POJO包装类的时候，collection属性值就为该包装类中需要进行遍历的数组或集合的属性名。

6.<bind>元素
首先，看一下这条sql：

select * from t_customer where username like '%${value}%'
1.如果使用“${}”进行字符串拼接，则无法防止SQL注入问题；

2.如果改用concat函数进行拼接，则只针对MySQL数据库有效；

3.如果改用“||”进行字符串拼接，则只针对Oracle数据库有效。

这样，映射文件中的SQL就要根据不同的情况提供不同形式的实现，这显然是比较麻烦的，且不利于项目的移植。为了减少这种麻烦，就可以使用MyBatis的元素来解决这一问题。

MyBatis的元素可以通过OGNL表达式来创建一个上下文变量，其使用方式如下：

     <select id="findCustomerByName" parameterType="com.itheima.po.Customer"
                 resultType="com.itheima.po.Customer">
            <!--_parameter.getUsername()表示传递进来的参数（也可以直接写成对应的参数变量名，如username）-->
          <bind name="pattern_username" value="'%'+_parameter.getUsername()+'%'" />
           select * from t_customer 
           where 
            <!--需要的地方直接引用<bind>元素的name属性值即可-->
           username like #{pattern_username}
     </select>
```



