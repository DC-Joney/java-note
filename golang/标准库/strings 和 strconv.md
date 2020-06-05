## 字符串标准库

strings 库 适用于 对字符串的一些处理，比如拼接，修剪

strconv 用于与其他类型的转换



### 前缀和后缀

 `HasPrefix` 判断字符串 `s` 是否以 `prefix` 开头： 

```go
 strings.HasPrefix(s, prefix string) bool 
```

 `HasSuffix` 判断字符串 `s` 是否以 `suffix` 结尾： 

```go
 strings.HasSuffix(s, suffix string) bool 
```

示例：

```go
package main

import (
    "fmt"
    "strings"
)

func main() {
    var str string = "This is an example of a string"
    fmt.Printf("T/F? Does the string \"%s\" have prefix %s? ", str, "Th")
    fmt.Printf("%t\n", strings.HasPrefix(str, "Th"))
}
```



###  判断子字符串或字符在父字符串中出现的位置（索引）

 `Index` 返回字符串 `str` 在字符串 `s` 中的索引（`str` 的第一个字符的索引），-1 表示字符串 `s` 不包含字符串 `str`： 

```go
strings.Index(s, str string) int
```

LastIndex 返回字符串 str 在字符串 s 中最后出现位置的索引（str 的第一个字符的索引），-1 表示字符串 s 不包含字符串 str：

```go
strings.LastIndex(s, str string) int
```

如果 ch 是非 ASCII 编码的字符，建议使用以下函数来对字符进行定位：

```go
strings.IndexRune(s string, r rune) int
```



### 字符串替换

Replace 用于将字符串 str 中的前 n 个字符串 old 替换为字符串 new，并返回一个新的字符串，如果 n = -1 则替换所有字符串 old 为字符串 new：

```go
strings.Replace(str, old, new, n) string
```



### 统计字符串出现次数

 `Count` 用于计算字符串 `str` 在字符串 `s` 中出现的非重叠次数： 

```go
strings.Count(s, str string) int
```



### 重复字符串

 `Repeat` 用于重复 `count` 次字符串 `s` 并返回一个新的字符串： 

```go
strings.Repeat(s, count int) string
```



### 修改字符串大小写

ToLower 将字符串中的 Unicode 字符全部转换为相应的小写字符：

```go
strings.ToLower(s) string
```

ToUpper 将字符串中的 Unicode 字符全部转换为相应的大写字符：

```go
strings.ToUpper(s) string
```



### 修剪字符串

你可以使用 strings.TrimSpace(s) 来剔除字符串开头和结尾的空白符号；如果你想要剔除指定字符，则可以使用 strings.Trim(s, "cut") 来将开头和结尾的 cut 去除掉。该函数的第二个参数可以包含任何字符，如果你只想剔除开头或者结尾的字符串，则可以使用 TrimLeft 或者 TrimRight 来实现



### 分割字符串

strings.Fields(s) 利用空白作为分隔符将字符串分割为若干块，并返回一个 slice 。如果字符串只包含空白符号，返回一个长度为 0 的 slice 。

strings.Split(s, sep) 自定义分割符号对字符串分割，返回 slice 。

因为这 2 个函数都会返回 slice，所以习惯使用 for-range 循环来对其进行处理



### 拼接 slice 到字符串

 `Join` 用于将元素类型为 string 的 slice 使用分割符号来拼接组成一个字符串： 

```go
strings.Join(sl []string, sep string) string
```

示例：

```go
package main

import (
    "fmt"
    "strings"
)

func main() {
    str := "The quick brown fox jumps over the lazy dog"
    sl := strings.Fields(str)
    fmt.Printf("Splitted in slice: %v\n", sl)
    for _, val := range sl {
        fmt.Printf("%s - ", val)
    }
    fmt.Println()
    str2 := "GO1|The ABC of Go|25"
    sl2 := strings.Split(str2, "|")
    fmt.Printf("Splitted in slice: %v\n", sl2)
    for _, val := range sl2 {
        fmt.Printf("%s - ", val)
    }
    fmt.Println()
    str3 := strings.Join(sl2,";")
    fmt.Printf("sl2 joined by ;: %s\n", str3)
}

输出结果：
Splitted in slice: [The quick brown fox jumps over the lazy dog]
The - quick - brown - fox - jumps - over - the - lazy - dog -
Splitted in slice: [GO1 The ABC of Go 25]
GO1 - The ABC of Go - 25 -
sl2 joined by ;: GO1;The ABC of Go;25
```



### 从字符串中读取内容

函数 strings.NewReader(str) 用于生成一个 Reader 并读取字符串中的内容，然后返回指向该 Reader 的指针，从其它类型读取内容的函数还有：

- Read() 从 [] byte 中读取内容。
- ReadByte() 和 ReadRune() 从字符串中读取下一个 byte 或者 rune。



### 字符串与其它类型的转换

 该包包含了一些变量用于获取程序运行的操作系统平台下 int 类型所占的位数，如：`strconv.IntSize`。 

 任何类型 **T** 转换为字符串总是成功的。 

针对从数字类型转换到字符串，Go 提供了以下函数：

- strconv.Itoa(i int) string 返回数字 i 所表示的字符串类型的十进制数。
- strconv.FormatFloat(f float64, fmt byte, prec int, bitSize int) string 将 64 位浮点型的数字转换为字符串，其中 fmt 表示格式（其值可以是 'b'、'e'、'f' 或 'g'），prec 表示精度，bitSize 则使用 32 表示 float32，用 64 表示 float64。



将字符串转换为其它类型 tp 并不总是可能的，可能会在运行时抛出错误 parsing "…": invalid argument。

针对从字符串类型转换为数字类型，Go 提供了以下函数：

- strconv.Atoi(s string) (i int, err error) 将字符串转换为 int 型。
- strconv.ParseFloat(s string, bitSize int) (f float64, err error) 将字符串转换为 float64 型。



利用多返回值的特性，这些函数会返回 2 个值，第 1 个是转换后的结果（如果转换成功），第 2 个是可能出现的错误，因此，我们一般使用以下形式来进行从字符串到其它类型的转换：

val, err = strconv.Atoi(s)

```go
package main

import (
    "fmt"
    "strconv"
)

func main() {
    var orig string = "666"
    var an int
    var newS string

    fmt.Printf("The size of ints is: %d\n", strconv.IntSize)      

    an, _ = strconv.Atoi(orig)
    fmt.Printf("The integer is: %d\n", an) 
    an = an + 5
    newS = strconv.Itoa(an)
    fmt.Printf("The new string is: %s\n", newS)
}

输出结果：
64 位系统：
The size of ints is: 64
32 位系统：
The size of ints is: 32
The integer is: 666
The new string is: 671
```

