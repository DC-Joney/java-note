# NIO 补充

## 1、FileChannel 的写入、读取

首先这里要声明一点就是，不管是FileChannel的写入 还是读取 都需要借助堆外内存来存取数据，比如通过FileChannel写入数据，会首先判断ByteBuffer是否为堆外内存，如果不是堆外内存，则申请一个临时的堆外内存，然后将数据拷贝到堆外，再写入到文件中，为什么要这么做呢？ 原因如下：

**java底层写入或者读取数据都是通过调用系统底层函数实现**

读取：read、pread, readv

写入：write、pwrite, writev

**Linux提供的pread或者read系统调用，只能操作一块固定的内存区域。这就意味着只能对direct memory进行操作**（heap memory中的对象在经历gc后内存地址会发生改变，如果一定要直接写入heap memory，就必须要将这个对象pin住，但是hotspot不提供单个对象层面的object pinning，一定要pin的话就只能暂时禁用gc了，也就是把整个Java堆都给pin住，这显然代价太高了。）所以heap buffer要么直接在上层就转成direct buffer，要么像前面介绍的FileInputStream.read中那样在jvm层面申请一个临时byte数组再调用read方法，然后将临时byte数组里的数组写入到heap buffer里。出于统一逻辑的角度考虑，当然是直接申请一个临时direct buffer对象的好。





## 2、对比 java io 与 nio

Java io 与 nio不同的地方主要在于以下几点：

### API 增强

Java io 的API 都是基于 InputStream 与 OutputStream 两个抽象父类实现，通过装饰模式对底层API 做增强，比如底层实现为FileInputStream，上层API 则是通过装饰增强的比如BufferdInputStream等等

java nio 则是通过Channel、Buffer 等API 来实现文件IO 操作 （这里暂时忽略网络IO），比如 nio 针对堆外内存、零拷贝以及内存隐射等增强



### 读取、写入数据

根据 上述所说的 在read或者write过程内存地址不能移动的限制，io 与 nio处理的方式也不一样

#### java io 读取数据

java io 是在jvm内部通过malloc开辟一块新的堆外内存，然后将文件中的数据读取或者写入到该内存，然后再写入到文件或者读取到堆内存中。实现流程如下：

1、先申请一段byte数组作为缓冲区，然后调用FileInputStream.read方法把文件里的数据灌到缓冲区中，代码如下所示

```java
        FileInputStream reader = new FileInputStream(fileName);
        byte[] buf = new byte[1024 * 64];//在heap memory中开辟一块缓冲区
        while (reader.read(buf) != -1) {//调用FileInputStream.read()方法遍历文件
        }    
```

