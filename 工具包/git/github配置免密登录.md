### github免密码登录

先使用ssh-keygen 生成免密私匙

```
ssh-keygen.exe   -t  rsa -C  @email.com
```





![1573483768847](./assets/1573483768847.png)



2. 进入.ssh文件包，打印id_rsa.pub的内容，复制全部内容



步骤4. 登录Github后，右上角点击setting

![1573483886675](./assets/1573483886675.png)



步骤5. 在左侧菜单中选择SSH and GPG keys,在右边点击New SSH key

![1573483915917](./assets/1573483915917.png)



步骤6 : Title随便写，Key 把之前id_rsa.pub的内容复制进去，点击 Add SSH key，设置ssh key 完成。

![1573483941270](./assets/1573483941270.png)





步骤7 : 检查SSH key是否有效

      在git命令行输入：ssh  -T git@github.com；这里会要求你输入SSH key密码，如果刚才生成SSH key时未输入密码，密码就为空；然后看到信息：

```
ERROR: Hi 用户名! You’ve successfully authenticated；说明配置成功； 
```

