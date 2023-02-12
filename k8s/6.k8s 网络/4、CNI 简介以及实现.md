## Kubernates CNI

Docker 传统的网络墨香已经不在适应于现在的场景，由此K8s 设计了一种新的网络模型，该网络模型规定 所有的容器都能通过一个扁平的网络平面进行通讯，（在同一网络中），在k8s集群中，Ip地址是以Pod为单位，并非容器，统一Pod内的所有Pod容器共享统一网络名称空间

Kubernates 规定Pod 与Pod 之间是可以相互通信的，并且不允许经过SNAT与DNat转换。

 k8s之所以不直接使用docker0的原因，因为是k8s项目认为Docker网络模型（CMM）不够优秀，所以其不想采用，所以也就不想去也不能够直接配置Docker网桥；再一个就是CNI模型与k8s如何配置 infra容器的Network Namespace有关 

目前实现 CNI 的有很多种方案，比如： Calico、flannel、openshift-sdn

### 三层网络



### 二层网络



### Underley

host-gw

### Overalay

Vxlan 是 对Overlay网络的实现