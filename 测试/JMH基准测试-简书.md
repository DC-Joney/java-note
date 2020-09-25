## JMH基准测试框架

JMH，即Java Microbenchmark Harness，这是专门用于进行代码的微基准测试的一套工具API。
 JMH 是一个由 OpenJDK/Oracle 里面那群开发了 Java 编译器的大牛们所开发的 Micro Benchmark Framework 。何谓 Micro Benchmark 呢？简单地说就是在 method 层面上的 benchmark，精度可以精确到微秒级。可以看出 JMH 主要使用在当你已经找出了热点函数，而需要对热点函数进行进一步的优化时，就可以使用 JMH 对优化的效果进行定量的分析。

 比较典型的使用场景还有：

- 当你已经找出了热点函数，而需要对热点函数进行进一步的优化时，就可以使用 JMH 对优化的效果进行定量的分析。
- 想定量地知道某个函数需要执行多长时间，以及执行时间和输入 n 的相关性
- 一个函数有两种不同实现（例如实现 A 使用了 FixedThreadPool，实现 B 使用了 ForkJoinPool），不知道哪种实现性能更好

 尽管 JMH 是一个相当不错的 Micro Benchmark Framework，但很无奈的是网上能够找到的文档比较少，而官方也没有提供比较详细的文档，对使用造成了一定的障碍。但是有个好消息是官方的 [Code Sample](http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/) 写得非常浅显易懂，推荐在需要详细了解 JMH 的用法时可以通读一遍——本文则会介绍 JMH 最典型的用法和部分常用选项。

#### 第一个例子

如果你使用 maven 来管理你的 Java 项目的话，引入 JMH 是一件很简单的事情——只需要在 pom.xml 里增加 JMH 的依赖即可



```xml
    <properties>
        <jmh.version>1.21</jmh.version>
    </properties>


    <dependencies>
        <!-- JMH-->
        <dependency>
            <groupId>org.openjdk.jmh</groupId>
            <artifactId>jmh-core</artifactId>
            <version>${jmh.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjdk.jmh</groupId>
            <artifactId>jmh-generator-annprocess</artifactId>
            <version>${jmh.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
```

 接下来我写一个比较字符串连接操作的时候，直接使用字符串相加和使用StringBuilder的append方式的性能比较测试：



```java
/**
 * <Description> 比较字符串直接相加和StringBuilder的效率<br>
 *
 * @author Sunny<br>
 * @version 1.0<br>
 * @taskId: <br>
 * @createDate 2018/12/04 23:13 <br>
 * @see com.sunny.jmh.string <br>
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 5, timeUnit = TimeUnit.SECONDS)
@Threads(8)
@Fork(2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StringBuilderBenchmark {
    @Benchmark
    public void testStringAdd() {
        String a = "";
        for (int i = 0; i < 10; i++) {
            a += i;
        }
        print(a);
    }

    @Benchmark
    public void testStringBuilderAdd() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(i);
        }
        print(sb.toString());
    }

    private void print(String a) {
    }
}
```

 这个代码里面有好多注解，你第一次见可能不知道什么意思。先不用管，我待会一一介绍。

 我们来运行这个测试，运行JMH基准测试有多种方式，一个是生成jar文件执行， 一个是直接写main函数或写单元测试执行。

 一般对于大型的测试，需要测试时间比较久，线程比较多的话，就需要去写好了丢到linux程序里执行， 不然本机执行很久时间什么都干不了了。



```go
mvn clean package
java -jar target/benchmarks.jar
```

 先编译打包之后，然后执行就可以了。当然在执行的时候可以输入-h参数来看帮助。

 另外如果对于一些小的测试，比如我写的上面这个小例子，在IDE里面就可以完成了，丢到linux上去太麻烦。 这时候可以在里面添加一个main函数如下：



```cpp
public static void main(String[] args) throws RunnerException {
    Options options = new OptionsBuilder()
            .include(StringBuilderBenchmark.class.getSimpleName())
            .output("E:/Benchmark.log")
            .build();
    new Runner(options).run();
}
```

 这里其实也比较简单，new个Options，然后传入要运行哪个测试，选择基准测试报告输出文件地址，然后通过Runner的run方法就可以跑起来了。

 测试报告如下：



