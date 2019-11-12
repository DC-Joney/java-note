设置配置属性（一般属性都是在conf文件中）
config get xxx
config set xxx 



info ${state} 

查看redis的状态



cluster nodes

查看当前集群节点的状态



cluster slots

查看集群节点的槽分配状态



client list

查看连接到redis的连接



clustor info 查看集群状态



clustor forget node-id 忘记某个节点



clustor keysolt keyname 算出key slot的位置