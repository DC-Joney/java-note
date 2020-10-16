## IDEA-HTTP工具

在intellij idea`2017.3`版本中，增加了一个类VS Code的`Rest-client`的工具，通过这个工具我们可以完成绝大部分http请求，是一个不错的`post-man`替代工具，接下来详细讲讲它如何简化你的操作。

### 请求示例

所有HTTP请求需要在后缀为`.http`的文件中进行，新建一个`test.http`文件。基本格式为：



```cpp
请求类型(如：GET， POST，PUT) +  请求地址（http://www.baidu.com）
请求头
...

请求体
```

写完请求后，点击左侧三角符号即可执行

![img](https:////upload-images.jianshu.io/upload_images/3432069-ff526fa552046778.png?imageMogr2/auto-orient/strip|imageView2/2/w/587/format/webp)

image.png

#### GET请求



```csharp
#### 一般GET请求
GET http://www.baidu.com?hi=hello
Accept: application/json
```



```ruby
### 带状态的GET请求
GET http://127.0.0.1:9085/api/item/list
Cookie: JessionId=TG4OKFVOZP6A9ML4
Authorization: Bearer TG4OKFVOZP6A9ML4
```

##### POST请求



```csharp
### 带body体的POST请求
POST http://127.0.0.1:9085/login
Content-Type: application/json

{
  "username":"zhangsan",
  "password":"123456"
}
```



```csharp
### 模仿form表单POST请求
POST http://127.0.0.1:9085/login
Content-Type: application/x-www-form-urlencoded

username=zhangsan&password=123
```



```kotlin
### POST请求上传多类型
POST http://127.0.0.1:9085/upload
Content-Type: multipart/form-data; boundary=WebAppBoundary

### text域
--WebAppBoundary
Content-Disposition: form-data; name="element-name"
Content-Type: text/plain

username=zhangsan

### json文件域
--WebAppBoundary
Content-Disposition: form-data; name="data"; filename="data.json"
Content-Type: application/json

< ./data.json
```

### 快捷键

同样有内置快捷键帮你简化这些操作，内置的快捷键需要版本`2018 +`，`2017.3`版本的同样也可以在`live templates`设置中手写快捷键，下面简单说下这几个快捷键。

-  `gtr`: 创建一个普通的`GET`请求，通过tab键一步步填写即可



```csharp
GET http://localhost:80/api/item
Accept: application/json

###
```

-  `gtrp`: 功能同`gtr`，只不过多了一个参数位置的tab



```csharp
GET http://localhost:80/api/item?id=99
Accept: application/json

###
```

-  `ptr`: 创建一个普通的`post`请求，内置了地址、请求头、json体的位置



```csharp
POST http://localhost:80/api/item
Content-Type: application/json

{}

###
```

-  `ptrp`: 形式同`ptr`，将json体的位置换成了`key-value`参数



```dart
POST http://localhost:80/api/item
Content-Type: application/x-www-form-urlencoded

id=99&content=new-element
```

-  `mptr`: 多类型post请求，内置：地址、请求头、一种请求类型（form-data）的位置



```csharp
POST http://localhost:80/api/item
Content-Type: multipart/form-data; boundary=WebAppBoundary

--WebAppBoundary
Content-Disposition: form-data; name="field-name"

field-value
--WebAppBoundary--
###
```

-  `fptr`: 上传文件post请求，样式同`mptr`，将请求类型的位置换成文件



```kotlin
POST http://localhost:80/api/item
Content-Type: multipart/form-data; boundary=WebAppBoundary

--WebAppBoundary
Content-Disposition: form-data; name="field-name" filename="file.txt"

< ./relative/path/to/local_file.txt
--WebAppBoundary--

###
```

### 变量

工具也有`post-man`变量机制，避免了多种环境下切换的麻烦。变量文件存放的文件名字为：`http-client.env.json`，隐私变量可以存放到`http-client.private.env.json`文件中，变量文件名为固定这两个，变量重复时以隐私变量为准。

变量定义格式如：



```json
{
  "product": {
    "host": "http://127.0.0.1:8675"
  },
  "test": {
    "host": "http://192.168.28.90:9023"
  }
}
```

使用时通过`{{var}}`引用即可：
 

![img](https:////upload-images.jianshu.io/upload_images/3432069-e77339dc686a0995.png?imageMogr2/auto-orient/strip|imageView2/2/w/315/format/webp)

2018-12-09 21-27-45 的屏幕截图.png



### 其他

同样`IDEA 2018 +`提供了示例库，示例库的位置在`http`文件的右上角，相信聪明的你们一定会找到的。与示例库图标并列的是你的请求的历史记录，也可以中`./idea/httpRequests`文件夹下找到历史记录与请求下载文件。

如果想看可视化操作，依次点击选项卡：`Tools` -> `Http client` -> `Test RESTful Web Service`