```csharp
# JMH version: 1.21
# VM version: JDK 1.8.0_144, Java HotSpot(TM) 64-Bit Server VM, 25.144-b01
# VM invoker: C:\ProgramFiles\Java\jdk1.8.0_144\jre\bin\java.exe
# VM options: -Dvisualvm.id=173373474307600 -javaagent:C:\ProgramFiles\JetBrains\IntelliJ IDEA 2017.2.1\lib\idea_rt.jar=57482:C:\ProgramFiles\JetBrains\IntelliJ IDEA 2017.2.1\bin -Dfile.encoding=UTF-8
# Warmup: 5 iterations, 1 s each
# Measurement: 10 iterations, 5 s each
# Timeout: 10 min per iteration
# Threads: 8 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.sunny.jmh.demo.StringBuilderBenchmark.testStringAdd

# Run progress: 0.00% complete, ETA 00:07:20
# Fork: 1 of 2
# Warmup Iteration   1: 6070.405 ops/ms
# Warmup Iteration   2: 8255.261 ops/ms
# Warmup Iteration   3: 8698.859 ops/ms
# Warmup Iteration   4: 9141.179 ops/ms
# Warmup Iteration   5: 8182.672 ops/ms
Iteration   1: 8013.397 ops/ms
Iteration   2: 7866.167 ops/ms
Iteration   3: 7928.024 ops/ms
Iteration   4: 8080.173 ops/ms
Iteration   5: 8073.074 ops/ms
Iteration   6: 8495.461 ops/ms
Iteration   7: 7709.082 ops/ms
Iteration   8: 8329.249 ops/ms
Iteration   9: 8112.167 ops/ms
Iteration  10: 7823.246 ops/ms

# Run progress: 12.50% complete, ETA 00:07:10
# Fork: 2 of 2
# Warmup Iteration   1: 5506.481 ops/ms
# Warmup Iteration   2: 7616.767 ops/ms
# Warmup Iteration   3: 7893.031 ops/ms
# Warmup Iteration   4: 7581.665 ops/ms
# Warmup Iteration   5: 7622.357 ops/ms
Iteration   1: 7999.950 ops/ms
Iteration   2: 7947.046 ops/ms
Iteration   3: 7791.413 ops/ms
Iteration   4: 8263.884 ops/ms
Iteration   5: 8083.529 ops/ms
Iteration   6: 8429.626 ops/ms
Iteration   7: 7999.973 ops/ms
Iteration   8: 8267.097 ops/ms
Iteration   9: 8354.462 ops/ms
Iteration  10: 8320.870 ops/ms


Result "com.sunny.jmh.demo.StringBuilderBenchmark.testStringAdd":
  8094.394 ±(99.9%) 193.825 ops/ms [Average]
  (min, avg, max) = (7709.082, 8094.394, 8495.461), stdev = 223.209
  CI (99.9%): [7900.570, 8288.219] (assumes normal distribution)


# JMH version: 1.21
# VM version: JDK 1.8.0_144, Java HotSpot(TM) 64-Bit Server VM, 25.144-b01
# VM invoker: C:\ProgramFiles\Java\jdk1.8.0_144\jre\bin\java.exe
# VM options: -Dvisualvm.id=173373474307600 -javaagent:C:\ProgramFiles\JetBrains\IntelliJ IDEA 2017.2.1\lib\idea_rt.jar=57482:C:\ProgramFiles\JetBrains\IntelliJ IDEA 2017.2.1\bin -Dfile.encoding=UTF-8
# Warmup: 5 iterations, 1 s each
# Measurement: 10 iterations, 5 s each
# Timeout: 10 min per iteration
# Threads: 8 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.sunny.jmh.demo.StringBuilderBenchmark.testStringBuilderAdd

# Run progress: 25.00% complete, ETA 00:06:06
# Fork: 1 of 2
# Warmup Iteration   1: 20955.985 ops/ms
# Warmup Iteration   2: 26262.316 ops/ms
# Warmup Iteration   3: 20401.440 ops/ms
# Warmup Iteration   4: 19642.776 ops/ms
# Warmup Iteration   5: 21810.749 ops/ms
Iteration   1: 20276.237 ops/ms
Iteration   2: 21604.222 ops/ms
Iteration   3: 21094.282 ops/ms
Iteration   4: 22015.407 ops/ms
Iteration   5: 20207.019 ops/ms
Iteration   6: 21496.461 ops/ms
Iteration   7: 21413.303 ops/ms
Iteration   8: 20926.099 ops/ms
Iteration   9: 21938.762 ops/ms
Iteration  10: 20640.369 ops/ms

# Run progress: 37.50% complete, ETA 00:05:03
# Fork: 2 of 2
# Warmup Iteration   1: 25752.391 ops/ms
# Warmup Iteration   2: 33634.702 ops/ms
# Warmup Iteration   3: 19614.646 ops/ms
# Warmup Iteration   4: 21379.381 ops/ms
# Warmup Iteration   5: 20710.261 ops/ms
Iteration   1: 20279.798 ops/ms
Iteration   2: 21302.430 ops/ms
Iteration   3: 20715.865 ops/ms
Iteration   4: 22068.016 ops/ms
Iteration   5: 21006.399 ops/ms
Iteration   6: 20509.550 ops/ms
Iteration   7: 21605.828 ops/ms
Iteration   8: 20125.785 ops/ms
Iteration   9: 21470.167 ops/ms
Iteration  10: 20688.491 ops/ms


Result "com.sunny.jmh.demo.StringBuilderBenchmark.testStringBuilderAdd":
  21069.224 ±(99.9%) 542.533 ops/ms [Average]
  (min, avg, max) = (20125.785, 21069.224, 22068.016), stdev = 624.782
  CI (99.9%): [20526.691, 21611.758] (assumes normal distribution)


# JMH version: 1.21
# VM version: JDK 1.8.0_144, Java HotSpot(TM) 64-Bit Server VM, 25.144-b01
# VM invoker: C:\ProgramFiles\Java\jdk1.8.0_144\jre\bin\java.exe
# VM options: -Dvisualvm.id=173373474307600 -javaagent:C:\ProgramFiles\JetBrains\IntelliJ IDEA 2017.2.1\lib\idea_rt.jar=57482:C:\ProgramFiles\JetBrains\IntelliJ IDEA 2017.2.1\bin -Dfile.encoding=UTF-8
# Warmup: 5 iterations, 1 s each
# Measurement: 10 iterations, 5 s each
# Timeout: 10 min per iteration
# Threads: 8 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.sunny.jmh.string.StringBuilderBenchmark.testStringAdd

# Run progress: 50.00% complete, ETA 00:04:01
# Fork: 1 of 2
# Warmup Iteration   1: 7091.196 ops/ms
# Warmup Iteration   2: 8846.815 ops/ms
# Warmup Iteration   3: 8191.191 ops/ms
# Warmup Iteration   4: 7918.475 ops/ms
# Warmup Iteration   5: 7677.783 ops/ms
Iteration   1: 8107.558 ops/ms
Iteration   2: 7804.800 ops/ms
Iteration   3: 7815.767 ops/ms
Iteration   4: 8445.645 ops/ms
Iteration   5: 8074.722 ops/ms
Iteration   6: 8218.089 ops/ms
Iteration   7: 8163.977 ops/ms
Iteration   8: 7843.616 ops/ms
Iteration   9: 8557.757 ops/ms
Iteration  10: 8025.778 ops/ms

# Run progress: 62.50% complete, ETA 00:03:01
# Fork: 2 of 2
# Warmup Iteration   1: 6632.392 ops/ms
# Warmup Iteration   2: 7942.598 ops/ms
# Warmup Iteration   3: 7741.897 ops/ms
# Warmup Iteration   4: 8969.824 ops/ms
# Warmup Iteration   5: 9159.238 ops/ms
Iteration   1: 9592.754 ops/ms
Iteration   2: 9136.667 ops/ms
Iteration   3: 7361.047 ops/ms
Iteration   4: 9217.467 ops/ms
Iteration   5: 9220.959 ops/ms
Iteration   6: 9740.287 ops/ms
Iteration   7: 9589.307 ops/ms
Iteration   8: 9415.877 ops/ms
Iteration   9: 9015.438 ops/ms
Iteration  10: 8012.396 ops/ms


Result "com.sunny.jmh.string.StringBuilderBenchmark.testStringAdd":
  8567.995 ±(99.9%) 631.440 ops/ms [Average]
  (min, avg, max) = (7361.047, 8567.995, 9740.287), stdev = 727.167
  CI (99.9%): [7936.556, 9199.435] (assumes normal distribution)


# JMH version: 1.21
# VM version: JDK 1.8.0_144, Java HotSpot(TM) 64-Bit Server VM, 25.144-b01
# VM invoker: C:\ProgramFiles\Java\jdk1.8.0_144\jre\bin\java.exe
# VM options: -Dvisualvm.id=173373474307600 -javaagent:C:\ProgramFiles\JetBrains\IntelliJ IDEA 2017.2.1\lib\idea_rt.jar=57482:C:\ProgramFiles\JetBrains\IntelliJ IDEA 2017.2.1\bin -Dfile.encoding=UTF-8
# Warmup: 5 iterations, 1 s each
# Measurement: 10 iterations, 5 s each
# Timeout: 10 min per iteration
# Threads: 8 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.sunny.jmh.string.StringBuilderBenchmark.testStringBuilderAdd

# Run progress: 75.00% complete, ETA 00:02:00
# Fork: 1 of 2
# Warmup Iteration   1: 29514.594 ops/ms
# Warmup Iteration   2: 34363.625 ops/ms
# Warmup Iteration   3: 21830.529 ops/ms
# Warmup Iteration   4: 16672.395 ops/ms
# Warmup Iteration   5: 19167.425 ops/ms
Iteration   1: 23537.009 ops/ms
Iteration   2: 26558.898 ops/ms
Iteration   3: 28540.710 ops/ms
Iteration   4: 28857.648 ops/ms
Iteration   5: 26367.646 ops/ms
Iteration   6: 28499.358 ops/ms
Iteration   7: 28387.032 ops/ms
Iteration   8: 27544.734 ops/ms
Iteration   9: 28621.245 ops/ms
Iteration  10: 27376.623 ops/ms

# Run progress: 87.50% complete, ETA 00:01:00
# Fork: 2 of 2
# Warmup Iteration   1: 31498.127 ops/ms
# Warmup Iteration   2: 26523.053 ops/ms
# Warmup Iteration   3: 24347.554 ops/ms
# Warmup Iteration   4: 25594.657 ops/ms
# Warmup Iteration   5: 24913.044 ops/ms
Iteration   1: 25242.148 ops/ms
Iteration   2: 24921.964 ops/ms
Iteration   3: 25271.209 ops/ms
Iteration   4: 23288.893 ops/ms
Iteration   5: 23138.775 ops/ms
Iteration   6: 25735.081 ops/ms
Iteration   7: 25004.206 ops/ms
Iteration   8: 24597.182 ops/ms
Iteration   9: 24222.304 ops/ms
Iteration  10: 19132.539 ops/ms


Result "com.sunny.jmh.string.StringBuilderBenchmark.testStringBuilderAdd":
  25742.260 ±(99.9%) 2127.587 ops/ms [Average]
  (min, avg, max) = (19132.539, 25742.260, 28857.648), stdev = 2450.132
  CI (99.9%): [23614.673, 27869.847] (assumes normal distribution)


# Run complete. Total time: 00:08:03

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

Benchmark                                                  Mode  Cnt      Score      Error   Units
c.s.j.demo.StringBuilderBenchmark.testStringAdd           thrpt   20   8094.394 ±  193.825  ops/ms
c.s.j.demo.StringBuilderBenchmark.testStringBuilderAdd    thrpt   20  21069.224 ±  542.533  ops/ms
c.s.j.string.StringBuilderBenchmark.testStringAdd         thrpt   20   8567.995 ±  631.440  ops/ms
c.s.j.string.StringBuilderBenchmark.testStringBuilderAdd  thrpt   20  25742.260 ± 2127.587  ops/ms
```

 仔细看，三大部分，第一部分是字符串用加号连接执行的结果，第二部分是StringBuilder执行的结果，第三部分就是两个的简单结果比较。这里注意我们forks传的2，所以每个测试有两个fork结果。

 前两部分是一样的，简单说下。首先会写出每部分的一些参数设置，然后是预热迭代执行（Warmup Iteration）， 然后是正常的迭代执行（Iteration），最后是结果（Result）。这些看看就好，我们最关注的就是第三部分， 其实也就是最终的结论。千万别看歪了，他输出的也确实很不爽，error那列其实没有内容，score(thrpt)的结果是xxx ± xxx，单位是每毫秒多少个操作。可以看到，StringBuilder的速度还确实是要比String进行文字叠加的效率好太多。

