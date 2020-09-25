## 反射 相关面试题

### 反射是如何实现的？

根据Class类实现反射，可以拿到该类中的所有信息，包括方法、构造器、属性等等

### 动态代理有几种实现方式？分别如何实现？

有两种方式（代理也分两种，分别是静态代理和动态代理，也就是所谓的修饰着模式）

第一种方式是根据jdk自带的Proxy 来生成代理对象，Proxy是根据接口来生成代理对象的

该类方式需要实现InvocationHandler接口 

```
// Java Proxy
// 1. 首先实现一个InvocationHandler，方法调用会被转发到该类的invoke()方法。
class LogInvocationHandler implements InvocationHandler{
    ...
    private Hello hello;
    public LogInvocationHandler(Hello hello) {
        this.hello = hello;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if("sayHello".equals(method.getName())) {
            logger.info("You said: " + Arrays.toString(args));
        }
        return method.invoke(hello, args);
    }
}
// 2. 然后在需要使用Hello的时候，通过JDK动态代理获取Hello的代理对象。
Hello hello = (Hello)Proxy.newProxyInstance(
    getClass().getClassLoader(), // 1. 类加载器
    new Class<?>[] {Hello.class}, // 2. 代理需要实现的接口，可以有多个
    new LogInvocationHandler(new HelloImp()));// 3. 方法调用的实际处理者
System.out.println(hello.sayHello("I love you!"));
```





第二种方式是根据Cglib来生成代理对象，CGLIB通过继承方式实现代理 （就是动态生成子类）



```
// CGLIB动态代理
// 1. 首先实现一个MethodInterceptor，方法调用会被转发到该类的intercept()方法。
class MyMethodInterceptor implements MethodInterceptor{
  ...
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        logger.info("You said: " + Arrays.toString(args));
        return proxy.invokeSuper(obj, args);
    }
}
// 2. 然后在需要使用HelloConcrete的时候，通过CGLIB动态代理获取代理对象。
Enhancer enhancer = new Enhancer();
enhancer.setSuperclass(HelloConcrete.class);
enhancer.setCallback(new MyMethodInterceptor());

HelloConcrete hello = (HelloConcrete)enhancer.create();
System.out.println(hello.sayHello("I love you!"));
```



### 反射的原理，反射创建类实例的三种方式是什么 ？

```
获取Class对象
在 Java API 中，提供了获取 Class 类对象的三种方法：

第一种，使用 Class.forName 静态方法。

前提：已明确类的全路径名。

第二种，使用 .class 方法。

说明：仅适合在编译前就已经明确要操作的 Class

第三种，使用类对象的 getClass() 方法。
```





获取对象实例共两种方法：

```
直接用字节码文件获取对应实例

// 调用无参构造器 ，若是没有，则会报异常

Object o = clazz.newInstance();　　

有带参数的构造函数的类，先获取到其构造对象，再通过该构造方法类获取实例：



/ /获取构造函数类的对象

Constroctor constroctor = clazz.getConstructor(String.class,Integer.class); /

// 使用构造器对象的newInstance方法初始化对象

Object obj = constroctor.newInstance("龙哥", 29); 

```



### Object.class 返回什么？ 那 Object.class.getClass() 又返回什么？

```
Object.class 返回当前Object对象对应的类实例，
而Object.class.getClass() 返回的则是Class对象对应的类实例
```







### 反射的结构图？

```
GenericDeclaration（返回泛型类型参数）
	Executable（）
		Method
		Constructor
	Class
	
AnnotatedElement（获取注解的超类接口）
// 表示当前类的所有子类是否是可以被动态修改的
	AccessibleObject（public static void setAccessible(AccessibleObject[] array, boolean flag)）
	Executable（是 除了Class以前的集合）
	Field （因为Filed本身不支持返回泛型参数）
	
Type （String getTypeName()）
	Class 
	ParameterizedType
	TypeVariable
	AnnotatedType（返回Annotation的类型）
	ParameterizedType





GenericArrayType
这个也是java.lang.reflect中的接口，如果你翻译成“通用数组类型”那就大错特错了。其实它是用来描述形如A<T>[]或T[]类型的。如此看来称之为“泛型数组”更为适合
```



#### GenericDeclaration

```
GenericDeclaration

这也是java.lang.reflect包中的一个接口，这个接口在很多文章中的翻译是“通用声明”，我看后直接是N脸蒙B的状态，完全不知道他在说什么。经常大量阅读资料后慢慢其解了，应该称它为“可以声明范型变量的实体”。

在他的定义中说的很明白：“只有实现了这个接口的‘实体’才能声明‘范型变量’”。实现了这个接口的“实体”有哪些呢？如下所示：Class，Constructor，Method。

获取当前“实体”上声明的“泛型变量"。

按被声明的顺序返回一个TypeVariable对象的数组，其中TypeVariable对象是由GenericDeclaration对象进行声明的。如果底层没有使用GenericDeclaration声明“范型变量”，那么返回一个长度为0的数组。

下面是一个例子：

public class Main<K extends classA & interfaceB, V> {
    classA<K>[][] key;
    V value;
    public static void main(String[] args) throws Exception{
        TypeVariable[] types = Main.class.getTypeParameters();
        for(TypeVariable type : types){
            System.out.println(type.getName());
        }
    }
}
//输出结果
K
V
```



#### TypeVariable

