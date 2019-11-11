直接yum 安装的redis 不是最新版本

```
yum install redis
```

如果要安装最新的redis，需要安装Remi的软件源，官网地址：http://rpms.famillecollet.com/

```
yum install -y http://rpms.famillecollet.com/enterprise/remi-release-7.rpm
```

然后可以使用下面的命令安装最新版本的redis：

```
yum --enablerepo=remi install redis
```

安装完毕后，即可使用下面的命令启动redis服务

```
service redis start
或者
systemctl start redis
```

 redis安装完毕后，我们来查看下redis安装时创建的相关文件，如下：

```
rpm -qa |grep redis
```

![img](https://images2015.cnblogs.com/blog/946553/201702/946553-20170223161750085-1792037982.png)

```
rpm -ql redis
```

![img](https://images2015.cnblogs.com/blog/946553/201702/946553-20170223161957320-1424572697.png)

查看redis版本：

```
redis-cli --version
```

 

设置为开机自动启动：

```
chkconfig redis on
或者
systemctl enable redis.service
```

Redis开启远程登录连接，redis默认只能localhost访问，所以需要开启远程登录。解决方法如下：

在redis的配置文件**/etc/redis.conf**中

将**bind 127.0.0.1** 改成了 **bind 0.0.0.0**

然后要配置防火墙 开放端口6379

连接redis

```
redis-cli
```

![img](https://images2015.cnblogs.com/blog/946553/201702/946553-20170223161019538-1163618486.png)

 **更新：2018-01-22**

**在azure vm centos7.4 安装了最新的redis 4.0.6 bind 0.0.0.0 发现外网连接不上，发现azure vm 打开端口的地方已经变了，需要注意：要将源端口设置为 \* ，目标端口为我们要打开的redis 端口，打开后可以使用telnet 命令测试一下：telnet 101.200.189.125 6379**

**![img](https://images2017.cnblogs.com/blog/946553/201801/946553-20180122193144631-1095097650.png)**

 

**另外： redis 3.2后新增protected-mode配置，默认是yes，即开启。解决方法分为两种：1、设置 protected-mode 为 no  2、配置bind或者设置密码**

**测试的时候我使用了配置bind 方式，没有加密码，正式生产环境可以使用加密码方式**