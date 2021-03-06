## Path

Path 类似于IO 中的 File，用来表示某个文件、文件夹，也可以用来当作文件路径，支持路径相加，替换 等等操作，Path表示一个分层的路径，由一个特殊分隔符或分隔符分隔的目录和文件名元素序列组成。 也可以存在标识文件系统层次结构的根组件 。 距离目录层次结构根目录最远的名称元素是文件或目录的名称。 其他名称元素是目录名称。 A Path可以表示根，根和序列的名称，或简单的一个或多个名称元素。 如果Path仅由一个空的名称元素组成，则认为是空的路径 。 使用空路径访问文件等同于访问文件系统的默认目录。 Path定义getFileName ， getParent ， getRoot和subpath方法来访问路径部件或它的名称元素的子序列。



除了访问路径的组件之外， Path还定义了组合路径的resolve和resolveSibling方法。 可以用于构建两个路径之间的相对路径的relativize方法。 路径可以是compared ，并使用startsWith和endsWith方法相互测试。

此接口扩展了Watchable接口，使得路径位于的目录可以是registered ，带有一个WatchService和目录中的条目。

```
Path path = Paths.get("/data/logs/web.log");  
//属性  
//获取路径中的文件名或者最后一个节点元素  
System.out.printf("FileName:%s%n", path.getFileName());  
//路径节点元素的格式  
System.out.printf("NameCount:%s%n", path.getNameCount());  
//遍历路径节点方法1  
Iterator<Path> names = path.iterator();  
int i = 0;  
while (names.hasNext()) {  
    Path name = names.next();  
    System.out.printf("Name %s:%s%n",i,name.toString());  
    i++;  
}  
//方法2  
for(int j = 0; j < path.getNameCount(); j++) {  
    System.out.printf("Name %s:%s%n",j,path.getName(j));  
}  
//父路径  
System.out.printf("Parent:%s%n",path.getParent());  
//跟路径,比如"/"、"C:";如果是相对路径，则返回null。  
System.out.printf("Root:%s%n",path.getRoot());  
//子路径，结果中不包含root，前开后闭  
System.out.printf("Subpath[0,2]:%s%n",path.subpath(0,2));
```



 **互操作性** 

与默认provider相关联的路径通常可以与java.io.File类进行互操作。 由其他提供商创建的路径不可能与由java.io.File表示的抽象路径名称互java.io.File 。 该toPath方法可以被用于获得Path从由A表示的抽象路径名java.io.File对象。 所产生的Path可以用于与java.io.File对象相同的文件操作。 此外， toFile方法是有用的构建File从String一个的表示Path 

**并发性**

该接口的实现是不可变且安全的，可供多个并行线程使用。



**创建 Path**

 [java]

```
Path = path = Paths.get("c:\\data\\myfile.txt");
```

也可以通过 File 直接转换为 Path

 [java]

```
Path path = file.toPath();
```



**文件路径**

- 绝对路径

 [java]

```
Path path = Paths.get("/home/jakobjenkov/myfile.txt");
```

- 相对路径

创建一个相对路径可以通过调用 `Path.get(basePath, relativePath)`,

下面是一个示例：

 [java]

```
Path file = Paths.get("d:\\data", "projects\\a-project\\myfile.txt");
```

- 当前路径

 [java]

```
Path currentDir = Paths.get(".");
```

- 父级路径

 [java]

```
Path parentDir = Paths.get("..");
```



**API 方法：**

类型和参数	方法和描述
int	compareTo(Path other)
比较两个抽象的路径词典。

boolean	endsWith(Path other)
测试此路径是否以给定的路径结束。

boolean	endsWith(String other)
测试此路径是否以 Path结束，通过转换给定的路径字符串，完全按照 endsWith(Path)方法指定的方式构建。

boolean	equals(Object other)
测试此路径与给定对象的相等性。

Path	getFileName()
将此路径表示的文件或目录的名称返回为 Path对象。

FileSystem	getFileSystem()
返回创建此对象的文件系统。

Path	getName(int index)
返回此路径的名称元素作为 Path对象。

int	getNameCount()
返回路径中的名称元素的数量。

Path	getParent()
返回 父路径 ，或 null如果此路径没有父。

