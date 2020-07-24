### Spring mvc 教程

 ### @RequestMapping注解

**正则URL匹配**

支持在URI模板变量中使用正则表达式。 语法是{varName：regex}，其中第一部分定义了变量名，第二部分定义了正则表达式。 



**Ant 路径匹配** 除了URI模板之外，@RequestMapping注释和所有组合的@RequestMapping变体也支持Ant样式的路径模式（例如/myPath/*.do）。 还支持URI模板变量和Ant-style glob的组合（例如/ owners / * / pets / {petId}） 



**支持占位符**

 @RequestMapping注释中的模式支持对本地属性和/或系统属性和环境变量的$ {…}占位符。 在将控制器映射到的路径可能需要通过配置进行定制的情况下，这可能是有用的 

