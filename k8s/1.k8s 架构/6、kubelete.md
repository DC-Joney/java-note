## Kubelet 解析



### 生产参数

```
KUBELET_EXTRA_ARGS="--allow-privileged=true \
  --hostname-override 10.128.130.77 \
  --node-ip 10.128.130.77 \
  --allowed-unsafe-sysctls=kernel.shm*,kernel.msg*,kernel.sem,fs.mqueue.*,net.* \
  --bootstrap-kubeconfig=/etc/kubernetes/bootstrap-kubelet.conf \
  --kubeconfig=/etc/kubernetes/kubelet.conf \
  --cni-bin-dir=/opt/cni/bin \
  --cni-conf-dir=/etc/cni/net.d \
  --config=/var/lib/kubelet/config.yaml \
  --network-plugin=cni  \
  --log-dir=/data/log/kubelet \
  --logtostderr=false \
  --pod-infra-container-image=harbor.infra.dv.com/library/pause-amd64:3.1 \
  --register-with-taints node.netease.com/phase=checking:NoSchedule,node-role.kubernetes.io/master=:NoSchedule \
  --node-labels node-role.kubernetes.io/master=,node.netease.com/phase=checking,beta.kubernetes.io/instance-type=virtualMachine,network.netease.com/kube-proxy=lb-masquerade,colocation.netease.com/node-pool=service,failure-domain.beta.kubernetes.io/zone=az1 \
  --feature-gates=SupportPodPidsLimit=true \
  -v=4"

```



### 添加node 到 k8s 集群

