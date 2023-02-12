## Docker 命令深入

### Docker 网络命令

- docker network 

  - ls : 查看当前所有的网络
  - inspect：查看具体的网络信息
  - connect：将容器连接至某个网络
  - disconnect：将容器从某个网络断开
  - create：创建一个网络

  

### Docker 存储命令

docker volume 

- ls ：查看所有的存储卷信息
- create：创建一个存储卷
- inspect：查看存储卷的具体信息
- prune：删除 unused 存储卷
- rm：删除某个存储卷



### docker 扩展

- builder ：从dockerfile 构建镜像（一般不常用）
  - build ：Build an image from a Dockerfile
  - prune ：remove build cache

context：(目前不知道有什么用处，k8s 中有context的概念，所以也不需要了解)

-  create      Create a context
-   export      Export a context to a tar or kubeconfig file
-   import      Import a context from a tar or zip file
-   inspect     Display detailed information on one or more contexts
-   ls          List contexts
-   rm          Remove one or more contexts
-   update      Update a context
-   use         Set the current docker context

wait：等待容器直到退出

events：查看容器的事件信息

- **-f ：**根据条件过滤事件；
- **--since ：**从指定的时间戳后显示所有事件;
- **--until ：**流水时间显示到指定的时间为止；

### 实例

显示docker 2016年7月1日后的所有事件。

```
runoob@runoob:~/mysql$ docker events  --since="1467302400"
2016-07-08T19:44:54.501277677+08:00 network connect 66f958fd13dc4314ad20034e576d5c5eba72e0849dcc38ad9e8436314a4149d4 (container=b8573233d675705df8c89796a2c2687cd8e36e03646457a15fb51022db440e64, name=bridge, type=bridge)
2016-07-08T19:44:54.723876221+08:00 container start b8573233d675705df8c89796a2c2687cd8e36e03646457a15fb51022db440e64 (image=nginx:latest, name=elegant_albattani)
2016-07-08T19:44:54.726110498+08:00 container resize b8573233d675705df8c89796a2c2687cd8e36e03646457a15fb51022db440e64 (height=39, image=nginx:latest, name=elegant_albattani, width=167)
2016-07-08T19:46:22.137250899+08:00 container die b8573233d675705df8c89796a2c2687cd8e36e03646457a15fb51022db440e64 (exitCode=0, image=nginx:latest, name=elegant_albattani)
...
```

显示docker 镜像为mysql:5.6 2016年7月1日后的相关事件。

```
runoob@runoob:~/mysql$ docker events -f "image"="mysql:5.6" --since="1467302400" 
2016-07-11T00:38:53.975174837+08:00 container start 96f7f14e99ab9d2f60943a50be23035eda1623782cc5f930411bbea407a2bb10 (image=mysql:5.6, name=mymysql)
2016-07-11T00:51:17.022572452+08:00 container kill 96f7f14e99ab9d2f60943a50be23035eda1623782cc5f930411bbea407a2bb10 (image=mysql:5.6, name=mymysql, signal=9)
2016-07-11T00:51:17.132532080+08:00 container die 96f7f14e99ab9d2f60943a50be23035eda1623782cc5f930411bbea407a2bb10 (exitCode=137, image=mysql:5.6, name=mymysql)
2016-07-11T00:51:17.514661357+08:00 container destroy 96f7f14e99ab9d2f60943a50be23035eda1623782cc5f930411bbea407a2bb10 (image=mysql:5.6, name=mymysql)
2016-07-11T00:57:18.551984549+08:00 container create c8f0a32f12f5ec061d286af0b1285601a3e33a90a08ff1706de619ac823c345c (image=mysql:5.6, name=mymysql)
2016-07-11T00:57:18.557405864+08:00 container attach c8f0a32f12f5ec061d286af0b1285601a3e33a90a08ff1706de619ac823c345c (image=mysql:5.6, name=mymysql)
2016-07-11T00:57:18.844134112+08:00 container start c8f0a32f12f5ec061d286af0b1285601a3e33a90a08ff1706de619ac823c345c (image=mysql:5.6, name=mymysql)
2016-07-11T00:57:19.140141428+08:00 container die c8f0a32f12f5ec061d286af0b1285601a3e33a90a08ff1706de619ac823c345c (exitCode=1, image=mysql:5.6, name=mymysql)
2016-07-11T00:58:05.941019136+08:00 container destroy c8f0a32f12f5ec061d286af0b1285601a3e33a90a08ff1706de619ac823c345c (image=mysql:5.6, name=mymysql)
2016-07-11T00:58:07.965128417+08:00 container create a404c6c174a21c52f199cfce476e041074ab020453c7df2a13a7869b48f2f37e (image=mysql:5.6, name=mymysql)
2016-07-11T00:58:08.188734598+08:00 container start a404c6c174a21c52f199cfce476e041074ab020453c7df2a13a7869b48f2f37e (image=mysql:5.6, name=mymysql)
2016-07-11T00:58:20.010876777+08:00 container top a404c6c174a21c52f199cfce476e041074ab020453c7df2a13a7869b48f2f37e (image=mysql:5.6, name=mymysql)
2016-07-11T01:06:01.395365098+08:00 container top a404c6c174a21c52f199cfce476e041074ab020453c7df2a13a7869b48f2f37e (image=mysql:5.6, name=mymysql)
```

如果指定的时间是到秒级的，需要将时间转成时间戳。如果时间为日期的话，可以直接使用，如--since="2016-07-01"





### Docker 系统相关

> docker 引擎相关扩展命令

- info	与 system info 一致

- version	docker版本信息

- system	

  - df 查看当前docker 使用的磁盘存储情况

    ```sh
    [root@apollo ~]# docker system df
    TYPE            TOTAL     ACTIVE    SIZE      RECLAIMABLE
    Images          9         6         1.486GB   891.9MB (60%)
    Containers      6         4         184.2MB   0B (0%)
    Local Volumes   1         1         224.6MB   0B (0%)
    Build Cache     0         0         0B        0B
    ```

  - events   查看docker 当前的事件信息

  - info  查看当前docker engine的信息

  - prune 删除未使用的数据，包括镜像、容器、网络等等（一般不执行）

  docker系统管理

- stats（类似于 常用的top命令）	

  docker容器资源使用统计
  
- plugin	docker插件管理