Path	getRoot()
返回此路径的根组分作为 Path对象，或 null如果该路径不具有根组件。

int	hashCode()
计算此路径的哈希码。

boolean	isAbsolute()
告诉这条路是否是绝对的。

Iterator<Path>	iterator()
返回此路径的名称元素的迭代器。

Path	normalize()
返回一个路径，该路径是冗余名称元素的消除。

WatchKey	register(WatchService watcher, WatchEvent.Kind<?>... events)
使用手表服务注册此路径所在的文件。

WatchKey	register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers)
使用手表服务注册此路径所在的文件。

Path	relativize(Path other)
构造此路径和给定路径之间的相对路径。

Path	resolve(Path other)
根据这条路径解决给定的路径。

Path	resolve(String other)
一个给定的路径字符串转换为 Path并解析它针对此 Path在完全按规定的方式 resolve方法。

Path	resolveSibling(Path other)
根据此路径的 parent路径解决给定的路径。

Path	resolveSibling(String other)
将给定的路径字符串转换为 Path ，并按照 resolveSibling方法指定的方式将其解析为该路径的 parent路径。

boolean	startsWith(Path other)
测试此路径是否以给定的路径开始。

boolean	startsWith(String other)
测试此路径是否以 Path ，通过转换给定的路径字符串，按照 startsWith(Path)方法指定的方式构建。

Path	subpath(int beginIndex, int endIndex)
返回一个相对的 Path ，它是该路径的名称元素的子序列。

Path	toAbsolutePath()
返回表示此路径的绝对路径的 Path对象。

File	toFile()
返回表示此路径的File对象。

Path	toRealPath(LinkOption... options)
返回现有文件的 真实路径。

String	toString()
返回此路径的字符串表示形式。

URI	toUri()
返回一个URI来表示此路径。 



