Kubelet 可通过配置项 container-runtime，container-runtime-endpoint 和 image-service-endpoint 来选择使用 docker，rkt（不推荐）或任何 CRI API 兼容的 container-runtime 。

# Kubernetes 接口

## Native

#### Docker

Docker 是迄今为止最常用的容器引擎，主要得益于其容器仓库和社区。Docker 是 dockerd 、containerd、containerd-shim 和 runc 的组合，其中 runc 作为 container-runtime。 由于 kubernetes 移除对于 container-runtime 的直接和非 CRI 接口的支持，因此 Native 支持的方式的后续将会被移除。

#### Rktnetes

截至 kubernetes 1.10.0，直接集成 rkt（或 rktnetes）支持已被弃用。

## CRI

容器运行时接口（CRI）的出现是因为 CoreOS 想要为 kubernetes 添加 rkt 支持。Kubernetes 初期提供了对于 docker 的支持，但在后续添加 rkt 支持的补丁后，代码变得丑陋务必，充斥着大量的 “if this do rkt else do docker”。为后续出现的 runtimes 添加支持将无法再继续维护，基于此，CRI 诞生了。

CRI 是一个基于 protobuf 定义的 api，包括两个 gRPC 服务，ImageService 和 RuntimeService。 ImageService 提供 RPC 以从仓库中提取镜像，检查和删除镜像。RuntimeService 包含用于管理 pod 和容器生命周期的 RPC，也提供与容器交互的调用（exec/attach/port-forward）。

#### cri-containerd

cri-containerd 是一种向 containerd 添加 CRI 支持的服务，containerd是由 Docker 创建并捐赠给 CNCF ，提供 runtime 管理和镜像服务。后续 containerd 与 Docker 分离，将 runtime 管理器与 docker 其余的工具分离，以利于不断增长的容器管理工具生态系统在 docker api上实现标准化。这有助于 docker 社区的扩展并帮助在 docker 仓库中提供丰富的容器镜像。

cri-containerd 在kubernetes 1.9 中处于 beta 阶段。



