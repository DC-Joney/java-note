### nginx默认配置语法

####全局语法

![1572064912906](assets\1572064912906.png)

```
user : 设置 nginx服务的系统使用用户

use epoll 使用的内核模型

include /etc/dir *.conf 包含进来的配置文件

error /dir error 指定日志级别
```



#### http模块语法

![1572064941216](assets\1572064941216.png)

```
http 是最外层
	sendFile on 开启内核态传输
	
	include /etc/nginx/mime.types 指定包含的mime类型
	
	log_format main ..... 指定日志格式以及格式名称
	
	access_log /dir main 指定用户访问日志文件的存储路径，main是指 日志的格式
	
	keepalive_timeout : 客户端跟服务端的超时时间
	
	gzip on; 是否开启gzip压缩，节省空间
	
	include /etc/dir/conf.d/*.conf; 可以包含指定的 conf文件一起加载（例如 demo1-server.conf,demo2-server.conf）
	
	-- server 虚拟主机层
		
		access_log /dir main 指定日志格式以及路径
		
		server_name 需要设置的虚拟主机名称
		
		listen 监听的端口号
		
		error_page 500 502 501 /50x.html 当遇到返回这些状态吗时，返回指定的错误页面路径
		
		location /50x.html {
            root ...; 设置错误页面的根目录，根目录下的错误页面要和路径相同
		}
		
		--location 配置具体的路径
			root 设置需要获取文件的根路径
			index 指定具体的文件
```





#### log_format 语法

![1572065913546](assets\1572065913546.png)





####nginx 变量

![1572066032565](assets\1572066032565.png)

```
arg_PARAMTER
http_HEADER 【request header】 （http_Accept_Enconding） 
send_http_HEADER 【response header】	（http_Content_Enconding）
```



####nginx相关模块以及语法

```
stu_status 查看nginx相关的链接信息

location /path {
    stu_status;
}

##############################
http_random_index_moudle 没啥用

##############################
http_sub_module http内容替换，替换的是html中的内容
语法 sub_filter string replce_string
作用域: http,server,location

sub_filter_last_modifiled on|off; 默认为off，校验服务端的时间是否发生变更
作用域: http,server,location

sub_filter_once off|on 表示要替换的内容是否是全局替换,on代表是 ，off代表只替换第一个

##############################

nginx 请求限制
limit_conn_module 连接频率限制

limit_conn_zone key zone = name:size （http）
zone 内存开辟一块空间 
key: 以什么作为key 例如 ip
name :空间的名字
size：申请空间的大小

limit_conn zone number （http，server，location）
zone ：为申请空间的名字
number 并发限制的个数
----------------------------------------------------------
limit_req_module 请求频率限制
limit_req_zone key zone=name:size rate=rate; （http）

rate:以秒为单位多少个请求

limit_req_zone=name [burst=number][nodelay] （http，server，location）
brust =3：表示客户端请求在超过速率以后遗留的三个是放在下一秒请求执行
nodelay：直接返回403 不允许访问
```

![1572067275834](assets\1572067275834.png)

```
$remote_addr 请求的ip地址 
```



````
基于ip的访问控制 
http_access_module 允许访问和拒绝访问的ip地址
allow address |CDIR | UNIX：| all; [http.server,location,limit_except]
CDIR : 基于网段的模式

deny address | CDIR | UNIX: | all；[http.server,location,limit_except]

基于用户密码登录
auth_basic_module

auth_basic string | off[default]; [http.server,location,limit_except]
string : 会在登录时显示的信息

auth_basic_user_file file; [http.server,location,limit_except]
file : 是用来存储用户登录的密码信息文件

使用的 命令 
htpasswd -c [文件名称] 用户名称 -> 输入密码 生成文件
````





####nginx 文件传输

![1572068095414](assets\1572068095414.png)

