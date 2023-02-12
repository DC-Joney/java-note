## 面试题

### 简答题

1、请详细阐述kubernetes 的五大组件，以及其作用（可画图描述）

2、请描述Pod创建的过程，可画图

3、请描述Linux的 netfilter机制

4、请描述kubernetes与docker的关系，并且描述其交互过程（就是kubernetes 如何与docker进行交互）

5、聊一聊你对CRD的理解

6、请描述kube-proxy 与 serivce 之间的联系

7、请描述Ingress 与Service 区别，Ingrss 与 LoadBalencer的区别 以及Ingress 常见的落地实现有哪些？

8、请描述常见的kubernetes 控制器，以及各种控制器之间的区别，以及如何平滑升级、回滚

9、请描述kubernetes常见的认证机制

10、请描述kubernetes常见的授权机制

11、请描述pod网络通信与 pod网络到service网络的通信过程，当网络不可达时，如何排查问题，如何解决？

12、谈一谈你对etcd 、prometheus 的理解，并且对kubernetes 集群产生了什么样的作用

13、谈一谈你对静态PV与动态PV的理解，并且给出两种PV的场景使用

14、谈一谈 startProbe、livenessProbe、readinessProbe、InitContainer 的区别以及作用

15、谈一谈kuberntes的调度机制





### 选择题

这部分问题将包括多项选择面试问题，这些问题在面试中经常被问到。

**Q1、什么是 Kubernetes 集群中的 minions？**

1. 它们是主节点的组件。
2. 它们是集群的工作节点。[答案]
3. 他们正在监控 Kubernetes 中广泛使用的引擎。
4. 他们是 Docker 容器服务。

**Q2、Kubernetes 集群数据存储在以下哪个位置？**

1. KUBE-API服务器
2. Kubelet
3. ETCD [答案]
4. 以上都不是

**Q3、哪个是 Kubernetes 控制器？**

1. ReplicaSet
2. Deployment
3. Rolling Updates
4. ReplicaSet和Deployment [答案]

**Q4、以下哪个是核心 Kubernetes 对象？**

1. Pods
2. Services
3. Volumes
4. 以上所有[答案]

**Q5、Kubernetes Network 代理在哪个节点上运行？**

1. Master Node
2. Worker Node
3. 所有节点[答案]
4. 以上都不是

**Q6、节点控制器的职责是什么？**

1. 将 CIDR 块分配给节点
2. 维护节点列表
3. 监视节点的运行状况
4. 以上所有[答案]

**Q7、Replication Controller 的职责是什么？**

1. 使用单个命令更新或删除多个 Pod
2. 有助于达到理想状态
3. 如果现有 Pod 崩溃，则创建新 Pod
4. 以上所有[答案]

**Q8、如何在没有选择器的情况下定义服务？**

1. 指定外部名称[答案]
2. 指定具有 IP 地址和端口的端点
3. 只需指定 IP 地址即可
4. 指定标签和 API 版本

**Q9、1.8 版本的 Kubernetes 引入了什么？**

1. Taints and Tolerations [答案]
2. Cluster level Logging
3. Secrets
4. Federated Clusters

**Q10、Kubelet 调用的处理检查容器的 IP 地址是否打开的程序是？**

1. HTTPGetAction
2. ExecAction
3. TCPSocketAction [答案]
4. 以上都不是



### 综合考题

1、理论篇

1.1 简要说下Kubernetes有哪些核心组件以及这些组件负责什么工作？

```
etcd：提供数据库服务保存了整个集群的状态
kube-apiserver：提供了资源操作的唯一入口，并提供认证、授权、访问控制、API注册和发现等机制
kube-controller-manager：负责维护集群的状态，比如故障检测、自动扩展、滚动更新等
cloud-controller-manager：是与底层云计算服务商交互的控制器
kub-scheduler：负责资源的调度，按照预定的调度策略将Pod调度到相应的机器上
kubelet：负责维护容器的生命周期，同时也负责Volume和网络的管理
kube-proxy：负责为Service提供内部的服务发现和负载均衡，并维护网络规则
container-runtime：是负责管理运行容器的软件，比如docker
```

1.2 你对 Kubernetes 的负载均衡器有什么了解？

```
负载均衡器是暴露服务的最常见和标准方式之一。

根据工作环境使用两种类型的负载均衡器，即内部负载均衡器或外部负载均衡器。内部负载均衡器自动平衡负载并使用所需配置分配容器，而外部负载均衡器将流量从外部负载引导至后端容器。
```

1.3 经典pod的生命周期

```
Pod都处于以下几种状态之一，可通过查询Pod详情查看。

Pending 部署Pod事务已被集群受理，但当前容器镜像还未下载完。
Running 所有容器已被创建，并被部署到k8s节点。
Successed Pod成功退出，并不会被重启。
Failed Pod中有容器被终止。
Unknown 未知原因，如kube-apiserver无法与Pod进行通讯。
详细叙述如下：

首先拖取Pod内容器的镜像，选择某个Node节点并启动Pod。 监控Pod状态，若Pod终止则根据策略决定是否重新调度。 Pod退出，并根据策略决定是否重启。
```

1.4 详述kube-proxy原理

```
问题：详述kube-proxy原理，一个请求是如何经过层层转发落到某个pod上的整个过程。请求可能来自pod也可能来自外部。

kube-proxy部署在每个Node节点上，通过监听集群状态变更，并对本机iptables做修改，从而实现网络路由。 而其中的负载均衡，也是通过iptables的特性实现的。

另外我们需要了解k8s中的网络配置类型，有如下几种：

hostNetwork Pod使用宿主机上的网络，此时可能端口冲突。
hostPort 宿主机上的端口与Pod的目标端口映射。
NodePort 通过Service访问Pod，并给Service分配一个ClusterIP。
```

