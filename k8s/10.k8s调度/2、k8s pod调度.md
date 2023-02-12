# K8s 节点调度

## K8s 固定节点调度

当我们在 Pod 的 yaml资源文件中配置 nodeName 与 NodeSelector 的定向调度时，如果没有合适的 node节点，那么我么们创建的Pod资源将一直处于Pending状态直到有合适的node节点就绪

### NodeName 调度

**I、Pod.spec.nodeName 将 Pod 直接调度到指定的 Node 节点上，会跳过 Scheduler 的调度策略，该匹配规则是强制匹配 **

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: myweb
spec:
  selector:
    matchLabels:
      app: myweb
  replicas: 7
  template:   
    metadata:     
      labels:    
        app: myweb    
    spec:      
      nodeName: k8s-node01      
      containers:
        - name: myweb
          image: hub.atguigu.com/library/myapp:v1
          ports: 
          - containerPort: 80
```



### NodeSelector 调度

**II、Pod.spec.nodeSelector：通过 kubernetes 的 label-selector 机制选择节点，由调度器调度策略匹配 label，而后调度 Pod 到目标节点，该匹配规则属于强制约束 **

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:  
  name: myweb
spec:  
  replicas: 2  
  selector:
    matchExpressions:
      - key: app
        operator: Exists
      - key: app
        operator: In
        values:
          - prod
          - myweb
  template:    
    metadata:      
      labels:        
        app: myweb    
    spec:      
      nodeSelector:        
        type: backEndNode1      
      containers:      
        - name: myweb        
          image: harbor/tomcat:8.5-jre8        
          ports:        
            - containerPort: 80
```



案例如下: 

**第一步: 将标签附加到节点上**

运行`kubectl get nodes`来获取节点的名称。然后选择要添加标签的那个节点，然后运行`kubectl label nodes  =`来向你选择的节点添加标签。

例如，在节点名称是 '**kube-node3**' 上打上标签为`disktype = ssd`，那么我可以运行`kubectl label nodes kube-node3 disktype=ssd`，如下：

```sh
# kubectl get nodes
kube-node1   Ready                      <none>   425d   v1.13.2
kube-node2   Ready                      <none>   405d   v1.13.2
kube-node3   Ready                      <none>   405d   v1.13.2

# 添加标签
kubectl label nodes kube-node3 disktype=ssd
```



你可以运行`--show-lables`命令来验证你的标签是否生效了。同样你也可以使用`describe node nodename`来查该节点的完整标签列表，如下：

```sh
kubectl get nodes --show-labels
或者
kubectl describe node kube-node3
```



**第二步，添加nodeSelector到你的pod配置文件中**

例如，如果这是我的pod配置：

```
apiVersion: v1
kind: Pod
metadata:
  name: nginx
  labels:
    env: test
spec:
  containers:
  - name: nginx
    image: nginx
```



然后像这样添加一个`nodeSelector`：

```
pods/pod-nginx.yaml Copy pods/pod-nginx.yaml to clipboard
apiVersion: v1
kind: Pod
metadata:
  name: nginx
  labels:
    env: test
spec:
  containers:
  - name: nginx
    image: nginx
    imagePullPolicy: IfNotPresent
  nodeSelector:
    disktype: ssd
```



运行它：

```
kubectl apply -f https://k8s.io/examples/pods/pod-nginx.yaml
```



正常情况下，该Pod将会被调度到加过标签的节点上。可以通过运行以下命令查看Pod分配给的“NODE”来验证它是否有效。

```
kubectl get pods -o wide
```



### 扩展

除了你给`Node（节点）`添加标签外，Kubernetes也会给`Node`预设义一些标签，包括:

- kubernetes.io/hostname
- failure-domain.beta.kubernetes.io/zone
- failure-domain.beta.kubernetes.io/region
- beta.kubernetes.io/instance-type
- kubernetes.io/os
- kubernetes.io/arch

注意：这些标签的值是特定于`云提供商`的，不保证是可靠的。 例如，`kubernetes.io/hostname`的值可能与某些环境中的节点名称相同，而在其他环境中就不同了



## 亲和性与反亲和性

### Node 亲和性与反亲和性

 pod.spec.nodeAffinity

- preferredDuringSchedulingIgnoredDuringExecution：软策略
- requiredDuringSchedulingIgnoredDuringExecution：硬策略 





 键值运算关系In：label 的值在某个列表中

- NotIn：label 的值不在某个列表中
- Gt：label 的值大于某个值
- Lt：label 的值小于某个值
- Exists：某个 label 存在
- DoesNotExist：某个 label 不存在 



## K8s 反亲和性

