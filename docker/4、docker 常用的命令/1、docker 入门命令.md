# Docker 入门命令

## 镜像相关命令

### 查看镜像

- docker images、docker image ls 查看本地所有docker 镜像
- docker image history 查看镜像的历史分层
  - **-H :**以可读的格式打印镜像大小和日期，默认为true；
  - **--no-trunc :**显示完整的提交记录；
  - **-q :**仅列出提交记录ID。



### 设置镜像名称、标签

- docker tag imageName:tag newImageName:tag

   为镜像从新设置名称以及标签，一般在推送到远端仓库的时间会用到



### 导出、导入镜像

- docker export  -o containerName.tar container_name 将某个container 打包为tar包
- docker import (PATH | URL) imageName.tar newImageName:newTag 引入某个镜像tar包，并且可以为镜像指定新名称
- docker load -i imageName.tar   加载某个tar包为镜像
- docker save imageName -o imageName.tar 将某个镜像保存为tar 包

> save、load 与 import、export的区别
>
> docker save保存的是镜像（image），docker export保存的是容器（container）；
> docker load用来载入镜像包,必须是一个分层文件系统，必须是是save的包；
> docker import用来载入容器包，但两者都会恢复为镜像；
> docker load不能对载入的镜像重命名，而docker import可以为镜像指定新名称。
> docker export的包会比save的包要小，原因是save的是一个分层的文件系统，export导出的只是一个linux系统的文件目录



### 构建镜像

- docker build  PATH | URL | -t imageName   从Dockerfile 构建镜像
  - -t 指定镜像的名称以及标签
  - **--build-arg=[] :**设置镜像创建时的变量；（常用）
  - **--cpu-shares :**设置 cpu 使用权重；
  - **--cpu-period :**限制 CPU CFS周期；
  - **--cpu-quota :**限制 CPU CFS配额；
  - **--cpuset-cpus :**指定使用的CPU id；
  - **--cpuset-mems :**指定使用的内存 id；
  - **--disable-content-trust :**忽略校验，默认开启；
  - **-f :**指定要使用的Dockerfile路径；（常用）
  - **--force-rm :**设置镜像过程中删除中间容器；
  - **--isolation :**使用容器隔离技术；
  - **--label=[] :**设置镜像使用的元数据；
  - **-m :**设置内存最大值；
  - **--memory-swap :**设置Swap的最大值为内存+swap，"-1"表示不限swap；
  - **--no-cache :**创建镜像的过程不使用缓存；
  - **--pull :**尝试去更新镜像的新版本；
  - **--quiet, -q :**安静模式，成功后只输出镜像 ID；
  - **--rm :**设置镜像成功后删除中间容器；
  - **--shm-size :**设置/dev/shm的大小，默认值是64M；
  - **--ulimit :**Ulimit配置。
  - **--squash :**将 Dockerfile 中所有的操作压缩为一层。
  - **--tag, -t:** 镜像的名字及标签，通常 name:tag 或者 name 格式；可以在一次构建中为一个镜像设置多个标签。（常用）
  - **--network:** 默认 default。在构建期间设置RUN指令的网络模式

上面这些限制cpu 设置网络、限制内存等等参数 都是在创建容器时才会指定。构建镜像的时候我们只会构建一个干净的镜像



### 删除镜像

- 删除某个镜像

  - docker image rm imageName

  - docker rm imageName

  - docker rmi imageName 

  - docker remove imageName

  - Options：

    - -f  强制删除某个镜像
    - --no-prune 不删除未打标签的父镜像

    

- docker image prune 删除没有使用的镜像

  - -a --all 删除所有未使用的镜像
  - -f 强制删除
  - --filter 根据过滤条件删除

  

### 镜像仓库操作

> docker 默认使用的镜像仓库是 docker hub

- docker pull 拉取镜像

- docker push imageName:tag 推送某个镜像到远端仓库

- docker login registryUrl ： 登录镜像仓库

  - -u 用户名称
  - -p 密码

