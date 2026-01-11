package ch20_generics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author runningpig66
 * @date 2026/1/7 周三
 * @time 16:58
 * P.708 §20.10 问题 §20.10.1 基本类型不可作为类型参数
 * <p>
 * 下面是另一种方式，创建一个由 Byte 组成的 Set：
 */
public class ByteSet {
    // 场景 1：数组初始化上下文，编译器对数组初始化语法提供了特殊支持（赋值上下文）：
    // 虽然 1, 2 等字面量默认为 int 类型，但在数组赋值中，编译器会自动检查数值范围。
    // 若数值在 byte 范围内，编译器会自动执行“窄化并装箱”操作 (int -> byte -> Byte)，因此编译通过。
    Byte[] possibles = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    Set<Byte> mySet = new HashSet<>(Arrays.asList(possibles));

    // 场景 2：显式窄化转换，这是一个正确的写法。
    // 1. 手动介入：通过 (byte) 1 显式执行了“窄化原始类型转换” (int -> byte)。
    // 2. 自动装箱：此时参数已是 byte 基本类型，编译器仅需执行标准的“装箱转换” (byte -> Byte)。
    // 这种分步处理避免了让编译器同时处理两层隐式转换，符合 JLS (Java语言规范) 的要求。
    Set<Byte> mySet1 = new HashSet<>(
            Arrays.asList((byte) 1, (byte) 2, (byte) 3));

    // 场景 3：方法调用上下文，编译错误：varargs mismatch; int cannot be converted to Byte。
    // 1. 上下文限制：在方法调用中，编译器不应用赋值上下文的特殊规则。Arrays.<Byte>asList 显式指定参数必须为 Byte 类型。
    // 2. 转换链失效：Java 不支持在单次调用中执行“两步隐式转换”链：
    // 即无法自动完成 int (字面量) -> byte (窄化) -> Byte (装箱) 的连续跳跃。
    // 编译器只能尝试将 int 装箱为 Integer，但这与泛型参数 <Byte> 类型不兼容。
    // But you can't do this:
    //- Set<Byte> mySet2 = new HashSet<>(
    //-         Arrays.<Byte>asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    // ByteSet.java:36: error: method asList in class Arrays cannot be applied to given types;
    // Arrays.<Byte>asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    //                 ^
    // required: T[]
    // found:    int,int,int,int,int,int,int,int,int
    // reason: varargs mismatch; int cannot be converted to Byte
    // where T is a type-variable:
    // T extends Object declared in method <T>asList(T...)
    // error
}
