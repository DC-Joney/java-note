### 设置用户名和密码

git config --global user.name "dcjoney"

git config --global user.email "@qq.com"



查看用户名和密码

git config --global user.name

git config --global user.email





### git其他命令

```
git init 初始化仓库

git add .（*） 添加文件

git commit -m "注释"

git log  查看git的信息

git log --pretty=oneline 查看具体的历史版本号

git reset --hard HEAD^ 回滚上一个版本

git reset --hard HEAD~n 回滚到想要的版本

git reflog 查看 版本的所有信息

git reset --hard 版本号 直接回退到某个版本号

git remote -v 查看远程信息

git remote add url 添加某个远程分支

git status 查看当前状态

 3.查看文件提交记录
• 执行 git log 文件名 进行查看历史记录
• git log --pretty=oneline 文件名 简易信息查看

4.回退历史
• git reset --hard HEAD^ 回退到上一次提交
• git reset --hard HEAD~n 回退n次操作

6.还原文件
• git checkout -- 文件名

5.版本穿越
• 进行查看历史记录的版本号，执行 git reflog 文件名
• 执行 git reset --hard 版本号

7.删除某个文件
• 先删除文件
• 再git add 再提交


git branch -b dev : 先创建分支 再切换分支


git clone url.git 克隆 地址
```

