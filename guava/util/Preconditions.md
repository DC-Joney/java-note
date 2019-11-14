### Preconditions 

Preconditions 类比于 Spring的 Assert 或者类比于Objects 类，实现了 前置条件预判断，不满足抛出异常

Guava在 `Preconditions`类中提供了若干前置条件判断的实用方法。每个方法都有三个变种：

- 没有额外参数：抛出的异常中没有错误消息；
- 有一个Object对象作为额外参数：抛出的异常使用Object.toString() 作为错误消息；
- 有一个String对象作为额外参数，并且有一组任意数量的附加Object对象：这个变种处理异常消息的方式有点类似printf，只支持%s指示符。例如

```
checkArgument(i >= 0, "Argument was %s but expected nonnegative", i);
checkArgument(i < j, "Expected i < j, but %s > %s", i, j);
```



| 方法声明（不包括额外参数）                                   | 描述                                                         | 检查失败时抛出的异常      |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------- |
| [`checkArgument(boolean)`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Preconditions.html#checkArgument(boolean)) | 检查boolean是否为true，用来检查传递给方法的参数。            | IllegalArgumentException  |
| [`checkNotNull(T)`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Preconditions.html#checkNotNull(T)) | 检查value是否为null，该方法直接返回value，因此可以内嵌使用checkNotNull`。` | NullPointerException      |
| [`checkState(boolean)`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Preconditions.html#checkState(boolean)) | 用来检查对象的某些状态。                                     | IllegalStateException     |
| [`checkElementIndex(int index, int size)`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Preconditions.html#checkElementIndex(int, int)) | 检查index作为索引值对某个列表、字符串或数组是否有效。index>=0 && index<size * | IndexOutOfBoundsException |
| [`checkPositionIndex(int index, int size)`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Preconditions.html#checkPositionIndex(int, int)) | 检查index作为位置值对某个列表、字符串或数组是否有效。index>=0 && index<=size * | IndexOutOfBoundsException |
| [`checkPositionIndexes(int start, int end, int size)`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Preconditions.html#checkPositionIndexes(int, int, int)) | 检查[start, end]表示的位置范围对某个列表、字符串或数组是否有效* | IndexOutOfBoundsException |