```
#############文件传输优化
sendfile on|off,[http,server,location] 
模块with-file-aio

####################tcp传输
tcp_nopush on|off [http,server,location]  将tcp包进行一个整合一起发送
在sendfile开启的情况下，提升效率


tcp_nodelay on|off   [http,server,location] 有就发送，不堆积
在 keepalived的连接下，提升网络传输实时性

##################文件压缩
gzip on|off [http,server,location] 在传输时进行压缩

Broser --->  nginx （压缩）
nginx -> browser （压缩）

gzip_comp_level number 压缩的级别 [http,server,location]

gzip_types 表示需要压缩的mime类型 [http,server,location]

gzip_http_version 1.0|1.1 支持http的协议类型

扩展模块：
http_gzip_static_module 预压缩文件，如过有压缩好的文件，就直接返回压缩好的文件
gzip_static on|off; [http,server,location]
```

![1572068487372](assets\1572068487372.png)



####nginx缓存



```
expires 24h; 设置过期时间
```



![1572068712304](assets\1572068712304.png)

![1572068732002](assets\1572068732002.png)



### nginx跨域访问

![1572068850183](assets\1572068850183.png)

```
csrf 攻击，用户访问A 将信息放入到cookie，然后再去访问B 信息会被泄漏
设置cookie允许的请求域，如果是 cros 设置跨域访问的域名即可
add header name value [always] 
```

![1572068965316](assets\1572068965316.png)





### nginx 代理配置

![1572069104059](assets\1572069104059.png)

```
rtmp 一些流代理
正向代理是为客户端服务的
反向代理是为客户端服务的
```

![1572069143315](assets\1572069143315.png)

![1572069157929](assets\1572069157929.png)

![1572069431696](assets\1572069431696.png)



```
reslover 8.8.8.8 dns地址解析

反向代理配置
proxy_pass url; 实际访问的地址 [location]
http://some_url
http://socket 

location ~ \.php$ {
    proxy_pass http://127.0.0.1;
}

proxy_redirect default; 
proxy_redirect off;
proxy_redirect redirect url_string;
[http,server,location]

proxy_connection_timeout 


```

![1572069665527](assets\1572069665527.png)

![1572069784919](assets\1572069784919.png)

![1572069816494](assets\1572069816494.png)

![1572069863069](assets\1572069863069.png)





###nginx 负载均衡

![1572069905842](assets\1572069905842.png)





![1572069976654](assets\1572069976654.png)





```
upstrem name {} 【http】 定义一组server

upstream name {
    server ip:port;
    server ip:port;
    server ip:port;
}

其他参数：
backup 表示备份节点（当其他服务器不能使用的时候就会使用backup节点）
weight 表示权重
down 表示当前server不参与负载均衡
max_fails 允许请求失败的次数
fail_timeout 经过max_fails 失败后，服务器暂停的时间
max_conns 限制最大的接受和连接数


轮训算法：




```

![1572070281003](assets\1572070281003.png)



### nginx 代理缓存

```
配置语法

proxy_cache
```

![1572070409375](assets\1572070409375.png)

![1572070422827](assets\1572070422827.png)



![1572070437778](assets\1572070437778.png)

![1572070450306](assets\1572070450306.png)

![1572070498305](assets\1572070498305.png)



![1572070601969](assets\1572070601969.png)





#### 文件切割



![1572070693663](assets\1572070693663.png)



![1572070661224](assets\1572070661224.png)



#### nginx动静分离

![1572070772724](assets\1572070772724.png)





### Rewirte重写



```
配置语法
```



![1572070885222](assets\1572070885222.png)

![1572070929283](assets\1572070929283.png)

![1572070939982](assets\1572070939982.png)

![1572070957750](assets\1572070957750.png)



![1572071077042](assets\1572071077042.png)



```
break 是不会在向下一层请求（而是直接到root路径下访问该文件）

last会新建一个新的请求 


pcretest 正则表达式测试工具
```







#### nginx 变量

```
set $demo 1;

默认变量：

```









#### sendFile 优化

默认需要切换多次状态从内核空间切换到用户态空间传输，nginx取消了用户态传输

![1572065299913](assets\1572065299913.png)



![1572065311590](assets\1572065311590.png)