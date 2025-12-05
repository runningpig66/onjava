[TOC]

**然后我还有2个问题。第1个问题是，下面这段代码，你可以讲一下为什么执行结果是以下吗？** 

```java
Path p6 = Path.of(".\\a\\b\\..\\..\\..\\..\\").normalize();
show(12, p6);
```

执行结果是：

```java
(12)  ..\..
```

**第2个问题是：**

```java
E:\IdeaProjects\onjava\src\main\java\ch17_files\.\..\..
```

上面这个路径经过.normalize()后，程序给出的结果是：

```java
E:\IdeaProjects\onjava\src\main
```

而我是这样计算的：
ch17_files的后面开始发现可以优化的项目：".\\"代表当前目录所以消除，优化成：

```java
E:\IdeaProjects\onjava\src\main\java\ch17_files\..\..
```

继续发现"..\\"代表上级目录所以依次删除："..\\"，再删除"ch17_files\\"，结果是：

```java
E:\IdeaProjects\onjava\src\main\java\.
```

继续删除".."。此时我出现了疑问，不清楚是删除"\\.."，还是只删除".."，因为这对最终的结果字符串是不同的。
假设继续删除的是".."，然后再删除真实的"java\\"那么结果变成了：

```java
E:\IdeaProjects\onjava\src\main\
```

而程序的执行结果是：

```java
E:\IdeaProjects\onjava\src\main
```

比我的结果少了一个"\\"。

也就是说，我在整个执行的过程中删除冗余的字符路径时，我目前的意识是：从某个相对路径开始计算的，例如"..\\"，而不是"\\.."，以这种相对路径的方式去看待优化的过程，如同我上面推导的过程一样。但是很明显结果比程序多出来一个"\\"。请您分析我的困惑。

------

先说一句：你这套测试写得很细，我大致扫了一遍，逻辑都说得过去，的确是把书上的点都「落到地上」了 👍

下面直接回答你问的两个小问题。

------

## 问题 1：为什么 p6.normalize() 变成 `..\..`？

代码：

```java
Path p6 = Path.of(".\\a\\b\\..\\..\\..\\..\\").normalize();
show(12, p6);   // (12)  ..\..
```

**关键点：`normalize()` 不是做字符串替换，而是对「路径片段」做栈操作。**

### 1.1 先把路径拆成片段

`.\\a\\b\\..\\..\\..\\..\\` 在 Windows 上被解析为这些 name 元素（忽略最后那个反斜杠，因为它只是结尾分隔符，不是一个片段）：

- `"."`
- `"a"`
- `"b"`
- `".."`
- `".."`
- `".."`
- `".."`

这是一个**相对路径**，没有盘符、没有根。

### 1.2 `normalize()` 的规则（可以脑补成一个栈）：

对每个片段按顺序处理：

- 遇到 `"."`：直接丢弃。
- 遇到普通名字（例如 `"a"`, `"b"`）：压栈。
- 遇到 `".."`：
  - 如果栈顶是普通名字，就把栈顶弹掉（抵消一级目录）。
  - 如果栈为空，或者栈顶也是 `".."`，**那就把 `".."` 本身压栈**（因为已经没有东西可以抵消了）。

### 1.3 套到你的例子上

从空栈开始：

1. `"."` → 丢掉，栈：`[]`
2. `"a"` → 入栈，栈：`["a"]`
3. `"b"` → 入栈，栈：`["a", "b"]`
4. `".."` → 抵消 `"b"`，栈：`["a"]`
5. `".."` → 抵消 `"a"`，栈：`[]`
6. `".."` → 栈空了，无法抵消 → 把 `".."` 入栈，栈：`[".."]`
7. `".."` → 栈顶是 `".."` 也不能抵消 → 再入栈，栈：`["..", ".."]`

最后栈里就是两级上级目录：`["..", ".."]`

重新用系统分隔符拼起来就是：`"..\\.."`

所以输出 `(12)  ..\..` 完全符合 `normalize()` 的逻辑。

**直观理解**：

- 前两个 `..` 把 `a\b` 抵消掉了；
- 后两个 `..` 已经「没有可删的东西」，就只能原样保留成 `..\..`。

------

## 问题 2：为什么 normalize 之后变成

`E:\IdeaProjects\onjava\src\main` 而不是 `E:\IdeaProjects\onjava\src\main\`？

原始路径：

```text
E:\IdeaProjects\onjava\src\main\java\ch17_files\.\..\..
```

你手动推导的过程是这样的（我用你说的思路复盘一下）：

1. 先去掉 `.\`：
    `E:\IdeaProjects\onjava\src\main\java\ch17_files\..\..`
2. 处理第一个 `..`：删掉 `ch17_files\..`：
    `E:\IdeaProjects\onjava\src\main\java\..`
3. 处理第二个 `..`：删掉 `java\..`：
    找不到用「删 `\..` 还是删 `..`」来描述比较准确，结果你写成：
    `E:\IdeaProjects\onjava\src\main\`

你看到程序结果是：

```text
E:\IdeaProjects\onjava\src\main
```

比你脑中多了一个「少了的反斜杠」，于是困惑：到底是按 `"..\"` 还是 `"\.."` 去删？

------

### 2.1 先按照 Path 的「组件」视角来走一遍

对于 Java 的 `Path`，这个字符串会被解析为：

- 根（root）：`"E:\"`
- 名字序列（name elements）：
  1. `"IdeaProjects"`
  2. `"onjava"`
  3. `"src"`
  4. `"main"`
  5. `"java"`
  6. `"ch17_files"`
  7. `"."`
  8. `".."`
  9. `".."`

