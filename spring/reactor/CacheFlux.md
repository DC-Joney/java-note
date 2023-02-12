## CacheFlux 

CacheFlux 是专门负责来缓存上游数据



两个入口方法

1、将上游数据缓存在Map中

```java
public static <KEY, VALUE> FluxCacheBuilderMapMiss<VALUE> lookup(
			Map<KEY, ? super List> cacheMap, KEY key, Class<VALUE> valueClass)
```







2、自定义缓存上游数据，既自己设定缓存的位置

```
public static <KEY, VALUE> FluxCacheBuilderCacheMiss<KEY, VALUE> lookup(      Function<KEY, Mono<List<Signal<VALUE>>>> reader, KEY key)
```





通用方法：

1、onCacheMissResume方法是当查不到缓存时，则从上游从新生成数据

2、andWriteWith（）将生成的数据写入到缓存中，比如spring的Cache、java的Map等等





### 举例

1、Spring cloud 获取服务实例：

```java
this.serviceInstances = CacheFlux.lookup(key -> {
			// TODO: configurable cache name
			Cache cache = cacheManager.getCache(SERVICE_INSTANCE_CACHE_NAME);
			if (cache == null) {
				if (log.isErrorEnabled()) {
					log.error("Unable to find cache: " + SERVICE_INSTANCE_CACHE_NAME);
				}
				return Mono.empty();
			}
			List<ServiceInstance> list = cache.get(key, List.class);
			if (list == null || list.isEmpty()) {
				return Mono.empty();
			}
			return Flux.just(list).materialize().collectList();
		}, delegate.getServiceId()).onCacheMissResume(delegate.get().take(1))
				.andWriteWith((key, signals) -> Flux.fromIterable(signals).dematerialize()
						.doOnNext(instances -> {
							Cache cache = cacheManager
									.getCache(SERVICE_INSTANCE_CACHE_NAME);
							if (cache == null) {
								if (log.isErrorEnabled()) {
									log.error("Unable to find cache for writing: "
											+ SERVICE_INSTANCE_CACHE_NAME);
								}
							}
							else {
								cache.put(key, instances);
							}
						}).then());
	}
```



2、spring cloud gateway 获取缓存的router 信息

```java
private final RouteLocator delegate;

	//缓存的发射器
	private final Flux<Route> routes;

	//缓存的容器
	private final Map<String, List> cache = new ConcurrentHashMap<>();

	private ApplicationEventPublisher applicationEventPublisher;

	public CachingRouteLocator(RouteLocator delegate) {
		this.delegate = delegate;
		routes = CacheFlux.lookup(cache, CACHE_KEY, Route.class)
				.onCacheMissResume(this::fetch);
	}

```

