## HystrixCommand 注解

HystrixCommand 

封装执行的代码，然后据有故障延迟用错，断路器和统计等功能，但他是阻塞的，可以和Observable 公用

HystrixObserableCommand

意思和上面差不多，主要区别是 它是一个非阻塞的调用模式

Hystrix 在使用过程中除了HystrixCommand 还有 HystrixObservableComamnd。这两个命令有很多共同点，比如都支持故障和延迟容错、断路器、指标统计。

两者的区别如下：

1）HystrixCommand 是阻塞式的，可以提供同步和异步两种方式，但是HystrixObservableCommand 是非阻塞式的，默认只能是异步的

2）HystrixCommand 的方法是run，HystrixObservableCommand 执行的是construct

3）HystrixCommand 一个实例一次只能发一条书出去，HystrixObservableCommand可以发送多条数据

介绍一些 HystrixCommand 的一些属性

- comamndKey： 全局唯一的标识符，如果不配，则默认是方法名
- defaultCallback：默认的fallback方法，该函数不能有入参，返回值和方法保持一致，但fallbaclMethod优先级更高
- fallbackMethod：指定的处理回退逻辑的方法，必须和HystrixCommand在同一个类里，方法的参数要保持一致，可以增加 发生的异常为参数
- threadPoolProperties：用来配置线程池相关的属性。
- groupKey：全局唯一的标识服务分组的名称，内部为根据这个兼职来显示统计数，仪表盘等信息，默认的线程划分是根据这个命令组的名称来进行的，一般会在窗子按HystrixCommand时间，指定命令组的来实现默认的线程池的划分；

- threadPoolKey: 对服务的线程信息进行设置i，用户HystrixThreadPool监控metrics、缓存等用途。