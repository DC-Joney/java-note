```
@ApplicationScope
@RequestScope
@SessionScope
```



```
HttpRequestHandlerServlet


HttpRequestHandler

ServletWebRequest

SessionScope

RequestScope
```



Utils

```
WebApplicationContextUtils

CorsUtils

HtmlUtils

WebUtils
UriUtils

ServletRequestAttributes

RequestContextHolder

RequestContextUtils

MergedAnnotations
```



Aware接口

```
ServletContextAware
ServletConfigAware

Spring MVC相关
ConfigurableWebEnvironment
ContextCleanupListener
ContextLoader
```



Cors接口

```
CorsProcessor

DefaultCorsProcessor

CorsFilter
```





相关类接口

```
RequestHeaderMapMethodArgumentResolver
RequestHeaderMethodArgumentResolver
RequestParamMapMethodArgumentResolver
RequestParamMethodArgumentResolver
SessionAttributesHandler


CookieGenerator

WebApplicationInitializer
```



处理Handler返回类型以及参数类型

```
HandlerMethodArgumentResolver
AsyncHandlerMethodReturnValueHandler
HandlerMethodReturnValueHandler
UriComponentsContributor

InvocableHandlerMethod

HandlerMethod

HandlerTypePredicate


```



上传下载类型的

```
MultipartFile


MultipartResolver
MultipartRequest
MultipartHttpServletRequest
```



处理URI以及Path

```
PathPattern
PathPatternParser




UriComponents
UriComponentsBuilder
UriTemplate
DefaultUriBuilderFactory
UriTemplate
UrlPathHelper
ServletUriComponentsBuilder
```





spring boot接入

```
SpringServletContainerInitializer
```





重定向

```
RedirectAttributesModelMap

RedirectAttributes
```





处理Handler

```
Controller

SimpleControllerHandlerAdapter
```





核心类

```
HandlerAdapter
FrameworkServlet
AsyncHandlerInterceptor
DispatcherServlet

HandlerInterceptor

HandlerMapping

RequestAttributes

NativeWebRequest

WebRequestInterceptor
```





相关注解

```
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
@package-info
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
```



Filter

```
GenericFilterBean
HiddenHttpMethodFilter
ForwardedHeaderFilter
DelegatingFilterProxy
RelativeRedirectFilter
RequestContextFilter
CorsFilter
CharacterEncodingFilter
CompositeFilter
```





Session

```
SessionAttributesHandler
SessionStatusMethodArgumentResolver
RequestHeaderMapMethodArgumentResolver
RequestHeaderMethodArgumentResolver
RequestParamMapMethodArgumentResolver
RequestParamMethodArgumentResolver
SessionStatusMethodArgumentResolver

ModelAttributeMethodProcessor

ModelMethodProcessor
MapMethodProcessor

ExpressionValueMethodArgumentResolver
```



Exception

```
ExceptionHandlerMethodResolver

ResponseEntityExceptionHandler
```





RequestCondition

```
PatternsRequestCondition
ProducesRequestCondition
RequestCondition
RequestConditionHolder
RequestMethodsRequestCondition
ParamsRequestCondition
```





org.springframework.web.servlet.mvc.method.annotation



拼接Handler

```
MvcUriComponentsBuilder

JsonViewResponseBodyAdvice

RequestBodyAdvice

ResponseBodyAdvice
```



返回类型

```
HttpEntity
ReponseEntity

ServletCookieValueMethodArgumentResolver
ServletRequestMethodArgumentResolver
ServletResponseMethodArgumentResolver
SessionAttributeMethodArgumentResolver

StreamingResponseBody

StreamingResponseBodyReturnValueHandler

UriComponentsBuilderMethodArgumentResolver

ViewMethodReturnValueHandler

ViewNameMethodReturnValueHandler

ControllerAdviceBean

ResponseBodyEmitterReturnValueHandler
```





```
public class UniversalViewResolver{

    @Autowired
    private InternalResourceViewResolver viewResolver;

    public View forwardView(String url) throws Exception {
       return viewResolver.resolveViewName(UrlBasedViewResolver.FORWARD_URL_PREFIX + url, Locale.CHINA);
    }

    public View redirectView(String url) throws Exception {
        return viewResolver.resolveViewName(UrlBasedViewResolver.REDIRECT_URL_PREFIX + url, Locale.CHINA);
    }
    
    public static UniversalViewResolver shardInstance(){
        return UniversalViewResolverHolder.INSTANCE;
    } 
    
    private static class UniversalViewResolverHolder{
       private static UniversalViewResolver INSTANCE = new UniversalViewResolver();
    }
}

```

