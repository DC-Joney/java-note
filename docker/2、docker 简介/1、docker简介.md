## Docker 简介

### 什么是Docker

 Docker 属于 Linux 容器的一种封装，提供简单易用的容器使用接口（Docker 与虚拟机不同，无需占用过多资源，自身大小由所添加的组件决定，速度更快，且）。Docker 相当于一个虚拟环境容器，可以将你的开发环境、代码、配置文件等一并打包到这个容器中，并发布和应用到任意平台中。 

 **Docker** 使用 `Google` 公司推出的 [Go 语言 (opens new window)](https://golang.org/)进行开发实现，基于 `Linux` 内核的 [cgroup](https://zh.wikipedia.org/wiki/Cgroups)，[namespace](https://en.wikipedia.org/wiki/Linux_namespaces)，以及 [OverlayFS ](https://docs.docker.com/storage/storagedriver/overlayfs-driver/)类的 [Union FS ](https://en.wikipedia.org/wiki/Union_mount)等技术，对进程进行封装隔离，属于 [操作系统层面的虚拟化技术 ](https://en.wikipedia.org/wiki/Operating-system-level_virtualization)。由于隔离的进程独立于宿主和其它的隔离的进程，因此也称其为容器。最初实现是基于 [LXC](https://linuxcontainers.org/lxc/introduction/)，从 `0.7` 版本以后开始去除 `LXC`，转而使用自行开发的 [libcontainer ](https://github.com/docker/libcontainer)，从 `1.11` 版本开始，则进一步演进为使用 [runC ](https://github.com/opencontainers/runc)和 [containerd ](https://github.com/containerd/containerd)。 

 ![Docker 架构](assets/docker-on-linux.png) 

> `runc` 是一个 Linux 命令行工具，用于根据 [OCI容器运行时规范 ](https://github.com/opencontainers/runtime-spec)创建和运行容器。

> `containerd` 是一个守护程序，它管理容器生命周期，提供了在一个节点上执行容器和管理镜像的最小功能集。

 **Docker** 在容器的基础上，进行了进一步的封装，从文件系统、网络互联到进程隔离等等，极大的简化了容器的创建和维护。使得 `Docker` 技术比虚拟机技术更为轻便、快捷。 

  

下面的图片比较了 **Docker** 和传统虚拟化方式的不同之处。传统虚拟机技术是虚拟出一套硬件后，在其上运行一个完整操作系统，在该系统上再运行所需应用进程；而容器内的应用进程直接运行于宿主的内核，容器内没有自己的内核，而且也没有进行硬件虚拟。因此容器要比传统虚拟机更为轻便。 

 ![传统虚拟化](assets/virtualization.bfc621ce.png) 

 ![Docker](assets/docker.20496661.png) 