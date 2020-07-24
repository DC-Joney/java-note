## Spring MVC 扩展

WebApplicationContext：是普通ApplicationContext的扩展，  **它与正常的ApplicationContext不同之处在于它能够解析主题，并且知道它与哪个Servlet相关联（通过连接到ServletContext）。 WebApplicationContext绑定在ServletContext中** 



### servlet 3.0 + 扩展 WebConfig 与  RootConfig

```java
public class GolfingWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
@Override
 protected Class<?>[] getRootConfigClasses() {
 	// GolfingAppConfig defines beans that would be in root-context.xml
 	return new Class[] { GolfingAppConfig.class };
 }
@Override
 protected Class<?>[] getServletConfigClasses() {
 	// GolfingWebConfig defines beans that would be in golfing-servlet.xml
 	return new Class[] { GolfingWebConfig.class };
 }
@Override
 protected String[] getServletMappings() {
 	return new String[] { "/golfing/*" };
 }
}
```



### 核心实现类

- HandlerMapping： 根据一些标准将传入的请求映射到处理程序和前处理程序和后处理程序列表（处理程序拦截器），其细节由HandlerMapping实现而异。 最流行的实现支持注释控制器，但其他实现也存在。 
- HandlerAdaptor： 帮助DispatcherServlet调用映射到请求的处理程序，而不管实际调用哪个处理程序。 例如，调用带注释的控制器需要解析各种注释。 因此，HandlerAdapter的主要目的是屏蔽DispatcherServlet和这些细节 
- HandlerExceptionResolver ： 映射视图的异常，也允许更复杂的异常处理代码。 
- ViewResolver： 将基于逻辑字符串的视图名称解析为实际的View类型。 
- ThemeResolver： 解决您的Web应用程序可以使用的主题，例如，提供个性化的布局 
- MutipartResolver： 解析multi-part请求，以支持从HTML表单处理文件上传。 
- FlashMapResolver： 存储并检索可以用于将属性从一个请求传递到另一个请求的“输入”和“输出”FlashMap，通常是通过重定向。
-  LocaleResolver & LocaleContextResolver ： 解决客户端正在使用的区域设置以及可能的时区，以便能够提供国际化的视图 







### 注意点

- Controller 控制器在运行时，默认是使用AOP代理类进行装饰的，如果需要使用@Transaction 注解时，最好使用基于类的代理（proxyTargetClass = true）

-  RequestMappingHandlerMapping ：内部维护了所有的连接请求
- 



### 路径匹配

PathMacther





### Spring  web 扩展接口

- HandlerMapping ：存储了所有的Handler 请求
- HandlerArgumentReslover：处理HanddlerMethod 中 的参数
- InitBinder、WebDataBinder：处理http 请求中的参数绑定
- HandlerReturnValueHandler ： 处理返回数据
- ServletRequestHandledEvent：当servelet 请求完成时，spring 会发布一个request 请求完成事件
- WebApplicationInitializer：当spring启动web容器时，会调用该接口对serveletConfig 进行初始,主要用于初始化servlet3.0容器
- HttpRequestHandler：http 请求的 handler 处理器



#### 上传

MultipartRequest：上传Request

MultipartFile：代表某个上传的文件信息

MultipartHttpServletRequest：表示文件上传类型的http请求， 多部分文件解析器 



#### **Handler** method

- HandlerMethod 代表某一个controller method
- ControllerAdviceBean：用于获取使用@ControllerAdviceBean 注解处理异常的接口



#### **Async**

- DefferResult：异步返回结果
- Callable : 异步返回结果
- WebAsyncManager
- WebAsyncTask
- WebAsyncUtils



#### **Web client**

- RestTemplate：client 请求数据接口
- AsyncRestTemplate：异步请求数据接口
- RestGatewaySupport



#### **Web Binder**

- ServletRequestUtils ： 获取 request 请求参数

- WebDataBinder： 绑定 http 请求数据 到 实体类

- ServletRequestDataBinder

- ServletRequestParameterPropertyValues：继承自PropertyValues ，绑定 request 请求参数到实体类

- WebArgumentResolver

- WebBindingInitializer

- WebDataBinderFactory

- WebRequestDataBinder

- SessionAttributeStore

- SessionStatus

- SpringWebConstraintValidatorFactory

- 支持的注解：

  ```java
  @ControllerAdvice
  @CookieValue
  @CrossOrigin
  @DeleteMapping
  @ExceptionHandler
  @GetMapping
  @InitBinder
  @Mapping
  @MatrixVariable
  @ModelAttribute
  @PatchMapping
  @PathVariable
  @PostMapping
  @PutMapping
  @RequestAttribute
  @RequestBody
  @RequestHeader
  @RequestMapping
  @RequestMethod
  @RequestParam
  @RequestPart
  @ResponseBody
  @ResponseStatus
  @RestController
  @RestControllerAdvice
  @SessionAttribute
  @SessionAttributes
  @ValueConstants
  ```



#### **Accept**

- ContentNegotiationStrategy：解析MediaType 请求
- ContentNegotiationManager





### Spring web 工具类

- UrlPathHelper ： spring 从servelet 解析路径

- WebUtils：spring web 工具类
- RequestAttruibutes：spring 提供的servlet 的 工具类，可以获取 Request Reponse 等对象
- UriUtils ：用于 URI 编码 解码等
- UriComponentsBuilder：用于处理URI 连接，删除 添加 query path 等等
- UriTemplate、UriTemplateHandler、UriComponents：与上面相符
- ServletContextPropertyUtils：用于处理ServletContext中所有的参数信息
- CookieGenerator：处理cookie 信息



### Spring Web Mvc

#### resource：处理静态资源

- ResourceResolver：解析静态资源处理器
- ResourceTransformer：将静态资源转为Resource 类



#### mvc：处理controller 隐射

- Controller
- HandlerAdapter
- WebContentInterceptor：缓存拦截器

- **condition**
  - RequestCondition

- **method**
  - RequestBodyAdvice
  - JsonViewRequestBodyAdvice
  - MvcUriComponentsBuilder：获取 mvc 中 的url 路径
  - ResponseBodyAdvice
  - StreamingResponseBody
  - RequestMappingInfo



#### view：处理返回试图

- SmartView
- View



#### core

- RequestContextUtils：用于获取  WebApplicationContext 上下文对象
- AsyncHandlerInterceptor：异步拦截器
- HandlerAdapter：Handler 适配器接口
- HandlerInterceptpr ：Handler 前后拦截器
- HandlerMapping：Handler  隐射器
  - RequestMappingHandlerMapping
  - SimpleUrlHandlerMapping
  - BeanNameUrlHandlerMapping









