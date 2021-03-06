### 设置用户名和密码

git config --global user.name "dcjoney"

git config --global user.email "@qq.com"



查看用户名和密码

git config --global user.name

git config --global user.email

<br/><br/>

### git 初始化仓库

```
git init 初始化仓库

git add .（*） 添加文件

git commit -m "注释"

git status 查看当前的文件状态

git clone url.git 克隆 地址
```



<br/><br/>

### git 查看日志

```
git log  查看git的信息(查看全部版本的历史记录)
git log 文件名 进行查看历史记录

git log --pretty=oneline 查看具体的历史版本号（查看的更舒服一点）(查看全部版本的历史记录)
git log --pretty=oneline 文件名 简易信息查看
```



<br/><br/>

### git 版本回退

```
git reset --hard 版本号（版本号在git log中查看） 直接回退到某个版本号

git reset --hardi HEAD^ 回滚上一个版本

git reset --hard HEAD~n 回退n次操作

git reflog 查看 版本的所有信息（查看仓库的所有操作）
```

![image-20200517200011155](./assets/image-20200517200011155.png)

![å¨è¿éæå¥å¾çæè¿°](./assets/20200111122301534.png)



```
a. 要想回到过去，必须先得到commit id，然后通过git reset –hard 进行回退；

b. 要想回到未来，需要使用git reflog进行历史操作查看，得到最新的commit id； 

c. 在写回退指令的时候commit id可以不用写全，git自动识别，但是也不能写太少，至少需要写前4位字符；
```





<br/>

### git修改分支信息

```
 git remote -h

usage: git remote [-v | --verbose]
   or: git remote add [-t <branch>] [-m <master>] [-f] [--tags | --no-tags] [--mirror=<fetch|push>] <name> <url>
   or: git remote rename <old> <new>
   or: git remote remove <name>
   or: git remote set-head <name> (-a | --auto | -d | --delete | <branch>)
   or: git remote [-v | --verbose] show [-n] <name>
   or: git remote prune [-n | --dry-run] <name>
   or: git remote [-v | --verbose] update [-p | --prune] [(<group> | <remote>)...]
   or: git remote set-branches [--add] <name> <branch>...
   or: git remote get-url [--push] [--all] <name>
   or: git remote set-url [--push] <name> <newurl> [<oldurl>]
   or: git remote set-url --add <name> <newurl>
   or: git remote set-url --delete <name> <url>

    -v, --verbose         be verbose; must be placed before a subcommand



git remote -v 查看远程信息

git remote add url 添加某个远程分支
```



<br/>

### git分支管理

![å¨è¿éæå¥å¾çæè¿°](./assets/20200111123032768.png)

在版本回退的章节里，每次提交后都会有记录，Git把它们串成时间线，形成类似于时间轴的东西，这个时间轴就是一个分支，我们称之为master分支。

在开发的时候往往是团队协作，多人进行开发，因此光有一个分支是无法满足多人同时开发的需求的，并且在分支上工作并不影响其他分支的正常使用，会更加安全，Git鼓励开发者使用分支去完成一些开发任务。

```
查看分支：git branch
创建分支：git branch 分支名
切换分支：git checkout 分支名 
删除分支：git branch -d 分支名
合并分支：git merge 被合并的分支名
```

<br/>

查看分支：

![å¨è¿éæå¥å¾çæè¿°](./assets/20200111123050384.png)



**注意：当前分支前面有个标记 “*”**

<br/>

创建分支：

![å¨è¿éæå¥å¾çæè¿°](./assets/20200111123100378.png)

<br/>

切换分支：

![å¨è¿éæå¥å¾çæè¿°](./assets/20200111123146370.png)

<br/>

合并分支：

1、现在先在dev分支下的readme文件中新增一行并提交本地

![å¨è¿éæå¥å¾çæè¿°](./assets/20200111123210116.png)

<br/>

2、切换到master分支下观察readme文件

![å¨è¿éæå¥å¾çæè¿°](./assets/20200111123243161.png)

<br/>

3、将dev分支的内容与master分支合并：

![å¨è¿éæå¥å¾çæè¿°](./assets/20200111123319287.png)

<br/>

4、删除分支：

![å¨è¿éæå¥å¾çæè¿°](./assets/20200111123333865.png)

注意：**在删除分支的时候，一定要先退出要删除的分支，然后才能删除。**

<br/>

5、合并所有分支之后，需要将master分支提交线上远程仓库中：

![å¨è¿éæå¥å¾çæè¿°](./assets/20200111123401710.png)



<br/><br/>

### git 冲突管理

案例：模拟产生冲突

①同事在下班之后修改了线上仓库的代码

![å¨è¿éæå¥å¾çæè¿°](./assets/20200111123407956.png)

**注意：此时我本地仓库的内容与线上不一致的。**

![å¨è¿éæå¥å¾çæè¿°](./assets/20200111123426454.png)

<br/>

②第二天上班的时候，我没有做git pull操作，而是直接修改了本地的对应文件的内容

![å¨è¿éæå¥å¾çæè¿°](./assets/2020011112343821.png)

<br/>

③需要在下班的时候将代码修改提交到线上仓库（git push）

![å¨è¿éæå¥å¾çæè¿°](./assets/2020011112345830.png)

提示我们要在再次push之前先git pull操作。

<br/>

**【解决冲突】**

④先git pull

![å¨è¿éæå¥å¾çæè¿°](./assets/20200111123525479.png)

此时git已经将线上与本地仓库的冲突合并到了对应的文件中。

<br/>

⑤打开冲突文件，解决冲突

解决方法：需要和同事（谁先提交的）进行商量，看代码如何保留，将改好的文件再次提交即可。

![å¨è¿éæå¥å¾çæè¿°](./assets/20200111123544221.png)

<br/>

⑥重新提交

![å¨è¿éæå¥å¾çæè¿°](./assets/2020011112355714.png)



线上效果：



![å¨è¿éæå¥å¾çæè¿°](./assets/20200111123607461.png)

<br/><br/>

### 忽略文件 .gitignore文件

在git目录下新建 .gitignore 文件，然后根据具体规则写入（规则如下）:

```
1）/mtk/               过滤整个文件夹
2）*.zip                过滤所有.zip文件
3）/mtk/do.c           过滤某个具体文件
4) !index.php			   不过滤具体某个文件	
```







<br/>

### git 其他命令

```
6.还原文件
• git checkout -- 文件名


7.删除某个文件
• 先删除文件
• 再git add 再提交


git branch -b dev : 先创建分支 再切换分支

git clone url.git 克隆 地址
```

