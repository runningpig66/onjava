## notes: StormyInning.md

**这是我根据 OnJava 的练习代码和书中的摘录段落写的带注释的代码。**
**我有一点不清楚： event() 方法同时存在于基类 Inning 和接口 Storm 中，**
**但是在基类和接口中声明的两个 event() 抛出的异常不同，导致我这里是不是无法让重写的 event() 方法抛出任何异常了？**
**我似乎只能这样收窄异常，收窄到无法写任何异常，才能正常的重写 event() 方法。**
**另外，假设基类 Inning 和接口 Storm 中的 event() 方法的抛出异常是相同的，**
**那么我如果没有重写需求的话，就可以不用强制在 StormyInning 中手动重写 event() 方法，**
**因为基类 Inning 已经自动帮我实现了接口 Storm 中的 event() 方法。**
**但是如今，由于基类和接口中声明的两个 event() 抛出的异常不同，**
**导致我必须强制在 StormyInning 中手动重写 event() 方法，且重写的 event() 方法不能抛出任何异常（异常收窄），**
**我的理解对吗？**

你理解得基本是**完全正确的**，我们把规则用更精确的语言捋一遍，顺便给你一两句可以写进笔记里的话。

------

## 1. 先回答你的两个问题

### 问题 1：现在这个例子里，`event()` 在 `StormyInning` 里是不是**不能再声明任何受检异常**了？

是的，在这个例子里 **`StormyInning.event()` 不能再声明任何受检异常**，所以书上才写成：

```
@Override
public void event() { }     // 不写 throws
```

原因是：

- `Inning.event()`：`public void event() throws BaseballException`
- `Storm.event()`：`void event() throws RainedOut`

`StormyInning.event()` 同时：

1. **重写** `Inning.event()`，又
2. **实现** `Storm.event()`。

对一个“同时来自基类和接口”的方法，Java 的规则是：

> 重写方法中每一个 **受检异常 E**，都必须是
>
> - 父类版本 throws 列表里某个异常的**子类**，并且
> - 接口版本 throws 列表里某个异常的**子类**。

也就是：
 **允许的受检异常集合 =「父类 throws 列表」和「接口 throws 列表」的交集（按“子类”关系算交集）。**

在本例里：

- 父类允许：`BaseballException` 及其子类
- 接口允许：`RainedOut` 及其子类

这两棵继承树**互不相干**，没有任何类既是 `BaseballException` 的子类，又是 `RainedOut` 的子类，所以交集是空的。
 因此：

> `StormyInning.event()` **不能声明任何新的受检异常**，只好把 `throws` 列表收窄到“什么都没有”。

> 当然，你依然可以在里面抛 `RuntimeException` / `Error` 之类的未检查异常，但这些本来就不需要出现在 throws 里。

------

### 问题 2：如果基类和接口的 `event()` 抛的是**同一个异常**，是不是就可以不用重写？

是的，你的理解对。

如果改成：

```
abstract class Inning {
    public void event() throws RainedOut { }
}

interface Storm {
    void event() throws RainedOut;
}
```

那么：

```
class StormyInning extends Inning implements Storm {
    // 什么都不写也能通过编译
}
```

- `StormyInning` 直接从 `Inning` 继承了 `event()`；
- 这个继承来的 `event()` 的签名和 throws 列表，**刚好符合** `Storm.event()` 的要求；
- 所以编译器认为“接口的方法已经被继承的方法实现了”，你不需要再手动重写。

在你现在的原始代码里，`Inning.event()` 的 throws 是 `BaseballException`，**不符合** 接口 `Storm.event()` 的 throws（`RainedOut`），所以：

> `StormyInning` 必须自己提供一个兼容二者的 `event()` 实现，
>  结果就是你看到的：**只能收窄到不抛受检异常**。

------

## 2. 给你一段可以写进笔记的总结

你可以这样记：

> **当一个方法同时继承自基类和接口时，它在子类中的 `throws` 列表必须是：
>  「基类声明的受检异常」∩「所有接口声明的受检异常」的子集。
>  如果交集为空，就只能把受检异常收窄到“一个也不声明”。**

配到你这个例子里就是：

- `Inning.event()`：允许 `BaseballException` 体系
- `Storm.event()`：允许 `RainedOut` 体系
- 交集为空 → `StormyInning.event()` 只能写成 `public void event() {}`

这样以后你几个月再翻回来看，也能立刻想起现在这个冲突场景。
