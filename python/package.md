### Python package



Python 导入包的方法：

- from xx import xx

- import xx as xx
- from xx import *

```

from xx import  *

这个 * 代表的是 __init__.py 中定义的 __all__,允许加载什么模块
```



```
__name__ 的含义：

当 该模块做为主模块的时候，也就是当前文件是主文件之后的时候，name名称就为main

当模块做为依赖导入的时候，__name__ 就是他的模块名称


main.py
from test import demo

print(__name__) #main

#输出 
main
demo 


demo.py
print(__name__)


上面的demo就是代表引入demo模块后的__name__名称
```



 ### 语法

from 包 import 模块

from 包.模块 import  类|函数|变量

from 包.模块 import  *

from .xxx import xxx  # .xxx 代表当前模块下的文件



### \__init__.py的作用

如过一个文件下存在\__init__.py就代表文件是一个python package

如过导入一个包，就会默认执行\__init__.py文件下的东西

```
from 模块 import * 表示可以使用模块里边的所有内容，
如过没有定义_all__ 所有的都可以访问，
如过加上了_all__，则只能访问__all__ 中定义的模块
```



### 循环导入

1、from xxx import xxx 可以写在函数中，可以避免两个文件循环导入

2、将入到语句放在模块的最后