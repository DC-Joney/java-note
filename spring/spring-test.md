教程地址

 https://blog.csdn.net/qq_35915384/article/details/80227297 



### JUnit中的注解

@BeforeClass：针对所有测试，只执行一次，且必须为static void
@Before：初始化方法，执行当前测试类的每个测试方法前执行。
@Test：测试方法，在这里可以测试期望异常和超时时间
@After：释放资源，执行当前测试类的每个测试方法后执行
@AfterClass：针对所有测试，只执行一次，且必须为static void
@Ignore：忽略的测试方法（只在测试类的时候生效，单独执行该测试方法无效）
@RunWith:可以更改测试运行器 ，缺省值 org.junit.runner.Runner

一个单元测试类执行顺序为：

```
@BeforeClass` –> `@Before` –> `@Test` –> `@After` –> `@AfterClass
```

每一个测试方法的调用顺序为：

```
@Before` –> `@Test` –> `@After
```



### 测试变量：

```
超时测试
@Test(timeout = 1000)

异常测试
@Test(expected = NullPointerException.class)

套件测试
public class TaskOneTest {
    @Test
    public void test() {
        System.out.println("Task one do.");
    }
}

public class TaskTwoTest {
    @Test
    public void test() {
        System.out.println("Task two do.");
    }
}

public class TaskThreeTest {
    @Test
    public void test() {
        System.out.println("Task Three.");
    }
}


@RunWith(Suite.class) // 1. 更改测试运行方式为 Suite
// 2. 将测试类传入进来
@Suite.SuiteClasses({TaskOneTest.class, TaskTwoTest.class, TaskThreeTest.class})
public class SuitTest {
    /**
     * 测试套件的入口类只是组织测试类一起进行测试，无任何测试方法，
     */
}
```



### spring boot测试

```
// 获取启动类，加载配置，确定装载 Spring 程序的装载方法，它回去寻找 主配置启动类（被 @SpringBootApplication 注解的）
@SpringBootTest
public class EmployeeServiceImplTest {
    // do 
}


//web环境
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeControllerTest {
    /**
     * Interface to provide configuration for a web application.
     */
    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    /**
     * 初始化 MVC 的环境
     */
    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void listAll() throws Exception {
        mockMvc
                .perform(get("/emp") // 测试的相对地址
                
                // accept response content type
                .accept(MediaType.APPLICATION_JSON_UTF8)) 
                
                // 期待返回状态吗码200
                .andExpect(status().isOk()) 
                
                // JsonPath expression  https://github.com/jayway/JsonPath
                // 这里是期待返回值是数组，并且第二个值的 name 存在
                .andExpect(jsonPath("$[1].name").exists()) 
                .andDo(print()); // 打印返回的 http response 信息
    }
}

部分方法说明
perform：执行一个RequestBuilder请求，会自动执行SpringMVC的流程并映射到相应的控制器执行处理；
andExpect：添加ResultMatcher验证规则，验证控制器执行完成后结果是否正确；
andDo：添加ResultHandler结果处理器，比如调试时打印结果到控制台；
andReturn：最后返回相应的MvcResult；然后进行自定义验证/进行下一步的异步处理；

测试整个流程程：
1、mockMvc.perform执行一个请求；
2、MockMvcRequestBuilders.get("/1/2")构造一个请求
3、ResultActions.andExpect添加执行完成后的断言
4、ResultActions.andDo添加一个结果处理器，表示要对结果做点什么事情，比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息。
5、ResultActions.andReturn表示执行完成后返回相应的结果。

测试过程如下：
1、准备测试环境
2、通过MockMvc执行请求
3.1、添加验证断言
3.2、添加结果处理器
3.3、得到MvcResult进行自定义断言/进行下一步的异步请求
4、卸载测试环境




```



### SpringBoot RestTemplate or WebClient

