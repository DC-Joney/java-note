Function分为四大类

- Consumer （void类型，一般是处理一些数据）

  - BiConsumer（T，T）

  ```
  void accept(T t);
  
  default Consumer<T> andThen(Consumer<? super T> after) {
          Objects.requireNonNull(after);
          return (T t) -> { accept(t); after.accept(t); };
  }
  ```

​    

- Predicate （用来做比较返回的是boolean类型）

  - BiPredicate

  ```
  boolean test(T t);
  
  // &&
  default Predicate<T> and(Predicate<? super T> other) {
      Objects.requireNonNull(other);
      return (t) -> test(t) && other.test(t);
  }
  
  //！ 反过来的
  default Predicate<T> negate() {
    return (t) -> !test(t);
  }
  
  
  default Predicate<T> or(Predicate<? super T> other) {
    Objects.requireNonNull(other);
    return (t) -> test(t) || other.test(t);
  }
  
  // ||
  // 生成一个Predicate对象，使用的是equal判断
  static <T> Predicate<T> isEqual(Object targetRef) {
    return (null == targetRef)
            ? Objects::isNull
            : object -> targetRef.equals(object);
  }
  ```

  

- Function （做数据转换）

  ```
  R apply(T t);
  
  //返回生成key类型
  default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
  	Objects.requireNonNull(before);
  	return (V v) -> apply(before.apply(v));
  }
  	
  // 对 function返回的类型再做处理
  default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
  	Objects.requireNonNull(after);
   	return (T t) -> after.apply(apply(t));
  }
      
  static <T> Function<T, T> identity() {
     	return t -> t;
  }
  ```

  - BiFunction

    - BinaryOperator

  - UnaryOperator

    ```
    UnaryOperator<T> extends Function<T, T>
    static <T> UnaryOperator<T> identity() {
            return t -> t;
    }
    ```

- Supplier（用来生产数据）

  ```
   T get();
  ```

  

  

  

  

针对基本类型的 FunctionInterface介绍

| Int                 | Long                 | Boolean         | Dubbo                |
| ------------------- | -------------------- | --------------- | -------------------- |
| IntConsumer         | LongConsumer         | BooleanSupplier | DoubleConsumer       |
| IntFunction         | LongFunction         |                 | DoubleFunction       |
| IntBinaryOperator   | LongBinaryOperator   |                 | DoubleBinaryOperator |
| IntSupplier         | LongSupplier         |                 | DoubleSupplier       |
| IntPredicate        | LongPredicate        |                 | DoublePredicate      |
| IntToLongFunction   | LongToIntFunction    |                 | DoubleToIntFunction  |
| IntToDoubleFunction | LongToDoubleFunction |                 | DoubleToLongFunction |
| IntUnaryOperator    | LongUnaryOperator    |                 | DoubleUnaryOperator  |
| ToIntFunction       | ToLongFunction       |                 | ToDoubleFunction     |
| ToIntBiFunction     | ToLongBiFunction     |                 | ToDoubleBiFunction   |
| ObjIntConsumer      | ObjLongConsumer      |                 | ObjDoubleConsumer    |