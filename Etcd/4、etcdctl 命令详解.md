# ETCD 常用命令

## CRUD 命令

### PUT

 格式：PUT [options] <key> <value>

选项：

- --lease 附到键上的租约ID，租约和键的到期时间有关 
- --prev-kv 返回修改前的键值
-  --ignore-value 更新键，值不变 
- --ignore-lease 更新键，租约不变

```
etcdctl put foo bar --lease=1234abcd
etcdctl put foo --ignore-value

etcdctl put --lease=leaseId K V
```



### DEL

格式：DEL [options] <key> [range_end]

选项：

- prefix 删除匹配前缀的值
- form-key 删除比指定键更大的值
- prev-key 返回删除的键值

```
etcdctl del --prefix=true ""
```





### GET

格式：GET [options] <key> [range_end]

选项：

- hex  打印为HEX格式
- limit 限制最大结果数量
- prefix 仅仅获取匹配前缀的键
- order 结果升降序 ASCEND, DESCEND
- sort-by 结果排序依据 CREATE, KEY, MODIFY, VALUE, VERSION
- rev 指定修订版
- print-value-only 仅仅打印值
- consistency  一致性保证，串行化操作
- from-key  获取比指定键更大（字节比较）的键
- keys-only 仅仅打印键



### LOCK

 在指定的名字上请求一个分布式锁，一旦获取，则知道etcdctl命令退出，会一直占用锁 

 格式：LOCK [options] <lockname> [command arg1 arg2 ...] 

选项：

- ttl 锁会话超时秒数

```
etcdctl lock --ttl 10 /TEST/LOCK
```







## 权限命令

### role add

### role delete

### role get

### role grant-permission

### role list

### role revoke-permission

### user add

### user delete

### user get

### user grant-role

### user list

### user passwd

### user revoke-role



## WATCH 

### watch



## Lease

lease grant		Creates leases
	lease keep-alive	Keeps leases alive (renew)
	lease list		List all active leases
	lease revoke		Revokes leases
	lease timetolive	Get lease information



## TXN 事务



## 成员节点操作

​	member add		Adds a member into the cluster
​	member list		Lists all members in the cluster
​	member promote		Promotes a non-voting member in the cluster
​	member remove		Removes a member from the cluster
​	member update		Updates a member in the cluster





## 成员节点状态

endpoint hashkv		Prints the KV history hash for each endpoint in --endpoints
	endpoint health		Checks the healthiness of endpoints specified in `--endpoints` flag
	endpoint status		Prints out the status of endpoints specified in `--endpoints` flag



## 快照操作

snapshot restore	Restores an etcd member snapshot to an etcd directory
	snapshot save		Stores an etcd node backend snapshot to a given file
	snapshot status		Gets backend snapshot status of a given file





## 其他操作

alarm disarm

alarm list

auth disable

auth enable

check datascale

check perf

compaction

defrag

elect

make-mirror

migrate

move-leader









### 快照操作



## Options

```sh
--cacert=""				verify certificates of TLS-enabled secure servers using this CA bundle
--cert=""					identify secure client using this TLS certificate file
--command-timeout=5s			timeout for short running command (excluding dial timeout)
--debug[=false]				enable client-side debug logging
--dial-timeout=2s				dial timeout for client connections
-d, --discovery-srv=""			domain name to query for SRV records describing cluster endpoints
--discovery-srv-name=""			service name to query when using DNS discovery
--endpoints=[127.0.0.1:2379]		gRPC endpoints
--hex[=false]				print byte strings as hex encoded strings
--insecure-discovery[=true]		accept insecure SRV records describing cluster endpoints
--insecure-skip-tls-verify[=false]	skip server certificate verification
--insecure-transport[=true]		disable transport security for client connections
--keepalive-time=2s			keepalive time for client connections
--keepalive-timeout=6s			keepalive timeout for client connections
--key=""					identify secure client using this TLS key file
--password=""				password for authentication (if this option is used, --user option shouldn't include password)
--user=""					username[:password] for authentication (prompt if password is not supplied)
-w, --write-out="simple"			set the output format (fields, json, protobuf, simple, table)
```

| 选项                       | 解释                                                         |
| -------------------------- | ------------------------------------------------------------ |
| --cacert                   | 用于校验服务器证书的根证书                                   |
| --cert                     | 表明本客户端身份的证书                                       |
| --key=                     | 表明本客户端身份的私钥                                       |
| --debug                    | 启用客户端调试日志                                           |
| --dial-timeout=2s          | 连接超时                                                     |
| --command-timeout=5s       | 短时运行的命令的超时                                         |
| --endpoints                | gRPC端点列表                                                 |
| --insecure-skip-tls-verify | 跳过服务器证书验证                                           |
| --insecure-transport       | 禁用传输层安全                                               |
| --user=username[:password] | 基于口令进行身份验证                                         |
| -w  --write-out="simple"   | 输出格式：fields, json, protobuf, simple, table              |
| --discovery-srv-name       | service name to query when using DNS discovery               |
| -d, --discovery-srv=""     | domain name to query for SRV records describing cluster endpoints |
| --hex[=false]              | 打印16进制的数据格式                                         |
| -w, --write-out="simple"   | 设置打印的输出格式，有以下几种格式 (fields, json, protobuf, simple, table) |
| --keepalive-time=2s        | keepalive time for client connections                        |
| --keepalive-timeout=6s     | keepalive timeout for client connections                     |
| --user                     | 设置用户名，或者以 username[:password] 的方式指定用户名、密码 |
| --password                 | 设置密码                                                     |