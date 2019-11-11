##HTTP协议

### http1.0和http1.1有什么区别？ 

```
HTTP 1.0主要有以下几点变化： 
请求和相应可以由于多行首部字段构成 
响应对象前面添加了一个响应状态行 
响应对象不局限于超文本 
服务器与客户端之间的连接在每次请求之后都会关闭 
实现了Expires等传输内容的缓存控制 
内容编码Accept-Encoding、字符集Accept-Charset等协商内容的支持 
这时候开始有了请求及返回首部的概念，开始传输不限于文本（其他二进制内容）

HTTP 1.1加入了很多重要的性能优化：持久连接、分块编码传输、字节范围请求、增强的缓存机制、传输编码及请求管道。
```





### 说说你知道的几种HTTP响应码，比如200, 302, 404？

- 200 OK //客户端请求成功
- 301 Moved Permanently //永久重定向,使用域名跳转
- 302 Found // 临时重定向,未登陆的用户访问用户中心重定向到登录页面
- 400 Bad Request //客户端请求有语法错误，不能被服务器所理解
- 401 Unauthorized //请求未经授权，这个状态代码必须和WWW-Authenticate报头域一起使用
- 403 Forbidden //服务器收到请求，但是拒绝提供服务
- 404 Not Found //请求资源不存在，eg：输入了错误的URL
- 500 Internal Server Error //服务器发生不可预期的错误
- 503 Server Unavailable //服务器当前不能处理客户端的请求，一段时间后可能恢复正常





### 如何避免浏览器缓存 ？

利用Http协议请求头的 Cache-Control来缓存静态资源

分一下几种情况

禁止进行缓存

缓存中不得存储任何关于客户端请求和服务端响应的内容。每次由客户端发起的请求都会下载完整的响应内容。

```
禁止进行缓存
Cache-Control: no-store


强制确认缓存
ache-Control: no-cache


私有缓存和公共缓存
"public" 指令表示该响应可以被任何中间人（译者注：比如中间代理、CDN等）缓存。若指定了"public"，则一些通常不被中间人缓存的页面（译者注：因为默认是private）（比如 带有HTTP验证信息（帐号密码）的页面 或 某些特定状态码的页面），将会被其缓存。

而 "private" 则表示该响应是专用于某单个用户的，中间人不能缓存此响应，该响应只能应用于浏览器私有缓存中。
Cache-Control: private
Cache-Control: public

缓存过期机制
Cache-Control: max-age=31536000

缓存验证确认
当使用了 "must-revalidate" 指令，那就意味着缓存在考虑使用一个陈旧的资源时，必须先验证它的状态，已过期的缓存将不被使用。详情看下文关于缓存校验的内容。
Cache-Control: must-revalidate
```



```
Expires 响应头包含日期/时间， 即在此时候之后，响应过期。
 
无效的日期，比如 0, 代表着过去的日期，即该资源已经过期。

如果在Cache-Control响应头设置了 "max-age" 或者 "s-max-age" 指令，那么 Expires 头会被忽略。
```



```
ETags
```



```
Pragma 
是一个在 HTTP/1.0 中规定的通用首部，这个首部的效果依赖于不同的实现，所以在“请求-响应”链中可能会有不同的效果。它用来向后兼容只支持 HTTP/1.0 协议的缓存服务器，那时候 HTTP/1.1 协议中的 Cache-Control 还没有出来
no-cache
与 Cache-Control: no-cache 效果一致。强制要求缓存服务器在返回缓存的版本之前将请求提交到源头服务器进行验证
```



###Http如何支持压缩？