## Paths

 此类仅由静态方法组成，通过转换路径字符串返回[`Path`](https://blog.csdn.net/java/nio/file/Path.html)或[`URI`](https://blog.csdn.net/java/net/URI.html)  

### API

类型和参数	方法和描述
static Path	get(String first, String... more)
将路径字符串或连接到路径字符串的字符串序列转换为 Path 。

static Path	get(URI uri)
将给定的URI转换为Path对象。



## Files

 该类只包含对文件，目录或其他类型文件进行操作的静态方法。 

 在大多数情况下，这里定义的方法将委托给相关的文件系统提供程序来执行文件操作。 



###  Files 常用的方法：

- Files.exists()

  Files.exits() 方法用来检查给定的Path在文件系统中是否存在。

  ```java
  Path path = Paths.get("data/logging.properties");
  boolean pathExists = Files.exists(path, new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
  ```

  注意Files.exists()的的第二个参数。

  他是一个数组，这个参数直接影响到Files.exists()如何确定一个路径是否存在。

  在本例中，这个数组内包含了LinkOptions.NOFOLLOW_LINKS，表示检测时不包含符号链接文件。



- Files.createDirectory()

   Files.createDirectory()会创建Path表示的路径，下面是一个示例： 

  ```java
  Path path = Paths.get("data/subdir");
  
  try {
      Path newDir = Files.createDirectory(path);
  } catch(FileAlreadyExistsException e){
      // the directory already exists.
  } catch (IOException e) {
      //something else went wrong
      e.printStackTrace();
  }
  ```



- Files.copy()

   Files.copy()方法可以吧一个文件从一个地址复制到另一个位置。 

  ```java
  Path sourcePath      = Paths.get("data/logging.properties");
  Path destinationPath = Paths.get("data/logging-copy.properties");
  
  try {
      Files.copy(sourcePath, destinationPath);
  } catch(FileAlreadyExistsException e) {
      //destination file already exists
  } catch (IOException e) {
      //something else went wrong
      e.printStackTrace();
  }
  ```

  

    覆盖已经存在的文件 

  ```java
  //copy 操作可以强制覆盖已经存在的目标文件。
  Path sourcePath      = Paths.get("data/logging.properties");
  Path destinationPath = Paths.get("data/logging-copy.properties");
  
  try {
      Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
  } catch(FileAlreadyExistsException e) {
      //destination file already exists
  } catch (IOException e) {
      //something else went wrong
      e.printStackTrace();
  }
  ```

  

  

- Files.move()

   Java NIO的Files类也包含了移动的文件的接口。 

   移动文件和重命名是一样的，但是还会改变文件的目录位置。 

   java.io.File类中的renameTo()方法与之功能是一样的。 

  ```java
  Path sourcePath      = Paths.get("data/logging-copy.properties");
  Path destinationPath = Paths.get("data/subdir/logging-moved.properties");
  
  try {
      Files.move(sourcePath, destinationPath,
              StandardCopyOption.REPLACE_EXISTING);
  } catch (IOException e) {
      //moving file failed.
      e.printStackTrace();
  }
  ```



- Files.delete()

   Files.delete()方法可以删除一个文件或目录： 

  ```java
  Path path = Paths.get("data/subdir/logging-moved.properties");
  
  try {
      Files.delete(path);
  } catch (IOException e) {
      //deleting file failed
      e.printStackTrace();
  }
  ```



- Files.walkFileTree()

  Files.walkFileTree()方法具有递归遍历目录的功能。

  walkFileTree接受一个Path和FileVisitor作为参数。

  Path对象是需要遍历的目录，FileVistor则会在每次遍历中被调用。

  

  FileVisitor需要调用方自行实现，然后作为参数传入walkFileTree().FileVisitor的每个方法会在遍历过程中被调用多次。

  如果不需要处理每个方法，那么可以继承他的默认实现类SimpleFileVisitor，它将所有的接口做了空实现。

  ```java
  Files.walkFileTree(path, new FileVisitor<Path>() {
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
      System.out.println("pre visit dir:" + dir);
      return FileVisitResult.CONTINUE;
    }
  
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      System.out.println("visit file: " + file);
      return FileVisitResult.CONTINUE;
    }
  
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
      System.out.println("visit file failed: " + file);
      return FileVisitResult.CONTINUE;
    }
  
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
      System.out.println("post visit directory: " + dir);
      return FileVisitResult.CONTINUE;
    }
  });
  ```

  

  递归删除文件

  Files.walkFileTree()也可以用来删除一个目录以及内部的所有文件和子目。

  Files.delete()只用用于删除一个空目录。

  我们通过遍历目录，然后在visitFile()接口中三次所有文件，最后在postVisitDirectory()内删除目录本身。

  ```java
  Path rootPath = Paths.get("data/to-delete");
  
  try {
    Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        System.out.println("delete file: " + file.toString());
        Files.delete(file);
        return FileVisitResult.CONTINUE;
      }
  
      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        Files.delete(dir);
        System.out.println("delete dir: " + dir.toString());
        return FileVisitResult.CONTINUE;
      }
    });
  } catch(IOException e){
    e.printStackTrace();
  }
  ```

  **注意：** 在遍历目录中文件的时候，如果该文件也是一个目录则还会调用 `Pre 与 Post `方法



### Files 全部方法列表

static long	copy(InputStream in, Path target, CopyOption... options)
将输入流中的所有字节复制到文件。

static long	copy(Path source, OutputStream out)
将文件中的所有字节复制到输出流。

static Path	copy(Path source, Path target, CopyOption... options)
将文件复制到目标文件。

static Path	createDirectories(Path dir, FileAttribute<?>... attrs)
首先创建所有不存在的父目录来创建目录。

static Path	createDirectory(Path dir, FileAttribute<?>... attrs)
创建一个新的目录。

static Path	createFile(Path path, FileAttribute<?>... attrs)
创建一个新的和空的文件，如果该文件已存在失败。

static Path	createLink(Path link, Path existing)
为现有文件创建新的链接（目录条目） （可选操作） 。

static Path	createSymbolicLink(Path link, Path target, FileAttribute<?>... attrs)
创建到目标的符号链接 （可选操作） 。

static Path	createTempDirectory(Path dir, String prefix, FileAttribute<?>... attrs)
在指定的目录中创建一个新目录，使用给定的前缀生成其名称。

static Path	createTempDirectory(String prefix, FileAttribute<?>... attrs)
在默认临时文件目录中创建一个新目录，使用给定的前缀生成其名称。

static Path	createTempFile(Path dir, String prefix, String suffix, FileAttribute<?>... attrs)
在指定的目录中创建一个新的空文件，使用给定的前缀和后缀字符串生成其名称。

static Path	createTempFile(String prefix, String suffix, FileAttribute<?>... attrs)
在默认临时文件目录中创建一个空文件，使用给定的前缀和后缀生成其名称。

static void	delete(Path path)
删除文件。

static boolean	deleteIfExists(Path path)
删除文件（如果存在）。

static boolean	exists(Path path, LinkOption... options)
测试文件是否存在。

static Stream<Path>	find(Path start, int maxDepth, BiPredicate<Path,BasicFileAttributes> matcher, FileVisitOption... options)
返回一个 Stream ，它通过搜索基于给定起始文件的文件树中的文件来懒惰地填充 Path 。

static Object	getAttribute(Path path, String attribute, LinkOption... options)
读取文件属性的值。

static <V extends FileAttributeView>
V	getFileAttributeView(Path path, 类<V> type, LinkOption... options)
返回给定类型的文件属性视图。

static FileStore	getFileStore(Path path)
返回表示文件所在文件存储区的 FileStore。

static FileTime	getLastModifiedTime(Path path, LinkOption... options)
返回文件的上次修改时间。

static UserPrincipal	getOwner(Path path, LinkOption... options)
返回文件的所有者。

static Set<PosixFilePermission>	getPosixFilePermissions(Path path, LinkOption... options)
返回文件的POSIX文件权限。

static boolean	isDirectory(Path path, LinkOption... options)
测试文件是否是目录。

static boolean	isExecutable(Path path)
测试文件是否可执行。

static boolean	isHidden(Path path)
告知文件是否被 隐藏 。

static boolean	isReadable(Path path)
测试文件是否可读。

static boolean	isRegularFile(Path path, LinkOption... options)
测试文件是否是具有不透明内容的常规文件。

static boolean	isSameFile(Path path, Path path2)
测试两个路径是否找到相同的文件。

static boolean	isSymbolicLink(Path path)
测试文件是否是符号链接。

static boolean	isWritable(Path path)
测试文件是否可写。

static Stream<String>	lines(Path path)
从 Stream读取文件中的所有行。

static Stream<String>	lines(Path path, Charset cs)
从文件中读取所有行作为 Stream 。

static Stream<Path>	list(Path dir)
返回一个懒惰的填充 Stream ，其元素是 Stream中的条目。

static Path	move(Path source, Path target, CopyOption... options)
将文件移动或重命名为目标文件。

static BufferedReader	newBufferedReader(Path path)
打开一个文件进行阅读，返回一个 BufferedReader以高效的方式从文件读取文本。

static BufferedReader	newBufferedReader(Path path, Charset cs)
打开一个文件进行阅读，返回一个 BufferedReader ，可以用来以有效的方式从文件读取文本。

static BufferedWriter	newBufferedWriter(Path path, Charset cs, OpenOption... options)
打开或创建一个写入文件，返回一个 BufferedWriter ，可以用来以有效的方式将文本写入文件。

static BufferedWriter	newBufferedWriter(Path path, OpenOption... options)
打开或创建一个写入文件，返回一个 BufferedWriter以高效的方式写入文件。

static SeekableByteChannel	newByteChannel(Path path, OpenOption... options)
打开或创建文件，返回可访问的字节通道以访问该文件。

static SeekableByteChannel	newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs)
打开或创建文件，返回可访问的字节通道以访问该文件。

