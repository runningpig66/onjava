## notes: StringInverter.md 

**为什么要引入动态测试？@Test这种测试很简单很好理解，但是您可以从代码层面开始讲吗？不要直接就说需求：有很多版本，要跑同一套测试。你可以从一个类开始，告诉要测试什么，后来发生了什么问题，每个类都不少，整理可以跑通的一个案例吗？从普通的@Test完整的实现，包括实际的Java代码和需要测试用的@Test代码。然后先跑通，再一步一步引出问题，再到我们这次的代码，内容多长都可以，但是要非常连贯，请给我整套的代码，从0到1到2，我照着在IDEA上写一遍就能理解了。**

好，我们这回完全从“写代码”开始，一步一步把你带到 `@TestFactory` 这套动态测试上去，中间每一个类都给你完整代码，你照抄到 IDEA 里就能跑。

我用的包名统一写成 `ch16_validating`，你可以按自己工程微调，但最好保持一致，这样少踩坑。

目录建议

```text
src
 ├── main
 │   └── java
 │       └── ch16_validating
 │           ├── StringInverter.java
 │           ├── Inverter1.java
 │           ├── Inverter2.java
 │           ├── Inverter3.java
 │           └── Inverter4.java
 └── test
     └── java
         └── ch16_validating
             ├── SimpleInverterTest.java
             ├── ManyInvertersNaiveTest.java
             ├── DynamicStringInverterTestsV1.java
             └── DynamicStringInverterTests.java
```

------

## 第 1 步：只有一个实现，用最普通的 @Test

### 1.1 业务接口 + 第一版实现

```
src/main/java/ch16_validating/StringInverter.java
package ch16_validating;

/**
 * 把传入字符串的大小写反转：
 *  - 大写变小写
 *  - 小写变大写
 *  - 其他字符原样返回
 */
public interface StringInverter {
    String invert(String str);
}
src/main/java/ch16_validating/Inverter1.java
package ch16_validating;

/**
 * 第一版实现：逐个字符判断大/小写。
 */
public class Inverter1 implements StringInverter {

    @Override
    public String invert(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // 大写 → 小写，小写 → 大写，其它字符原样
            if (Character.isUpperCase(c)) {
                result.append(Character.toLowerCase(c));
            } else if (Character.isLowerCase(c)) {
                result.append(Character.toUpperCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
```

### 1.2 最普通的测试类

```
src/test/java/ch16_validating/SimpleInverterTest.java
package ch16_validating;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleInverterTest {

    @Test
    void basicInversion() {
        StringInverter inverter = new Inverter1();
        assertEquals("aBc", inverter.invert("AbC"));
        assertEquals("hELLO", inverter.invert("Hello"));
    }

    @Test
    void emptyString() {
        StringInverter inverter = new Inverter1();
        assertEquals("", inverter.invert(""));
    }
}
```

> 现在先什么都别想，直接运行 `SimpleInverterTest`，看它变绿。
>  这一层你肯定没压力：一个实现，一个测试类，两三个 `@Test` 方法。

------

## 第 2 步：实现越来越多，复制粘贴开始爆炸

现在假设你要试验好几种不同的实现方式（这也是 OnJava 的套路）：
 我们再写 3 个版本，其中故意搞一个“有 bug”的。

### 2.1 多个实现

```
src/main/java/ch16_validating/Inverter2.java
package ch16_validating;

/**
 * 第二版实现：用 StringBuilder，但逻辑跟 Inverter1 一样。
 * 这里我们假装以后要在这里扩展更多功能。
 */
public class Inverter2 implements StringInverter {

    @Override
    public String invert(String str) {
        StringBuilder result = new StringBuilder(str.length());
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result.append(Character.toLowerCase(c));
            } else if (Character.isLowerCase(c)) {
                result.append(Character.toUpperCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
src/main/java/ch16_validating/Inverter3.java
package ch16_validating;

/**
 * 第三版实现：故意留个 bug —— 只处理小写 → 大写，
 * 大写保持不变（错误实现，用来验证测试是否能抓出来）。
 */
public class Inverter3 implements StringInverter {

    @Override
    public String invert(String str) {
        StringBuilder result = new StringBuilder(str.length());
        for (char c : str.toCharArray()) {
            if (Character.isLowerCase(c)) {
                result.append(Character.toUpperCase(c));
            } else {
                // BUG：大写不反转
                result.append(c);
            }
        }
        return result.toString();
    }
}
src/main/java/ch16_validating/Inverter4.java
package ch16_validating;

/**
 * 第四版实现：用 Java 8 流来写（只是换个写法）。
 */
public class Inverter4 implements StringInverter {

    @Override
    public String invert(String str) {
        StringBuilder result = new StringBuilder();
        str.chars().forEach(codePoint -> {
            char c = (char) codePoint;
            if (Character.isUpperCase(c)) {
                result.append(Character.toLowerCase(c));
            } else if (Character.isLowerCase(c)) {
                result.append(Character.toUpperCase(c));
            } else {
                result.append(c);
            }
        });
        return result.toString();
    }
}
```