#### 注解介绍

好了，当你对JMH有了一个基本认识后，现在来详细解释一下前面代码中的各个注解含义。

### @BenchmarkMode

基准测试类型。这里选择的是Throughput也就是吞吐量。根据源码点进去，每种类型后面都有对应的解释，比较好理解，吞吐量会得到单位时间内可以进行的操作数。

- Throughput: 整体吞吐量，例如“1秒内可以执行多少次调用”。
- AverageTime: 调用的平均时间，例如“每次调用平均耗时xxx毫秒”。
- SampleTime: 随机取样，最后输出取样结果的分布，例如“99%的调用在xxx毫秒以内，99.99%的调用在xxx毫秒以内”
- SingleShotTime: 以上模式都是默认一次 iteration 是 1s，唯有 SingleShotTime 是只运行一次。往往同时把 warmup 次数设为0，用于测试冷启动时的性能。
- All(“all”, “All benchmark modes”);

### @Warmup

上面我们提到了，进行基准测试前需要进行预热。一般我们前几次进行程序测试的时候都会比较慢， 所以要让程序进行几轮预热，保证测试的准确性。其中的参数iterations也就非常好理解了，就是预热轮数。

为什么需要预热？因为 JVM 的 JIT 机制的存在，如果某个函数被调用多次之后，JVM 会尝试将其编译成为机器码从而提高执行速度。所以为了让 benchmark 的结果更加接近真实情况就需要进行预热。

