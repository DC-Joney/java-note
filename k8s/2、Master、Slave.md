## Kuberneters 的一些基本知识

在 Kuberneters 中， Service (服务)是分布式集群架构的核心，一个 Service对象拥有如下关键特征。

- 拥有一个唯一指定的名字 (比如`mysql-server`)。
- 拥有一个虚拟IP (ClusterIP、`ServiceIP`或`VIP`)和端口号。 
- 能够提供某种远程服务能力。
- 被映射到了提供这种服务能力的 一组容器应用上。

`Service`的服务进程目前都基于`Socket`通信方式对外提供服务，比如 Redis、 Memcache、 MySQL、 Web Server，或者是实现了某个具体业务的一个特定的`TCP Server`进程 。虽然一个 Service 通常由`多个相关的服务进程`来提供服务，每个服务进程都有一个独立的`Endpoint (IP+Port)`访问点，但 Kuberneters 能够让我们通过 Service (虚拟 Cluster IP + Service Port〉连接到指定的 Service 上。有了 `Kuberneters`内建的透明负载均衡和故障恢复机制，不管后端有多少服务进程，也不管某个服务进程是否会由于发生故障而重新部署到其他机器，都不会影响到我们对服务的正常调用。更重要的是这个Service本身一旦创建就不再变化，这意味着在 Kubernetes 集群中，我们再也不用为了服务的 IP 地址变来变去的问题而头疼了 。

容器提供了强大的隔离功能，所以有必要把为 Service 提供服务的这组进程放入容器中进行隔离。为此，Kuberneters 设计了`Pod`对象，将每个服务进程包装到相应的`Pod`中，使其成为`Pod`中运行的一个容器(Container)。为了建立`Service`和`Pod`间的关联关系， Kuberneters 给每个Pod贴上一个`标签(Label)`，给运行`MySQL`的Pod贴上`name=mysql`标签，给运行PHP的`Pod`贴上`name=php`标签，然后给相应的 Service 定义标签选择器( Label Selector)，比如`MySQL Service`的标签选择器的选择条件为`name=mysql`，意为该Service要作用于所有包含`name=mysql`Label的Pod上。这样一来，就巧妙地解决了Service与Pod的关联问题。

说到 Pod，我们这里先简单介组其概念。首先，`Pod`运行在一个我们称之为节点(Node)的环境中，这个节点既可以是物理机，也可以是私有云或者公有云中的一个虚拟机，通常在一个节点上运行几百个Pod，其次，每个 Pod 里运行着一个特殊的被称之为`Pause`的容器，其他容器则为业务容器，这些业务容器共享`Pause`容器的网络栈和`Volume`挂载卷，因此它们之间的通信和数据交换更为高效，在设计时我们可以充分利用这一特性将一组密切相关的服务进程放入同一个Pod 中；最后，需要注意的是，并不是每个 Pod 和它里面运行的容器都能“映射”到一个Service上，只有那些提供服务（无论是对内还是对外）的一组Pod才会被“映射”成一个 服务。

在集群管理方面，Kuberneters将集群中的机器划分为一个Master节点和一群工作节点(Node)。其中，在`Master`节点上运行着集群管理相关的一组进程`kube-apiserver`、`kube-controller-manager`和`kube-scheduler`，这些进程实现了整个集群的资源管理、 Pod 调度、弹性伸缩、安全控制、系统监控和纠错等管理功能，井且都是全自动完成的。 Node作为集群中的工作节点，运行真正的应用程序，在 Node 上 Kuberneters 管理的最小运行单元是 Pod。 Node 上运行着 Kuberneters 的 kubelet、 kube-proxy 服务进程，这些服务进程负责 Pod 的创建、启动、监控、重启、销毁，以及实现软件模式的负载均衡器。

最后，我们再来看看传统的 IT 系统中服务扩容和服务升级这两个难题，以及 Kuberneters 所提供的全新解决思路。服务的扩容涉及资源分配(选择哪个节点进行扩容)、实例部署和启动等环节，在一个复杂的业务系统中，这两个问题基本上靠人工一步步操作才得以完成，费时费力又难以保证实施质量 。

在 Kuberneters 集群中，你只需为需要扩容的 Service 关联的 Pod 创建一个 RC (Replication Controller)，则该 Service 的扩容以至于后来的 Service 升级等头疼问题都迎刃而解。在一个 RC 定义文件中包括以下3个关键信息。

- 目标Pod的定义。
- 目标 Pod 需要运行的副本数量( Replicas)
- 要监控的目标 Pod 的标签( Label)。

在创建好 RC (系统将自动创建好 Pod)后， Kuberneters 会通过 RC 中定义的 Label 筛选出对应的 Pod 实例井实时监控其状态和数量，如果实例数量少于定义的副本数量( Replicas)，则 会根据 RC 中定义的 Pod模板来创建一个新的Pod，然后将此 Pod调度到合适的 Node上启动运行，直到 Pod 实例的数量达到预定目标。这个过程完全是自动化的，无须人工干预。有了RC,服务的扩容就变成了一个纯粹的简单数字游戏了，只要修改 RC 中的副本数量即可。后续的`Service`升级也将通过修改`RC`来自动完成