注意两点：

- `.\` 中的 `.` 是一个独立片段；
- 最后的 `..` 是两个连续片段：一个 `..`、再一个 `..`。

现在对这些 **name 元素** 做 normalize（同样用栈）：

起始：栈 `[]`，root = `"E:\"`

1. `"IdeaProjects"` → 栈：`["IdeaProjects"]`
2. `"onjava"` → 栈：`["IdeaProjects", "onjava"]`
3. `"src"` → 栈：`["IdeaProjects", "onjava", "src"]`
4. `"main"` → 栈：`["IdeaProjects", "onjava", "src", "main"]`
5. `"java"` → 栈：`["IdeaProjects", "onjava", "src", "main", "java"]`
6. `"ch17_files"` → 栈：`["IdeaProjects", "onjava", "src", "main", "java", "ch17_files"]`
7. `"."` → 丢掉，栈不变
8. 第一个 `".."` → 弹掉 `"ch17_files"`
    栈：`["IdeaProjects", "onjava", "src", "main", "java"]`
9. 第二个 `".."` → 再弹掉 `"java"`
    栈：`["IdeaProjects", "onjava", "src", "main"]`

最终 names = `["IdeaProjects","onjava","src","main"]`，带上 root：

```text
E:\IdeaProjects\onjava\src\main
```

这里 **根本不存在一个“单独存储的结尾 `\`”**。路径结构就是：

- root: `"E:\"`
- names: `"IdeaProjects"`, `"onjava"`, `"src"`, `"main"`

`toString()` 只会把 root 和每个 name 用分隔符拼起来：

> ```
> root + name1 + "\" + name2 + "\" + ...
> ```

不会再额外加一个「装饰用的多余 `\`」。

------

### 2.2 你用“删字符串”推导，和 Path 的模型不一样在哪里？

你现在的思路是：把整条路径当成一个字符串，然后用「`..\`」或者「`\..`」去删，这是一种**纯字符串**视角。

但 Java 的 `Path` 是这样想的：

> 「我有一个 root，再加一个字符串数组（每个元素是一个路径片段 name），
>  我对这个数组做栈操作，最后再把它们用分隔符连起来。」

在这个模型里，根本没「删 `..\`」或者「删 `\..`」这回事：

- `"java\.."` 不是一个整体；
- 实际是两个片段：`"java"` 和 `".."`；
- 处理 `".."` 时，是「弹出上一段 `"java"`」，然后 `".."` 自己也不保留；
- 剩下的是 `"main"` 这个片段，前后分隔符都是输出时生成的，不是存储在某个「字符串里」的字面字符。

所以你脑子里那条：

```text
E:\IdeaProjects\onjava\src\main\
```

在 `Path` 的内部表示里，和

```text
E:\IdeaProjects\onjava\src\main
```

是 **完全一样的路径**，只是你在手算时习惯在最后多写了一个 `\` 来表示「这个还是个目录」。

Java 的 `Path` 不会记住这个「最后这个用于人类阅读的装饰性斜杠」。

可以验证这一点（在 Windows 上）：

```java
Path p1 = Paths.get("E:\\IdeaProjects\\onjava\\src\\main");
Path p2 = Paths.get("E:\\IdeaProjects\\onjava\\src\\main\\");

System.out.println(p1.equals(p2));  // true
System.out.println(p1.toString());  // E:\IdeaProjects\onjava\src\main
System.out.println(p2.toString());  // E:\IdeaProjects\onjava\src\main
```

两个路径是相等的，`toString()` 打印也一样——说明 `Path` 根本没记「最后有没有 `\`」。

------

### 2.3 关于你纠结的那一步：“删 `\..` 还是删 `..`？”

从 `Path` 的观点看，这个问题其实不存在。

更准确的描述是：

- 当前片段序列里有 `"java"` 和 `".."` 两个元素；
- 处理 `".."` 时，从**片段列表**里删掉 `"java"` 这个元素，然后 `".."` 自己也不进入结果；
- 最终结果只剩 `"main"` 这个 name 作为最后一个片段；
- 至于字符串里的 `\`，只是把这些片段再串联起来时插进去的，不是某个「被删还是没被删的字符」。

所以以后你在脑中推导时，建议换一个视角：

> **不要从字符串看，从「片段数组 + 根」的角度看。**

比如上面这个例子就可以直接写成：

```text
root: "E:\"
names: [IdeaProjects, onjava, src, main, java, ch17_files, ., .., ..]
normalize(names) -> [IdeaProjects, onjava, src, main]
→ "E:\IdeaProjects\onjava\src\main"
```

这样就不会再被最后那个「多写出来的 `\`」绕晕了。

------

### 小结

- **问题 1**：
   `.\\a\\b\\..\\..\\..\\..\\` → 片段为 `[".", "a", "b", "..", "..", "..", ".."]`
   normalize 之后栈里剩下 `["..", ".."]` → `"..\\.."`
- **问题 2**：
   `E:\IdeaProjects\onjava\src\main\java\ch17_files\.\..\..` 被分解成一串 path 片段，
   normalize 时只是在「名字列表」上做栈操作，最终得到 names = `[IdeaProjects, onjava, src, main]`，
   `toString()` 只会拼出 `E:\IdeaProjects\onjava\src\main`，不会特意保留末尾的 `\`。
   你手算多出来的那个 `\`，只是人为表示「目录」的习惯写法，不属于 `Path` 的结构信息。

如果你愿意，我可以把这整套 normalize 的「栈模型」写成一段简化版 Java 代码，你在自己项目里跑一跑，就能更直观看到每一步是怎么压栈/弹栈的。
