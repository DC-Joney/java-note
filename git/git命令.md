git config --global user.name "dcjoney"



git config --global user.email "@qq.com"



```
github 上传

git init

git add .

git commit -m "注释"

git log 查看git的信息

git log --pretty=oneline 查看具体的历史版本号

git reset --hard HEAD^ 回滚上一个版本

git reset --hard HEAD~n 回滚到想要的版本

git reflog 查看 版本的所有信息

git reset --hard 版本号 直接回退到某个版本号



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

