Hibernate能在各种不同环境下工作而设计的, 因此存在着大量的配置参数。多数配置参数都 有比较直观的默认值, 并有随

Hibernate一同分发的配置样例hibernate.properties 来展示各种配置选项。 所需做的仅仅是将这个样例文件复制到类路径

(classpath)下并做一些自定义的修改。



### 属性1、Hibernate JDBC属性

属性名	用途
hibernate.connection.driver_class	jdbc驱动类
hibernate.connection.url	jdbc URL
hibernate.connection.username	数据库用户
hibernate.connection.password	数据库用户密码
hibernate.connection.pool_size	连接池容量上限数目



### 属性2：Hibernate 数据源属性

### hibernate.connection.datasource	数据源JNDI名字
hibernate.jndi.url	JNDI提供者的URL (可选)
hibernate.jndi.class	JNDI InitialContextFactory类 (可选)
hibernate.connection.username	数据库用户 (可选)
hibernate.connection.password	数据库用户密码 (可选)



### 属性3：可选的配置属性

属性名	用途
hibernate.dialect	一个Hibernate Dialect类名允许Hibernate针对特定的关系数据库生成优化的SQL.
取值 full.classname.of.Dialect

hibernate.show_sql	输出所有SQL语句到控制台. 有一个另外的选择是把org.hibernate.SQL这个log category设为debug。
eg. true | false

hibernate.format_sql	在log和console中打印出更漂亮的SQL。
取值 true | false

hibernate.default_schema	在生成的SQL中, 将给定的schema/tablespace附加于非全限定名的表名上.
取值 SCHEMA_NAME

hibernate.default_catalog	在生成的SQL中, 将给定的catalog附加于非全限定名的表名上.
取值 CATALOG_NAME

hibernate.session_factory_name	SessionFactory创建后，将自动使用这个名字绑定到JNDI中.
取值 jndi/composite/name

hibernate.max_fetch_depth	为单向关联(一对一, 多对一)的外连接抓取（outer join fetch）树设置最大深度. 值为0意味着将关闭默认的外连接抓取.
取值 建议在0到3之间取值

hibernate.default_batch_fetch_size	为Hibernate关联的批量抓取设置默认数量.
取值 建议的取值为4, 8, 和16

hibernate.default_entity_mode	为由这个SessionFactory打开的所有Session指定默认的实体表现模式.
取值 dynamic-map, dom4j, pojo

hibernate.order_updates	强制Hibernate按照被更新数据的主键，为SQL更新排序。这么做将减少在高并发系统中事务的死锁。
取值 true | false

hibernate.generate_statistics	如果开启, Hibernate将收集有助于性能调节的统计数据.
取值 true | false

hibernate.use_identifer_rollback	如果开启, 在对象被删除时生成的标识属性将被重设为默认值.
取值 true | false

hibernate.use_sql_comments	如果开启, Hibernate将在SQL中生成有助于调试的注释信息, 默认值为false.
取值 true | false

 

 

### 属性4：Hibernate JDBC和连接(connection)属性：

属性名	用途
hibernate.jdbc.fetch_size	非零值，指定JDBC抓取数量的大小 (调用Statement.setFetchSize()).
hibernate.jdbc.batch_size	非零值，允许Hibernate使用JDBC2的批量更新取值 建议取5到30之间的值

hibernate.jdbc.batch_versioned_data	如果你想让你的JDBC驱动从executeBatch()返回正确的行计数 , 那么将此属性设为true(开启这个选项通常是安全的). 同时，Hibernate将为自动版本化的数据使用批量DML. 默认值为false eg. true | false

hibernate.jdbc.factory_class	选择一个自定义的Batcher. 多数应用程序不需要这个配置属性.eg. classname.of.Batcher

hibernate.jdbc.use_scrollable_resultset	允许Hibernate使用JDBC2的可滚动结果集. 只有在使用用户提供的JDBC连接时，这个选项才是必要的, 否则Hibernate会使用连接的元数据.
取值 true | false

hibernate.jdbc.use_streams_for_binary	在JDBC读写binary (二进制)或serializable (可序列化) 的类型时使用流(stream)(系统级属性).
取值 true | false

hibernate.jdbc.use_get_generated_keys	在数据插入数据库之后，允许使用JDBC3 PreparedStatement.getGeneratedKeys() 来获取数据库生成的key(键)。需要JDBC3+驱动和JRE1.4+, 如果你的数据库驱动在使用Hibernate的标 识生成器时遇到问题，请将此值设为false. 默认情况下将使用连接的元数据来判定驱动的能力.
取值 true|false

hibernate.connection.provider_class	自定义ConnectionProvider的类名, 此类用来向Hibernate提供JDBC连接.
取值 classname.of.ConnectionProvider

hibernate.connection.isolation	设置JDBC事务隔离级别. 查看java.sql.Connection来了解各个值的具体意义, 但请注意多数数据库都不支持所有的隔离级别.
取值 1, 2, 4, 8

hibernate.connection.autocommit	允许被缓存的JDBC连接开启自动提交(autocommit) (不建议).
取值 true | false

hibernate.connection.release_mode	指定Hibernate在何时释放JDBC连接. 默认情况下,直到Session被显式关闭或被断开连接时,才会释放JDBC连接. 对于应用程序服务器的JTA数据源, 你应当使用after_statement, 这样在每次JDBC调用后，都会主动的释放连接. 对于非JTA的连接, 使用after_transaction在每个事务结束时释放连接是合理的. auto将为JTA和CMT事务策略选择after_statement, 为JDBC事务策略选择after_transaction.
取值 on_close | after_transaction | after_statement | auto