- iterations：预热的次数。
- time：每次预热的时间。
- timeUnit：时间的单位，默认秒。
- batchSize：批处理大小，每次操作调用几次方法。

### @Measurement

度量，其实就是一些基本的测试参数。

1. iterations 进行测试的轮次
2. time 每轮进行的时长
3. timeUnit 时长单位

都是一些基本的参数，可以根据具体情况调整。一般比较重的东西可以进行大量的测试，放到服务器上运行。

### @Threads

每个进程中的测试线程，可用于类或者方法上。一般选择为cpu乘以2。如果配置了 Threads.MAX ，代表使用 Runtime.getRuntime().availableProcessors() 个线程。

### @Fork

进行 fork 的次数。可用于类或者方法上。如果 fork 数是2的话，则 JMH 会 fork 出两个进程来进行测试。

### @OutputTimeUnit

这个比较简单了，基准测试结果的时间类型。一般选择秒、毫秒、微秒。

### @Benchmark

方法级注解，表示该方法是需要进行 benchmark 的对象，用法和 JUnit 的 @Test 类似。

### @Param

属性级注解，@Param 可以用来指定某项参数的多种情况。特别适合用来测试一个函数在不同的参数输入的情况下的性能。

### @Setup

方法级注解，这个注解的作用就是我们需要在测试之前进行一些准备工作，比如对一些数据的初始化之类的。

