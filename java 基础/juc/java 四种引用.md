### java 四种引用类型

### 强引用

普通的java对象就是强引用



### 软引用

SoftRefrence，在SoftRefrence内部维护一个对象指向

- jvm存储不了大对象的时候，会被回收
- 适合用来做缓存，或者缓存的Key



### 弱引用

WeakRefrence

为了解决内存泄漏问题，只要遇到GC就会被回收





### 虚引用

RefrenceQueue -> PhatoRefrence

- 管理堆外内存

  ![image-20200717130401013](assets/image-20200717130401013.png)

  当指向堆外内存的对象要被回收的时候，清除堆外内存

