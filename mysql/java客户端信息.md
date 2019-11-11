```
异常解决：
Caused by: com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

异常描述
com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

The last packet successfully received from the server was 59,977 milliseconds ago.  The last packet sent successfully to the server was 1 milliseconds ago.
        at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
        at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
        at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
        at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
        at com.mysql.jdbc.Util.handleNewInstance(Util.java:404)
        at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:988)
        at com.mysql.jdbc.MysqlIO.reuseAndReadPacket(MysqlIO.java:3552)
        at com.mysql.jdbc.MysqlIO.reuseAndReadPacket(MysqlIO.java:3452)
        at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:3893)
        at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:2526)
        at com.mysql.jdbc.MysqlIO.sqlQueryDirect(MysqlIO.java:2673)
        at com.mysql.jdbc.ConnectionImpl.execSQL(ConnectionImpl.java:2549)
        at com.mysql.jdbc.PreparedStatement.executeInternal(PreparedStatement.java:1861)
        at com.mysql.jdbc.PreparedStatement.executeQuery(PreparedStatement.java:1962)
        at com.alibaba.druid.pool.DruidPooledPreparedStatement.executeQuery(DruidPooledPreparedStatement.java:227)
        at org.springframework.jdbc.core.JdbcTemplate$1.doInPreparedStatement(JdbcTemplate.java:692)
        at org.springframework.jdbc.core.JdbcTemplate.execute(JdbcTemplate.java:633)
        ... 20 common frames omitted


### 解决方法
https://www.cnblogs.com/chrischennx/p/7279215.html 
https://blog.csdn.net/henryzhang2009/article/details/44175725 
```