1.5 deployment/rs的区别

```
问题：deployment/rs有什么区别。其使用方式、使用条件和原理是什么。

deployment是rs的超集，提供更多的部署功能，如：回滚、暂停和重启、 版本记录、事件和状态查看、滚动升级和替换升级。

如果能使用deployment，则不应再使用rc和rs。
```

1.6 rc/rs实现原理

```
问题：rc/rs功能是怎么实现的。详述从API接收到一个创建rc/rs的请求，到最终在节点上创建pod的全过程，尽可能详细。另外，当一个pod失效时，kubernetes是如何发现并重启另一个pod的？

Replication Controller 可以保证Pod始终处于规定的副本数。

而当前推荐的做法是使用Deployment+ReplicaSet。

ReplicaSet 号称下一代的 Replication Controller，当前唯一区别是RS支持set-based selector。

RC是通过ReplicationManager监控RC和RC内Pod的状态，从而增删Pod，以实现维持特定副本数的功能。

RS也是大致相同。
```

2、命令篇

2.1 查看ops这个命名空间下的所有pod，并显示pod的IP地址

```
kubectl get pods -n ops -o wide
```

2.2 查看tech命名空间下的pod名称为tech-12ddde-fdfde的日志，并显示最后30行

```
kubectl logs tech-12ddde-fdfde -n tech|tail -n 30
```

2.3 怎么查看test的命名空间下pod名称为test-5f7f56bfb7-dw9pw的状态

```
kubectl describe pods test-5f7f56bfb7-dw9pw -n test
```

2.4 如何查看test命名空间下的所有endpoints

```
kubectl get ep -n test
```

2.5 如何列出所有 namespace 中的所有 pod

```
kubectl get pods --all-namespaces
```

2.6、如何查看test命名空间下的所有ingress

```
kubectl get ingress -n test
```

2.7、如何删除test命名空间下某个deploymemt，名称为gitlab

```
kubectl delete deploy gitlab -n test
```

2.8 如何缩减test命名空间下deployment名称为gitlab的副本数为1

```
kubectl scale deployment gitlab -n test --replicas=1
```

2.9 如何在不进入pod内查看命名空间为test,pod名称为test-5f7f56bfb7-dw9pw的hosts

```
kubectl exec -it test-5f7f56bfb7-dw9pw -n test -- cat /etc/hosts
```

2.10 如何设置节点test-node-10为不可调度以及如何取消不可调度

```
kubectl cordon test-node-10     #设置test-node-10为不可调度
kubectl uncordon test-node-10   #取消 
```

3、考察实际生产经验(最重要)

3.1 某个pod启动的时候需要用到pod的名称，请问怎么获取pod的名称，简要写出对应的yaml配置(考察是否对k8s的Downward API有所了解)

```
env:
- name: test
  valueFrom:
    fieldRef:
      fieldPath: metadata.name
```

3.2 某个pod需要配置某个内网服务的hosts，比如数据库的host，刑如：192.168.4.124 db.test.com，请问有什么方法可以解决，简要写出对应的yaml配置或其他解决办法

```
解决办法：搭建内部的dns，在coredns配置中配置内网dns的IP

要是内部没有dns的话，在yaml文件中配置hostAliases，刑如：

hostAliases:
- ip: "192.168.4.124"
  hostnames:
  - "db.test.com"
```

3.3 请用系统镜像为centos:latest制作一个jdk版本为1.8.142的基础镜像，请写出dockerfile（考察dockerfile的编写能力）

```
FROM centos:latest
ARG JDK_HOME=/root/jdk1.8.0_142
WORKDIR /root
ADD jdk-8u142-linux-x64.tar.gz /root
ENV JAVA_HOME=/root/jdk1.8.0_142
ENV PATH=$PATH:$JAVA_HOME/bin
CMD ["bash"]
```

3.4 假如某个pod有多个副本，如何让两个pod分布在不同的node节点上，请简要写出对应的yaml文件（考察是否对pod的亲和性有所了解）

```
affinity:
  podAntiAffinity:
    requiredDuringSchedulingIgnoredDuringExecution:
    - topologyKey: "kubernetes.io/hostname"
      labelSelector:
        matchLabels:
          app: test
```

3.5 pod的日志如何收集，简要写出方案(考察是否真正有生产经验，日志收集是必须解决的一个难题)

```
每个公司的都不一样，下面三个链接可当做参考
https://jimmysong.io/kubernetes-handbook/practice/app-log-collection.html
https://www.jianshu.com/p/92a4c11e77ba
https://haojianxun.github.io/2018/12/21/kubernetes%E5%AE%B9%E5%99%A8%E6%97%A5%E5%BF%97%E6%94%B6%E9%9B%86%E6%96%B9%E6%A1%88/
```

3.6 谈下你对k8s集群监控的心得，口述

3.7 集群如何预防雪崩，简要写出必要的集群优化措施

```
1、为每个pod设置资源限制
2、设置Kubelet资源预留
```

3.8 集群怎么升级，证书过期怎么解决，简要说出做法

```
参考
https://googlebaba.io/post/2019/09/11-renew-ca-by-kubeadm/   #更新证书

https://jicki.me/kubernetes/2019/05/09/kubeadm-1.14.1/       #集群升级
```

3.9 etcd如何备份，简要写出命令

```
参考：https://www.bladewan.com/2019/01/31/etcd_backup/
export ETCDCTL_API=3
etcdctl --cert=/etc/kubernetes/pki/etcd/server.crt \
        --key=/etc/kubernetes/pki/etcd/server.key \
        --cacert=/etc/kubernetes/pki/etcd/ca.crt \
        snapshot save /data/test-k8s-snapshot.db
```

3.10 在以往的k8s运维中遇到过哪些问题，怎么解决的

好东西