现在我们有 **4 个实现**。

### 2.2 朴素测试写法：复制粘贴地狱

```
src/test/java/ch16_validating/ManyInvertersNaiveTest.java
package ch16_validating;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManyInvertersNaiveTest {

    // 场景 1：基本大小写反转
    @Test
    void inverter1_basic() {
        StringInverter inverter = new Inverter1();
        assertEquals("aBc", inverter.invert("AbC"));
    }

    @Test
    void inverter2_basic() {
        StringInverter inverter = new Inverter2();
        assertEquals("aBc", inverter.invert("AbC"));
    }

    @Test
    void inverter3_basic() {
        StringInverter inverter = new Inverter3();
        assertEquals("aBc", inverter.invert("AbC")); // 会失败
    }

    @Test
    void inverter4_basic() {
        StringInverter inverter = new Inverter4();
        assertEquals("aBc", inverter.invert("AbC"));
    }

    // 场景 2：空字符串
    @Test
    void inverter1_empty() {
        StringInverter inverter = new Inverter1();
        assertEquals("", inverter.invert(""));
    }

    @Test
    void inverter2_empty() {
        StringInverter inverter = new Inverter2();
        assertEquals("", inverter.invert(""));
    }

    @Test
    void inverter3_empty() {
        StringInverter inverter = new Inverter3();
        assertEquals("", inverter.invert(""));
    }

    @Test
    void inverter4_empty() {
        StringInverter inverter = new Inverter4();
        assertEquals("", inverter.invert(""));
    }
}
```

你可以跑一下这个测试类：

- 你会看到 **8 个测试**（4 个实现 × 2 个场景）。
- 有些会失败（特别是 `Inverter3` 的 basic 场景）。

到这里为止——

*你已经亲手体验到了：**

> “同一个场景，要对不同实现重复写一大堆 `@Test` 方法”。

现在只是 2 个场景、4 个实现就 8 个 `@Test`；
 如果现实里是 10 个场景、10 个实现，就是 100 个 `@Test`，而逻辑几乎一模一样——只差类名不同。

这就是书里说的“所有版本都要跑同一套测试”时的痛点。

------

## 第 3 步：把重复抽出来，用动态测试干掉复制粘贴（版本 1：Consumer 写法）

接下来我们改测试，不动业务类。

### 3.1 先把“所有版本”集中成一个列表

在测试类里写一个静态列表：

```java
private static final List<StringInverter> VERSIONS = List.of(
        new Inverter1(),
        new Inverter2(),
        new Inverter3(),
        new Inverter4()
);
```

以后新增 `Inverter5`，只要往这个列表里加一行就行。

### 3.2 一个小工具：给“所有版本”生成 DynamicTest

```
src/test/java/ch16_validating/DynamicStringInverterTestsV1.java
package ch16_validating;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class DynamicStringInverterTestsV1 {

    // 目前所有要测试的实现
    private static final List<StringInverter> VERSIONS = List.of(
            new Inverter1(),
            new Inverter2(),
            new Inverter3(),
            new Inverter4()
    );

    /**
     * 工具方法：
     *   给定一个“场景描述” + 对单个实现的检查逻辑（Consumer），
     *   为 VERSIONS 中的每个实现生成一个 DynamicTest。
     */
    private Stream<DynamicTest> forAllVersions(
            String description,
            Consumer<StringInverter> check
    ) {
        return VERSIONS.stream().map(inv ->
                dynamicTest(
                        inv.getClass().getSimpleName() + " : " + description,
                        () -> check.accept(inv)
                )
        );
    }

    // ===== 下面开始写具体场景 =====

    /** 场景 1：基本大小写互换 */
    @TestFactory
    Stream<DynamicTest> basicInversion() {
        return forAllVersions("basic inversion", inverter -> {
            assertEquals("aBc", inverter.invert("AbC"));
        });
    }

    /** 场景 2：空字符串 */
    @TestFactory
    Stream<DynamicTest> emptyString() {
        return forAllVersions("empty string", inverter -> {
            assertEquals("", inverter.invert(""));
        });
    }
}
```

先运行这个类看看效果：

- JUnit 会找到两个 `@TestFactory` 方法：
  - `basicInversion()`
  - `emptyString()`
- 每个方法内部通过 `forAllVersions(...)` 生成 **4 个 DynamicTest**。
- 所以总共还是 **8 个测试用例**，但这次：
  - 对每个场景，代码只写了一遍；
  - 新增版本只改 `VERSIONS`；
  - 新增场景只多写一个 `@TestFactory`，把“对一个实现怎么测”写清楚即可。

