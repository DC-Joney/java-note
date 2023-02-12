## http服务端处理函数：

- func Handle(pattern string, handler Handler) 

  这个是根据匹配来给其分配对应的处理逻辑方法。

- func HandleFunc(pattern string, handler func(ResponseWriter, *Request)

  同上，主要用来实现动态文件内容的展示。

- func ListenAndServe(addr string, handler Handler) error 

   监听网络请求然后调用具有handler的Serve去处理连接请求.通常情况下Handler是nil,使用默认的DefaultServeMux 。

- func ListenAndServeTLS(addr string, certFile string, keyFile string, handler Handler) error  

  该函数与ListenAndServe功能基本相同,二者不同之处是该函数需要HTTPS连接.也就是说,必须给该服务Serve提供一个包含整数的秘钥的文件,如果证书是由证书机构签署的,那么证书文件必须是服务证书之后跟着CA证书。

- func ServeFile(w ResponseWriter, r *Request, name string)

  利用指定的文件或者目录的内容来响应相应的请求。

- func SetCookie(w ResponseWriter, cookie *Cookie)

  给w设定cookie。

-  func StatusText(code int) string

  对于http状态码返回文本表示,如果这个code未知,则返回空的字符串 。

- func MaxBytesReader(w ResponseWriter, r io.ReadCloser, n int64) io.ReadCloser

  该函数类似于io.LimitReader但是该函数是用来限制请求体的大小.与io.LimitReader不同的是,该函数返回一个ReaderCloser,当读超过限制时,返回一个non-EOF,并且当Close方法调用时,关闭底层的reader.该函数组织客户端恶意发送大量请求,浪费服务器资源。

- func ParseHTTPVersion(vers string) (major, minor int, ok bool)

  解析http字符串版本进行解析,”HTTP/1.0” 返回 (1, 0, true) 。

- func ProxyURL(fixedURL *url.URL) func(*Request) (*url.URL, error)

  返回一个用于传输的代理函数,该函数总是返回相同的URL 。

- func Redirect(w ResponseWriter, r *Request, urlStr string, code int)

  返回一个重定向的url给指定的请求,这个重定向url可能是一个相对请求路径的一个相对路径. 

-  func Serve(l net.Listener, handler Handler) error

  该函数接受listener l的传入http连接,对于每一个连接创建一个新的服务协程,这个服务协程读取请求然后调用handler来给他们响应.handler一般为nil,这样默认的DefaultServeMux被使用.





## http客户端请求函数：

​     Client具有Do，Get，Head，Post以及PostForm等方法。 其中Do方法可以对Request进行一系列的设定，而其他的对request设定较少。如果Client使用默认的Client，则其中的Get，Head，Post以及PostForm方法相当于默认的http.Get,http.Post,http.Head以及http.PostForm函数。 

- func (c *Client) Do(req *Request) (resp *Response, err error)

  Do发送http请求并且返回一个http响应,遵守client的策略,如重定向,cookies以及auth等.错误经常是由于策略引起的,当err是nil时,resp总会包含一个非nil的resp.body.当调用者读完resp.body之后应该关闭它,如果resp.body没有关闭,则Client底层RoundTripper将无法重用存在的TCP连接去服务接下来的请求,如果resp.body非nil,则必须对其进行关闭.通常来说,经常使用Get,Post,或者PostForm来替代Do. 

- func (c *Client) Get(url string) (resp *Response, err error)

  利用get方法请求指定的url.Get请求指定的页面信息，并返回实体主体。 

- func (c *Client) Head(url string) (resp *Response, err error)

  利用head方法请求指定的url，Head只返回页面的首部。 

- func (c *Client) Post(url string, bodyType string, body io.Reader) (resp *Response, err error)

  利用post方法请求指定的URl,如果body也是一个io.Closer,则在请求之后关闭它 

- func (c *Client) PostForm(url string, data url.Values) (resp *Response, err error)

  利用post方法请求指定的url,利用data的key和value作为请求体. 

- Do方法可以灵活的对request进行配置，然后进行请求。利用http.Client以及http.NewRequest来模拟请求。模拟request中带有cookie的请求。

- func (c *Cookie) String() string该函数返回cookie的序列化结果。如果只设置了Name和Value字段，序列化结果可用于HTTP请求的Cookie头或者HTTP回复的Set-Cookie头；如果设置了其他字段，序列化结果只能用于HTTP回复的Set-Cookie头。 *

- *func (d Dir) Open(name string) (File, error)type FileFile是通过FileSystem的Open方法返回的,并且能够被FileServer实现.该方法与*os.File行为表现一样。

- func FileServer(root FileSystem) HandlerFileServer

  返回一个使用FileSystem接口提供文件访问服务的HTTP处理器。可以使用httpDir来使用操作系统的FileSystem接口实现。其主要用来实现静态文件的展示。 

- func NotFoundHandler() Handler

  返回一个简单的请求处理器,该处理器对任何请求都会返回”404 page not found” 

- func RedirectHandler(url string, code int) Handler

  使用给定的状态码将它接受到的任何请求都重定向到给定的url 

  

- func StripPrefix(prefix string, h Handler) Handler

  将请求url.path中移出指定的前缀,然后将省下的请求交给handler h来处理,对于那些不是以指定前缀开始的路径请求,该函数返回一个http 404 not found 的错误. 

- func TimeoutHandler(h Handler, dt time.Duration, msg string) Handler

  具有超时限制的handler,该函数返回的新Handler调用h中的ServerHTTP来处理每次请求,但是如果一次调用超出时间限制,那么就会返回给请求者一个503服务请求不可达的消息,并且在ResponseWriter返回超时错误. 

其中FileServer经常和StripPrefix一起连用，用来实现静态文件展示，举例如下：

type HandlerFuncHandlerFunc type是一个适配器，通过类型转换我们可以将普通的函数作为HTTP处理器使用。如果f是一个具有适当签名的函数，HandlerFunc(f)通过调用f实现了Handler接口。

type Hijackerinterface{// Hijack让调用者接管连接,在调用Hijack()后,http server库将不再对该连接进行处理,对于该连接的管理和关闭责任将由调用者接管.Hijack() (net.Conn, *bufio.ReadWriter, error)//conn表示连接对象，bufrw代表该连接的读写缓存对象。

- func NewRequest(method, urlStr string, body io.Reader) (*Request, error)

  利用指定的method,url以及可选的body返回一个新的请求.如果body参数实现了io.Closer接口，Request返回值的Body 字段会被设置为body，并会被Client类型的Do、Post和PostForm方法以及Transport.RoundTrip方法关闭。 

- func ReadRequest(b *bufio.Reader) (req *Request, err error)

  从b中读取和解析一个请求. 

- func (r *Request) AddCookie(c *Cookie)

  给request添加cookie,AddCookie向请求中添加一个cookie.按照RFC 6265 section 5.4的规则,AddCookie不会添加超过一个Cookie头字段.这表示所有的cookie都写在同一行,用分号分隔（cookie内部用逗号分隔属性） 

- func (r *Request) Cookie(name string) (*Cookie, error)

  返回request中指定名name的cookie，如果没有发现，返回ErrNoCookie 

- func (r *Request) Cookies() []*Cookie

  返回该请求的所有cookies 

- func (r *Request) SetBasicAuth(username, password string)

  利用提供的用户名和密码给http基本权限提供具有一定权限的header。当使用http基本授权时，用户名和密码是不加密的 

-  func (r *Request) UserAgent() string

  如果在request中发送，该函数返回客户端的user-Agent

- func (r *Request) FormFile(key string) (multipart.File, *multipart.FileHeader, error)

  对于指定格式的key，FormFile返回符合条件的第一个文件，如果有必要的话，该函数会调用ParseMultipartForm和ParseForm。

- func (r *Request) FormValue(key string) string

  返回key获取的队列中第一个值。在查询过程中post和put中的主题参数优先级高于url中的value。为了访问相同key的多个值，调用ParseForm然后直接检查RequestForm。 

- func (r *Request) MultipartReader() (*multipart.Reader, error)

  如果这是一个有多部分组成的post请求，该函数将会返回一个MIME 多部分reader，否则的话将会返回一个nil和error。使用本函数代替ParseMultipartForm可以将请求body当做流stream来处理。 

- func (r *Request) ParseForm() 

  error解析URL中的查询字符串，并将解析结果更新到r.Form字段。对于POST或PUT请求，ParseForm还会将body当作表单解析，并将结果既更新到r.PostForm也更新到r.Form。解析结果中，POST或PUT请求主体要优先于URL查询字符串（同名变量，主体的值在查询字符串的值前面）。如果请求的主体的大小没有被MaxBytesReader函数设定限制，其大小默认限制为开头10MB。ParseMultipartForm会自动调用ParseForm。重复调用本方法是无意义的。 

- func (r *Request) ParseMultipartForm(maxMemory int64) error

  ParseMultipartForm将请求的主体作为multipart/form-data解析。请求的整个主体都会被解析，得到的文件记录最多 maxMemery字节保存在内存，其余部分保存在硬盘的temp文件里。如果必要，ParseMultipartForm会自行调用 ParseForm。重复调用本方法是无意义的。 

- func (r *Request) PostFormValue(key string) string

  返回post或者put请求body指定元素的第一个值，其中url中的参数被忽略。 

- 

- func (r *Request) ProtoAtLeast(major, minor int) bool

  检测在request中使用的http协议是否至少是major.minor 

- func (r *Request) Referer() string

  如果request中有refer，那么refer返回相应的url。Referer在request中是拼错的，这个错误从http初期就已经存在了。该值也可以从Headermap中利用Header[“Referer”]获取；在使用过程中利用Referer这个方法而不是map的形式的好处是在编译过程中可以检查方法的错误，而无法检查map中key的错误。 

-  func (r *Request) Write(w io.Writer) error

  Write方法以有线格式将HTTP/1.1请求写入w（用于将请求写入下层TCPConn等）。本方法会考虑请求的如下字段：Host URL Method (defaults to “GET”) Header ContentLength TransferEncoding Body如果存在Body，ContentLength字段<= 0且TransferEncoding字段未显式设置为[“identity”]，Write方法会显式添加”Transfer-Encoding: chunked”到请求的头域。Body字段会在发送完请求后关闭。 

- func (r *Request) WriteProxy(w io.Writer) error

  该函数与Write方法类似，但是该方法写的request是按照http代理的格式去写。尤其是，按照RFC 2616 Section 5.1.2，WriteProxy会使用绝对URI（包括协议和主机名）来初始化请求的第1行（Request-URI行）。无论何种情况，WriteProxy都会使用r.Host或r.URL.Host设置Host头。 

type Response指对于一个http请求的响应response

-  func Get(url string) (resp *Response, err error)

  利用GET方法对一个指定的URL进行请求，如果response是如下重定向中的一个代码，则Get之后将会调用重定向内容，最多10次重定向。 

  - 301 (永久重定向，告诉客户端以后应该从新地址访问) 。
  - 302 (暂时性重定向，作为HTTP1.0的标准，以前叫做Moved Temporarily，现在叫做Found。现在使用只是为了兼容性处理，包括PHP的默认Location重定向用到也是302)，注：303和307其实是对302的细化。 
  - 303 (对于Post请求，它表示请求已经被处理，客户端可以接着使用GET方法去请求Location里的URl) 。
  - 307 (临时重定向，对于Post请求，表示请求还没有被处理，客户端应该向Location里的URL重新发起Post请求) 。如果有太多次重定向或者有一个http协议错误将会导致错误。当err为nil时，resp总是包含一个非nil的resp.body，Get是对DefaultClient.Get的一个包装。 

- func Head(url string) (resp *Response, err error)该函数功能见net中Head方法功能。该方法与默认的defaultClient中Head方法一致。 

-  func Post(url string, bodyType string, body io.Reader) (resp *Response, err error)该方法与默认的defaultClient中Post方法一致。 

- func PostForm(url string, data url.Values) (resp *Response, err error)该方法与默认的defaultClient中PostForm方法一致。



## http 基础案例

### http客户端

import “net/http”
http包提供了HTTP客户端和服务端的实现。

Get、Head、Post和PostForm函数发出HTTP/ HTTPS请求。

```
package main
import (
    "fmt"
    "io/ioutil"
    "net/http"
)

func main() {
    response, err := http.Get("http://www.baidu.com")
    if err != nil {
    // handle error
    }
    //程序在使用完回复后必须关闭回复的主体。
    defer response.Body.Close()

    body, _ := ioutil.ReadAll(response.Body)
    fmt.Println(string(body))
}
package main

import (
    "fmt"
    "io/ioutil"
    "net/http"
    "bytes"
)

func main() {
    body := "{\"action\":20}"
    res, err := http.Post("http://xxx.com", "application/json;charset=utf-8", bytes.NewBuffer([]byte(body)))
    if err != nil {
        fmt.Println("Fatal error ", err.Error())
    }

    defer res.Body.Close()

    content, err := ioutil.ReadAll(res.Body)
    if err != nil {
        fmt.Println("Fatal error ", err.Error())
    }

    fmt.Println(string(content))
}
```

还可以使用：
http.Client和http.NewRequest来模拟请求

```
package main
import (
    "fmt"
    "io/ioutil"
    "net/http"
    "net/url"
    "strings"
)

func main() {
    v := url.Values{}
    v.Set("username", "xxxx")
    v.Set("password", "xxxx")
    //利用指定的method,url以及可选的body返回一个新的请求.如果body参数实现了io.Closer接口，Request返回值的Body 字段会被设置为body，并会被Client类型的Do、Post和PostFOrm方法以及Transport.RoundTrip方法关闭。
    body := ioutil.NopCloser(strings.NewReader(v.Encode())) //把form数据编下码
    client := &http.Client{}//客户端,被Get,Head以及Post使用
    reqest, err := http.NewRequest("POST", "http://xxx.com/logindo", body)
    if err != nil {
        fmt.Println("Fatal error ", err.Error())
    }
    //给一个key设定为响应的value.
    reqest.Header.Set("Content-Type", "application/x-www-form-urlencoded;param=value") //必须设定该参数,POST参数才能正常提交

    resp, err := client.Do(reqest)//发送请求
    defer resp.Body.Close()//一定要关闭resp.Body
    content, err := ioutil.ReadAll(resp.Body)
    if err != nil {
        fmt.Println("Fatal error ", err.Error())
    }

    fmt.Println(string(content))
}
```



### 如何创建web服务端?

```
package main

import (
    "net/http"
)

func SayHello(w http.ResponseWriter, req *http.Request) {
    w.Write([]byte("Hello"))
}

func main() {
    http.HandleFunc("/hello", SayHello)
    http.ListenAndServe(":8001", nil)

}
```

首先调用Http.HandleFunc
按顺序做了几件事：

- 调用了DefaultServerMux的HandleFunc
- 调用了DefaultServerMux的Handle
- 往DefaultServeMux的map[string]muxEntry中增加对应的handler和路由规则

其次调用http.ListenAndServe(“:8001”, nil)
按顺序做了几件事情：

- 实例化Server
- 调用Server的ListenAndServe()
- 调用net.Listen(“tcp”, addr)监听端口
- 启动一个for循环，在循环体中Accept请求
- 对每个请求实例化一个Conn，并且开启一个goroutine为这个请求进行服务go c.serve()
- 读取每个请求的内容w, err := c.readRequest()
- 判断header是否为空，如果没有设置handler（这个例子就没有设置handler），handler就设置为DefaultServeMux
- 调用handler的ServeHttp
- 在这个例子中，下面就进入到DefaultServerMux.ServeHttp
- 根据request选择handler，并且进入到这个handler的ServeHTTP

```
   mux.handler(r).ServeHTTP(w, r)
```

- 选择handler：

```
A 判断是否有路由能满足这个request（循环遍历ServerMux的muxEntry）

B 如果有路由满足，调用这个路由handler的ServeHttp

C 如果没有路由满足，调用NotFoundHandler的ServeHttp
```



## 基本API

ServerMux 代表处理的路径集合

Server 代表要监听的server 端

```go
	http.ListenAndServe(":8081", nil)

	mux := http.NewServeMux()

	mux.HandleFunc("", func(writer http.ResponseWriter, request *http.Request) {
		
	})
	
	server := http.Server{
		Handler: mux,
		Addr: "",
	}
	
	server.ListenAndServe()
	
	http.ListenAndServe("",mux)
```