```
以RestTemplate测试

当你想启动一个完整的 HTTP 服务器对 Spring Boot 的 Web 应用编写测试代码时，可以使用@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)注解开启一个随机的可用端口。Spring Boot 针对 REST 调用的测试提供了一个 TestRestTemplate 模板，它可以解析链接服务器的相对地址。
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeController1Test {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void listAll() {
        ResponseEntity<List> result = restTemplate.getForEntity("/emp", List.class);
        Assert.assertThat(result.getBody(), Matchers.notNullValue());
    }
}
```







### spring mvc测试

```
@RunWith(SpringRunner.class)
@WebMvcTest

使用@WebMvcTest注解时，只有一部分的 Bean 能够被扫描得到，它们分别是：
@Controller
@ControllerAdvice
@JsonComponent
Filter
WebMvcConfigurer
HandlerMethodArgumentResolver
其他常规的@Component（包括@Service、@Repository等）Bean 则不会被加载到 Spring 测试环境上下文中。
```



### spring data jpa 测试

```
@RunWith(SpringRunner.class)
@DataJpaTest

我们可以使用 @DataJpaTest注解表示只对 JPA 测试；@DataJpaTest注解它只扫描@EntityBean 和装配 Spring Data JPA 存储库，其他常规的@Component（包括@Service、@Repository等）Bean 则不会被加载到 Spring 测试环境上下文。


@DataJpaTest 还提供两种测试方式：
使用内存数据库 h2database，Spring Data Jpa 测试默认采取的是这种方式；
使用真实环境的数据库。


使用真实数据库测试
如要需要使用真实环境中的数据库进行测试，需要替换掉默认规则，使用@AutoConfigureTestDatabase(replace = Replace.NONE)注解：

@RunWith(SpringRunner.class)
@DataJpaTest
// 加入 AutoConfigureTestDatabase 注解
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeDaoTest {

    @Autowired
    private EmployeeDao employeeDao;

    @Test
    public void testSave() {
        Employee employee = new Employee();
        EmployeeDetail detail = new EmployeeDetail();
        detail.setName("kronchan");
        detail.setAge(24);
        employee.setDetail(detail);
        assertThat(detail.getName(), Matchers.is(employeeDao.save(employee).getDetail().getName()));;
    }
}
```



### 其他测试环境

```
@JsonTest
@DataRedisTest
@RestClientTest

@PropertyMapping
SpringBootTestContextBootstrapper.class
```







### spring事务控制

```
执行上面的新增数据的测试，发现测试通过，但是数据库却并没有新增数据。默认情况下，在每个 JPA 测试结束时，事务会发生回滚。这在一定程度上可以防止测试数据污染数据库。

如果你不希望事务发生回滚，你可以使用@Rollback(false)注解，该注解可以标注在类级别做全局的控制，也可以标注在某个特定不需要执行事务回滚的方法级别上。

也可以显式的使用注解 @Transactional 设置事务和事务的控制级别，放大事务的范围
```

```
sql执行
@Sql
@SqlConfig
@SqlGroup
@SqlMergeMode

事务控制
@Commit
@Rollback
@IfProfileValue
@Repeat
@Timed

事务切面
@AfterTransaction
@BeforeTransaction
```



### spring jdbc 

```
JdbcTestUtils 

countRowsInTable(..)：统计给定表的行数。
countRowsInTableWhere(..)：使用提供的where语句进行筛选统计给定表的行数。
deleteFromTables(..)：删除特定表的全部数据。
deleteFromTableWhere(..)：使用提供的where语句进行筛选并删除给定表的数据。
dropTables(..)：删除指定的表
```



### spring junit 

```
## junit event
AfterTestClass
AfterTestExecution
AfterTestMethod
BeforeTestClass
BeforeTestExecution
BeforeTestMethod
PrepareTestInstance


## junit rules
SpringClassRule
SpringMethodRule
```



### spring通用配置

```
Profile文件
@ActiveProfiles

测试类的启动环境
@BootstrapWith

配置类测试
@ContextConfiguration

@ContextHierarchy

测试构造器注入
@TestConstructor

测试properties文件加载
@TestExecutionListeners
@TestPropertySource
@TestPropertySources
```



### spring web 环境测试

```
@WebAppConfiguration
org.springframework.test.web

ModelAndViewAssert 与 MockHttpServletRequest，MockHttpSession
```