```
Response:
Accept-Encoding: gzip
Accept-Encoding: compress
Accept-Encoding: deflate
Accept-Encoding: br
Accept-Encoding: identity
Accept-Encoding: *


Request:
Content-Encoding 是一个实体消息首部，用于对特定媒体类型的数据进行压缩。当这个首部出现的时候，它的值表示消息主体进行了何种方式的内容编码转换。这个消息首部用来告知客户端应该怎样解码才能获取在 Content-Type 中标示的媒体类型内容。

一般建议对数据尽可能地进行压缩，因此才有了这个消息首部的出现。不过对于特定类型的文件来说，比如jpeg图片文件，已经是进行过压缩的了。有时候再次进行额外的压缩无助于负载体积的减小，反而有可能会使其增大。

Content-Encoding: gzip
Content-Encoding: compress
Content-Encoding: deflate
Content-Encoding: identity
Content-Encoding: br

```



### 如何理解HTTP协议的无状态性 ?

无状态是指协议对于事务处理没有记忆能力，服务器不知道客户端是什么状态。即我们给服务器发送 HTTP 请求之后，服务器根据请求，会给我们发送数据过来，但是，发送完，不会记录任何信息。

HTTP 是一个无状态协议，这意味着每个请求都是独立的，Keep-Alive 没能改变这个结果。

缺少状态意味着如果后续处理需要前面的信息，则它必须重传，这样可能导致每次连接传送的数据量增大。另一方面，在服务器不需要先前信息时它的应答就较快。

HTTP 协议这种特性有优点也有缺点，优点在于解放了服务器，每一次请求“点到为止”不会造成不必要连接占用，缺点在于每次请求会传输大量重复的内容信息。

客户端与服务器进行动态交互的 Web 应用程序出现之后，HTTP 无状态的特性严重阻碍了这些应用程序的实现，毕竟交互是需要承前启后的，简单的购物车程序也要知道用户到底在之前选择了什么商品。于是，两种用于保持 HTTP 连接状态的技术就应运而生了，一个是 Cookie，而另一个则是 Session。

无状态是指，当浏览器发送请求给服务器的时候，服务器响应，但是同一个浏览器再发送请求给服务器的时候，他会响应，但是他不知道你就是刚才那个浏览器，简单地说，就是服务器不会去记得你，所以是无状态协议。 





### 简述Http请求get和post的区别以及数据包格式 ?

