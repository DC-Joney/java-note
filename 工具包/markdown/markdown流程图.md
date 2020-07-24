

## markdown 流程图

一直在用markdown（不得不说markdown语法真的太强大太简洁了，效果也太优美，没学过markdown语法的可以自己学下）写东西，知道用markdown可以画出来很性感的流程图，遂决定学下如何用markdown来画流程图。

### 代码



```php
flow
st=>start: Start
op=>operation: Your Operation
cond=>condition: Yes or No?
e=>end
st->op->cond
cond(yes)->e
cond(no)->op
```

------

### 效果

![img](https:////upload-images.jianshu.io/upload_images/1713353-7715410cedf27866.png?imageMogr2/auto-orient/strip|imageView2/2/w/291/format/webp)

深度截图20160909223653.png

------

### 说明

这样几行简单的代码就生成了一个优雅的流程图。
 流程图大致分为两段，第一段是定义元素，第二段是定义元素之间的走向。

#### 定义元素的语法



```bash
tag=>type: content:>url
```

- tag就是元素名字，
- type是这个元素的类型，有6中类型，分别为：

>  

- start         # 开始
- end           # 结束
- operation     # 操作
- subroutine    # 子程序
- condition     # 条件
- inputoutput   # 输入或产出
- content就是在框框中要写的内容，注意type后的冒号与文本之间一定要有个空格。
- url是一个连接，与框框中的文本相绑定

#### 连接元素的语法

用**->**来连接两个元素，需要注意的是condition类型，因为他有yes和no两个分支，所以要写成



```php
c2(yes)->io->e 
c2(no)->op2->e
```

------

### 实际应用

下边献上我的拙作，这是一个爬取某网站的商品评论数据，然后进行情感分析的小项目，有四个模块：获取待爬取商品id，爬取代理，爬取评论，情感分析

#### 代码



```php
flow
st=>start: Start|past:>http://www.google.com[blank]
e=>end: End:>http://www.google.com
op1=>operation: get_hotel_ids|past
op2=>operation: get_proxy|current
sub1=>subroutine: get_proxy|current
op3=>operation: save_comment|current
op4=>operation: set_sentiment|current
op5=>operation: set_record|current

cond1=>condition: ids_remain空?
cond2=>condition: proxy_list空?
cond3=>condition: ids_got空?
cond4=>condition: 爬取成功??
cond5=>condition: ids_remain空?

io1=>inputoutput: ids-remain
io2=>inputoutput: proxy_list
io3=>inputoutput: ids-got

st->op1(right)->io1->cond1
cond1(yes)->sub1->io2->cond2
cond2(no)->op3
cond2(yes)->sub1
cond1(no)->op3->cond4
cond4(yes)->io3->cond3
cond4(no)->io1
cond3(no)->op4
cond3(yes, right)->cond5
cond5(yes)->op5
cond5(no)->cond3
op5->e
```

#### 效果

![img](https:////upload-images.jianshu.io/upload_images/1713353-70d5e8127a5a12d9.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

流程图.png

这个流程图有个问题，我希望ids_remain的两个条件都为空，但是markdown语法没法实现我这需求（不知道我这需求是不是有毛病），只能先这样画着了。