### github clone 过慢

最近发现使用git clone的速度比较慢，于是找到了办法分享给大家：



#### 思路：

git clone特别慢是因为`github.global.ssl.fastly.net`域名被限制了。
 只要找到这个域名对应的ip地址，然后在hosts文件中加上ip–>域名的映射，刷新DNS缓存便可。



#### 实施：

1、在网站  https://www.ipaddress.com/ 分别搜索：



```css
github.global.ssl.fastly.net
github.com
```

得到ip:



![img](https:////upload-images.jianshu.io/upload_images/6832097-245f0041fe09c6e1.png?imageMogr2/auto-orient/strip|imageView2/2/w/859/format/webp)

github.global.ssl.fastly.net的ip



![img](https:////upload-images.jianshu.io/upload_images/6832097-3d64828e3d089611.png?imageMogr2/auto-orient/strip|imageView2/2/w/685/format/webp)

github.com的ip





2、打开hosts文件

- Windows上的hosts文件路径在`C:\Windows\System32\drivers\etc\hosts`
- Linux的hosts文件路径在：`sudo vim /etc/hosts`



3、在hosts文件末尾添加两行(对应上面查到的ip)



```css
151.101.185.194 github.global-ssl.fastly.net
192.30.253.112 github.com
```

4、保存更新DNS

- Winodws系统的做法：打开CMD，输入`ipconfig /flushdns`
- Linux的做法：在终端输入`sudo /etc/init.d/networking restart`



5、完成，试试`git clone`这条命令速度如何？



### 关联git 和 github

我们有时候开发代码需要把代码同步到多个远程库中，如何操作才能做到呢？

我们知道，git是分布式版本控制系统，同步到多个远程库时，需要用不同的名称来标识不同的远程库，而git给远程库起的默认名称是origin。所以我们需要修改、配置名称，以关联不同远程库。有两种方式！

为了方便举例，我以GitHub和Gitee(码云)作为示例！

#### 同步方式
##### 命令方式同步
先删除已关联的名为origin的远程库：

git remote rm origin



然后，再关联GitHub的远程库：

git remote add github git@github.com:chloneda/demo.git



接着，再关联码云的远程库：

git remote add gitee git@gitee.com:chloneda/demo.git



##### 配置方式同步
修改.git文件夹内的config文件：

复制

```
[core]
	repositoryformatversion = 0
	filemode = true
	bare = false
	logallrefupdates = true
[remote "origin"]
	url = git@github.com:chloneda/demo.git
	fetch = +refs/heads/:refs/remotes/github/
[branch "master"]
	remote = origin
	merge = refs/heads/master
```


将上述文件内容[remote “origin”]内容复制，修改origin名称，内容如下：

复制

```
[core]
	repositoryformatversion = 0
	filemode = true
	bare = false
	logallrefupdates = true
[remote "github"]
	url = git@github.com:chloneda/demo.git
	fetch = +refs/heads/:refs/remotes/github/
[remote "gitee"]
	url = git@gitee.com:chloneda/demo.git
	fetch = +refs/heads/:refs/remotes/gitee/
[branch "master"]
	remote = origin
	merge = refs/heads/master
```



#### 查看远程库
通过以上两种方式的任一种方式配置完成后，我们用git remote -v查看远程库信息：

复制

gitee   git@gitee.com:chloneda/demo.git (fetch)
gitee   git@gitee.com:chloneda/demo.git (push)
github  git@github.com:chloneda/demo.git (fetch)
github  git@github.com:chloneda/demo.git (push)


可以看到两个远程库，说明配置生效了。

#### 上传代码
复制

git add .
git commit -m "update"



#### 提交到github
git push github master



#### 提交到码云

git push gitee master

#### 更新代码

> 从github拉取更新
>
> git pull github



> 从gitee拉取更新
>
> git pull gitee


#### 踩到的坑
上述过程中，更新或提交代码时可能会遇到fatal:refusing to merge unrelated histories (拒绝合并无关的历史) 错误，解决办法：

首先将远程仓库和本地仓库关联起来。
git branch --set-upstream-to=origin/remote_branch  your_branch

其中，origin/remote_branch是你本地分支对应的远程分支，your_branch是你当前的本地分支。然后使用git pull整合远程仓库和本地仓库。

git pull --allow-unrelated-histories    (忽略版本不同造成的影响)

重新更新、提交即可。

