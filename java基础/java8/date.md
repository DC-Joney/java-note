### java8 Date 学习网站

 https://www.jianshu.com/p/433d923031c8 



TemporalAccessor

- Month
- DayOfWeek
- MonthDay
- ZoneOffset
- Termpol



TemporalAdjuster(addJustTo)

- OffsetTime
- ChronoLocalDate
- ChronoLocalDateTime
- Month (java.time)
- MonthDay (java.time)
- Instant
- OffsetDateTime
- Year
- DayOfWeek 
- LocalDate 
- LocalTime 
- YearMonth
- ZoneOffset 



utils： TemporalAdjusters





Termpol

- ChronoLocalDate
  - LocalDate：表示的是当前时区的日期格式 （ yyyy-MM-dd）
- ChronoLocalDateTime
  - LocalDateTime: 表示的是当前的全部日期以及时间格式（yyyy-MM-dd HH:mm:ss:S）
- LocalTime: 表示的是时间日期格式为主的（HH:mm:ss）
- Instant（和Date差不多，但是表示的是以秒以及毫秒为基础的时间）
- OffsetDateTime (可以指定时区)
- OffsetTime (可以指定时区)
- Year（当前年份）
- YearMonth（当前年、月份）



TemporalAmount

- Duration(针对的是以天 或者比天更小的单位)
- ChronoPeriod
  - Period（针对的是天，或者比天更大的单位）





TermporalQueries（查询某个时间字段）

utils：TemporalQueries



TemporalField

- ChronoField（指的是时间的字段）

相关Utils工具类：

WeekFields

IsoFields





TemporalUnit

- ChronoUnit（返回的是时间的单位）





DateTimeFormatter（时间转换 格式化）

DateTimeFormatterBuilder

DecimalStyle

FormatStyle

ResolverStyle

SignStyle
TextStyle



```
ZoneId 表示的是时区
```



LocalDateTime、LocalTime、LocalDate、Instance

isAfter() equals() isBefore()



### java8date 与 Date做转换

#### 1.LocalDate转Date

```java
LocalDate nowLocalDate = LocalDate.now();
Date date = Date.from(localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
```

#### 2.LocalDateTime转Date

```java
LocalDateTime localDateTime = LocalDateTime.now();
Date date = Date.from(localDateTime.atZone(ZoneOffset.ofHours(8)).toInstant());
```

#### 3.Date转LocalDateTime(LocalDate)

```java
Date date = new Date();
LocalDateTime localDateTime = date.toInstant().atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
LocalDate localDate = date.toInstant().atZone(ZoneOffset.ofHours(8)).toLocalDate();
```

#### 4.LocalDate转时间戳

```java
LocalDate localDate = LocalDate.now();
long timestamp = localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
```

#### 5.LocalDateTime转时间戳

```java
LocalDateTime localDateTime = LocalDateTime.now();
long timestamp = localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
```

#### 6.时间戳转LocalDateTime(LocalDate)

```java
long timestamp = System.currentTimeMillis();
LocalDate localDate = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDate();
LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
```



### java8date 与 java.sql.Date做转换

```
Jsr310JpaConverters（jsr310转换）
```



### spring java8 date 与json做转换

```
DateTimeFormatterRegistrar
```