FileInputStream.read方法会直接调用到FileInputStream.readBytes()方法，这是一个native方法，具体实现位于[src/share/native/java/io/FileInputStream.c](http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/native/java/io/FileInputStream.c#l70)中

```c
JNIEXPORT jint JNICALL
Java_java_io_FileInputStream_readBytes(JNIEnv *env, jobject this,
        jbyteArray bytes, jint off, jint len) {
    return readBytes(env, this, bytes, off, len, fis_fd);
}
```

它调用了[src/share/native/java/io/io_util.c](http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/native/java/io/io_util.c#l73)的readBytes方法：

```c
jint
readBytes(JNIEnv *env, jobject this, jbyteArray bytes,
          jint off, jint len, jfieldID fid)
{
    jint nread;
    char stackBuf[BUF_SIZE];
    char *buf = NULL;
    FD fd;

    if (IS_NULL(bytes)) {
        JNU_ThrowNullPointerException(env, NULL);
        return -1;
    }

    if (outOfBounds(env, off, len, bytes)) {
        JNU_ThrowByName(env, "java/lang/IndexOutOfBoundsException", NULL);
        return -1;
    }

    if (len == 0) {
        return 0;
    } else if (len > BUF_SIZE) {
        buf = malloc(len);//申请一个与传入buf等长的char数组
        if (buf == NULL) {
            JNU_ThrowOutOfMemoryError(env, NULL);
            return 0;
        }
    } else {
        buf = stackBuf;
    }

    //前面的代码全是检查参数
    fd = GET_FD(this, fid);//获取打开文件的fd
    if (fd == -1) {
        JNU_ThrowIOException(env, "Stream Closed");
        nread = -1;
    } else {
        nread = IO_Read(fd, buf, len);//调用IO_Read方法从fd中读取数据
        if (nread > 0) {
            (*env)->SetByteArrayRegion(env, bytes, off, nread, (jbyte *)buf);//将装有数据的char数组里的数据复制到buf中
        } else if (nread == -1) {
            JNU_ThrowIOExceptionWithLastError(env, "Read error");
        } else { /* EOF */
            nread = -1;
        }
    }

    if (buf != stackBuf) {
        free(buf);//释放临时申请的char数组
    }
    return nread;
}
```

**以上代码解读如下：**

1、先申请一个等长的char数组（系统底层调用无法直接操作从Java应用层面传入的jbyteArray，只能先将数据读取到临时申请的char数组中，再复制到jbyteArray里）

2、然后调用一个叫做IO_Read的方法从指定的fd里读取数据

3、这个IO_Read实际上只是一个宏，其定义根据平台有所不同

<br/>

**Windows中的定义位于[src/windows/native/java/io/io_util_md.h](http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/windows/native/java/io/io_util_md.h#l82)中，对应于[handleRead](http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/windows/native/java/io/io_util_md.c#l456)方法**

其源码如下所示：

```C
JNIEXPORT
jint
handleRead(FD fd, void *buf, jint len)
{
    DWORD read = 0;
    BOOL result = 0;
    HANDLE h = (HANDLE)fd;
    if (h == INVALID_HANDLE_VALUE) {
        return -1;
    }
    result = ReadFile(h,          /* File handle to read */
                      buf,        /* address to put data */
                      len,        /* number of bytes to read */
                      &read,      /* number of bytes read */
                      NULL);      /* no overlapped struct */
    if (result == 0) {
        int error = GetLastError();
        if (error == ERROR_BROKEN_PIPE) {
            return 0; /* EOF */
        }
        return -1;
    }
    return (jint)read;
}
```

直接调用Windows平台提供的[ReadFile](https://msdn.microsoft.com/en-us/library/windows/desktop/aa365467(v=vs.85).aspx)函数完成文件的读取

Linux中的定义位于[src/solaris/native/java/io/io_util_md.h](http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/solaris/native/java/io/io_util_md.h#l70)中，也对应于[handleRead](http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/solaris/native/java/io/io_util_md.c#l152)方法：

```C
ssize_t
handleRead(FD fd, void *buf, jint len)
{
    ssize_t result;
    RESTARTABLE(read(fd, buf, len), result);
    return result;
}
```

其中RESTARTABLE是一个简单的宏，用于发生中断时的重试读取

```c
/*
 * Retry the operation if it is interrupted
 */
#define RESTARTABLE(_cmd, _result) do { \
    do { \
        _result = _cmd; \
    } while((_result == -1) && (errno == EINTR)); \
} while(0)
```

最终还是调用[read这个系统调用](https://linux.die.net/man/2/read)。

再往下就是Linux的内核实现，超出本文的范围了。

现在我们可以对FileInputStream.read方法的实现做一个总结：

1. 调用native的FileInputStream.readBytes()方法，参数中带有一个byte数组作为读缓冲区

2. 通过jni将这个heap memory中的byte数组的指针传递给jvm

3. jvm开辟一个等长的char类型数组，然后调用IO_Read宏从指定的fd中读取数据将其填满

4. Windows系统中IO_Read的实现是ReadFile，Linux系统中的实现则是read系统调用

5. 调用SetByteArrayRegion方法将step2中的char数组里的数据写到step1中传入的byte数组里，在Java应用的层面来看，此时FileInputStream.read已经读到数据了

6. 释放step2中开辟的临时数组并返回



#### java nio 读取写入数据

java nio 则是在java层面 实现的内存赋值，这里以从文件读取数据为例，首先判断传入的ByteBuffer 是否是DirectByteBuffer 类型，如果不是DirectByteBuffer 类型，则会申请一块临时堆外内存用于读取数据，然后将文件中的数据读取到这块临时的堆外内存中，接着在复制到对内，下面我们通过源码来梳理读取的整个流程是。实现流程如下：

1、FileChannelImpl#read(ByteBuffer dst)，调用FileChannel 的read方法进行数据读取

```java
     do {
					//通过IOUtils.read 读取文件数据      	
          var3 = IOUtil.read(this.fd, var1, -1L, this.nd);
      } while(var3 == -3 && this.isOpen());
```



2、IOUtil#read(FileDescriptor , ByteBuffer , long , NativeDispatcher )

```java
static int read(FileDescriptor var0, ByteBuffer var1, long var2, NativeDispatcher var4) throws IOException {
        
  if (var1.isReadOnly()) {
            throw new IllegalArgumentException("Read-only buffer");
    
   }
  //如果 buffer 类型为nativeBuffer（既堆外内存，则直接进行读取）
  else if (var1 instanceof DirectBuffer) {
            return readIntoNativeBuffer(var0, var1, var2, var4);
  } else {
    				//开辟一块新的临时堆外内存
            ByteBuffer var5 = Util.getTemporaryDirectBuffer(var1.remaining());

            int var7;
            try {
               //将文件中的数据读取到申请的临时内存中
                int var6 = readIntoNativeBuffer(var0, var5, var2, var4);
                var5.flip();
                if (var6 > 0) {
                    //将堆外内存的数据复制到堆内存
                    var1.put(var5);
                }

                var7 = var6;
            } finally {
                //释放堆外内存
                Util.offerFirstTemporaryDirectBuffer(var5);
            }

            return var7;
        }
    }
```



3、IOUtils.readIntoNativeBuffer ，将文件描述符中的数据读取到对应的Buffer中, 但是读取的方式有些许不同，我们从下面代码中可以看到，不仅仅是调用了read方法进行数据读取，而且还调用了pread方法读取，它们的区别请见 **系统底层函数的区别**

```java
private static int readIntoNativeBuffer(FileDescriptor var0, ByteBuffer var1, long var2, NativeDispatcher var4) throws IOException {
    int var5 = var1.position();
    int var6 = var1.limit();

    assert var5 <= var6;

    int var7 = var5 <= var6 ? var6 - var5 : 0;
    if (var7 == 0) {
        return 0;
    } else {
        boolean var8 = false;
        int var9;
        if (var2 != -1L) {
          
            //调用NativeDispatcher.pread方法进行读取
            var9 = var4.pread(var0, ((DirectBuffer)var1).address() + (long)var5, var7, var2);
        } else {
          
          	//调用NativeDispatcher.read方法进行读取
            var9 = var4.read(var0, ((DirectBuffer)var1).address() + (long)var5, var7);
        }

        if (var9 > 0) {
            var1.position(var5 + var9);
        }

        return var9;
    }
}
```