static DirectoryStream<Path>	newDirectoryStream(Path dir)
打开一个目录，返回一个DirectoryStream以遍历目录中的所有条目。

static DirectoryStream<Path>	newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter)
打开一个目录，返回一个DirectoryStream来迭代目录中的条目。

static DirectoryStream<Path>	newDirectoryStream(Path dir, String glob)
打开一个目录，返回一个DirectoryStream来迭代目录中的条目。

static InputStream	newInputStream(Path path, OpenOption... options)
打开一个文件，返回输入流以从文件中读取。

static OutputStream	newOutputStream(Path path, OpenOption... options)
打开或创建文件，返回可用于向文件写入字节的输出流。

static boolean	notExists(Path path, LinkOption... options)
测试此路径所在的文件是否不存在。

static String	probeContentType(Path path)
探测文件的内容类型。

static byte[]	readAllBytes(Path path)
读取文件中的所有字节。

static List<String>	readAllLines(Path path)
从文件中读取所有行。

static List<String>	readAllLines(Path path, Charset cs)
从文件中读取所有行。

static <A extends BasicFileAttributes>
A	readAttributes(Path path, 类<A> type, LinkOption... options)
读取文件的属性作为批量操作。

static Map<String,Object>	readAttributes(Path path, String attributes, LinkOption... options)
读取一组文件属性作为批量操作。

