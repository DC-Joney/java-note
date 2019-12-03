## Files

guava的 Flies 对应的是java的 Files，与 apache的 common-io 做对比

```
Files.asCharSource(File,Charset) 转化为CharSource

Files.asCharSink(new File(""), Charsets.UTF_8, FileWriteMode.APPEND); 转为CharSink

Files.asByteSource(File file)
Files.asByteSink(File,WriteMode)
```



## Closer

```
将 需要关闭的流放入Closer，由Closer管理

Closer closer = Closer.create();
CharSink charSink = Files.asCharSink(new File(""), Charsets.UTF_8, FileWriteMode.APPEND);
try{
    InputStream input = closer.register(charSink.openBufferedStream())
}catch(Exception e){
    
}finally{
	//将注册的流全部关闭
    close.close();
}

防止压制的异常无法捕捉
```





##Stream

ByteSource 和ByteSink 主要是针对字节流，CharSource 和 CharSink主要针对的是字符流



### ByteStreams（主要针对字节）

```
ByteSource ---> InputStream

ByteSink ---> OutputStream
limit(InputStream,long) 只读取limit范围内的字节
```





### ByteSource

```
slice(start,offset) 从start位置开始，取offset个元素（切片）
```







### ByteSink

```
write(byte[]data) 写入数据
```









### BaseEncoding













### CharStreams(主要针对字符)

```
CharSource ---> Reader

CharSink ---> Writer


copy(Readable,Appendable)
toString(Readable)
readLines(Readable)
readLies(Readable,LineProcessor<T>)
exhaust() 把Readable中的 数据消耗光
asWriter(Appendable) 
```



### CharSource

从文件读取数据流

```
asByteSource 将字符流转为字节流
openStream 打开一个流作为输入流
openBufferedStream 作为带缓冲区的字符输入流
Stream<String> lines 将所有字符串按照行切割转为Stream流 
lengthIfKnown
length
countBySkipping
copyTo(Appendable appendable)
copyTo(CharSink sink)
read() 读取所有字符串
readFirstLine() 读取第一行
readLines() 读取所有行数据
readLines（LineProcessor<T> processor） 对每行数据做处理
forEachLine(Consumer<? super String> action)
isEmpty() 是否为空
CharSource concat(Iterable<? extends CharSource> sources)
CharSource concat(Iterator<? extends CharSource> sources)
CharSource concat(CharSource... sources)
CharSource wrap(CharSequence charSequence) 包装一个字符串作为输入流
CharSource empty() 包装一个空的输入流
```





### CharSink

作为输出流，输出到哪里

```
openStream 作为一个Writer
openBufferStream
writeLines（CharSequence）
writeLines(Iterable<? extends CharSequence> lines)
writeLines(Iterable<? extends CharSequence> lines, String lineSeparator)
writeLines(Stream<? extends CharSequence> lines)
writeLines(Stream<? extends CharSequence> lines, String lineSeparator)
writeLines(Iterator<? extends CharSequence> lines, String lineSeparator)
writeFrom(Readable readable)
```