### @TearDown

方法级注解，这个注解的作用就是我们需要在测试之后进行一些结束工作，比如关闭线程池，数据库连接等的，主要用于资源的回收等。

@Setup主要实现测试前的初始化工作，只能作用在方法上。用法和Junit一样。使用该注解必须定义 @State注解。

@TearDown主要实现测试完成后的垃圾回收等工作，只能作用在方法上。用法和Junit一样。使用该注解必须定义 @State 注解。

这两个注解都有一个 Level 的枚举value，它有三个值（默认的是Trial）：

- Trial：在每次Benchmark的之前/之后执行。
- Iteration：在每次Benchmark的iteration的之前/之后执行。
- Invocation：每次调用Benchmark标记的方法之前/之后都会执行。

可见，Level的粒度从Trial到Invocation越来越细。

### @State

当使用@Setup参数的时候，必须在类上加这个参数，不然会提示无法运行。

State 用于声明某个类是一个“状态”，然后接受一个 Scope 参数用来表示该状态的共享范围。 因为很多 benchmark 会需要一些表示状态的类，JMH 允许你把这些类以依赖注入的方式注入到 benchmark 函数里。Scope 主要分为三种。

1. Thread: 该状态为每个线程独享。
2. Group: 该状态为同一个组里面所有线程共享。
3. Benchmark: 该状态在所有线程间共享。
    首先说一下Benchmark，对于同一个@Benchmark，所有线程共享实例，也就是只会new Person 1次



```java
@State(Scope.Benchmark)
public static class BenchmarkState {
    Person person = new Person(21, "ben", "benchmark");
    volatile double x = Math.PI;
}

@Benchmark
public void measureShared(BenchmarkState state) {
    state.x++;
}

public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
            .include(JMHSample_03_States.class.getSimpleName())
            .threads(8)
            .warmupTime(TimeValue.seconds(1))
            .measurementTime(TimeValue.seconds(1))
            .forks(1)
            .build();

    new Runner(opt).run();
}
```

再说一下thread，这个比较好理解，不同线程之间的实例不共享。对于上面我们设定的线程数为8个，也就是会new Person 8次。



```java
@State(Scope.Thread)
public static class ThreadState {
    Person person = new Person(21, "ben", "thread");
    volatile double x = Math.PI;
}

@Benchmark
public void measureUnshared(ThreadState state) {
    state.x++;
}
```

而对于Group来说，同一个group的作为一个执行单元，所以 measureGroup 和 measureGroup2 共享8个线程，所以一个方法也就会执行new Person 4次。



```java
@State(Scope.Group)
public static class GroupState {
    Person person = new Person(21, "ben", "group");
    volatile double x = Math.PI;
}

@Benchmark
@Group("ben")
public void measureGroup(GroupState state) {
    state.x++;
}

@Benchmark
@Group("ben")
public void measureGroup2(GroupState state) {
    state.x++;
}
```

