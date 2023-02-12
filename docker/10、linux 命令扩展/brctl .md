## 网桥管理工具brctl 

### 一.安装

- Centos系统

  ```sh
  $ yum install bridge-utils
  ```

- Ubuntu系统   

  ```sh
  $ apt-get  install bridge-utils
  ```

  

<br/>

### 参数说明和示例

| 参数            | 说明                   | 示例                  |
| --------------- | ---------------------- | --------------------- |
| `addbr `        | 创建网桥               | brctl addbr br10      |
| `delbr `        | 删除网桥               | brctl delbr br10      |
| `addif  `       | 将网卡接口接入网桥     | brctl addif br10 eth0 |
| `delif  `       | 删除网桥接入的网卡接口 | brctl delif br10 eth0 |
| `show `         | 查询网桥信息           | brctl show br10       |
| `stp  {on|off}` | 启用禁用 STP           | brctl stp br10 off/on |
| `showstp `      | 查看网桥 STP 信息      | brctl showstp br10    |
| `setfd  `       | 设置网桥延迟           | brctl setfd br10 10   |
| `showmacs `     | 查看 mac 信息          | brctl showmacs br10   |