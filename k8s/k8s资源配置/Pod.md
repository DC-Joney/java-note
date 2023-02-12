## Pod 资源配置

Pod 资源配置分为三部分组成，分别是 ObjectMetadata、PodSpec、Containers

其中ObjectMetadata 代表Pod的元数据信息，PodSec 配置的是Pod的状态信息、Containers配置Pod中的容器信息

### Metadata

Pod 元数据信息配置

#### name

pod名称配置

#### namespace

#### label

#### annotaions

#### ownerRefrence

#### uid

#### clusterName

#### deletionTimestamp

#### finalizers



### PodSpec

#### restartPolicy

#### nodeSelector

#### imagePullSecrets

#### hostNetwork

#### volumes

#### hostAliases

#### affinity

#### tolerations

#### dnsPolicy

#### securityContext

#### initContainers

#### shareProcessNamespace

#### nodeName

#### priority

#### priorityClassName

#### hostPID

#### hostIPC

#### dnsConfig

#### subdomain

#### hostname

#### terminationGracePeriodSeconds

#### activeDeadlineSeconds

#### automountServiceAccountToken

#### enableServiceLinks

#### overhead

#### ephemeralContainers

#### preemptionPolicy

#### topologySpreadConstraints



### Containers

#### name

#### image

#### imagePullPolicy

#### command

#### args

#### workingDir

#### volumeMounts

#### volumeDevices

#### ports

#### env

#### envFrom

#### livenessProbe

#### startProbe

#### readinessProbe

#### securityContext

#### lifecycle

#### terminationMessagePolicy 

默认由kuberntes 为我们指定

#### tty

#### 





