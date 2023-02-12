## Quartz是什么

Quartz提供了一些Scheduler(调度策略)，以便我们管理和执行Job(任务)。

### 官网介绍

> Quartz是一个功能丰富、开源的任务调度库，它可以被集成到所有的Java程序，无论是很小的单节点还是规模庞大的商业系统。Quartz可以被用来创建简单或者复杂的调度策略，以执行成千上万的任务。任务一般是指一个标准的Java组件，实际上可以是你写代码指定的任何逻辑。Quartz Scheduler还包括很多企业级的特性，例如JTA事务控制和集群。



### 怎么使用Quartz

#### 大致步骤



```css
1. 创建SchedulerFactory
2. 创建Scheduler
3. 创建JobDetail
4. 创建Trigger
5. 注册到Scheduler：scheduler.scheduleJob(jobDetail, trigger)
6. 启动Scheduler：scheduler.start()
```



#### 实例代码

以非集群的RamJob为例

- RAMJob.java

```java
public class RAMJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Hello Quartz: " + new Date() + " " + Thread.currentThread().getName());
    }

}
```



- RAMJobTest.java

```tsx
public class RAMJobTest {

    @Test
    public void testExecute() throws SchedulerException, InterruptedException {
        // 1.创建Scheduler的工厂
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        // 2.从工厂中获取调度器实例
        Scheduler scheduler = schedulerFactory.getScheduler();
        // 3.创建JobDetail
        JobDetail jobDetail = JobBuilder.newJob(RAMJob.class)
                .withDescription("this is a ram job") //job的描述
                .withIdentity("ramJob", "ramGroup") //job 的name和group
                .build();
        // 4.创建Trigger
        Trigger trigger = TriggerBuilder.newTrigger().withDescription("")
                .withIdentity("ramTrigger", "ramTriggerGroup")
                .startAt(new Date()) // 默认当前时间启动
                .withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?")) // 两秒执行一次
                .build();
        // 5.注册任务和定时器
        scheduler.scheduleJob(jobDetail, trigger);
        // 6.启动调度器
        scheduler.start();
        System.out.println("启动时间 ： " + new Date() + " " + Thread.currentThread().getName());
        Thread.sleep(60000);        
        System.out.println("done");
    }

}
```



- 程序主要输出

```bash
2018-08-31 15:14:03,769 INFO [org.quartz.core.QuartzScheduler] - Scheduler meta-data: Quartz Scheduler (v${version}.UNKNOWN.UNKNOWN) 'DefaultQuartzScheduler' with instanceId 'NON_CLUSTERED'
  Scheduler class: 'org.quartz.core.QuartzScheduler' - running locally.
  NOT STARTED.
  Currently in standby mode.
  Number of jobs executed: 0
  Using thread pool 'org.quartz.simpl.SimpleThreadPool' - with 10 threads.
  Using job-store 'org.quartz.simpl.RAMJobStore' - which does not support persistence. and is not clustered.

2018-08-31 15:14:03,769 INFO [org.quartz.impl.StdSchedulerFactory] - Quartz scheduler 'DefaultQuartzScheduler' initialized from default resource file in Quartz package: 'quartz.properties'
2018-08-31 15:14:03,769 INFO [org.quartz.impl.StdSchedulerFactory] - Quartz scheduler version: ${version}.UNKNOWN.UNKNOWN
2018-08-31 15:14:03,820 INFO [org.quartz.core.QuartzScheduler] - Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED started.
启动时间 ： Fri Aug 31 15:14:03 CST 2018 main
Hello Quartz: Fri Aug 31 15:14:04 CST 2018 DefaultQuartzScheduler_Worker-1
Hello Quartz: Fri Aug 31 15:14:06 CST 2018 DefaultQuartzScheduler_Worker-2
Hello Quartz: Fri Aug 31 15:14:08 CST 2018 DefaultQuartzScheduler_Worker-3
Hello Quartz: Fri Aug 31 15:14:10 CST 2018 DefaultQuartzScheduler_Worker-4
Hello Quartz: Fri Aug 31 15:14:12 CST 2018 DefaultQuartzScheduler_Worker-5
Hello Quartz: Fri Aug 31 15:14:14 CST 2018 DefaultQuartzScheduler_Worker-6
Hello Quartz: Fri Aug 31 15:14:16 CST 2018 DefaultQuartzScheduler_Worker-7
Hello Quartz: Fri Aug 31 15:14:18 CST 2018 DefaultQuartzScheduler_Worker-8
Hello Quartz: Fri Aug 31 15:14:20 CST 2018 DefaultQuartzScheduler_Worker-9
Hello Quartz: Fri Aug 31 15:14:22 CST 2018 DefaultQuartzScheduler_Worker-10
Hello Quartz: Fri Aug 31 15:14:24 CST 2018 DefaultQuartzScheduler_Worker-1
...

done
```