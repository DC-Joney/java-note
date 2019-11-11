eureka黑名单如何设置？

```
Service Registry
客户端和服务端都要配置

#heart beat during
#指定当前服务多久与服务器连接一次，即心跳时间
eureka.instance.lease-renewal-interval-in-seconds=5  
#tell server evit self when time more than 7s after last heart beat,the set must bigger than heart beat time
#告诉服务器，如果当前服务不可用，则从最后一次心跳时间t1开始，到t2为止，如果t2-t1>=30s,则立马将服务进行剔除
eureka.instance.lease-expiration-duration-in-seconds=30
 
注意事项，第二个参数的值必须大于第一个参数值，否则服务出现不断的断掉和启用的现象。
2 黑名单和白名单设置

#set ips that can to register to eureka server,when same to ignored-interfaces ,then use cur setting
# 白名单设置，当白名单和黑名单相同时，黑名单失效。可以使用正则表达式进行配置。
spring.cloud.inetutils.preferredNetworks=192.168.29.28
#set ips that stop to register to eureka server.
#设置黑名单，指定哪些微服务地址不能够向注册中心进行注册。
spring.cloud.inetutils.ignored-interfaces=192.168.29.28
3 设置用户名密码访问权限

server.ip=192.168.29.28
server.port=8888
 
#real eureka login information 登录注册中心的用户名和密码，也是客户端连接时使用的用户名和密码。
security.user.name=224
security.user.password=224
#define user login eureka to visit services
client.user.name=${security.user.name}
client.user.password=${security.user.password}
#eureka.client.serviceUrl.defaultZone=http://${server.ip}:${server.port}/eureka/
#client to fetch service from eureka server login password is same to client connect password
#需要用户名密码才能访问注册中心
eureka.client.serviceUrl.defaultZone=http://${client.user.name}:${client.user.password}@${server.ip}:${server.port}/eureka/
```





eureka如何实现高可用？

```

```







eureka 如何实现扩容？

```
server1 8761
server1 8762
server1 8763

client -> serviceUrl 8761 8762 8763

server1 -> 8762 8763
server2 -> 8761 8763
server3 -> 8761 8762
```









eureka 参数调优？

```
应用实例异常挂掉，没能在挂掉之前告知Eureka Server 要下线掉服务实例的信息，需要依赖Eureka Server
的 Evication Task去剔除
eureka:
	server:
		eviction-interval-timer-in-ms : 5000

应用实例下线时有告知Eureka Server下线，但是由于Eureka Server的 REST-API 有 response cache，需要等待缓存过期才能更新：
eureka:
	server:
		response-cache-auto-expiration-in-seconds=60

针对新的服务上线，Eureka Client 获取不及时的问题，适当的提高client 拉取server注册信息的频率
eureka：
	client：
		registry-fetch-interval-seconds: 5
		

```









eureka核心参数？

```
register-with-eureka 是否将应用实例注册到eureka
less-expiration-duration-in-second 指定应用实例多长时间向enureka发送一次心跳信息
heartbeat-executor-thread-pool-size 心跳线程 HeartbeatThread的线程池大小
eureka.client.inital-instance-info-interval-seconds InstanceInfoReplicator 将实力信息同步到eureka的时间间隔

eureka.instance.hostanme

			registerWithEnureka :是否将应用实例注册到eureka

			fetchRegistry:

			preferIpAddress: 是否优先使用ip地址来代替host name作为实例的hostName地址
			
```



