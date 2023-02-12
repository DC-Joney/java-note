### 桥接方法

#### 定义

在源方法和目标方法之间创建一个中间层，通常作为泛型的类型擦除过程的一部分。

#### 第一个例子

```java
public class Person implements Comparable<Person> {

  @Override
  public int compareTo(Person o) {
    return 0;
  }
}
```

反编译之后的结果为

```java
public class Person implements Comparable {

  @Override
  public int compareTo(Person o) {
    return 0;
  }

  //桥接方法
  @Override
  public int compareTo(Object o) {
    return this.compareTo((Person)o);
  }
}
```

编译器会将泛型擦除，为了兼容1.5之前的版本，编译器创建了一个桥接方法。

```java
public class TestBridgeMethod {

  public static void main(String[] args) throws Exception {
    System.out.println(Person.class.getDeclaredMethod("compareTo", Person.class).isBridge());//false
    System.out.println(Person.class.getDeclaredMethod("compareTo", Object.class).isBridge());//true
  }
}
```

可以看到参数为Object的方法isBridge()为true，表示该方法为桥接方法。

#### 第二个例子

```java
public class TestBridgeMethod2 {

  public static void main(String[] args) throws Exception {
    System.out.println(Father.class.getDeclaredMethod("test").isBridge());
  }

  protected class Father {

    public void test() { }
  }

  public class Son extends Father {

  }
}
```

反编译之后为

```java
public class TestBridgeMethod2 {

  public static void main(String[] args) throws Exception {
    System.out.println(Father.class.getDeclaredMethod("test").isBridge());//true
  }

  protected class Father {

    public void test() { }
  }
  
  public class Son extends Father {
    //桥接方法
    public void test() {
      super.test();
    }
  }
}
```

这种情况编译器也会创建桥接方法。

### 合成方法

#### 定义

编译器为内部目的创建的方法。

#### 示例

```java
public class Father {

  private class Son {

    private String name;
  }

  public String getName() {
    return new Son().name;
  }
}
```

反编译之后的结果为

```java
public class Father {

  private class Son {

    private String name;

    //合成方法
    static String access$100(Son x0) {
        return x0.name;
    }
  }

  public String getName() {
    return return Son.access$100((Son)new Son(this, null));
  }
}
```

测试一下

```java
import java.lang.reflect.Method;

public class TestSyntheticMethod {

  public static void main(String[] args) {
    for (Method declaredMethod : Son.class.getDeclaredMethods()) {
      System.out.println(declaredMethod.getName() + "," + declaredMethod.isSynthetic());
    }
  }
}
```

输出为

```java
access$000,true
```

可以看到，该方法确实为合成方法。上面桥接方法示例中编译器创建的方法也是合成方法。

#### 注意

合成方法的示例代码在java8中可以正常运行，在java11中不能正常运行(获取不到合成方法)，具体原因不知

## Method.isVarArgs()

通过Method.isVarArgs() 来区别是否是可变参数。 ：

```
import java.lang.reflect.Method;

public class MethodDemo {
    public static void main(String... args) {
        Method[] methods = MethodDemo.class.getDeclaredMethods();
        for (Method method : methods) {
            System.out.print(method.getName() + "=>");
            System.out.println(method.isVarArgs());
        }
    }

    public void main2(String[] args) {
        Method[] methods = MethodDemo.class.getDeclaredMethods();
        for (Method method : methods) {
            System.out.print(method.getName() + "=>");
            System.out.println(method.isVarArgs());
        }
    }
}

```



结果

main2=>false
main=>true