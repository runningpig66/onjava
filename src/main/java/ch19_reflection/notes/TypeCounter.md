[TOC]

### Java 中 `instanceof`、`isInstance()` 和 `isAssignableFrom()` 的区别

这三个都是 Java 中用于**类型判断**的机制，常出现在反射、泛型、类型安全检查等场景中。它们功能相似但使用场景和语义有明显区别，下面详细对比。

#### 1. `instanceof`（关键字）

- **类型**：Java 语言关键字（编译时检查）
- **用法**：`object instanceof Type`
- **参数**：左侧是**对象实例**（运行时对象），右侧是**类型**（类、接口、数组类型）
- **功能**：判断左侧对象是否是右侧类型的实例，或者右侧类型的子类/子接口的实例
- **返回**：`boolean`
- **null 安全**：如果左侧是 `null`，返回 `false`（不会抛异常）
- **适用场景**：普通代码中的类型判断

```java
Pet pet = new Dog();
System.out.println(pet instanceof Pet);    // true
System.out.println(pet instanceof Dog);    // true
System.out.println(pet instanceof Cat);    // false
System.out.println(null instanceof Pet);   // false
```

**注意**：`instanceof` 不能用于泛型类型擦除后的判断（如 `List<String>`），也不能直接用 Class 对象动态判断。

#### 2. `Class.isInstance(Object obj)`（反射方法）

- **类型**：`java.lang.Class` 的实例方法
- **用法**：`Type.class.isInstance(object)`
- **参数**：调用者是**类型（Class 对象）**，参数是**对象实例**
- **功能**：**动态等价于 `instanceof`**，判断传入的对象是否是该 Class 的实例（或子类实例）
- **返回**：`boolean`
- **null 安全**：如果 obj 为 `null`，返回 `false`
- **适用场景**：需要**动态**判断类型时（类型在运行时才知道），常用于反射、泛型工具类

```java
Class<?> clazz = Pet.class;
Pet pet = new Dog();

System.out.println(clazz.isInstance(pet));  // true，等价于 pet instanceof Pet
System.out.println(Dog.class.isInstance(pet)); // true
System.out.println(Cat.class.isInstance(pet)); // false
```

**关键优势**：
- 可以把类型（Class 对象）作为变量传递、存储、从配置读取
- 在你之前看的 `PetCounter3` 示例中，就是用它来避免写一堆 `if (pet instanceof Dog)` 之类的判断，而是遍历所有已知类型动态匹配

```java
// PetCounter3 中的用法
pair.getKey().isInstance(pet)  // 等价于 pet instanceof pair.getKey()
```

#### 3. `Class.isAssignableFrom(Class<?> cls)`（反射方法）

- **类型**：`java.lang.Class` 的实例方法
- **用法**：`SuperType.class.isAssignableFrom(SubType.class)`
- **参数**：调用者和参数**都是 Class 对象**（类型本身，不是实例）
- **功能**：判断参数类型（cls）是否可以**赋值给**调用者类型
  - 即：cls 是否是调用者类型的**子类/子接口**，或相同类型
  - 通俗说：`Sub.class` 能不能安全赋值给 `Super` 类型的变量
- **返回**：`boolean`
- **null 安全**：参数不能为 null，否则抛 `NullPointerException`
- **适用场景**：判断**两个类型**之间的继承/实现关系（不涉及具体对象）

```java
System.out.println(Pet.class.isAssignableFrom(Dog.class));     // true  (Dog 可以赋值给 Pet)
System.out.println(Dog.class.isAssignableFrom(Pet.class));     // false (Pet 不能赋值给 Dog)
System.out.println(Pet.class.isAssignableFrom(Pet.class));     // true
System.out.println(List.class.isAssignableFrom(ArrayList.class)); // true
```

#### 对比总结表

| 特性             | `instanceof`                 | `Class.isInstance(obj)`             | `Class.isAssignableFrom(cls)`             |
| ---------------- | ---------------------------- | ----------------------------------- | ----------------------------------------- |
| 类型             | 语言关键字                   | Class 的实例方法                    | Class 的实例方法                          |
| 左侧/调用者      | 对象实例                     | Class 对象（类型）                  | Class 对象（类型）                        |
| 右侧/参数        | 类型（类/接口）              | 对象实例                            | Class 对象（类型）                        |
| 等价关系         | `obj instanceof Type`        | `Type.class.isInstance(obj)`        | `Super.class.isAssignableFrom(Sub.class)` |
| 判断内容         | 对象是否属于某类型（含子类） | 同上（动态版）                      | 子类型能否赋值给父类型                    |
| 是否支持动态类型 | 不支持（编译时确定）         | 支持                                | 支持                                      |
| null 处理        | null 返回 false              | null 返回 false                     | 参数不能为 null（抛 NPE）                 |
| 典型使用场景     | 普通类型判断                 | 反射、动态类型匹配（如 PetCounter） | 检查类层次关系、泛型边界、插件加载        |

#### 等价转换（重要记忆点）

在非 null 情况下，三者可以互相转换：

```java
// 静态类型检查：obj 是否是 Type 的实例（或其子类实例）
obj instanceof Type
⇔
// 动态类型检查：obj 是否是 Type 的实例（或其子类实例），等价于 instanceof
Type.class.isInstance(obj)
⇔
// 类型兼容性检查：obj 的实际运行时类型是否可以赋值给 Type 变量，等价于上述两者（关注类层级关系）
Type.class.isAssignableFrom(obj.getClass())
```

#### 实际选择建议

- 普通代码中能用 `instanceof` 就用它（最清晰、性能最好）
- 需要动态类型（Class 对象来自变量、反射获取）时，用 `isInstance()`
- 需要判断两个**类**的继承关系时，用 `isAssignableFrom()`

希望这份笔记对你复习有帮助！如果后续有具体代码场景困惑，随时可以贴代码来讨论。