关于State的用法，官方的 code sample 里有比较好的[例子](http://hg.openjdk.java.net/code-tools/jmh/file/cb9aa824b55a/jmh-samples/src/main/java/org/openjdk/jmh/samples/JMHSample_03_States.java)。

## @Group

结合@Benchmark一起使用，把多个基准方法归为一类，只能作用在**方法**上。同一个组中的所有测试设置相同的名称(否则这些测试将独立运行——没有任何警告提示！)

##  @GroupThreads

定义了多少个线程参与在组中运行基准方法。只能作用在**方法**上。

## @OutputTimeUnit

这个比较简单了，基准测试结果的时间类型。可用于**类或者方法**上。一般选择秒、毫秒、微秒。

## @CompilerControl

该注解可以控制方法编译的行为，可用于**类或者方法或者构造函数**上。它内部有6种模式，这里我们只关心三种重要的模式：

- CompilerControl.Mode.INLINE：强制使用内联。
- CompilerControl.Mode.DONT_INLINE：禁止使用内联。
- CompilerControl.Mode.EXCLUDE：禁止编译方法。



```java
public void target_blank() {
}

@CompilerControl(CompilerControl.Mode.DONT_INLINE)
public void target_dontInline() {
}

@CompilerControl(CompilerControl.Mode.INLINE)
public void target_inline() {
}

@CompilerControl(CompilerControl.Mode.EXCLUDE)
public void target_exclude() {
}

@Benchmark
public void baseline() {
}

@Benchmark
public void blank() {
    target_blank();
}

@Benchmark
public void dontinline() {
    target_dontInline();
}

@Benchmark
public void inline() {
    target_inline();
}

@Benchmark
public void exclude() {
    target_exclude();
}
```

最后得出的结果也表名，使用内联优化会影响实际的结果：



```cpp
Benchmark                                Mode  Cnt   Score   Error  Units
JMHSample_16_CompilerControl.baseline    avgt    3   0.338 ± 0.475  ns/op
JMHSample_16_CompilerControl.blank       avgt    3   0.343 ± 0.213  ns/op
JMHSample_16_CompilerControl.dontinline  avgt    3   2.247 ± 0.421  ns/op
JMHSample_16_CompilerControl.exclude     avgt    3  82.814 ± 7.333  ns/op
JMHSample_16_CompilerControl.inline      avgt    3   0.322 ± 0.023  ns/op
```

#### 避免JIT优化

我们在测试的时候，一定要避免JIT优化。对于有一些代码，编译器可以推导出一些计算是多余的，并且完全消除它们。 如果我们的基准测试里有部分代码被清除了，那测试的结果就不准确了。比如下面这一段代码：



```java
private double x = Math.PI;

@Benchmark
public void baseline() {
    // do nothing, this is a baseline
}

@Benchmark
public void measureWrong() {
    // This is wrong: result is not used and the entire computation is optimized away.
    Math.log(x);
}

@Benchmark
public double measureRight() {
    // This is correct: the result is being used.
    return Math.log(x);
}
```

由于 `measureWrong` 方法被编译器优化了，导致效果和 `baseline` 方法一样变成了空方法，结果也证实了这一点：



```jsx
Benchmark                           Mode  Cnt   Score   Error  Units
JMHSample_08_DeadCode.baseline      avgt    5   0.311 ± 0.018  ns/op
JMHSample_08_DeadCode.measureRight  avgt    5  23.702 ± 0.320  ns/op
JMHSample_08_DeadCode.measureWrong  avgt    5   0.306 ± 0.003  ns/op
```

如果我们想方法返回值还是void，但是需要让Math.log(x)的耗时加入到基准运算中，我们可以使用JMH提供给我们的类 `Blackhole` ，使用它的 `consume` 来避免JIT的优化消除。



```cpp
@Benchmark
public void measureRight_2(Blackhole bh) {
    bh.consume(Math.log(x));
}
```

但是有返回值的方法就不会被优化了吗？你想的太多了。。。重新改改刚才的代码，让字段 `x` 变成final的。



```java
private final double x = Math.PI;
```

运行后的结果发现 `measureRight` 被JIT进行了优化，从 `23.7ns/op` 降到了 `2.5ns/op`



```undefined
JMHSample_08_DeadCode.measureRight    avgt    5  2.587 ± 0.081  ns/op
```

当然 `Math.log(Math.PI );` 这种返回写法和字段定义成final一样，都会被进行优化。

优化的原因是因为JVM认为每次计算的结果都是相同的，于是就会把相同代码移到了JMH的循环之外。

**结论：**

1. 基准测试方法一定不要返回void。
2. 如果要使用void返回，可以使用 `Blackhole` 的 `consume` 来避免JIT的优化消除。
3. 计算不要引用常量，否则会被优化到JMH的循环之外。

#### 第二个例子

在看过第一个完全只为示范的例子之后，再来看一个有实际意义的例子。

问题：

计算 1 ~ n 之和，比较串行算法和并行算法的效率，看 n 在大约多少时并行算法开始超越串行算法

首先定义一个表示这两种实现的接口



```csharp
public interface Calculator {
    /**
     * calculate sum of an integer array
     * @param numbers
     * @return
     */
    public long sum(int[] numbers);

    /**
     * shutdown pool or reclaim any related resources
     */
    public void shutdown();
}
```

由于这两种算法的实现不是这篇文章的重点，而且本身并不困难，所以实际代码就不赘述了。如果真的感兴趣的话，可以看最后的附录。以下仅说明一下我所指的串行算法和并行算法的含义。

串行算法：使用 for-loop 来计算 n 个正整数之和。
 并行算法：将所需要计算的 n 个正整数分成 m 份，交给 m 个线程分别计算出和以后，再把它们的结果相加。
 进行 benchmark 的代码如下



```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class SecondBenchmark {
    @Param({"10000", "100000", "1000000"})
    private int length;

    private int[] numbers;
    private Calculator singleThreadCalc;
    private Calculator multiThreadCalc;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(SecondBenchmark.class.getSimpleName())
                .forks(2)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public long singleThreadBench() {
        return singleThreadCalc.sum(numbers);
    }

    @Benchmark
    public long multiThreadBench() {
        return multiThreadCalc.sum(numbers);
    }

    @Setup
    public void prepare() {
        numbers = IntStream.rangeClosed(1, length).toArray();
        singleThreadCalc = new SinglethreadCalculator();
        multiThreadCalc = new MultithreadCalculator(Runtime.getRuntime().availableProcessors());
    }

    @TearDown
    public void shutdown() {
        singleThreadCalc.shutdown();
        multiThreadCalc.shutdown();
    }
}
```

注意到这里用到了3个之前没有使用的注解。

### 一些值得注意的地方

##### 无用代码消除（Dead Code Elimination）

现代编译器是十分聪明的，它们会对你的代码进行推导分析，判定哪些代码是无用的然后进行去除，这种行为对微基准测试是致命的，它会使你无法准确测试出你的方法性能。JMH本身已经对这种情况做了处理，你只要记住：1.永远不要写void方法；2.在方法结束返回你的计算结果。有时候如果需要返回多于一个结果，可以考虑自行合并计算结果，或者使用JMH提供的BlackHole对象：



```cpp
/*
 * This demonstrates Option A:
 *
 * Merge multiple results into one and return it.
 * This is OK when is computation is relatively heavyweight, and merging
 * the results does not offset the results much.
 */
@Benchmark
public double measureRight_1() {
    return Math.log(x1) + Math.log(x2);
}
/*
 * This demonstrates Option B:
 *
 * Use explicit Blackhole objects, and sink the values there.
 * (Background: Blackhole is just another @State object, bundled with JMH).
 */
@Benchmark
public void measureRight_2(Blackhole bh) {
    bh.consume(Math.log(x1));
    bh.consume(Math.log(x2));
}
```

##### 常量折叠（Constant Folding）

常量折叠是一种现代编译器优化策略，例如，i = 320 * 200 * 32，多数的现代编译器不会真的产生两个乘法的指令再将结果储存下来，取而代之的，他们会辨识出语句的结构，并在编译时期将数值计算出来（i = 2,048,000）。

在微基准测试中，如果你的计算输入是可预测的，也不是一个@State实例变量，那么很可能会被JIT给优化掉。对此，JMH的建议是：1.永远从@State实例中读取你的方法输入；2.返回你的计算结果；3.或者考虑使用BlackHole对象；

见如下官方例子：



```java
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMHSample_10_ConstantFold {
    private double x = Math.PI;
    private final double wrongX = Math.PI;
    @Benchmark
    public double baseline() {
        // simply return the value, this is a baseline
        return Math.PI;
    }
    @Benchmark
    public double measureWrong_1() {
        // This is wrong: the source is predictable, and computation is foldable.
        return Math.log(Math.PI);
    }
    @Benchmark
    public double measureWrong_2() {
        // This is wrong: the source is predictable, and computation is foldable.
        return Math.log(wrongX);
    }
    @Benchmark
    public double measureRight() {
        // This is correct: the source is not predictable.
        return Math.log(x);
    }
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHSample_10_ConstantFold.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
```

##### 循环展开（Loop Unwinding）

循环展开最常用来降低循环开销，为具有多个功能单元的处理器提供指令级并行。也有利于指令流水线的调度。例如：



```swift
for (i = 1; i <= 60; i++) 
   a[i] = a[i] * b + c;
```

可以展开成：



```xml
for (i = 1; i <= 60; i+=3)
{
  a[i] = a[i] * b + c;
  a[i+1] = a[i+1] * b + c;
  a[i+2] = a[i+2] * b + c;
}
```

由于编译器可能会对你的代码进行循环展开，因此JMH建议不要在你的测试方法中写任何循环。如果确实需要执行循环计算，可以结合@BenchmarkMode(Mode.SingleShotTime)和@Measurement(batchSize = N)来达到同样的效果。参考如下例子：



```java
/*
 * Suppose we want to measure how much it takes to sum two integers:
 */
int x = 1;
int y = 2;
/*
 * This is what you do with JMH.
 */
@Benchmark
@OperationsPerInvocation(100)
public int measureRight() {
    return (x + y);
}
```

还有这个例子：



```java
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMHSample_34_SafeLooping {
    /*
     * JMHSample_11_Loops warns about the dangers of using loops in @Benchmark methods.
     * Sometimes, however, one needs to traverse through several elements in a dataset.
     * This is hard to do without loops, and therefore we need to devise a scheme for
     * safe looping.
     */
    /*
     * Suppose we want to measure how much it takes to execute work() with different
     * arguments. This mimics a frequent use case when multiple instances with the same
     * implementation, but different data, is measured.
     */
    static final int BASE = 42;
    static int work(int x) {
        return BASE + x;
    }
    /*
     * Every benchmark requires control. We do a trivial control for our benchmarks
     * by checking the benchmark costs are growing linearly with increased task size.
     * If it doesn't, then something wrong is happening.
     */
    @Param({"1", "10", "100", "1000"})
    int size;
    int[] xs;
    @Setup
    public void setup() {
        xs = new int[size];
        for (int c = 0; c < size; c++) {
            xs[c] = c;
        }
    }
    /*
     * First, the obviously wrong way: "saving" the result into a local variable would not
     * work. A sufficiently smart compiler will inline work(), and figure out only the last
     * work() call needs to be evaluated. Indeed, if you run it with varying $size, the score
     * will stay the same!
     */
    @Benchmark
    public int measureWrong_1() {
        int acc = 0;
        for (int x : xs) {
            acc = work(x);
        }
        return acc;
    }
    /*
     * Second, another wrong way: "accumulating" the result into a local variable. While
     * it would force the computation of each work() method, there are software pipelining
     * effects in action, that can merge the operations between two otherwise distinct work()
     * bodies. This will obliterate the benchmark setup.
     *
     * In this example, HotSpot does the unrolled loop, merges the $BASE operands into a single
     * addition to $acc, and then does a bunch of very tight stores of $x-s. The final performance
     * depends on how much of the loop unrolling happened *and* how much data is available to make
     * the large strides.
     */
    @Benchmark
    public int measureWrong_2() {
        int acc = 0;
        for (int x : xs) {
            acc += work(x);
        }
        return acc;
    }
    /*
     * Now, let's see how to measure these things properly. A very straight-forward way to
     * break the merging is to sink each result to Blackhole. This will force runtime to compute
     * every work() call in full. (We would normally like to care about several concurrent work()
     * computations at once, but the memory effects from Blackhole.consume() prevent those optimization
     * on most runtimes).
     */
    @Benchmark
    public void measureRight_1(Blackhole bh) {
        for (int x : xs) {
            bh.consume(work(x));
        }
    }
    /*
     * DANGEROUS AREA, PLEASE READ THE DESCRIPTION BELOW.
     *
     * Sometimes, the cost of sinking the value into a Blackhole is dominating the nano-benchmark score.
     * In these cases, one may try to do a make-shift "sinker" with non-inlineable method. This trick is
     * *very* VM-specific, and can only be used if you are verifying the generated code (that's a good
     * strategy when dealing with nano-benchmarks anyway).
     *
     * You SHOULD NOT use this trick in most cases. Apply only where needed.
     */
    @Benchmark
    public void measureRight_2() {
        for (int x : xs) {
            sink(work(x));
        }
    }
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public static void sink(int v) {
        // IT IS VERY IMPORTANT TO MATCH THE SIGNATURE TO AVOID AUTOBOXING.
        // The method intentionally does nothing.
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHSample_34_SafeLooping.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(3)
                .build();
        new Runner(opt).run();
    }
}
```