> 这就是“把数据（各个实现）和行为（场景）分开，把组合交给框架做”。

------

## 第 4 步：升级成 OnJava 里的写法（版本 2：Function 风格）

上面的 `Consumer<StringInverter>` 写法，你已经能理解了：
 `forAllVersions("描述", inverter -> { assertEquals(...); })`。

OnJava 的作者换了一个风格：不在 lambda 里面直接写 `assertEquals`，
 而是写成“返回 boolean”的 **性质函数**，由外层统一判断：

```java
Function<StringInverter, Boolean> property
```

我们就对刚才的代码稍微改造一下，变成跟书上更接近的版本。

```
src/test/java/ch16_validating/DynamicStringInverterTests.java
package ch16_validating;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class DynamicStringInverterTests {

    private static final List<StringInverter> VERSIONS = List.of(
            new Inverter1(),
            new Inverter2(),
            new Inverter3(),
            new Inverter4()
    );

    // 安全比较字符串，避免 null 抛异常
    private static boolean isEqual(String a, String b) {
        return a == null ? b == null : a.equals(b);
    }

    /**
     * 把“一个场景”应用到所有版本上：
     *
     * @param description 场景描述，用于测试名
     * @param property    性质函数：给一个 StringInverter，返回是否通过该场景
     */
    private Stream<DynamicTest> testVersions(
            String description,
            Function<StringInverter, Boolean> property
    ) {
        return VERSIONS.stream().map(inv ->
                dynamicTest(
                        inv.getClass().getSimpleName() + " : " + description,
                        () -> {
                            boolean ok = property.apply(inv);
                            if (!ok) {
                                throw new AssertionError(
                                        "Test failed for " + inv.getClass().getSimpleName()
                                                + " : " + description);
                            }
                        }
                )
        );
    }

    // ===== 具体场景 =====

    /** 场景 1：基本大小写互换应该成功 */
    @TestFactory
    Stream<DynamicTest> basicInversion_shouldSucceed() {
        String in = "AbC";
        String out = "aBc";

        return testVersions(
                "basic inversion (should succeed)",
                inverter -> isEqual(inverter.invert(in), out)
        );
    }

    /** 场景 2：空字符串应该原样返回 */
    @TestFactory
    Stream<DynamicTest> emptyString_shouldSucceed() {
        return testVersions(
                "empty string (should succeed)",
                inverter -> isEqual(inverter.invert(""), "")
        );
    }
}
```

这里的逻辑你可以对照刚才的 Consumer 版本看：

- 之前是：

  ```java
  forAllVersions("basic", inv -> {
      assertEquals("aBc", inv.invert("AbC"));
  });
  ```

- 现在变成：

  ```java
  testVersions("basic", inv -> isEqual(inv.invert("AbC"), "aBc"));
  ```

区别：

1. **场景 lambda 的返回值**
   - 原来：`Consumer<StringInverter>` —— 不返回值，内部自己 `assertEquals`。
   - 现在：`Function<StringInverter, Boolean>` —— 返回 true/false，由外层统一抛 `AssertionError`。
2. **好处**
   - 性质函数本身就可以在别处复用，比如以后你想在别的测试类里也用；
   - 逻辑更接近“数学上的性质测试”，更符合 OnJava 这个章节的味道。

> 这就是你之前看到的那句：
>  “所有版本都会复制到 testVersions() 中，这些不同实现展示了我们如何一步一步添加新功能，最终满足所有测试要求。”

------

## 可以动手的顺序建议

你可以按这个顺序在 IDEA 里一点点搭：

1. 先只建 `StringInverter` + `Inverter1` + `SimpleInverterTest`，跑一跑。
2. 再加 `Inverter2/3/4` + `ManyInvertersNaiveTest`，感受一下复制粘贴。
3. 然后写 `DynamicStringInverterTestsV1`（Consumer 版本），体会一下：
   - “一个场景 + 一个工具方法 + 一个版本列表 => 自动生成 N 个测试”的感觉。
4. 最后把 V1 复制一份改成 `DynamicStringInverterTests`，改成 `Function<Boolean>` 风格，对照 OnJava 的源码看。

等你把上面这几个类都亲手敲一遍并跑过，你再回头去看书里的 `DynamicStringInverterTests.java`，基本就不会再“雾水”，因为你已经从：

> 一个实现 → 多个实现 & 普通 @Test → 动态测试 V1 → 动态测试（书上那版）

完整走完一条技术演化线了。

如果你想继续深入，我们还可以在这个基础上再加第三步：
 **把版本列表也抽象成“工厂列表”，支持懒创建 / 参数化实现**，这个是 OnJava 里 testVersions 进一步玩的花样。等你把现在这条线吃透，我们再玩后续进阶。

