##  [Etcd](./) Etcd 启动参数

--name=etcd1

#数据存放目录
--data-dir=/var/lib/etcd
#预写日志的目录
--wal-dir=/var/lib/etcd

#WAL文件的最大数量，默认5，0表示不限制
--max-wals=5

#请求白名单
--cors=[]

--auto-compaction-mode=periodic
#Etcd存储多版本数据，随着写入的主键增加，历史版本需要定时清理。默认的历史数据是不会清理的，数据达到2G就不能写#入，必须要清理压缩历史数据才能继续写入

#该参数决定清理压缩间隔，单位小时, 默认每小时压缩一次
--auto-compaction-retention=1h

#每发生多少次变更，就保存一次快照。V2后端快照是高成本操作
--snapshot-count=10000



> 参考：https://www.cnblogs.com/cbkj-xd/p/11934599.html

### 通用参数

#### --name

- 指定节点的名称
- 默认值：default
- 关联环境变量：ETCD_NAME



#### --data-dir

- 数据存放目录
- 默认值：${name}.etcd
- 环境变量：ETCD_DATA_DIR



#### --wal-dir

- 预写日志的存放目录，etcd会将wal文件写入到我们指定的walDir 而不再写入到 dataDir，我们可以i使用专用磁盘来进行存储，这样可以增加IO 吞吐量
- 默认值：""
- 环境变量：ETCD_WAL_DIR



#### --snapshot-count

- 每发生多少次变更，就保存一次快照。V2后端快照是高成本操作
- 默认值："100000"
- 环境变量： ETCD_SNAPSHOP_COUNT 



#### --max-wals

- 保留WAL文件的最大数量，
- 默认5，0表示不限制
- 环境变量：ETCD_MAX_WALS
- Windows 默认是无限制的，建议改为5



#### --max-snapshots

- 保留snapshots 快照文件的最大数量
- 默认值 5，0表示不限制
- 环境变量：ETCD_MAX_SNAPSHOTS
- Windows 默认是无限制的，建议改为5



#### --auto-compaction-mode=periodic
- 由于Etcd 会保留键值空间的精确历史，所以旧的修订版本需要定期删除，避免影响性能，
- 取值有两种，一种是根据历史修订数量，一种是基于时间
  -  revision  ：基于历史修订版本的数量，比如 我们设置为1000，那么当历史修订版本达到1000的时候会就自定压缩
  -  periodic  ：基于时间进行压缩，比如们设置为1h，表示每一个小时进行一次压缩
- 默认值：periodic
- 环境变量：ETCD_AUTO_COMPACTION_MODE



#### --auto-compaction-retention=1h

- Etcd存储多版本数据，随着写入的主键增加，历史版本需要定时清理。默认的历史数据是不会清理的，数据达到2G就不能写#入，必须要清理压缩历史数据才能继续写入
- 该参数决定清理压缩间隔，单位小时
- 默认值：0，  0表示禁用自动压缩。 
- 环境变量：ETCD_AUTO_COMPACTION_RETENTION



####  **--max-txn-ops** 

- 允许开启事务的最大数量
- 默认值：128
- 环境变量：ETCD_MAX_TXN_OPS
- 一般不需要设置（根据应用场景而定）



####  **--heartbeat-interval** 

- 指定发送心跳间隔的时间, Leader通知所有的followers（单位：ms）
- 默认值:"100"
- 环境变量：ETCD_HEARTBEAT_INTERVAL
- 一般不需要设置



####  **--election-timeout** 

- 指定选举超时时间,，一个Foller节点等待大多数节点同意它成为Leader的心跳的最大时间,超时后放弃（单位：ms）
- 默认值："1000"
- 环境变量：ETCD_ELECTION_TIMEOUT
- 一般不需要设置



####  **--cors** 

- 允许 cors 跨域的白名单
- 默认值：""
- 环境变量： ETCD_CORS 
- 一般不需要配置





### 客户端、服务端监听地址

#### -- listen-client-urls

-  监听客户端请求的地址列表，格式：'[http://localhost:2379](http://localhost:2379/)', 多个用逗号分隔。要侦听所有接口的端口，请指定0.0.0.0侦听IP地址 
- 默认值："[http://localhost:2379](http://localhost:2379/)"
- 环境变量:ETCD_LISTEN_CLIENT_URLS
- 示例："[http://10.0.0.1:2379](http://10.0.0.1:2379/)"
- 无效的示例："[http://example.com:2379](http://example.com:2379/)"(绑定的域名是无效的)



#### -- advertise-client-urls

