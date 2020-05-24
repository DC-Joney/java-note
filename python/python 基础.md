## type,object class 之间的关系

type 也是object 对象 class 是type的对象，但是 object 和  class 都是type 类型

https://www.cnblogs.com/Eva-J/p/7277026.html

## python内置类型

![image-20200517215106697](assets/image-20200517215106697.png)

### 浅拷贝 深拷贝

copy(xxx,xxxx) 浅拷贝

```
import copy 

copy.deepcopy(list) 深拷贝
```



### 可变与不可变

int bool float tuple str (不可变参数)

list dict set（可变参数）



### 函数



####　普通类型

传入的是 不可变 就是 复制的值



#### 可变类型

传入的是地址



#### 默认值与关键字参数

```
def func(a=10,ll):
	...
	
func(ll=1)	
```



#### 可变参数

```
def func(*agrs):
	...

*args 代表的就是可变参数
```





#### 拆包

```
list1 = [1, 2, 3, 4, 5]

a, b, *c = list1

print(a, b, c)

输出：1,2,[3,4,5]
```



```python
print(123)
```