![img](https:////upload-images.jianshu.io/upload_images/5120230-677593a04ca320ab.png?imageMogr2/auto-orient/strip|imageView2/2/w/691/format/webp)

image.png

#### rktlet

rktlet 是一个使用 rkt 作为容器 runtime 的 Kubernetes 接口实现。
 当 kubelet 请求创建一个 pod 时，rktlet 将启动一个 systemd 服务，该服务将通过运行 rkt 二进制文件创建一个新的 rkt 沙盒（sandbox）。 创建 沙盒（sandbox） 后，kubelet 可以请求为 pod 添加/删除容器等。然后 rktlet 将使用相应的 rkt app 命令（app add，app rm，app start 或 app stop）运行 rkt 二进制文件。 其余的 CRI 方法也通过执行 rkt 二进制文件来处理。

rktlet 在 kubernetes 1.9 中为 alpha 状态。



![img](https:////upload-images.jianshu.io/upload_images/5120230-0be1d71755e7e6a9.png?imageMogr2/auto-orient/strip|imageView2/2/w/633/format/webp)

rktlet 架构图

#### cri-o

cri-o 是一个 CRI 实现，为 kubernetes 提供 CRI 接口支撑。 cri-o 提供了一组最小化的工具和接口来下载、提取和管理镜像、维护容器生命周期、并提供满足 CRI 所需的监控和日志记录。 它可以使用任何实现 OCI 运行时规范的 runtime，默认使用 runc。

cri-o 使用配置的 runtime（默认情况下为runc）创建容器 sandbox（pod），然后再次使用 runtime 在该 pod 中创建容器。

从kubernetes 1.9开始，cri-o 被认为是 stable 的。



![img](https:////upload-images.jianshu.io/upload_images/5120230-978b53cbd825ed2b.png?imageMogr2/auto-orient/strip|imageView2/2/w/681/format/webp)

cri-o 架构图

#### frakti

Frakti 允许 Kubernetes 通过 runV 直接在虚拟机管理程序中运行 pod 和容器 。

## OCI (Open Container Initiative) 兼容 Runtimes

### Containers

#### bwrap-oci

`bwrap-oci` 是为容器工具 bubblewrap 提供 OCI 兼容的包装器，bubblewrap 是一个无特权的容器工具。作者为：Per Giuseppe Scrivano，`bwrap-oci` 不满足e2e Kubernetes测试所需的许多功能，例如：无法指定绑定装载的选项，也无法通过 cgroup 限制资源。

#### crun

`crun`是用 C 编写的 OCI 运行时规范实现。配合 cri-o，作者 Giuseppe Scrivano 用它来通过了完整的 e2e 套件测试。它比同类型功能中的任何其他工具更小更轻。

#### railcar

railcar 是 OCI runtime-spec 规范的 rust 语言实现。参考并实现了 runc 的部分功能。一般来说，railcar 与 runc 非常相似，但不支持某些 runc 命令。截至目前不受支持的命令列表为：checkpoint，events，exec，init，list，pause，restore，resume，spec。 railcar 始终运行与容器进程分开的 init 进程。 railcar 在其研发过程号称发现了 OCI 运行时规范中的一些[缺陷](https://links.jianshu.com/go?to=https%3A%2F%2Fblogs.oracle.com%2Fdevelopers%2Fbuilding-a-container-runtime-in-rust)。通过使用 rust 语言重写 rust，作者能够消除在 go 语言实现过程中对于 c 语言中介层的的需要。

我尝试向编写缺陷那篇文章的作者询问了有关缺陷的具体内容，但是未得到回复。

#### rkt

rkt 是一个在 LInux 上运行容器而编写的 CLI 工具。它采用多层设计，允许 runtime 可根据实现者的需求更改 。 默认情况下，rkt 结合使用 `systemd`和 `systemd-nspawn` 来创建容器。 systemd-nspawn 用于管理执行 systemd 以管理 cgroup 的命名空间。容器应用程序作为 systemd 单元运行。通过使用不同与 “1阶段” 镜像，该工具可将运行应用程序的工具可以从 systemd 工具更改为其他任何工具。

rkt 包含与 runc 相同的功能，但是并不使用 OCI runtime-spec。相反，rkt 提供了一个命令行接口来提供类似的功能集。

#### runc

[runc](https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com%2Fopencontainers%2Frunc) 是一个主要用 go 编写的 CLI 工具（需要 c 语言的中介层 shim 来完成某些 go 不能完成的功能）。runc 是最受欢迎的 OCI runtime，被 containerd 和 cri-o 使用。

在大多数情况下，所有 `runc` 都会在生成进程时配置 namespace 和 cgroup。 这实际上就是是一个容器，包括 namespace 和 cgroups。

runc 依赖并跟踪 OCI runtime-spec，以保证 runc 和 OCI 规范主要版本保持同步。这意味着 runc 1.0.0 实现了 OCI 1.0 版本的规范。

#### runlxc

`runlxc` 是阿里巴巴即将开源的 OCI 兼容 runtime。 runlxc 目前尚未对外发布，与 [pouch](https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com%2Falibaba%2Fpouch) 配合使用。

### Virtual Machines

#### Clear Containers

[cc-runtime](https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com%2Fclearcontainers%2Fruntime%2F) 兼容 OCI runtime，其通过启动一个 Intel VT-x 安全 [Clear Containers]([https://github.com](https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com) / clearcontainers / agent) 管理程序，而不是标准的Linux 容器。

#### runv

[runv](https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com%2Fhyperhq%2Frunv) 是基于 hypervisor 的 OCI runtime，使用 KVM，Xen 或 QEMU 来运行 OCI 镜像。它不会创建容器。

## 分析

### 技术

#### containerd

containerd 是 CRI 实现中最复杂的，提供镜像和 runtime 服务所需的 3 个组件，采用 Go 编写。

这些组件包含两个独立的项目 [cri](https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com%2Fcontainerd%2Fcri.git)，提供了 19,000 行 Go 代码和 [containerd](https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com%2Fcontainerd%2Fcontainerd.git) 提供 runtime 的守护进程，运行程序和中介层代码一共 112,000 行。 containerd 默认使用它自己的 runc 分支，但是可以配置任何 OCI runtime 规范兼容的实现。

Docker/containerd 对 CRI 支持处于 beta 阶段。

我无法在 Arch Linux 中编译。由于时间有限，我没有花很多时间在这上面。依据 README.md 中的构建指令无法成功完成 `make install.deps` 阶段。

#### rkt

rkt 需要两个组件，rkt 和 rktlet。 尽管如此，它并没有遵循标准，而是以自己的方式做事情，尽管它的 github 描述说：“它是可组合的，安全的，并且建立在标准之上”。 它也是用 Go 编写的。

rkt 组件是 [rkt](https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com%2Frkt%2Frkt.git)，包含 71,000 行 Go 和 [rktlet](https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com%2Fkubernetes-incubator%2Frktlet.git)，包含 4,000 行。

rktlet 对 CRI 支持是 alpha 版。

#### cri-o

坐在中间是 [cri-o](https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com%2Fkubernetes-incubator%2Fcri-o.git)。cri-o 为容器编排工具 Kubernetes 定制而生。 凭借其聚焦的单一性，它很快就获得了 “稳定” 状态，仅用了 14,000 行 Go。 它与任何 OCI 运行时规范实现接口，默认为 runc。

cri-o 对 CRI 支持是稳定。

### 可用性

选择 CRI 实现的一个明显因素是可用性。 您应该能够运行完整的 e2e 测试并能够运行任何 OCI 容器。 如果出现问题，应该能够快速诊断问题。

#### cri-o

cri-o 可以作为包装安装在大多数发行版中。 使用 cri-o 非常简单。 只需更改 kubelet 标志即可使用默认值以使用cri-o 套接字。可以应用其他配置来添加其他功能，例如 repo 限制，selinux，apparmor seccomp，镜像签名等。调试问题很简单，因为只有两个部分，cri-o 守护程序和 `conmon` 控制台监视器。

使用 runc 与kubernetes 1.9 我很容易通过完整的 e2e 测试。

#### cri-containerd

由于无法编译，我无法测试其可用性。

#### rktlet

rktlet 可以在大多数发行版中作为包安装。使用 rkt 可能需要一点学习曲线。有许多有效的方法来配置它，并且学习哪一个适合的用例并不是非常简单。 rkt 使用 “阶段”，并且很少有关于阶段1 镜像像用于什么目的的文档。你*可以*创建自己的阶段1 镜像，但似乎有适当的文档可能会提供这种需要。

它只有三个部分，诊断问题通常非常简单，因为它与 systemd 集成，将所有输出留在日志中。

在尝试使用 rktlet 作为我的 CRI 提供程序时，某些容器不能够正常工作。不幸的是，由于时间限制，所以我无法正确诊断。 我希望使用 rkt 就像更改 kubelet 标志一样简单。