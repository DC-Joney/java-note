### HystrixCommands:

```
@HystrixCommand(fallback = "")
当发生错误的时候 会使用回调方法类进行


@FeignClient(name = "serviceName", fallBack = "")


config:
	feign:
		hystrix:
			enable: true
			
```

