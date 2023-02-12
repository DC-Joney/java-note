## ControllerManager







### 生产参数

```sh
 "kube-controller-manager",
  "--cluster-signing-cert-file=/etc/kubernetes/pki/ca.crt",
  "--cluster-signing-key-file=/etc/kubernetes/pki/ca.key",
  "--controllers=*,bootstrapsigner,tokencleaner",
  "--experimental-cluster-signing-duration=876000h0m0s",
  "--port=0",
  "--kube-api-burst=60",
  "--kube-api-qps=40",
  "--kubeconfig=/etc/kubernetes/controller-manager.conf",
  "--leader-elect=true",
  "--logtostderr=false",
  "--log-dir=/var/log/kubernetes",
  "--node-cidr-mask-size=24",
  "--root-ca-file=/etc/kubernetes/pki/ca.crt",
  "--secure-port=10257",
  "--service-account-private-key-file=/etc/kubernetes/pki/sa.key",
  "--use-service-account-credentials=true",
  "--unhealthy-zone-threshold=0.3",
  "-v=4"
```