- docker search imageName 从镜像仓库查找某个镜像

  - --filter、-f stars=10:  从 Docker Hub 查找所有镜像名包含 imageName，并且收藏数大于 10 的镜像 

    ```sh
    NAME       DESCRIPTION                 STARS     OFFICIAL   AUTOMATED
    nginx      Official build of Nginx.    14634                [OK]  
    
    NAME: 镜像仓库源的名称
    DESCRIPTION: 镜像的描述
    OFFICIAL: 是否 docker 官方发布
    STARS: 类似 Github 里面的 star，表示点赞、喜欢的意思。
    AUTOMATED: 自动构建。
    ```

  -  **--no-trunc :**显示完整的镜像描述； 

- docker logout 登出 registry



## 容器相关命令

### 容器生命周期管理

当我们使用 docker container pause 的时候，可以直接变为docker pause，这里docker pause 为别名

- docker  container

  - pause  暂停容器中所有的进程。 

  - unpause： 恢复容器中所有的进程。 

  - kill ： 杀死某个容器

    - -S 杀死进程时，传递的停止信号，类似于 linux 中kill  的 SIGNAL

  - start ： 启动容器

  - stop：停止容器

  - **run**：启动一个容器

    - ```csharp
      Usage: docker run [OPTIONS] IMAGE [COMMAND] [ARG...]    
      02.  
      03.  -d, --detach=false         指定容器运行于前台还是后台，默认为false     
      04.  -i, --interactive=false   打开STDIN，用于控制台交互    
      05.  -t, --tty=false            分配tty设备，该可以支持终端登录，默认为false    
      06.  -u, --user=""              指定容器的用户    
      07.  -a, --attach=[]            登录容器（必须是以docker run -d启动的容器）  
      08.  -w, --workdir=""           指定容器的工作目录   
      09.  -c, --cpu-shares=0        设置容器CPU权重，在CPU共享场景使用    
      10.  -e, --env=[]               指定环境变量，容器中可以使用该环境变量    
      11.  -m, --memory=""            指定容器的内存上限    
      12.  -P, --publish-all=false    指定容器暴露的端口    
      13.  -p, --publish=[]           指定容器暴露的端口   
      14.  -h, --hostname=""          指定容器的主机名    
      15.  -v, --volume=[]            给容器挂载存储卷，挂载到容器的某个目录    
      16.  --volumes-from=[]          给容器挂载其他容器上的卷，挂载到容器的某个目录  
      17.  --cap-add=[]               添加权限，权限清单详见：http://linux.die.net/man/7/capabilities    
      18.  --cap-drop=[]              删除权限，权限清单详见：http://linux.die.net/man/7/capabilities    
      19.  --cidfile=""               运行容器后，在指定文件中写入容器PID值，一种典型的监控系统用法    
      20.  --cpuset=""                设置容器可以使用哪些CPU，此参数可以用来容器独占CPU    
      21.  --device=[]                添加主机设备给容器，相当于设备直通    
      22.  --dns=[]                   指定容器的dns服务器    
      23.  --dns-search=[]            指定容器的dns搜索域名，写入到容器的/etc/resolv.conf文件    
      24.  --entrypoint=""            覆盖image的入口点    
      25.  --env-file=[]              指定环境变量文件，文件格式为每行一个环境变量    
      26.  --expose=[]                指定容器暴露的端口，即修改镜像的暴露端口    
      27.  --link=[]                  指定容器间的关联，使用其他容器的IP、env等信息    
      28.  --lxc-conf=[]              指定容器的配置文件，只有在指定--exec-driver=lxc时使用    
      29.  --name=""                  指定容器名字，后续可以通过名字进行容器管理，links特性需要使用名字    
      30.  --net="bridge"             容器网络设置:  
      31.                                bridge 使用docker daemon指定的网桥       
      32.                                host    //容器使用主机的网络    
      33.                                container:NAME_or_ID  >//使用其他容器的网路，共享IP和PORT等网络资源    
      34.                                none 容器使用自己的网络（类似--net=bridge），但是不进行配置   
      35.  --privileged=false         指定容器是否为特权容器，特权容器拥有所有的capabilities    
      36.  --restart="no"             指定容器停止后的重启策略:  
      37.                                no：容器退出时不重启    
      38.                                on-failure：容器故障退出（返回值非零）时重启   
      39.                                always：容器退出时总是重启    
      40.  --rm=false                 指定容器停止后自动删除容器(不支持以docker run -d启动的容器)    
      41.  --sig-proxy=true           设置由代理接受并处理信号，但是SIGCHLD、SIGSTOP和SIGKILL不能被代理    
      ```

  - restart：重启当前容器

  - exec ： 对一个容器执行shell命令 

    - **-d :**分离模式: 在后台运行（不常用）
    - **-i :**即使没有附加也保持STDIN 打开
    - **-t :**分配一个伪终端

  - create：创建一个容器（一般直接使用run）

  - rm： 删除一个或多个容器。 

    - **-l :**移除容器间的网络连接，而非容器本身。
    - **-v :**删除与容器关联的卷。

    