```
TypeVariable是“类型变量”（或者叫“泛型变量”更准确些）的通用的顶级接口。在泛型编程中会用到“泛型变量”来描述类型，或者说是用来表示泛型。一般用大写字母作为类型变量，比如K、V、E等。

说到TypeVariable<D extends GenericDeclaration>就不得不提起java泛型中另一个比较重要的接口对象，GenericDeclaration接口对象。该接口用来定义哪些对象上是可以声明（定义）“范型变量”，所谓“范型变量”就是<E extends List>或者<E>, 也就是TypeVariable<D extends GenericDeclaration>这个接口的对应的对象，TypeVariable<D extends GenericDeclaration>中的D是extends GenericDeclaration的，用来通过“范型变量”反向获取拥有这个变量的GenericDeclaration。

【注意】类型变量声明（定义）的时候不能有下限（既不能有super），否则编译报错。为什么？T extends classA表示泛型有上限classA，当然可以，因为这样，每一个传进来的类型必定是classA（具有classA的一切属性和方法），但若是T super classA，传进来的类型不一定具有classA的属性和方法，当然就不适用于泛型。

Type[] getBounds()
获得该“范型变量”的上限（上边界），若无显式定义（extends），默认为Object。类型变量的上限可能不止一个，因为可以用&符号限定多个（这其中有且只能有一个为类或抽象类，且必须放在extends后的第一个，即若有多个上边界，则第一个&后必为接口）。

下面是一个例子：

package com.ibestcode.wfso.web.blog.Controller;

import java.util.Map;
import java.lang.reflect.*;
public class Test<K extends Integer & Map, V>{
  K key;
  V value;
  public static void main(String[] args) throws Exception
  {
    Type[] types = Test.class.getTypeParameters();
    for(Type type : types){
      TypeVariable t = (TypeVariable)type;
      System.out.println(t.getGenericDeclaration());
      int size = t.getBounds().length;
      System.out.println(t.getBounds()[size - 1]);
      System.out.println(t.getName() + "\n-------------分割线-------------");
    }
  }
}

// 下面是运行结果
class com.ibestcode.wfso.web.blog.Controller.Test
interface java.util.Map
K
-------------分割线-------------
class com.ibestcode.wfso.web.blog.Controller.Test
class java.lang.Object
V
-------------分割线-------------

Process finished with exit code 0
D getGenericDeclaration()
获得声明（定义）这个“范型变量”的类型及名称，即如：

//当声明这个“范型变量”的GenericDeclaration是一个类时
class com.xxx.xxx.classA  

//当声明这个“范型变量”的GenericDeclaration是一个方法时
public void com.fcc.test.Main.test(java.util.List)   

//当声明这个“范型变量”的GenericDeclaration是一个构造器时
public com.fcc.test.Main()  
String getName()
获得这个“范型变量”在声明（定义）时候的名称。如：K、V、E等

```



#### ParameterizedType

```
ParameterizedType是Type的子接口，表示一个有参数的类型，例如Collection<T>，Map<K,V>等。但实现上 ParameterizedType并不直接表示Collection<T>和Map<K,V>等，而是表示 Collection<String>和Map<String,String>等这种具体的类型。是不是看着眼熟，其实这就是我们常说的泛型。而ParameterizedType代表的是一个泛型的实例，我们就称ParameterizedType为“泛型实例”吧。

当创建泛型P（如：Collection<String>）时，将解析P实例化的泛型类型声明（如：Collection<T>），并且递归地创建P的所有泛型参数（如：String）。

实现这个接口的“类”必须实现一个equals()方法，该方法将任何“泛型类型”（如：Collection<T>）声明相同且“类型参数”（如：String）也相同的两个“类”等同起来。

Type[] getActualTypeArguments()
获取“泛型实例”中<>里面的“泛型变量”（也叫类型参数）的值，这个值是一个类型。因为可能有多个“泛型变量”（如：Map<K,V>）,所以返回的是一个Type[]。

注意：无论<>中有几层<>嵌套，这个方法仅仅脱去最外层的<>，之后剩下的内容就作为这个方法的返回值，所以其返回值类型是不确定的。

煮个栗子：

List<ArrayList> a1;//返回ArrayList，Class类型
List<ArrayList<String>> a2;//返回ArrayList<String>，ParameterizedType类型
List<T> a3;//返回T，TypeVariable类型
List<? extends Number> a4; //返回? extends Number，WildcardType类型
List<ArrayList<String>[]> a5;//返回ArrayList<String>[]，GenericArrayType 类型
Type getRawType()
返回最外层<>前面那个类型，即Map<K ,V>的Map。

Type getOwnerType()
获得这个类型的所有者的类型。这主要是对嵌套定义的内部类而言的，例如于对java.util.Map.Entry<K,V>来说，调用getOwnerType方法返回的就是interface java.util.Map。

如果当前类不是内部类，而是一个顶层类，那么getOwnerType方法将返回null
```



#### GenericArrayType

```
GenericArrayType是Type的子接口，用于表示“泛型数组”，描述的是形如：A<T>[]或T[]的类型。其实也就是描述ParameterizedType类型以及TypeVariable类型的数组，即形如：classA<T>[][]、T[]等。

Type getGenericComponentType()
获取“泛型数组”中元素的类型，要注意的是：无论从左向右有几个[]并列，这个方法仅仅脱去最右边的[]之后剩下的内容就作为这个方法的返回值。
```



#### WildcardType

```
WildcardType是Type的子接口，用于描述形如“? extends classA” 或 “？super classB”的“泛型参数表达式”。

Type[] getUpperBounds()
获取泛型表达式上界.

根据API的注释提示：现阶段通配符表达式仅仅接受一个上边界或者下边界，这个和定义“范型变量”的时候可以指定多个上边界是不一样。但是API说了，为了保持扩展性，这里返回值类型写成了数组形式。实际上现在返回的数组的大小就是1，通配符?指定多个上边界或者下边界现在是会编译出错的（jdk1.7是这样的，至于7及以后就不知道了）。

Type[] getLowerBounds()
获取泛型表达式下界
```

