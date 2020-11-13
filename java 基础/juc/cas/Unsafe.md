

## Unsafe

> Unsafe是实现CAS的核心类，Java无法直接访问底层操作系统，而是通过本地（native）方法来访问。Unsafe类提供了硬件级别的原子操作。

### Unsafe函数列表

```
///--------------------- peek and poke 指令--------------
//获取对象o中给定偏移地址(offset)的值。以下相关get方法作用相同
public native int getInt(Object o, long offset);
//在对象o的给定偏移地址存储数值x。以下set方法作用相同
public native void putInt(Object o, long offset, int x);
public native Object getObject(Object o, long offset);
public native void putObject(Object o, long offset, Object x);
/**篇幅原因，省略其他类型方法 */
//从给定内存地址获取一个byte。下同
public native byte    getByte(long address);
//在给定内存地址放置一个x。下同
public native void    putByte(long address, byte x);
/**篇幅原因，省略其他类型方法*/
//获取给定内存地址的一个本地指针
public native long getAddress(long address);
//在给定的内存地址处存放一个本地指针x
public native void putAddress(long address, long x);

///------------------内存操作----------------------
//在本地内存分配一块指定大小的新内存，内存的内容未初始化;它们通常被当做垃圾回收。
public native long allocateMemory(long bytes);
//重新分配给定内存地址的本地内存
public native long reallocateMemory(long address, long bytes);
//将给定内存块中的所有字节设置为固定值（通常是0）
public native void setMemory(Object o, long offset, long bytes, byte value);
//复制一块内存，double-register模型
public native void copyMemory(Object srcBase, long srcOffset,
                              Object destBase, long destOffset,
                              long bytes);
//复制一块内存，single-register模型
public void copyMemory(long srcAddress, long destAddress, long bytes) {
    copyMemory(null, srcAddress, null, destAddress, bytes);
}
//释放给定地址的内存
public native void freeMemory(long address);
//获取给定对象的偏移地址
public native long staticFieldOffset(Field f);
public native long objectFieldOffset(Field f);

//------------------数组操作---------------------------------
//获取给定数组的第一个元素的偏移地址
public native int arrayBaseOffset(Class<?> arrayClass);
//获取给定数组的元素增量地址，也就是说每个元素的占位数
public native int arrayIndexScale(Class<?> arrayClass);

//------------------------------------------------------------
//告诉虚拟机去定义一个类。默认情况下，类加载器和保护域都来自这个方法
public native Class<?> defineClass(String name, byte[] b, int off, int len,
                                   ClassLoader loader,
                                   ProtectionDomain protectionDomain);
//定义匿名内部类
public native Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches);
//定位一个实例，但不运行构造函数
public native Object allocateInstance(Class<?> cls) throws InstantiationException;

///--------------------锁指令（synchronized）-------------------------------
//对象加锁
public native void monitorEnter(Object o);
//对象解锁
public native void monitorExit(Object o);
public native boolean tryMonitorEnter(Object o);
//解除给定线程的阻塞
public native void unpark(Object thread);
//阻塞当前线程
public native void park(boolean isAbsolute, long time);

// CAS
public final native boolean compareAndSwapObject(Object o, long offset,
                                                 Object expected,
                                                 Object x);
//获取对象o的给定偏移地址的引用值（volatile方式）
public native Object getObjectVolatile(Object o, long offset);
public native void    putObjectVolatile(Object o, long offset, Object x);
/** 省略其他类型方法  */


//用于lazySet，适用于低延迟代码。
public native void    putOrderedObject(Object o, long offset, Object x);
/** 省略其他类型方法  */
//获取并加上给定delta，返回加之前的值
public final int getAndAddInt(Object o, long offset, int delta)
/** 省略其他类型方法  */
//为给定偏移地址设置一个新的值，返回设置之前的值
public final int getAndSetInt(Object o, long offset, int newValue)
/** 省略其他类型方法  */

///--------------------1.8新增指令-----------------------
// loadFence() 表示该方法之前的所有load操作在内存屏障之前完成
public native void loadFence();
//表示该方法之前的所有store操作在内存屏障之前完成
public native void storeFence();
//表示该方法之前的所有load、store操作在内存屏障之前完成，这个相当于上面两个的合体功能
public native void fullFence();
```

Unsafe的方法比较简单，直接看方法字面意思就大概知道方法的作用。
 在Unsafe里有两个方法模型：
 **double-register模型**：给定对象，给定偏移地址offset。从给定对象的偏移地址取值。如`getInt(Object o, long offset)`；
 **single-register模型**：给定内存地址，直接从给定内存地址取值，如`getInt(long)`。

这里介绍一下几个比较重要的方法，在之后的源码阅读里会用到。

1.  `arrayBaseOffset`：操作数组，用于获取数组的第一个元素的偏移地址
2.  `arrayIndexScale`：操作数组，用于获取数组元素的增量地址，也就是说每个元素的占位数。打个栗子：如果有一个数组{1,2,3,4,5,6}，它第一个元素的偏移地址为16，每个元素的占位是4，如果我们要获取数组中“5”这个数字，那么它的偏移地址就是16+4*4。
3.  `putOrderedObject`：putOrderedObject 是 lazySet 的实现，适用于低延迟代码。它能够实现非堵塞写入，避免指令重排序，这样它使用快速的存储-存储(store-store) barrier,而不是较慢的存储-加载(store-load) barrier, 后者多是用在volatile的写操作上。但这种性能提升也是有代价的，也就是写后结果并不会被其他线程（甚至是自己的线程）看到，通常是几纳秒后被其他线程看到。类似的方法还有`putOrderedInt、putOrderedLong`。
4.  `loadFence`、`storeFence`、`fullFence`：这三个方法是1.8新增，主要针对内存屏障定义，也是为了避免重排序：

- loadFence() 表示该方法之前的所有load操作在内存屏障之前完成。
- storeFence()表示该方法之前的所有store操作在内存屏障之前完成。
- fullFence()表示该方法之前的所有load、store操作在内存屏障之前完成。

 

 

 

https://www.jianshu.com/p/a897c4b8929f