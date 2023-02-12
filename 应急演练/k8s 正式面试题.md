## 面试题

### 选择题

**1、Kubernetes 集群数据存储在以下哪个位置？**

- A. KUBE-API服务器 
- B. Kubelet 
- C. ETCD 
- D. 以上都不是

**2、Kubernetes Network 代理在哪个节点上运行？**

- A. Master Node
- B. Worker Node
- C. 所有节点
- D. 以上都不是

**3、节点控制器的职责是什么？**

- A. 使用单个命令更新或删除多个 Pod
- B. 有助于达到理想状态
- C. 如果现有 Pod 崩溃，则创建新 Pod
- D.以上所有

**4、以下选项中与kubernetes的认证、授权、准入控制相关的有哪些？**

- A. X509 认证
- B. Service Account
- C. Role
- D. ClustorRoleBinding
- E. MutatingAdmissionWebhook 
- F. Kubeconfig
- G. PodSecurity
- H. 以上所有

**5、以下哪个是核心组api  的Kubernetes 对象？**

- A. Pods
- B. Services
- C. Deployments
- D.以上所有

**6、kubernetes 是通过什么将service与 pod相互绑定的？**

- A. Replicaset Controller
- B. Deplpyment Controller
- C. Endpoint Controller
- D.以上所有

**7、kubelet 是通过自身哪些开放性插件为Pod 初始化环境以及绑定资源的？**

- A. CNI
- B. Scheduler
- C. CSI
- D. CRI
- E. OCI
- F. Informer

**8、kubernetes CRI 的实现包括哪些？**

- A. Kata
- B. cAdvisor
- C. docker
- D. gVisor

**9、kubernetes 的控制器包括以下哪几种？**

- A. ReplicaSet
- B. CornJob
- C. StatefulSet
- D. Service
- E. PersistenceVolume





### 画图题

**1、请画图阐述kubernetes 的基本组件，并且简单描述其作用**



**2、请画图展示Pod 创建的过程 （如果描述的是由控制器创建的Pod 最佳）**



### 简答题

> 以下题目除了第一题其他题目可以不使用专业的英语词汇，用汉语描述代替也可以

**1、请描述Linux的 netfilter机制**



**2、请描述  startupProbe 、 livenessProbe 、 readinessProbe 、InitContainer 的区别以及作用**



**3、请描述kuberntes的调度机制**



**4、请描述你对StorageClass的理解**



