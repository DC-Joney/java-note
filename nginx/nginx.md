###NGINX面试



####Nginx是如何处理一个请求的呢？

首先，nginx在启动时，会解析配置文件，得到需要监听的端口与ip地址，然后在nginx的master进程里面
先初始化好这个监控的socket，再进行listen
然后再fork出多个子进程出来, 子进程会竞争accept新的连接。
此时，客户端就可以向nginx发起连接了。当客户端与nginx进行三次握手，与nginx建立好一个连接后，此时，某一个子进程会accept成功，然后创建nginx对连接的封装，即ngx_connection_t结构体接着，根据事件调用相应的事件处理模块，如http模块与客户端进行数据的交换，最后，nginx或客户端来主动关掉连接，到此，一个连接就寿终正寝了



####什么是Nginx

Nginx是一个web服务器和反向代理服务器，用于HTTP、HTTPS、SMTP、POP3和IMAP协议。



####Nginx的负载均衡算法都有哪些

nginx 的 upstream目前支持 4 种方式的分配
 0)、轮询（默认）
 每个请求按时间顺序逐一分配到不同的后端服务器，如果后端服务器down掉，能自动剔除。
 1)、weight
 指定轮询几率，weight和访问比率成正比，用于后端服务器性能不均的情况。
 2)、ip_hash
 每个请求按访问ip的hash结果分配，这样每个访客固定访问一个后端服务器，可以解决session的问题。
 3)、fair（第三方）
 按后端服务器的响应时间来分配请求，响应时间短的优先分配。
 4)、url_hash（第三方）
 根据url的hash结果分配



####常见状态码

499：服务端处理时间过长，客户端主动关闭了连接。



####常用配置

worker_processes  8;     工作进程个数
 worker_connections  65535;  每个工作进程能并发处理（发起）的最大连接数（包含所有连接数）
 error_log         /data/logs/nginx/error.log;  错误日志打印地址
 access_log      /data/logs/nginx/access.log  进入日志打印地址
 log_format  main  '![remote_addr"](https://math.jianshu.com/math?formula=remote_addr%22)request" ''![status](https://math.jianshu.com/math?formula=status)upstream_addr "$request_time"'; 进入日志格式



####nginx和apache的区别

轻量级，同样起web 服务，比apache 占用更少的内存及资源
 抗并发，nginx 处理请求是异步非阻塞的，而apache 则是阻塞型的，在高并发下nginx 能保持低资源低消耗高性能
 高度模块化的设计，编写模块相对简单
 最核心的区别在于apache是同步多进程模型，一个连接对应一个进程；nginx是异步的，多个连接（万级别）可以对应一个进程



####静态http服务器配置

可以将服务器上的静态文件（如HTML、图片）通过HTTP协议展现给客户端。

```
 server {
listen80; # 端口号
location / {
    root /usr/share/nginx/html; # 静态文件路径
}
```

}



####反向代理配置

客户端本来可以直接通过HTTP协议访问某网站应用服务器，网站管理员可以在中间加上一个Nginx，客户端请求Nginx，Nginx请求应用服务器，然后将结果返回给客户端，此时Nginx就是反向代理服务器。

```
server {
  listen80;
  location / {
    proxy_pass http://192.168.20.1:8080; # 应用服务器HTTP地址
}
```

}



####负载均衡配置

```
upstream myapp {

server192.168.20.1:8080; # 应用服务器1

server192.168.20.2:8080; # 应用服务器2
}
  server {
listen80;

location / {

    proxy_pass http://myapp;

}
}
```



####虚拟主机配置

多个域名指向同一台主机

```
server {

listen80default_server;

server_name _;

return444; # 过滤其他域名的请求，返回444状态码
}
    server {

listen80;

server_name www.aaa.com; # www.aaa.com域名

location / {

    proxy_pass http://localhost:8080; # 对应端口号8080

}

}

server {

listen80;

server_name www.bbb.com; # www.bbb.com域名

location / {

    proxy_pass http://localhost:8081; # 对应端口号8081

}
}
```



####nginx 对http的优化？

```
proxy_cache 代理缓存

slice 文件切割

upstream  负载均衡

expires 24h	文件缓存

sendfile	开启0拷贝

gzip 	文件压缩

tcp_nopush tcp实时性和非实时性
```