### 容器操作

- logs  获取docker日志 

  - **-f :** 跟踪日志输出
  - **--since :**显示某个开始时间的所有日志
  - **-t :** 显示时间戳
  - **--tail :**仅列出最新N条容器日志

- rename oldName newName： 重命名容器 

- port： 列出一个容器的端口映射情况 

- inspect ： 获取容器或镜像的元数据 

  - **-f :**指定返回值的模板文件。
  - **-s :**显示总的文件大小。
  - **--type :**为指定类型返回JSON。

- ps： 列出容器 

  - **-a :**显示所有的容器，包括未运行的。
  - **-f :**根据条件过滤显示的内容。
  - **--format :**指定返回值的模板文件。
  - **-l :**显示最近创建的容器。
  - **-n :**列出最近创建的n个容器。
  - **--no-trunc :**不截断输出。
  - **-q :**静默模式，只显示容器编号。
  - **-s :**显示总的文件大小。

- export： 将一个容器的文件系统打成 tar 包 

- docker update containerName

  限制容器使用的内存、cpu 等

  ```sh
  Options:
        --blkio-weight uint16        Block IO (relative weight), between 10 and 1000, or 0 to disable (default 0)
        --cpu-period int             Limit CPU CFS (Completely Fair Scheduler) period
        --cpu-quota int              Limit CPU CFS (Completely Fair Scheduler) quota
        --cpu-rt-period int          Limit the CPU real-time period in microseconds
        --cpu-rt-runtime int         Limit the CPU real-time runtime in microseconds
    -c, --cpu-shares int             CPU shares (relative weight)
        --cpus decimal               Number of CPUs
        --cpuset-cpus string         CPUs in which to allow execution (0-3, 0,1)
        --cpuset-mems string         MEMs in which to allow execution (0-3, 0,1)
        --kernel-memory bytes        Kernel memory limit
    -m, --memory bytes               Memory limit
        --memory-reservation bytes   Memory soft limit
        --memory-swap bytes          Swap limit equal to memory plus swap: '-1' to enable unlimited swap
        --pids-limit int             Tune container pids limit (set -1 for unlimited)
        --restart string             Restart policy to apply when a container exits
  ```

- wait 



### 容器文件管理

- cp： 向一个正在运行的容器复制文件，或将容器中的文件复制出来 

  - -a：复制文件时，复制所有的属性
  -  **-L :**保持源目标中的链接 

- diff： 检查一个容器文件系统更改情况 

  ```sh
  [root@apollo ~]# docker diff registry-server 
  C /tmp
  A /tmp/hsperfdata_root
  A /tmp/hsperfdata_root/7
  A /tmp/tomcat-docbase.8761.5540602083543927581
  A /tmp/tomcat.8761.1235703227562692523
  A /tmp/tomcat.8761.1235703227562692523/work
  A /tmp/tomcat.8761.1235703227562692523/work/Tomcat
  A /tmp/tomcat.8761.1235703227562692523/work/Tomcat/localhost
  A /tmp/tomcat.8761.1235703227562692523/work/Tomcat/localhost/ROOT
  
  ```

- commit： 提交一个容器的文件系统，使之生成一个新的镜像  (一般不使用)

  - **-a :**提交的镜像作者；
  - **-c :**使用Dockerfile指令来创建镜像；
  - **-m :**提交时的说明文字；
  - **-p :**在commit时，将容器暂停。





