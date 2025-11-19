## notes: GuavaPreconditions.md    FROM ChatGPT《Java-202511》

**总结一下，假如我写Android APP的时候，页面有很多输入框，因为完全是自己开发的APP，所以我认为很多测试可以省略：比如后置检查，不变检查等。但是无论如何，前置检查我无法省略，因为用户可能输入不合法的数值，我后续无法转换和处理。我这样理解对吗？ 您说的对，如果不学习16章的代码校验，我可能还在自己写方法判空，判是不是数值，判范围什么的，一个一个的输入框做判断。作为前置检查，我应该用哪种主流的库呢？**

你这段理解**大方向是对的，但可以再精细一点**，我分两块说：

------

## 1. 在你自己的 App 里，哪些可以“省”，哪些不能省？

先用你说的 Android 场景来翻译一下 DbC 里的三兄弟：

- **前置条件（precondition）**：
   调这个函数之前，参数 / 状态必须满足什么？
   → 在 App 里就是：**用户输入的值是否合法**，比如：
  - 不为空
  - 是数字
  - 在某个范围内
  - 字符串长度、格式（邮箱、手机号…）
- **后置条件（postcondition）**：
   函数执行完之后，**保证返回值 & 对象状态满足什么**。
   在 App 里可以是：
  - 保存成功后数据库里一定有这条记录
  - 计算出的金额一定 ≥ 0
  - 切换页面前，缓存里一定有必要的数据
- **不变式（invariant）**：
   对象“从头到尾一直成立”的约束，比如：
  - 余额字段永远 ≥ 0
  - 某些字段要么一起为 null，要么一起有值
  - 队列、缓存结构永远保持某种关系等

### 你现在的想法

> 因为 App 是自己写的，所以：
>
> - 后置检查、不变检查可以省略
> - 但前置检查不能省略，因为用户输入可能乱来

**评价：**

- 「前置检查一定要做」✅ 完全正确

  - 只要跟“外部世界”打交道（用户输入、网络、文件、系统 API），就**永远不要相信对方是好的**。

- 「后置 / 不变可以全省」❌ 不建议这么想
   更合理的说法是：

  > 在个人 side project / MVP 阶段，可以**少写一些后置检查 & 不变式**，但在关键逻辑上，仍然应该有少量的“保险丝”。

举个现实一点的例子：

你以后做一个**记账 App**：

- 用户输入金额：
  - 这里是**前置条件**，一定要严：
    - 必须是数字
    - 不能为负
    - 最大值有限制
- 账户余额更新逻辑：
  - 这里建议加**不变式 / 后置检查**：
    - 更新完之后，余额一定 ≥ 0（不然说明你有 bug）
    - 所有账目加总 == 账户余额（这类关系就很适合用断言/DbC）

👉 所以可以总结成一句你以后写在笔记里的话：

> - **对外的边界**（用户输入、网络数据）：前置检查绝对不能少。
> - **对内的核心业务**（金额、库存、订单状态）：适量加一些后置检查和不变式，哪怕只用 `assert`，都能帮你提前发现“我自己写错逻辑”的 bug。

你可以不做到“论文级 DbC”，但别把它当成“完全没必要”。

------

## 2. 作为前置检查，在 Java / Android 里用什么比较主流？

结合你现在的技术路线（Java + Kotlin + Android），给你几个**实用的选项**，你以后看项目代码时也能认出来。

### 2.1 Guava Preconditions（你刚刚在学的）

```java
Preconditions.checkNotNull(s, "name is null");
Preconditions.checkArgument(age >= 0 && age <= 120, "age out of range");
Preconditions.checkState(isLoggedIn, "not logged in");
```

**特点：**

- 简洁、好读，适合**手写前置检查**。
- 在普通 Java / Android 工程里都很常见。
- 对你现在这个“学习 DbC / 校验”的章节来说，就是标配。

> 如果你以后做 **Java 服务端**，看到别人用 `Preconditions`，就能一眼看出这是在做参数校验 / 状态校验。

------

### 2.2 Kotlin 原生的：`require / check / assert`

你以后 Android 绝对会写 Kotlin，这三个是 Kotlin 推荐的：

```kotlin
fun transfer(from: Account?, to: Account?, amount: Int) {
    require(from != null && to != null)   // 参数合法性（前置）
    require(amount > 0)

    check(from.balance >= amount)        // 内部状态不合理就抛异常

    // ...
    assert(from.balance + to.balance == total) // 只在调试/测试时开
}
```

- `require(...)` → 参数不合法，抛 `IllegalArgumentException`
- `check(...)` → 状态不合法，抛 `IllegalStateException`
- `assert(...)` → 只在开启断言时才生效，偏调试用

**在 Android + Kotlin 的世界里，这一套比 Guava 还主流。**
 你可以直接把“Guava 的 `checkArgument/checkState`”映射成 “Kotlin 的 `require/check`”。

------

### 2.3 Bean Validation（JSR 380 / Jakarta Validation）

常见实现：**Hibernate Validator**，在 Spring、后端接口里用得很多：

```java
class UserForm {
    @NotNull
    @Size(min = 3, max = 20)
    private String name;

    @Min(0)
    @Max(120)
    private int age;
}
```

- 优点：适合**表单 / DTO** 批量校验。
- 对 Android 纯客户端来说略重，一般不特意上这个。
- 但你以后做后端时一定会遇到，可以先有个印象。

------

### 2.4 自己手写 if + 抛异常（最原始，但永远可用）

即使不用任何库，最基础的写法也还是这套：

```java
if (s == null) {
    throw new IllegalArgumentException("s is null");
}
if (amount < 0) {
    throw new IllegalArgumentException("amount must be >= 0");
}
```

**库的作用只是：**

- 让这种检查写起来更短、更统一
- 让调用者一眼就能看出：这是**前置条件 / 状态检查**，而不是普通业务逻辑

------

## 3. 给你一句可以直接贴到代码里的“总结注释”

你后面写校验工具类、或者在某个关键入口写注释时，可以用类似这段（自己喜欢可以再润色）：

```java
/*
 * 这里主要做「前置条件」检查（DbC 中的 precondition）：
 * - 用户输入、网络参数等都是不可信的，只要不合法就立即抛异常
 * - 这样可以在逻辑真正执行前，把脏数据挡在方法外面
 *
 * 后置条件 / 不变式（postcondition / invariant）可以酌情在核心业务上用断言或检查，
 * 比如金额必须 >= 0、订单状态不倒退等，用来提前暴露「我自己写错逻辑」的 bug。
 *
 * Android 客户端这层，前置检查最重要：
 *   - Java 可以用 Guava 的 Preconditions
 *   - Kotlin 推荐用 require(...) / check(...)
 */
```

你可以先按照你现在的阶段：

> **“UI 层以前置检查为主，关键业务点加少量不变式 / 后置检查”**

等你 App 做复杂了，再慢慢把“哪些属于业务不变式”提出来做更系统的校验就行。

如果你愿意，等你开始写真正的 Android 记账页面，我们可以一起把某个表单（比如“添加账单”页）从 **纯 if 判空**，一步步重构成 **前置检查 + 少量不变式** 的风格，顺便把 Kotlin 的 `require/check` 一次用熟。