hibernate.connection.<propertyName>	将JDBC属性propertyName传递到DriverManager.getConnection()中去.
hibernate.jndi.<propertyName>	将属性propertyName传递到JNDI InitialContextFactory中去.



### 属性5：Hibernate 缓冲属性

属性名	用途
hibernate.cache.provider_class	自定义的CacheProvider的类名.
取值 classname.of.CacheProvider

hibernate.cache.use_minimal_puts	以频繁的读操作为代价, 优化二级缓存来最小化写操作. 在Hibernate3中，这个设置对的集群缓存非常有用, 对集群缓存的实现而言，默认是开启的.
取值 true|false

hibernate.cache.use_query_cache	允许查询缓存, 个别查询仍然需要被设置为可缓存的.
取值 true|false

hibernate.cache.use_second_level_cache	能用来完全禁止使用二级缓存. 对那些在类的映射定义中指定<cache>的类，会默认开启二级缓存.
取值 true|false

hibernate.cache.query_cache_factory	自定义实现QueryCache接口的类名, 默认为内建的StandardQueryCache.
取值 classname.of.QueryCache

hibernate.cache.region_prefix	二级缓存区域名的前缀.
取值 prefix

hibernate.cache.use_structured_entries	强制Hibernate以更人性化的格式将数据存入二级缓存.
取值 true|false

 

 

### 属性6：Hibernate 事务属性

属性名	用途
hibernate.transaction.factory_class	一个TransactionFactory的类名, 用于Hibernate Transaction API (默认为JDBCTransactionFactory).
取值 classname.of.TransactionFactory

jta.UserTransaction	一个JNDI名字，被JTATransactionFactory用来从应用服务器获取JTA UserTransaction.
取值 jndi/composite/name

hibernate.transaction.manager_lookup_class	一个TransactionManagerLookup的类名 - 当使用JVM级缓存，或在JTA环境中使用hilo生成器的时候需要该类.
取值 classname.of.TransactionManagerLookup

hibernate.transaction.flush_before_completion	如果开启, session在事务完成后将被自动清洗(flush)。 现在更好的方法是使用自动session上下文管理。取值 true | false
hibernate.transaction.auto_close_session	如果开启, session在事务完成后将被自动关闭。 现在更好的方法是使用自动session上下文管理。
取值 true | false

 

 

### 属性7：方言

RDBMS	方言
DB2		org.hibernate.dialect.DB2Dialect
DB2 AS/400		org.hibernate.dialect.DB2400Dialect
DB2 OS390		org.hibernate.dialect.DB2390Dialect
PostgreSQL		org.hibernate.dialect.PostgreSQLDialect
MySQL		org.hibernate.dialect.MySQLDialect
MySQL with InnoDB		org.hibernate.dialect.MySQLInnoDBDialect
MySQL with MyISAM		org.hibernate.dialect.MySQLMyISAMDialect
Oracle (any version)		org.hibernate.dialect.OracleDialect
Oracle 9i/10g		org.hibernate.dialect.Oracle9Dialect
Sybase		org.hibernate.dialect.SybaseDialect
Sybase Anywhere	org.hibernate.dialect.SybaseAnywhereDialect
Microsoft SQL Server	org.hibernate.dialect.SQLServerDialect
SAP DB		org.hibernate.dialect.SAPDBDialect
Informix		org.hibernate.dialect.InformixDialect
HypersonicSQL		org.hibernate.dialect.HSQLDialect
Ingres			org.hibernate.dialect.IngresDialect
Progress		org.hibernate.dialect.ProgressDialect
Mckoi SQL		org.hibernate.dialect.MckoiDialect
Interbase		org.hibernate.dialect.InterbaseDialect
Pointbase		org.hibernate.dialect.PointbaseDialect
FrontBase		org.hibernate.dialect.FrontbaseDialect
Firebird		org.hibernate.dialect.FirebirdDialect



### 属性8：其他属性

属性名	用途
hibernate.current_session_context_class	为"当前" Session指定一个(自定义的)策略。
eg. jta | thread | custom.Class

hibernate.query.factory_class	选择HQL解析器的实现.
取值 org.hibernate.hql.ast.ASTQueryTranslatorFactory or org.hibernate.hql.classic.ClassicQueryTranslatorFactory

hibernate.query.substitutions	将Hibernate查询中的符号映射到SQL查询中的符号 (符号可能是函数名或常量名字).
取值 hqlLiteral=SQL_LITERAL, hqlFunction=SQLFUNC

hibernate.hbm2ddl.auto	在SessionFactory创建时，自动检查数据库结构，或者将数据库schema的DDL导出到数据库. 使用 create-drop时,在显式关闭SessionFactory时，将drop掉数据库schema.
取值 validate | update | create | create-drop

hibernate.cglib.use_reflection_optimizer	开启CGLIB来替代运行时反射机制(系统级属性). 反射机制有时在除错时比较有用. 注意即使关闭这个优化, Hibernate还是需要CGLIB. 你不能在hibernate.cfg.xml中设置此属性.
取值 true | false

### hibernate.hbm2ddl.auto 取值介绍

 ddl-auto：create ----每次运行该程序，没有表格会新建表格，表内有数据会清空；
ddl-auto：create-drop ----每次程序结束的时候会清空表
ddl-auto：update ---- 每次运行程序，没有表格会新建表格，表内有数据不会清空，只会更新
ddl-auto： validate ---- 运行程序会校验数据与数据库的字段类型是否相同，不同会报错。 