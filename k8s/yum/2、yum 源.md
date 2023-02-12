## yum 源

### yum 源配置文件位置

yum 的仓库地址都在 /etc/yum.repo.d 目录文件下，配置文件简介：

```properties
# repo id，可以使用--enable 或者--disable开启或者关闭，会列举在 repolist中
[kubernetes]

#仓库名称
name=Kubernetes
#仓库地址
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64

# 是否开启该仓库
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg
https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
```



执行 yum repolist enabled 显示如下：

```
repo id		 repo name	      status
!kubernetes   Kubernetes       633
```





### yum 常用命令



#### 1、列出某个软件的所有版本

```bash
yum list docker-ce --showduplicates | sort -r
```



```
yum repolist {all|enabled|disabled} 列出所有/已启用/已禁用的yum源

yum list {all|installed|avaliable} 列出所有/已安装/可安装的软件包

yum info package 显示某一个软件包的信息 --建议使用rpm -qi package_name，yum显示的比较慢，需要加载缓存


yum install package -y安装软件包

yum reinstall package 重新安装软件包

yum remove|earse package 卸载软件包

yum whatprovides|provides files 查询某个文件是哪个软件包生成的,files通常指命令，比如可以写nginx--这个命令比较实用

 
yum grouplist {all|installed|avaliable} 列出所有/已安装/可安装的软件包组  --用的不多，了解一下即可

yum groupinfo 显示某个软件包组的信息

yum groupinstall 安装某个软件包组

yum groupremove 删除某个软件包组

 

yum history 查看yum使用的历史

yum clean {all|packages|metadata} 清除所有/软件包/元数据

yum makecache 生成yum元数据

yum --nogpgcheck 不校验公钥文件

yum -y 自动进行操作

yum update 升级所有包，以及升级软件和系统内核，这就是一键升级  --

yum update package 更新指定程序包package --一般不要使用这条命令

 

yum --disablerepo=repo  --临时禁用某个repo源

yum  install nginx --disablerepo=nginx-stable  --临时禁用指定的repo，在其他的repo安装nginx包，这条命令比较实用

yum --enablerepo=

yum -C list nginx  --在缓存中查找包，速度更快
```