-  如果--listen-client-urls配置了，多个监听客户端请求的地址，这个参数可以给出，建议客户端使用什么地址访问etcd 
- 默认值：[http://localhost:2379](http://localhost:2379/)
- 环境变量：ETCD_ADVERTISE_CLIENT_URLS
- 示例："[http://example.com:2379](http://example.com:2379/), [http://10.0.0.1:2379](http://10.0.0.1:2379/)"



#### --listen-peer-urls

-  服务端节点之间通讯的监听地址，格式：'[http://localhost:2380](http://localhost:2380/)' 
- 默认值："[http://localhost:2380](http://localhost:2380/)"
- 环境变量: ETCD_LISTEN_PEER_URLS
- 示例："[http://10.0.0.1:2380](http://10.0.0.1:2380/)"
- 无效的示例："[http://example.com:2380](http://example.com:2380/)"(绑定的域名是无效的)









### 集群启动参数

>  所有以-initial-cluster开头的选项，在第一次运行（Bootstrap）后都被忽略 



#### --initial-advertise-peer-urls=https://10.128.130.77:2380

- 初始化集群时，向其他成员提供的当前节点通讯地址。
- 默认 http://localhost:2380     
- 环境变量：ETCD_INITIAL_ADVERTISE_PEER_URLS
- 示例："[http://example.com:2380](http://example.com:2380/), [http://10.0.0.1:2380](http://10.0.0.1:2380/)"



#### --initial-cluster-token

-  引导期间etcd群集的初始集群令牌 
- 默认值："etcd-cluster"
- 环境变量：ETCD_INITIAL_CLUSTER_TOKEN



#### --initial-cluster-state

- 加入集群时的集群初始状态  
  - 如果是初始化的新的集群则设置为new
  - 如果是加入已有的集群则设置为  existing   

- 默认值："new:
- 环境变量：ETCD_INITIAL_CLUSTER_STATE



#### --initial-cluster

-  etcd启动的时候，通过这个配置找到其他ectd节点的地址列表，格式：'节点名字1=http://节点ip1:2380,节点名字1=http://节点ip1:2380,.....' 
- 默认值："default=[http://localhost:2380](http://localhost:2380/)"
- 环境变量：ETCD_INITIAL_CLUSTER



#### --initial-election-tick-advance

- 如果设置为true，在Boot时会快进初始选举（fast-forward initial election）。目标是更快选举



### 配置集群自动发现

**--discovery**

- 发现URL用于引导启动集群
- 默认值：""
- 环境变量：ETCD_DISCOVERY

**--discovery-srv**

- 用于引导集群的DNS srv域。
- 默认值：""
- 环境变量：ETCD_DISCOVERY_SRV

**--discovery-srv-name**

- 使用DNS引导时查询的DNS srv名称的后缀。
- 默认值：""
- 环境变量：ETCD_DISCOVERY_SRV_NAME

**--discovery-fallback**

- 发现服务失败时的预期行为(“退出”或“代理”)。“代理”仅支持v2 API。
- 默认值： "proxy"
- 环境变量：ETCD_DISCOVERY_FALLBACK

**--discovery-proxy**

- HTTP代理，用于发现服务的流量。
- 默认值：""
- 环境变量：ETCD_DISCOVERY_PROXY





### 日志参数



#### --log-level

- 配置日志等级，仅支持`debug,info,warn,error,panic,fatal`
- 默认值：info
- 环境变量：ETCD_LOG_LEVEL



#### --logger

 **v3.4可以使用，警告：`--logger=capnslog`在v3.5被抛弃使用** 

- 指定用什么来输出日志格式 （可选cap、capnslog）
- 默认值：capnslog
- 环境变量：ETCD_LOGGER



#### --log-outputs

- 指定输出的日志路径 可选（stdout，stderr）
- 默认值：stdout
- 样例：--log-outputs=stderr,/var/log/etcd/etcd.log
- 环境变量：ETCD_LOG_OUTPUTS



#### **--debug**
**警告：在v3.5被抛弃使用**

- 将所有子程序包的默认日志级别降为DEBUG。
- 默认值：false(所有的包使用INFO)
- 环境变量：ETCD_DEBUG
- 一般不设置



#### **--log-package-levels**
**警告：在v3.5被抛弃使用**

- 将各个etcd子软件包设置为特定的日志级别。 一个例子是`etcdserver = WARNING，security = DEBUG`
- 默认值：""(所有的包使用INFO)
- 环境变量：ETCD_LOG_PACKAGE_LEVELS
- 一般不设置



### 代理参数

--proxy前缀标志将etcd配置为以代理模式运行。 “代理”仅支持v2 API。

**--proxy**

- 代理模式设置(”off","readonly"或者"on")
- 默认值："off"
- 环境变量：ETCD_PROXY

**--proxy-failure-wait**

- 在重新考虑端点请求之前，端点将保持故障状态的时间（以毫秒为单位）。
- 默认值：5000
- 环境变量：ETCD_PROXY_FAILURE_WAIT

**--proxy-refresh-interval**

- 节点刷新间隔的时间（以毫秒为单位）。
- 默认值：30000
- 环境变量：ETCD_PROXY_REFRESH_INTERVAL

**--proxy-dial-timeout**

- 拨号超时的时间（以毫秒为单位），或0以禁用超时
- 默认值：1000
- 环境变量：ETCD_PROXY_DIAL_TIMEOUT

**--proxy-write-timeout**

- 写入超时的时间（以毫秒为单位）或禁用超时的时间为0。
- 默认值：5000
- 环境变量：ETCD_PROXY_WRITE_TIMEOUT

**--proxy-read-timeout**

- 读取超时的时间（以毫秒为单位），或者为0以禁用超时。
- 如果使用Watch，请勿更改此值，因为会使用较长的轮询请求。
- 默认值：0
- 环境变量：ETCD_PROXY_READ_TIMEOUT





### tls客户端 安全认证参数  

#### --cert-file

- 指定客户端 证书的路径
- 默认值：""
- 环境变量：ETCD_CERT_FILE



#### --client-cert-auth

- 开启客户端证书认证
- 默认值：false
- 环境变量：ETCD_CLIENT_CERT_AUTH
- CN 权限认证不支持gRPC-网关



#### --key-file

- 客户端密钥文件的路径
- 默认值：""
- 环境变量：ETCD_KEY_FILE



#### --trusted-ca-file

- zgu客户端tls ca文件的路径
- 默认值：""
- 环境变量：ETCD_TRUSTED_CA_FILE



#### --auto-tls

- 客户端TLS使用自动生成的证书
- 默认值：false
- 环境变量：ETCD_AUTO_TLS
- 一般不使用



### tls 服务端认证参数

#### --peer-cert-file

- 指定服务端证书的位置
- 示例 =/etc/kubernetes/pki/etcd/peer.crt

#### --peer-client-cert-auth

- 开启服务端证书认证
- 默认值：false
- 环境变量：ETCD_PEER_CLIENT_CERT_AUTH



#### --peer-key-file

- 服务端密钥文件的路径
- 默认值：""
- 环境变量：ETCD_PEER_KEY_FILE



#### --peer-trusted-ca-file

- 指定服务端用于校验客户端的ca根证书位置
- 默认值：""
- 环境变量：ETCD_PEER_TRUSTED_CA_FILE



#### --peer-auto-tls

- 节点使用自动生成的证书
- 默认值：false
- 环境变量：ETCD_PEER_AUTO_TLS





### 权限参数

#### **--auth-token**

- 指定令牌类型和特定于令牌的选项，特别是对于JWT,格式为`type,var1=val1,var2=val2,...`,可能的类型是`simple`或者`jwt`.对于具体的签名方法jwt可能的变量为`sign-method`（可能的值为`'ES256', 'ES384', 'ES512', 'HS256', 'HS384', 'HS512', 'RS256', 'RS384', 'RS512', 'PS256', 'PS384','PS512'`）
- 对于非对称算法（“ RS”，“ PS”，“ ES”），公钥是可选的，因为私钥包含足够的信息来签名和验证令牌。`pub-key`用于指定用于验证jwt的公钥的路径,`priv-key`用于指定用于对jwt进行签名的私钥的路径，`ttl`用于指定jwt令牌的TTL。
- JWT的示例选项：`-auth-token jwt，pub-key=app.rsa.pub，privkey=app.rsasign-method = RS512，ttl = 10m`
- 默认值："simple"
- 环境变量：ETCD_AUTH_TOKEN



















### 样例配置

```sh
/usr/local/bin/etcd
      --advertise-client-urls=https://10.128.130.77:2379
      --auto-compaction-mode=periodic
      --auto-compaction-retention=1h
      --cert-file=/etc/kubernetes/pki/etcd/server.crt
      --client-cert-auth=true
      --data-dir=/var/lib/etcd
      --initial-advertise-peer-urls=https://10.128.130.77:2380
      --initial-cluster-token=chamc-cluster2-etcd
      --key-file=/etc/kubernetes/pki/etcd/server.key
      --listen-client-urls=https://10.128.130.77:2379,https://127.0.0.1:2379
      --listen-peer-urls=https://10.128.130.77:2380
      --logger=zap
      --log-outputs=stderr,/var/log/etcd/etcd.log
      --name=etcd1
      --peer-cert-file=/etc/kubernetes/pki/etcd/peer.crt
      --peer-client-cert-auth=true
      --peer-key-file=/etc/kubernetes/pki/etcd/peer.key
      --peer-trusted-ca-file=/etc/kubernetes/pki/etcd/ca.crt
      --snapshot-count=10000
      --trusted-ca-file=/etc/kubernetes/pki/etcd/ca.crt
```