```python
#!/usr/bin/python2
import json
import os
import sys
import ssl
import re
import urllib2
import commands
import base64
from datetime import datetime

# const vars
KubeconfigPath = "/etc/kubernetes/kubelet.conf"
BootstrapPath = "/etc/kubernetes/bootstrap-kubelet.conf"
KubeletConfigPath = "/var/lib/kubelet/config.yaml"
DefaultKubeletPath = "/etc/default/kubelet"
InitConfigPath = "/etc/kubernetes/init-config"
LogFilePath = "/var/log/init-k8s-node.log"


def log(msg):
    msg_with_time = "[%s] %s\n" % (datetime.now(), msg)
    print(msg_with_time)
    with open(LogFilePath, 'a+') as f:
        f.write(msg_with_time)


# check kubeconfig if exists
def check_kubeconfig_exists():
    if os.path.exists(KubeconfigPath) or os.path.exists(BootstrapPath):
        log("WARN: %s or %s already exists, recreating" % (KubeconfigPath, BootstrapPath))
        # sys.exit()


# get config from /etc/kubernetes/init-config
def parse_config():
    with open(InitConfigPath) as f:
        config = json.load(f)
    return config


class Node(object):
    def __init__(self, config, bootstrap_token):
        self.config = config
        self.bootstrap_token = bootstrap_token
        self.init_args()

    def init_args(self):
        self.apiserver_addr = self.config["apiserver"]
        if not self.apiserver_addr.startswith("http"):
            self.apiserver_addr = "https://" + self.apiserver_addr.rstrip("/")
        self.k8s_version = self.config.get("k8s_version", "1.11")
        if self.config.get('listenInterface'):
            self.address = self.bash_run(
                "ip addr show %s|grep -Eo 'inet [0-9]+\.[0-9]+\.[0-9]+\.[0-9]+'|awk '{print $2}'|head -n1" %
                self.config['listenInterface'])
        self.gpu_type = self.bash_run(
            "which nvidia-smi >/dev/null 2>&1 && nvidia-smi --query-gpu=gpu_name --format=csv,noheader --id=0|tr 'A-Z' 'a-z'|sed 's/ /-/g'||echo")
        status, self.os_version = self.bash_run_with_error("cat /etc/issue")
        if self.os_version.find("Debian") != -1:
            self.os_version = "Debian"
        elif self.os_version.find('CentOS') != -1:
            self.os_version = "CentOS"
        else:
            self.os_version = "Debian"
        # case mlx interface
        status, interface_name = self.bash_run_with_error("lspci|grep Ethernet")
        # 03:00.0 Ethernet controller: Mellanox Technologies MT27710 Family [ConnectX-4 Lx]
        pattern_mlnx = re.compile("Mellanox.*MT27710")
        # 01:00.1 Ethernet controller: Intel Corporation 82599ES 10-Gigabit SFI/SFP+ Network Connection (rev 01)
        pattern_82599 = re.compile("Intel.*82599.*10-Gigabit")
        # 05:00.0 Ethernet controller: Intel Corporation Ethernet Controller XXV710 for 25GbE SFP28 (rev 02)
        pattern_xxv710 = re.compile("Intel.*XXV710.*25GbE")
        # 06:00.1 Ethernet controller: Intel Corporation Ethernet Controller X710 for 10GbE SFP+ (rev 01)
        pattern_x710 = re.compile("Intel.*X710.*10GbE")
        if hasattr(re.search(pattern_mlnx, interface_name), 'group'):
            link_speed = self.bash_run("cat /sys/class/net/eth0/speed")
            self.interface = 'mlnx-' + str(int(link_speed)/1000) + 'G'
        # case XXV710 interface
        elif hasattr(re.search(pattern_xxv710, interface_name), 'group'):
            self.interface = 'intel-25G'
        # case X710 interface
        elif hasattr(re.search(pattern_x710, interface_name), 'group'):
            self.interface = 'intel-10G'
        # case 82599(X510) interface
        elif hasattr(re.search(pattern_82599, interface_name), 'group'):
            self.interface = 'intel-10G'
        else:
            self.interface = ''
        status, sriov_status = self.bash_run_with_error("ovs-vsctl show")
        if sriov_status.find('dpdk') != -1:
            self.sriov = True
        else:
            self.sriov = False

    @classmethod
    def request_k8s(cls, url, token=""):
        log("call %s with token %s" % (url, token))
        req = urllib2.Request(url)
        if token:
            req.add_header('Authorization', 'Bearer %s' % token)
        ctx = ssl.create_default_context()
        ctx.check_hostname = False
        ctx.verify_mode = ssl.CERT_NONE
        resp = urllib2.urlopen(req, context=ctx)
        status_code, resp_data = resp.getcode(), resp.read()
        if status_code >= 300:
            log("failed to request %s, status code: %s, response: %s" % (url, status_code, resp_data))
            sys.exit(1)
        return resp_data

    @classmethod
    def bash_run_with_error(cls, command):
        status, output = commands.getstatusoutput(command)
        if status != 0:
            log("can not run %s, with exit code: %s, output: %s. continue to run..." % (command, status, output))
        return status, output.strip()

    @classmethod
    def bash_run(cls, command):
        status, output = commands.getstatusoutput(command)
        if status != 0:
            log("can not run %s, with exit code: %s, output: %s" % (command, status, output))
            sys.exit(1)
        return output.strip()

    @classmethod
    def makedirs(cls, dir):
        if not os.path.exists(dir):
            os.makedirs(dir)

    # gen /etc/kubernetes/bootstrap-kubelet.conf
    def create_bootstrap_conf(self):
        url = "%s/api/v1/namespaces/kube-public/configmaps/cluster-info" % self.apiserver_addr
        data = self.request_k8s(url)
        kubeconfig = json.loads(data)['data']['kubeconfig']
        log("get kubeconfig from configmap: %s" % kubeconfig)
        ca_data = ""
        for i in kubeconfig.split('\n'):
            if 'certificate-authority-data:' in i:
                ca_data = i.strip().split()[-1]
        if not ca_data:
            log("ca data is empty")
            sys.exit(1)
        template = '''apiVersion: v1
clusters:
- cluster:
    certificate-authority-data: %s
    server: %s
  name: kubernetes
contexts:
- context:
    cluster: kubernetes
    user: tls-bootstrap-token-user
  name: tls-bootstrap-token-user@kubernetes
current-context: tls-bootstrap-token-user@kubernetes
kind: Config
preferences: {}
users:
- name: tls-bootstrap-token-user
  user:
    token: %s
'''
        bootstrap_config = template % (ca_data, self.apiserver_addr, self.bootstrap_token)
        self.makedirs(os.path.dirname(BootstrapPath))
        with open(BootstrapPath, "w") as f:
            f.write(bootstrap_config)
        self.makedirs("/etc/kubernetes/pki")
        with open("/etc/kubernetes/pki/ca.crt", "w") as f:
            f.write(base64.b64decode(ca_data))

    # gen /var/lib/kubelet/config.yaml
    def create_kubelet_config(self):
        url = "%s/api/v1/namespaces/kube-system/configmaps/kubelet-config-%s" % (self.apiserver_addr, self.k8s_version)
        data = self.request_k8s(url, self.bootstrap_token)
        kubelet_config = json.loads(data)['data']['vm-kubelet-config']
        log("get kubelet_config from configmap: %s" % kubelet_config)
        if not kubelet_config:
            log("kubelet-config from %s is empty, exit 1" % url)
            sys.exit(1)
        # modify kubelet bind address
        if self.address:
            kubelet_config = kubelet_config.replace("address: 0.0.0.0", "address: %s" % self.address)
        # modify kubeReserved and systemReserved
        kubelet_config = self.reset_cpu_reserved(kubelet_config)
        kubelet_config = self.reset_memory_reserved(kubelet_config)
        self.makedirs(os.path.dirname(KubeletConfigPath))
        with open(KubeletConfigPath, 'w') as f:
            f.write(kubelet_config)
        # touch /etc/kubernetes/resolv.conf
        if 'resolvConf: ' in kubelet_config:
            kubelet_config_list = kubelet_config.split()
            resolv_conf_path = kubelet_config_list[kubelet_config_list.index('resolvConf:') + 1]
            self.makedirs(os.path.dirname(resolv_conf_path))
            if not os.path.exists(resolv_conf_path):
                open(resolv_conf_path, 'a').close()

    # gen /etc/default/kubelet
    def create_default_kubelet(self):
        k8s_version = self.config.get("k8s_version", "1.11")
        url = "%s/api/v1/namespaces/kube-system/configmaps/kubelet-config-%s" % (self.apiserver_addr, k8s_version)
        data = self.request_k8s(url, self.bootstrap_token)
        default_kubelet = json.loads(data)['data']['default-kubelet']
        log("get default_kubelet from configmap: %s" % default_kubelet)
        if not default_kubelet:
            log("default-kubelet from %s is empty, exit 1" % url)
            sys.exit(1)
        default_kubelet_list = default_kubelet.split('\n')
        # set gpu label
        gpu_label = "accelerator=nvidia-%s" % self.gpu_type if self.gpu_type else ""
        default_label, vpc_port_label, single_subnet_id_label = "", "", ""
        for l in default_kubelet_list:
            if 'node-labels' in l:
                if 'node-labels=' in l:
                    default_label = l.split('=', 1)[1].strip("\\ ")
                else:
                    default_label = l.split()[1].strip("\\ ")
                log("default label from configmap: %s" % default_label)
                break
        if os.path.exists("/etc/kubernetes/port.conf"):
            with open("/etc/kubernetes/port.conf") as f:
                tmp_port_info = json.load(f)
                port_id = tmp_port_info.get('id', '')
                subnet_id = tmp_port_info.get('fixed_ips')[0].get('subnet_id', '')
            vpc_port_label = "node.netease.com/port_id=%s" % port_id
            single_subnet_id_label = "network.netease.com/single-subnet-id=%s" % subnet_id
        init_config_label = self.config.get('nodeLabels', '')
        # kubeproxy 1.13-netease version must have this label, and this label is suitable for all cases
        kubeproxy_label = 'network.netease.com/kube-proxy=lb-masquerade'
        new_node_label = ','.join(set(
            [i.strip() for i in
             ','.join([gpu_label, default_label, kubeproxy_label, vpc_port_label, init_config_label]).split(',') if
             i.strip()]))
        # if VPC + virtualMachine case, must labe the subnet-id
        if init_config_label.find('beta.kubernetes.io/instance-type=virtualMachine') != -1 and os.path.exists("/etc/kubernetes/port.conf"):
            new_node_label = new_node_label + ',' + single_subnet_id_label
        # if VPC and virtual network type is SR-IOV
        if self.interface and self.sriov:
            new_node_label = new_node_label + ',network.netease.com/sriov-type=' + self.interface
        node_label_index = -1
        for index, line in enumerate(default_kubelet_list):
            if '--node-labels' in line:
                node_label_index = index
                break
        if node_label_index == -1 and new_node_label:
            default_kubelet_list.insert(1, '  --node-labels %s \\' % new_node_label)
        elif new_node_label:
            default_kubelet_list[node_label_index] = '  --node-labels %s \\' % new_node_label
        # set gpu taint
        gpu_taint = "nvidia.com/gpu=true:NoSchedule" if self.gpu_type else ""
        # if VPC and virtual network type is SR-IOV
        if self.interface and self.sriov:
            sriov_taint = "network.netease.com/sriov-type=" + self.interface + ":NoSchedule"
        else:
            sriov_taint = ""
        default_taint = ""
        for l in default_kubelet_list:
            if 'register-with-taints' in l:
                if 'register-with-taints=' in l:
                    default_taint = l.split('=', 1)[1].strip("\\ ")
                else:
                    default_taint = l.split()[1].strip("\\ ")
                break
        init_config_taint = self.config.get('nodeTaints', '')
        new_node_taint = ','.join(set(
            [i.strip() for i in ','.join([gpu_taint, sriov_taint, init_config_taint, default_taint]).split(',') if
             i.strip()]))
        taint_index = -1
        for index, line in enumerate(default_kubelet_list):
            if 'register-with-taints' in line:
                taint_index = index
                break
        if taint_index == -1 and new_node_taint:
            default_kubelet_list.insert(1, '  --register-with-taints %s \\' % new_node_taint)
        elif new_node_taint:
            default_kubelet_list[taint_index] = '  --register-with-taints %s \\' % new_node_taint
        # set node ip
        if self.address:
            default_kubelet_list.insert(1, "  --node-ip %s \\" % self.address)

            # 
            default_kubelet_list.insert(1, "  --hostname-override %s \\" % self.address)
            # 
        with open(DefaultKubeletPath, 'w') as f:
            f.write('\n'.join(default_kubelet_list))
        # makedir log dir
        for line in default_kubelet_list:
            if 'log-dir' in line:
                log_path = line.split('log-dir=', 1)[1].strip("\\ ") if 'log-dir=' in line else line.split()[1].strip(
                    "\\ ")
                self.makedirs(log_path)
                break

    def restart_kubelet(self):
        self.bash_run("systemctl daemon-reload")
        self.bash_run("systemctl restart kubelet")

    def reset_cpu_reserved(self, kubelet_config):
        if not self.config.get('resources') or not self.config['resources'].get('cpuRemained'):
            return kubelet_config
        cpu_remained = int(self.config['resources']['cpuRemained'])
        cpu_total = open('/proc/cpuinfo').read().count('processor\t:')
        if cpu_total <= cpu_remained:
            return kubelet_config
        cpu_reserved = cpu_total - cpu_remained
        if cpu_reserved % 2 == 0:
            cpu_per_reserved = cpu_reserved / 2
        else:
            cpu_per_reserved = "%sm" % (cpu_reserved / 2 * 1000 + 500)
        kubelet_config_list = kubelet_config.split("\n")
        for index, line in enumerate(kubelet_config_list):
            if "  cpu: " in line:
                kubelet_config_list[index] = '  cpu: "%s"' % cpu_per_reserved
        return '\n'.join(kubelet_config_list)

    def reset_memory_reserved(self, kubelet_config):
        if not self.config.get('resources') or not self.config['resources'].get('memoryReserved'):
            return kubelet_config
        memory_reserved_percent = int(self.config['resources']['memoryReserved'].rstrip('%'))
        memory_bytes_total = os.sysconf('SC_PAGE_SIZE') * os.sysconf('SC_PHYS_PAGES')
        memory_gib_total = memory_bytes_total / (1024 ** 3)
        memory_per_reserved = memory_gib_total * memory_reserved_percent / 100 / 2
        kubelet_config_list = kubelet_config.split("\n")
        for index, line in enumerate(kubelet_config_list):
            if "  memory: " in line:
                kubelet_config_list[index] = '  memory: %sGi' % memory_per_reserved
        return '\n'.join(kubelet_config_list)


def main():
    check_kubeconfig_exists()
    config = parse_config()
    node = Node(config, sys.argv[1])
    node.create_bootstrap_conf()
    node.create_kubelet_config()
    node.create_default_kubelet()
    node.restart_kubelet()
    log("add node suceessfully.")


if __name__ == '__main__':
    main()

```

