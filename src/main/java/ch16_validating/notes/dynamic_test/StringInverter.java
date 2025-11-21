package ch16_validating.notes.dynamic_test;

/**
 * @author runningpig66
 * @date 2025/11/20 周四
 * @time 21:05
 * 这个系列是为了学习动态测试，请求 GPT 生成的完整案例，项目的目录如下：
 * <pre>
 * src
 *  ├── main
 *  │   └── java
 *  │       └── ch16_validating
 *  │           └── notes
 *  │               └── dynamic_test
 *  │                   ├── StringInverter.java // ※业务接口
 *  │                   ├── Inverter1.java // 第一版实现：逐个字符判断大小写
 *  │                   ├── Inverter2.java // 第二版实现：用 StringBuilder，但逻辑跟 Inverter1 一样
 *  │                   ├── Inverter3.java // 第三版实现：故意留个 bug —— 只处理小写 → 大写，大写保持不变
 *  │                   └── Inverter4.java // 第四版实现：用 Java 8 流来改写
 *  └── test
 *      └── java
 *          └── ch16_validating
 *              └── notes
 *                  └── dynamic_test
 *                      ├── SimpleInverterTest.java // 仅对第一版实现 Inverter1.java 的普通测试类
 *                      ├── ManyInvertersNaiveTest.java // 分别对 4个版本的朴素测试写法：复制粘贴地狱。
 *                      ├── DynamicStringInverterTestsV1.java // ※一个小工具：给“所有版本”生成 DynamicTest
 *                      └── DynamicStringInverterTests.java // 版本 2: 用 Function<StringInverter, Boolean> 表达“属性是否成立”
 * </pre>
 * 业务接口：是把传入字符串的大小写反转：大写变小写，小写变大写，其他字符原样返回
 * notes: StringInverter.md 为什么要引入动态测试？
 */
public interface StringInverter {
    String invert(String str);
}
