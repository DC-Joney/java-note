## OS 获取系统信息

获取环境变量

```go
var (
    HOME = os.Getenv("HOME")
    USER = os.Getenv("USER")
    GOROOT = os.Getenv("GOROOT")
)

下面这个例子展示了如何通过 runtime 包在运行时获取所在的操作系统类型，以及如何通过 os 包中的函数 os.Getenv() 来获取环境变量中的值，并保存到 string 类型的局部变量 path 中。

package main

import (
    "fmt"
   "runtime"
    "os"
)

func main() {
    var goos string = runtime.GOOS
    fmt.Printf("The operating system is: %s\n", goos)
    path := os.Getenv("PATH")
    fmt.Printf("Path is %s\n", path)
}
```

