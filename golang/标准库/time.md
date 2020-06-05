## time 时间和日期

当前时间可以使用 time.Now() 获取，或者使用 t.Day()、t.Minute() 等等来获取时间的一部分；你甚至可以自定义时间格式化字符串，例如： fmt.Printf("%02d.%02d.%4d\n", t.Day(), t.Month(), t.Year()) 将会输出 21.07.2011。



### 格式化字符串

包中的一个预定义函数 func (t Time) Format(layout string) string 可以根据一个格式化字符串来将一个时间 t 转换为相应格式的字符串，你可以使用一些预定义的格式，如：time.ANSIC 或 time.RFC822。

```go
fmt.Println(t.Format("02 Jan 2006 15:04")) 
```

示例：

```go
package main
import (
    "fmt"
    "time"
)

var week time.Duration
func main() {
    t := time.Now()
    fmt.Println(t) // e.g. Wed Dec 21 09:52:14 +0100 RST 2011
    fmt.Printf("%02d.%02d.%4d\n", t.Day(), t.Month(), t.Year())
    // 21.12.2011
    t = time.Now().UTC()
    fmt.Println(t) // Wed Dec 21 08:52:14 +0000 UTC 2011
    fmt.Println(time.Now()) // Wed Dec 21 09:52:14 +0100 RST 2011
    // calculating times:
    week = 60 * 60 * 24 * 7 * 1e9 // must be in nanosec
    week_from_now := t.Add(week)
    fmt.Println(week_from_now) // Wed Dec 28 08:52:14 +0000 UTC 2011
    // formatting times:
    fmt.Println(t.Format(time.RFC822)) // 21 Dec 11 0852 UTC
    fmt.Println(t.Format(time.ANSIC)) // Wed Dec 21 08:56:34 2011
    fmt.Println(t.Format("21 Dec 2011 08:52")) // 21 Dec 2011 08:52
    s := t.Format("20111221")
    fmt.Println(t, "=>", s)
    // Wed Dec 21 08:52:14 +0000 UTC 2011 => 20111221
}
```

如果你需要在应用程序在经过一定时间或周期执行某项任务（事件处理的特例），则可以使用 time.After 或者 time.Ticker：我们将会在第 14.5 节讨论这些有趣的事情。 另外，time.Sleep（Duration d） 可以实现对某个进程（实质上是 goroutine）时长为 d 的暂停。