static Path	readSymbolicLink(Path link)
读取符号链接的目标 （可选操作） 。

static Path	setAttribute(Path path, String attribute, Object value, LinkOption... options)
设置文件属性的值。

static Path	setLastModifiedTime(Path path, FileTime time)
更新文件上次修改的时间属性。

static Path	setOwner(Path path, UserPrincipal owner)
更新文件所有者。

static Path	setPosixFilePermissions(Path path, Set<PosixFilePermission> perms)
设置文件的POSIX权限。

static long	size(Path path)
返回文件的大小（以字节为单位）。

static Stream<Path>	walk(Path start, FileVisitOption... options)
返回一个 Stream ，它通过 Path根据给定的起始文件的文件树懒惰地填充 Path 。

static Stream<Path>	walk(Path start, int maxDepth, FileVisitOption... options)
返回一个 Stream ，它是通过走根据给定的起始文件的文件树懒惰地填充 Path 。

static Path	walkFileTree(Path start, FileVisitor<? super Path> visitor)
走一个文件树。

static Path	walkFileTree(Path start, Set<FileVisitOption> options, int maxDepth, FileVisitor<? super Path> visitor)
走一个文件树。

static Path	write(Path path, byte[] bytes, OpenOption... options)
将字节写入文件。

static Path	write(Path path, Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options)
将文本行写入文件。

static Path	write(Path path, Iterable<? extends CharSequence> lines, OpenOption... options)
将文本行写入文件。



## NIO 拓展内容

DirectoryStream、DirectoryStream.Filter, AccessMode、FileStore、FileVisitor、FileVisitResult、LinkOption、PathMatcher、StandardCopyOption、StandardOpenOption、StandardWatchEventKinds



DirectoryStream： 返回一个文件夹流，Files.newDirectoryStream(Path)

DirectoryStream.Filter： 返回一个文件夹流时，过滤掉不需要的文件，Files.newDirectoryStream(Path，Filter)

AccessMode：表示当前文件的可执行权限（READ,WRITE,EXECUTE）

```
Files.isReadable()、isWritexxx、isExecutexxx

FileSystemProvider.checkAccess(Path path)
```

FileStore: 表示存储文件的设备

```
Files.get("./test") 

输出：
新加卷 (D:)
```



FileVisitor： 用来遍历文件, FileVisitor的每个方法会在遍历过程中被调用多次。 



FileVistorResult ：  返回这个枚举值可以让调用方决定文件遍历是否需要继续。 

```
CONTINE 表示文件遍历和正常情况下一样继续。

TERMINATE 表示文件访问需要终止。

SKIP_SIBLINGS 表示文件访问继续，但是不需要访问其他同级文件或目录。

SKIP_SUBTREE 表示继续访问，但是不需要访问该目录下的子目录。这个枚举值仅在preVisitDirectory()中返回才有效。如果在另外几个方法中返回，那么会被理解为CONTINE。
```





LinkOption： 枚举选项

```
NOFOLLOW_LINKS： 表示非连接文件
```



PathMather : 匹配文件，过滤文件

```java
Files#newDirectoryStream(Path,String) //使用到了PathMatcher 进行过滤
FileSystem#getPathMatcher()
```



StandardCopyOption: 在copy文件的时候，需要的选项

```
/**
     * Replace an existing file if it exists.
     */
    REPLACE_EXISTING,
    /**
     * Copy attributes to the new file.
     */
    COPY_ATTRIBUTES,
    /**
     * Move the file as an atomic file system operation.
     */
    ATOMIC_MOVE;
```



StandardOpenOption: 打开文件的时候的选项



![image-20200615175921395](assets/image-20200615175921395.png)