![è¿éåå¾çæè¿°](https://img-blog.csdn.net/20170825180113222?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxNDA0MjA2Ng==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast) 







### HTTP有哪些method?

1. get:客户端向服务端发起请求，获得资源。请求获得URL处所在的资源。
2. post:向服务端提交新的请求字段。请求URL的资源后添加新的数据。
3. head:请求获取URL资源的响应报告，即获得URL资源的头部
4. patch：请求局部修改URL所在资源的数据项
5. put：请求修改URL所在资源的数据元素。
6. delete：请求删除url资源的数据



### 简述HTTP请求的报文格式 ?

```
请求消息 Request

请求行，用来说明请求类型,要访问的资源以及所使用的HTTP版本.

请求头部，紧接着请求行（即第一行）之后的部分，用来说明服务器要使用的附加信息从第二行起为请求头部，HOST将指出请求的目的地.User-Agent,服务器端和客户端脚本都能访问它,它是浏览器类型检测逻辑的重要基础.该信息由你的浏览器来定义,并且在每个请求中自动发送等等

空行，请求头部后面的空行是必须的

请求数据也叫主体，可以添加任意的其他数据。

响应消息Response

状态行，由HTTP协议版本号， 状态码， 状态消息 三部分组成。

消息报头，用来说明客户端要使用的一些附加信息

空行，消息报头后面的空行是必须的

响应正文，服务器返回给客户端的文本信息。

############################General
Request URL: https://www.baidu.com/s?wd=nginx%20%20%E9%9D%A2%E8%AF%95%E9%A2%98&rsv_spt=1&rsv_iqid=0x99606e7c0018235d&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&rqlang=cn&tn=baiduhome_pg&rsv_dl=tb&rsv_enter=1&oq=nginx&inputT=1794&rsv_t=d3d0tZuaQessP%2FBsW1OrpBRIvcv53W8RcVXaQkxMNi7L%2FVpHd3LU581yC%2FWNAnqXkGoj&rsv_pq=f4f12488001664fd&rsv_sug3=52&rsv_sug1=34&rsv_sug7=100&rsv_sug2=0&rsv_sug4=1794
Request Method: GET
Status Code: 200 OK
Remote Address: 127.0.0.1:1080
Referrer Policy: no-referrer-when-downgrade


############################ResponseHeaders：
Bdpagetype: 3
Bdqid: 0xf24907e7002f72e4
Cache-Control: private
Ckpacknum: 2
Ckrndstr: 7002f72e4
Connection: Keep-Alive
Content-Encoding: gzip
Content-Type: text/html;charset=utf-8
Date: Sat, 26 Oct 2019 06:36:24 GMT
P3p: CP=" OTI DSP COR IVA OUR IND COM "
Server: BWS/1.1
Set-Cookie: BDRCVFR[feWj1Vr5u3D]=mk3SLVN4HKm; path=/; domain=.baidu.com
Set-Cookie: delPer=0; path=/; domain=.baidu.com
Set-Cookie: BD_CK_SAM=1;path=/
Set-Cookie: PSINO=2; domain=.baidu.com; path=/
Set-Cookie: BDSVRTM=29; path=/
Set-Cookie: H_PS_PSSID=1438_21111_29910_29567_29220_26350; path=/; domain=.baidu.com
Strict-Transport-Security: max-age=172800
Traceid: 1572071784022191156217458494119200387812
Transfer-Encoding: chunked
Vary: Accept-Encoding
X-Ua-Compatible: IE=Edge,chrome=1

############################RequestHeaders：
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
Accept-Encoding: gzip, deflate, br
Accept-Language: zh-CN,zh;q=0.9
Cache-Control: no-cache
Connection: keep-alive
Cookie: PSTM=1563898364; BIDUPSID=3285F399ACBF626197AD4AD6BEB7857E; BDUSS=RURXNJMUlLV35CS0djOU9nOUZ2YTdxZkZKQ1MxaE5nTlRYTmE0Y0J5WE1hNkZkRUFBQUFBJCQAAAAAAAAAAAEAAADVbtdA1cXR79H4MjMyNDI0MwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMzeeV3M3nldZD; BD_UPN=12314353; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; BAIDUID=12EF2B22DB6481AEE63768097BC0675A:FG=1; H_PS_PSSID=1438_21111_29910_29567_29220_26350; BDSFRCVID=bLCsJeCCxG3j0M7w47uWWd77lvNijBQs9a1e3J; H_BDCLCKID_SF=tRk8oI-XJCvbfP0k247Hhn8thmT22-uS-K5bSCD-tCDahIPxDIjh5-umKUntbtJ-qCrKBRba-nO2J6QG5-crhtLOMqoLf4Jjbn3dopRbWJ5TEPnjDPC2Xj-Wjh79LMQuLTcL_hrV2qoGqITpDt_5y6TLjNujtTFtf5vfL5rL2njoqbTkq4bohj4yMf8_J6DDtRCeVItbjt5MDf7RhnoK-PQK-xQ0KnQjK5RE8t52ax7VeJbY5tFX-n83ePQE3bJZ5m7mXp0b2DJYMP0mLjL2X5t-ja59WtcB0H6eVRDh5COkbCD6ej0MejQM5pJfeJT3KCOjBRu8Kb7V-POYb5FaD6oLDH-OJTthb6nLM-OOb4ojeD6P-trf5J8SKhCh-R3LXKj-0nTaaC6jq--r363hKUI8LPbO05JBtgDfLKbpQMcajq6CQTbFMtIY0q-856o-t2LEoC0XtI-BMDv65nt_24k_hp302D62aKDSQbTeqCDKhDDlDTA2DIK05Mcdb-Rt-D7-SRoq2RbhqbOx2Cjh5-umhfobaRQLqC7jLIvu0-55OR5JLn7nDPTQXHJftUoa5mue-pQJJhrEjlohKTrHDtQyyGCDt5FjJnKtV-35b5r0jbjvK-r_q4tehHnea40_tRuDoCDbfIt5245mhqn5hnLX-U__5to05TTj3-JLjt5MDf7zbtOE5trK-p3JqM3-W57Z0lOnMp05jCJEjntMLtDmqJ7iKUIeBD5B5fjbfRDaeDO_e6LbejO3eH_s-bbfHjQ33R7M2Ru_Hn7zHnbEb5rLDGLDJ6tOtnAhL4bEX-OoftonbjcD-tFO5q32bnIhb6nLMb6H5nThDD6nKb83-xAdKP6-3MJO3b7BopC5bK-bff35b5tbhbKNMf6GQRt8KR6-ohFLK-oj-DDRjjOP; sug=3; sugstore=1; ORIGIN=0; bdime=0; delPer=0; BD_CK_SAM=1; PSINO=2; COOKIE_SESSION=0_0_1_0_0_1_0_0_0_1_65_0_0_0_0_0_0_0_1572063939%7C1%230_0_1572063939%7C1; BDSVRTM=0
Host: www.baidu.com
Pragma: no-cache
Upgrade-Insecure-Requests: 1
```







### Spring 跨域如何处理

```
@CrossOrigin 

MvcConfigurer类中实现 addCorsMappings 方法


Access-Control-Allow-Origin:  http://www.YOURDOMAIN.com            // 设置允许请求的域名，多个域名以逗号分隔
Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS      // 设置允许请求的方法，多个方法以逗号分隔
Access-Control-Allow-Headers: Authorization                        // 设置允许请求自定义的请求头字段，多个字段以逗号分隔
Access-Control-Allow-Credentials: true                              // 设置是否允许发送 Cookies
```









### HTTP的长连接是什么意思?

```
Keep-Alive 是一个通用消息头，允许消息发送者暗示连接的状态，还可以用来设置超时时长和最大请求数。

parameters
一系列用逗号隔开的参数，每一个参数由一个标识符和一个值构成，并使用等号 ('=') 隔开。下述标识符是可用的：
timeout：指定了一个空闲连接需要保持打开状态的最小时长（以秒为单位）。需要注意的是，如果没有在传输层设置 keep-alive TCP message 的话，大于 TCP 层面的超时设置会被忽略。
max：在连接关闭之前，在此连接可以发送的请求的最大值。在非管道连接中，除了 0 以外，这个值是被忽略的，因为需要在紧跟着的响应中发送新一次的请求。HTTP 管道连接则可以用它来限制管道的使用。


Connection 头（header） 决定当前的事务完成后，是否会关闭网络连接。如果该值是“keep-alive”，网络连接就是持久的，不会关闭，使得对同一个服务器的请求可以继续在该连接上完成
Connection: keep-alive
```



### HTTPS的加密方式是什么，讲讲整个加密解密流程?

HTTPS其实是有两部分组成：HTTP + SSL / TLS，也就是在HTTP上又加了一层处理加密信息的模块。服务端和客户端的信息传输都会通过TLS进行加密，所以传输的数据都是加密后的数据。具体是如何进行加密，解密，验证的，且看下图。

![img](https://pic002.cnblogs.com/images/2012/38542/2012072310244445.png)

1. 客户端发起HTTPS请求

这个没什么好说的，就是用户在浏览器里输入一个https网址，然后连接到server的443端口。

2. 服务端的配置

采用HTTPS协议的服务器必须要有一套数字证书，可以自己制作，也可以向组织申请。区别就是自己颁发的证书需要客户端验证通过，才可以继续访问，而使用受信任的公司申请的证书则不会弹出提示页面(startssl就是个不错的选择，有1年的免费服务)。这套证书其实就是一对公钥和私钥。如果对公钥和私钥不太理解，可以想象成一把钥匙和一个锁头，只是全世界只有你一个人有这把钥匙，你可以把锁头给别人，别人可以用这个锁把重要的东西锁起来，然后发给你，因为只有你一个人有这把钥匙，所以只有你才能看到被这把锁锁起来的东西。

3. 传送证书

这个证书其实就是公钥，只是包含了很多信息，如证书的颁发机构，过期时间等等。

4. 客户端解析证书

这部分工作是有客户端的TLS来完成的，首先会验证公钥是否有效，比如颁发机构，过期时间等等，如果发现异常，则会弹出一个警告框，提示证书存在问题。如果证书没有问题，那么就生成一个随即值。然后用证书对该随机值进行加密。就好像上面说的，把随机值用锁头锁起来，这样除非有钥匙，不然看不到被锁住的内容。

5. 传送加密信息

这部分传送的是用证书加密后的随机值，目的就是让服务端得到这个随机值，以后客户端和服务端的通信就可以通过这个随机值来进行加密解密了。

6. 服务段解密信息

服务端用私钥解密后，得到了客户端传过来的随机值(私钥)，然后把内容通过该值进行对称加密。所谓对称加密就是，将信息和私钥通过某种算法混合在一起，这样除非知道私钥，不然无法获取内容，而正好客户端和服务端都知道这个私钥，所以只要加密算法够彪悍，私钥够复杂，数据就够安全。

7. 传输加密后的信息

这部分信息是服务段用私钥加密后的信息，可以在客户端被还原

8. 客户端解密信息

客户端用之前生成的私钥解密服务段传过来的信息，于是获取了解密后的内容。整个过程第三方即使监听到了数据，也束手无策



https的流程图：

![1572072131751](assets\1572072131751.png)

![1572072151579](assets\1572072151579.png)









### 什么是分块传送 ?

```

分块传输编码（Transfer-Encoding）就是这样一种解决方案：它把数据分解成一系列数据块，并以多个块发送给客户端，服务器发送数据时不再需要预先告诉客户端发送内容的总大小，只需在响应头里面添加Transfer-Encoding: chunked，以此来告诉浏览器我使用的是分块传输编码，这样就不需要 Content-Length 了，这就是分块传输编码 Transfer-Encoding 的作用。

Transfer-Encoding: gzip, chunked
```



###Http的范围传送是什么？ 如何实现的？







### Session和cookie的区别 ?

```
Cookies是一种能够让网站服务器把少量数据储存到客户端的硬盘或内存，或是从客户端的硬盘读取数据的一种技术。Cookies是当你浏览某网站时，由Web服务器置于你硬盘上的一个非常小的文本文件，它可以记录你的用户ID、密码、浏览过的网页、停留的时间等信息。session: 当用户请求来自应用程序的 Web 页时，如果该用户还没有会话，则 Web 服务器将自动创建一个 Session 对象。当会话过期或被放弃后，服务器将终止该会话。cookie机制：采用的是在客户端保持状态的方案，而session机制采用的是在服务端保持状态的方案。同时我们看到由于服务器端保持状态的方案在客户端也需要保存一个标识，所以session机制可能需要借助cookie机制来达到保存标识的目的。

Session是服务器用来跟踪用户的一种手段，每个Session都有一个唯一标识：session ID。当服务器创建了Session时，给客户端发送的响应报文包含了Set-cookie字段，其中有一个名为sid的键值对，这个键值Session ID。客户端收到后就把Cookie保存浏览器，并且之后发送的请求报表都包含SessionID。HTTP就是通过Session和Cookie这两个发送一起合作来实现跟踪用户状态，Session用于服务端，Cookie用于客户端
```









### 一次完整的HTTP请求过程

域名解析 --> 发起TCP的3次握手 --> 建立TCP连接后发起http请求 --> 服务器响应http请求，浏览器得到html代码 --> 浏览器解析html代码，并请求html代码中的资源（如js、css、图片等） --> 浏览器对页面进行渲染呈现给用户 



支持客户服务器模式

无状态

![1571971277502](assets\1571971277502.png)



![1571971318254](assets\1571971318254.png)





![1571971343670](assets\1571971343670.png)





![1571971396813](assets\1571971396813.png)







![1571971409124](assets\1571971409124.png)



![1571971428029](assets\1571971428029.png)