### spring test utils

```
ProfileValueUtils

TestConstructorUtils
TestPropertySourceUtils

TestContextTransactionUtils

TestContextResourceUtils

JdbcTestUtils

AopTestUtils

MetaAnnotationUtils

ReflectionTestUtils
```



### Spring Test 测试注解

Spring框架提供以下Spring特定的注解集合，你可以在单元和集成测试中协同TestContext框架使用它们。请参考相应的JAVA帮助文档作进一步了解，包括默认的属性，属性别名等等。、

#### [**@BootstrapWith**](https://github.com/BootstrapWith)

@BootstrapWith是一个用于配置Spring TestContext框架如何引导的类级别的注解。具体地说，@BootstrapWith用于指定一个自定义的TestContextBootstrapper。请查看[引导TestContext框架](http://docs.spring.io/spring/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/htmlsingle/#testcontext-bootstrapping)作进一步了解。

#### [**@ContextConfiguration**](https://github.com/ContextConfiguration)

@ContextConfiguration定义了类级别的元数据来决定如何为集成测试来加载和配置应用程序上下文。具体地说，@ContextConfiguration声明了用于加载上下文的应用程序上下文资源路径和注解类。

资源路径通常是类路径中的XML配置文件或者Groovy脚本；而注解类通常是使用@Configuration注解的类。但是，资源路径也可以指向文件系统中的文件和脚本，解决类也可能是组件类等等。

```
@ContextConfiguration("/test-config.xml")
public class XmlApplicationContextTests {
// class body...
}
```



```
@ContextConfiguration(classes = TestConfig.class)
public class ConfigClassApplicationContextTests {
 // class body...
}
```

作为声明资源路径或注解类的替代方案或补充，@ContextConfiguration可以用于声明ApplicationContextInitializer`类。`

```
@ContextConfiguration(initializers = CustomContextIntializer.class)
public class ContextInitializerTests {
 // class body...
}
```

@ContextConfiguration偶尔也被用作声明ContextLoader策略。但注意，通常你不需要显示的配置加载器，因为默认的加载器已经支持资源路径或者注解类以及初始化器。



```
@ContextConfiguration(locations = "/test-context.xml", loader = CustomContextLoader.class)
public class CustomLoaderXmlApplicationContextTests {
 // class body...
}
```

> @ContextConfiguration默认对继承父类定义的资源路径或者配置类以及上下文初始化器提供支持。



#### [**@WebAppConfiguration**](https://github.com/WebAppConfiguration)

@WebAppConfiguration是一个用于声明集成测试所加载的ApplicationContext须是WebApplicationContext的类级别的注解。测试类的@WebAppConfiguration注解只是为了保证用于测试的WebApplicationContext会被加载，它使用”file:src/main/webapp”路径默认值作为web应用的根路径（即，资源基路径）。资源基路径用于幕后创建一个`MockServletContext作为测试的WebApplicationContext的ServletContext。`

```
@ContextConfiguration
@WebAppConfiguration("classpath:test-web-resources")
public class WebAppTests {
 // class body...
}
```



注意@WebAppConfiguration必须和@ContextConfiguration一起使用，或者在同一个测试类，或者在测试类层次结构中。请参阅@WebAppConfiguration帮助文档作进一步了解。



#### [**@ContextHierarchy**](https://github.com/ContextHierarchy)

@ContextHierarchy是一个用于为集成测试定义`ApplicationContext层次结构的类级别的注解。@ContextHierarchy应该声明一个或多个@ContextConfiguration实例列表，其中每一个定义上下文层次结构的一个层次。下面的例子展示了在同一个测试类中@ContextHierarchy的使用方法。但是，@ContextHierarchy一样可以用于测试类的层次结构中。`

```
@ContextHierarchy({
 @ContextConfiguration("/parent-config.xml"),
 @ContextConfiguration("/child-config.xml")
})
public class ContextHierarchyTests {
 // class body...
}
@WebAppConfiguration
@ContextHierarchy({
 @ContextConfiguration(classes = AppConfig.class),
 @ContextConfiguration(classes = WebConfig.class)
})
public class WebIntegrationTests {
 // class body...
}
```

如果你想合并或者覆盖一个测试类的层次结构中的`应用程序上下文中指定层次的配置，你就必须在类层次中的每一个相应的层次通过为@ContextConfiguration的name属性提供与该层次相同的值的方式来显示地指定这个层次。请参阅`[`上下文层次关系`](http://docs.spring.io/spring/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/htmlsingle/#testcontext-ctx-management-ctx-hierarchies)`和@ContextHierarchy帮助文档来获得更多的示例。`



#### [**@ActiveProfiles**](https://github.com/ActiveProfiles)

@ActiveProfiles是一个用于当集成测试加载`ApplicationContext的时候声明哪一个`*`bean definition profiles`*`被激活的类级别的注解。`

```
@ContextConfiguration
@ActiveProfiles("dev")
public class DeveloperTests {
// class body...
}
```



```
@ContextConfiguration
@ActiveProfiles({"dev", "integration"})
public class DeveloperIntegrationTests {
// class body...
}
```

> @ActiveProfiles默认为继承激活的在超类声明的 bean definition profiles提供支持。通过实现一个自定义的 ActiveProfilesResolver并通过@ActiveProfiles的resolver属性来注册它的编程的方式来解决激活bean definition profiles问题也是可行的。
>
> 参阅使用环境profiles来配置上下文和@ActiveProfiles帮助文档作进一步了解。

参阅使用环境profiles来配置上下文和@ActiveProfiles帮助文档作进一步了解。



#### [**@TestPropertySource**](https://github.com/TestPropertySource)

@TestPropertySource是一个用于为集成测试加载ApplicationContext时配置属性文件的位置和增加到Environment中的PropertySources集中的内联属性的类级别的注解。

测试属性源比那些从系统环境或者Java系统属性以及通过@PropertySource或者编程方式声明方式增加的属性源具有更高的优先级。而且，内联属性比从资源路径加载的属性具有更高的优先级。

下面的例子展示了如何从类路径中声明属性文件。

```
@ContextConfiguration
@TestPropertySource("/test.properties")
public class MyIntegrationTests {
// class body...
}
```

面的例子展示了如何声明内联属性。

```
@ContextConfiguration
@TestPropertySource(properties = { “timezone = GMT”, “port: 4242” })
public class MyIntegrationTests {
// class body…
}
```



#### [**@DirtiesContext**](https://github.com/DirtiesContext)

@DirtiesContext指明测试执行期间该Spring应用程序上下文已经被弄脏（也就是说通过某种方式被更改或者破坏——比如，更改单例bean的状态）。当应用程序上下文被标为”脏”，它将从测试框架缓存中被移除并关闭。因此，Spring容器将为随后需要同样配置元数据的测试而被重建。

@DirtiesContext可以在同一个类或者类层次结构中的类级别和方法级别中使用。在这个场景下，应用程序上下文将在任意此注解的方法之前或之后以及当前测试类之前或之后被标为“脏”，这取决于配置的methodMode和classMode。

下面的例子解释了在多种配置场景下什么时候上下文会被标为“脏”。

- 当在一个类中声明并将类模式设为BEFORE_CLASS，则在当前测试类之前。

```
@DirtiesContext(classMode = BEFORE_CLASS)
public class FreshContextTests {
// some tests that require a new Spring container
}
```

- 当在一个类中声明并将类模式设为AFTER_CLASS（也就是，默认的类模式），则在当前测试类之后。

```
@DirtiesContext
public class ContextDirtyingTests {
// some tests that result in the Spring container being dirtied
}
```

- 当在一个类中声明并将类模式设为BEFORE_EACH_TEST_METHOD，则在当前测试类的每个方法之前。

```
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class FreshContextTests {
// some tests that require a new Spring container
}
```

- 当在一个类中声明并将类模式设为AFTER_EACH_TEST_METHOD，则在当前测试类的每个方法之后。

```
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class ContextDirtyingTests {
// some tests that result in the Spring container being dirtied
}
```

- 当在一个方法中声明并将方法模式设为BEFORE_METHOD，则在当前方法之前。

```
@DirtiesContext(methodMode = BEFORE_METHOD)
@Test
public void testProcessWhichRequiresFreshAppCtx() {
// some logic that requires a new Spring container
}
```

- 当在一个方法中声明并将方法模式设为AFTER_METHOD(也就是说，默认的方法模式），则在当前方法之后。

```
@DirtiesContext
@Test
public void testProcessWhichDirtiesAppCtx() {
// some logic that results in the Spring container being dirtied
}
```

如果@DirtiesContext被用于上下文被配置为通过@ContextHierarchy定义的上下文层次中的一部分的测试中，则hierarchyMode标志可用于控制如何声明上下文缓存。默认将使用一个穷举算法用于清除包括不仅当前层次而且与当前测试拥有共同祖先的其它上下文层次的缓存。所有在拥有共同祖先上下文的子层次的应用程序上下文都会从上下文中被移除并关闭。如果穷举算法对于特定的使用场景显得有点威力过猛，那么你可以指定一个更简单的当前层算法来代替，如下所。



```
@ContextHierarchy({
@ContextConfiguration("/parent-config.xml"),
@ContextConfiguration("/child-config.xml")
})
public class BaseTests {
// class body...
}

public class ExtendedTests extends BaseTests {

@Test
@DirtiesContext(hierarchyMode = CURRENT_LEVEL)
public void test() {
// some logic that results in the child context being dirtied
}
}
```

参阅DirtiesContext.HierarchyMode帮助文档以获得穷举和当前层算法更详细的了解。



#### [**@TestExecutionListeners**](https://github.com/TestExecutionListeners)

@TestExecutionListeners定义了一个类级别的元数据，用于配置需要用TestContextManager进行注册的`TestExecutionListener实现。通常，@TestExecutionListeners与@ContextConfiguration一起使用。`

```
@ContextConfiguration
@TestExecutionListeners({CustomTestExecutionListener.class, AnotherTestExecutionListener.class})
public class CustomTestExecutionListenerTests {
// class body...
}
```

@TestExecutionListeners默认支持继承监听器。参阅帮助文档获得示例和更详细的了解。



#### [**@Commit**](https://github.com/Commit)

@Commit指定事务性的测试方法在测试方法执行完成后对事务进行提交。@Commit可以用作@Rollback(false)的直接替代，以更好的传达代码的意图。和@Rollback一样，@Commit可以在类层次或者方法层级声明。

```
@Commit
@Test
public void testProcessWithoutRollback() {
// ...
}
```



#### [**@Rollback**](https://github.com/Rollback)

@Rollback指明当测试方法执行完毕的时候是否对事务性方法中的事务进行回滚。如果为true,则进行回滚；否则，则提交（请参加@Commit）。在Spring TestContext框架中，集成测试默认的Rollback语义为true，即使你不显示的指定它。

当被声明为方法级别的注解，则@Rollback为特定的方法指定回滚语义，并覆盖类级别的@Rollback和@Commit语义。



```
@Rollback(false)
@Test
public void testProcessWithoutRollback() {
// …
}
```



#### [**@BeforeTransaction**](https://github.com/BeforeTransaction)

@BeforeTransaction指明通过Spring的@Transactional注解配置为需要在事务中执行的测试方法在事务开始之前先执行注解的void方法。从Spring框架4.3版本起，@BeforeTransaction方法不再需要为public并可能被声明为基于Java8的接口的默认方法。

```
@BeforeTransaction
void beforeTransaction() {
// logic to be executed before a transaction is started
}
```



#### [**@AfterTransaction**](https://github.com/AfterTransaction)

@AfterTransaction指明通过Spring的@Transactional注解配置为需要在事务中执行的测试方法在事务结束之后执行注解的void方法。从Spring框架4.3版本起，@AfterTransaction方法不再需要为public并可能被声明为基于Java8的接口的默认方法。

```
@AfterTransaction
void afterTransaction() {
// logic to be executed after a transaction has ended
}
```



#### [**@Sql**](https://github.com/Sql)

@Sql用于注解测试类或者测试方法，以让在集成测试过程中配置的SQL脚本能够在给定的的数据库中得到执行。

```
@Test
@Sql({"/test-schema.sql", "/test-user-data.sql"})
public void userTest {
// execute code that relies on the test schema and test data
}
```

请参阅[通过@sql声明执行的SQL脚本](http://docs.spring.io/spring/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/htmlsingle/#testcontext-executing-sql-declaratively)作进一步了解。



#### [**@SqlConfig**](https://github.com/SqlConfig)

@SqlConfig定义了用于决定如何解析和执行通过@Sql注解配置的SQL脚本。

```
@Test
@Sql(
scripts = "/test-user-data.sql",
config = @SqlConfig(commentPrefix = "`", separator = "@@")
)
public void userTest {
// execute code that relies on the test data
}
```



#### [**@SqlGroup**](https://github.com/SqlGroup)

@SqlGroup是一个用于聚合几个@Sql注解的容器注解。@SqlGroup可以直接使用，通过声明几个嵌套的@Sql注解，也可以与Java8的可重复注解支持协同使用，即简单地在同一个类或方法上声明几个@Sql注解，隐式地产生这个容器注解。

```
@Test
@SqlGroup({
 @Sql(scripts = "/test-schema.sql", config = @SqlConfig(commentPrefix = "`")),
 @Sql("/test-user-data.sql")
)}
public void userTest {
 // execute code that uses the test schema and test data
}
```



####  标准注解支持

以下注解为Spring TestContext 框架所有的配置提供标准语义支持。注意这些注解不仅限于测试，可以用在Spring框架的任意地方。

- [**@Autowired**](https://github.com/Autowired)
- [**@Qualifier**](https://github.com/Qualifier)
- @Resource(javax.annotation)如果_JSR-250_存在
- @ManagedBean(javax.annotation)如果_JSR-250_存在
- @Inject(javax.inject)如果_JSR-330_存在
- @Named(javax.inject)如果_JSR-330_存在
- @PersistenceContext(javax.persistence)如果JPA存在
- @PersistenceUnit(javax.persistence)如果JPA存在
- [**@Required**](https://github.com/Required)
- [**@Transactional**](https://github.com/Transactional)

> 在Spring TestContext 框架中，[**@PostConstruct**](https://github.com/PostConstruct) 和 [**@PreDestroy**](https://github.com/PreDestroy) 可以通过标准语义在配置于应用程序上下文的任意应用程序组件中使用; 但是, 这些生命周期注解在实际测试类中只有很有限的作用。如果一个测试类的方法被注解为@PostConstruct，这个方法将在test框架中的任何before方法（也就是被JUnit中的@Before注解方法）调用之前被执行, 这个规则将被应用于测试类的每个方法。另一方面，如果一个测试类的方法被注解为 @PreDestroy，这个方法将永远不会被执行。因为建议在测试类中使用test 框架的测试生命周期回调来代替使用[**@PostConstruct**](https://github.com/PostConstruct) and @PreDestroy。



###  Spring JUnit 4 测试注解

#### @IfProfileValue

@IfProfileValue指明该测试只在特定的测试环境中被启用。如果ProfileValueSource配置的name属性与此注解配置的name属性一致，这该测试将被启用。否则，该测试将被禁用并忽略。

@IfProfileValue可以用在类级别、方法级别或者两个同时。使用类级别的@IfProfileValue注解优先于当前类或其子类的任意方法的使用方法级别的注解。有@IfProfileValue注解意味着则测试被隐式开启。这与JUnit4的@Ignore注解是相类似的，除了使用@Ignore注解是用于禁用测试的之外。



```
@IfProfileValue(name="java.vendor", value="Oracle Corporation")
@Test
public void testProcessWhichRunsOnlyOnOracleJvm() {
// some logic that should run only on Java VMs from Oracle Corporation
}
```

或者，你可以 配置@IfProfileValue使用`values列表（或语义）来实现JUnit 4环境中的类似TestNG对测试组的支持。`

```
@IfProfileValue(name="test-groups", values={"unit-tests", "integration-tests"})
@Test
public void testProcessWhichRunsForUnitOrIntegrationTestGroups() {
// some logic that should run only for unit and integration test groups
}
```



#### [**@ProfileValueSourceConfiguration**](https://github.com/ProfileValueSourceConfiguration)

@ProfileValueSourceConfiguration是类级别注解，用于当获取通过@IfProfileValue配置的profile值时指定使用什么样的ProfileValueSource类型。如果一个测试没有指定@ProfileValueSourceConfiguration，那么默认使用SystemProfileValueSource。

```
@ProfileValueSourceConfiguration(CustomProfileValueSource.class)
public class CustomProfileValueSourceTests {
// class body...
}
```



#### [**@Timed**](https://github.com/Timed)

@Timed用于指明被注解的测试必须在指定的时限（毫秒）内结束。如果测试超过指定时限，就当作测试失败。

时限包括测试方法本身所耗费的时间，包括任何重复（请查看@Repeat）及任意初始化和销毁所用的时间。

```
@Timed(millis=1000)
public void testProcessWithOneSecondTimeout() {
 // some logic that should not take longer than 1 second to execute
}
```

Spring的@Timed注解与JUnit 4的@Test(timeout=…)支持相比具有不同的语义。确切地说，由于在JUnit 4中处理方法执行超时的方式（也就是，在独立纯程中执行该测试方法），如果一个测试方法执行时间太长，@Test(timeout=…)将直接判定该测试失败。而Spring的@Timed则不直接判定失败而是等待测试完成。

#### [@Repeat](https://github.com/Repeat)

@Repeat指明该测试方法需被重复执行。注解指定该测试方法被重复的次数。重复的范围包括该测试方法自身也包括相应的初始化和销毁方法。

```
@Repeat(10)
@Test
public void testProcessRepeatedly() {
 // ...
}
```



### Test的所有注解汇总

可以将大部分测试相关的注解当作[meta-annotations](http://docs.spring.io/spring/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/htmlsingle/#beans-meta-annotations)使用，以创建自定义组合注解来减少测试集中的重复配置。

下面的每个都可以在[TestContext](http://docs.spring.io/spring/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/htmlsingle/#testcontext-framework)框架中被当作meta-annotations使用。

- `@BootstrapWith`
- `@ContextConfiguration`
- `@ContextHierarchy`
- `@ActiveProfiles`
- `@TestPropertySource`
- `@DirtiesContext`
- `@WebAppConfiguration`
- `@TestExecutionListeners`
- `@Transactional`
- `@BeforeTransaction`
- `@AfterTransaction`
- `@Commit`
- `@Rollback`
- `@Sql`
- `@SqlConfig`
- `@SqlGroup`
- `@Repeat`
- `@Timed`
- `@IfProfileValue`
- `@ProfileValueSourceConfiguration`

例如，如果发现我们在基于JUnit 4的测试集中重复以下配置…

```
@RunWith(SpringRunner.class)
@ContextConfiguration({"/app-config.xml", "/test-data-access-config.xml"})
@ActiveProfiles("dev")
@Transactional
public class OrderRepositoryTests { }

@RunWith(SpringRunner.class)
@ContextConfiguration({"/app-config.xml", "/test-data-access-config.xml"})
@ActiveProfiles("dev")
@Transactional
public class UserRepositoryTests { }
```

我们可以通过一个自定义的组合注解来减少上述的重复量，将通用的测试配置集中起来，就像这样：

```
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration({"/app-config.xml", "/test-data-access-config.xml"})
@ActiveProfiles("dev")
@Transactional
public @interface TransactionalDevTest { }
```

然后我们就可以像下面一样使用我们自定义的@TransactionalDevTest注解来简化每个类的配置：

```
@RunWith(SpringRunner.class)
@TransactionalDevTest
public class OrderRepositoryTests { }

@RunWith(SpringRunner.class)
@TransactionalDevTest
public class UserRepositoryTests { }
```