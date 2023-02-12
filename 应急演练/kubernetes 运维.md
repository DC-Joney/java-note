## K8S 运维

### 滚动删除deployment

```
1、kubectl patch deployment testdeployment -p   "{\"spec\":{\"template\":{\"metadata\":{\"annotations\":{\"date\":\"`date +'%s'`\"}}}}}"

# 查看滚动删除的状态
2、kubectl rollout status deployment testdeployment
```





### kubernetes 创建授权

