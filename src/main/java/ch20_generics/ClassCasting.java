package ch20_generics;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * @author runningpig66
 * @date 2026/1/8 周四
 * @time 20:59
 * P.711 §20.10 问题 §20.10.3 类型转换和警告
 * <p>
 * 你则会被强制转型，而此时你还并未告知要这么做。要解决这个问题，你需要使用 Java 5 中引入的转型方式，通过泛型类来转型：
 * 翻译：你被迫需要在这里进行强转（因为 readObject 返回 Object），但你写的这个强转代码，
 * 并没有真正‘告知’运行时系统去检查 <Widget> 这个类型（因为运行时做不到）。
 * 即使是 Class.cast() 这种看起来很“动态”、很“反射”的高级方法，在泛型面前也是无能为力。
 * 因为 Java 泛型是擦除的，所以你永远无法在运行时安全地转换泛型容器。你没法获得 List<Widget>.class 这种令牌。
 * 不管你用 (List<Widget>) 还是 List.class.cast()，在这个场景下，你都无法摆脱“未检查警告”。
 * <p>
 * 针对从外部（文件、网络）反序列化的泛型集合，运行时无法恢复其泛型参数类型。没有任何技巧能消除这个风险。
 * 必须使用 @SuppressWarnings("unchecked") 显式抑制警告，并由程序员保证外部数据的正确性。
 */
public class ClassCasting {
    @SuppressWarnings("unchecked")
    public void f(String[] args) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(args[0]));
        // 尝试使用 Java 5 的 Class.cast() 动态转型，试图绕过警告，但这是徒劳的。

        // 没法获取 List<Widget>.class 这种“泛型令牌”，只能获取 List.class。
        // Won't Compile:
        //- List<Widget> lw1 = List<>.class.cast(in.readObject());

        // List.class 代表原生类型 (Raw Type)。List.class.cast() 返回的也是原生 List。
        // 把原生 List 赋值给泛型引用 List<Widget> 时，依然会触发 "unchecked conversion" 警告。
        List<Widget> lw2 = List.class.cast(in.readObject());
        // ClassCasting.java:26: warning: [unchecked] unchecked conversion
        // List<Widget> lw2 = List.class.cast(in.readObject());
        //                                   ^
        // required: List<Widget>
        // found:    List
        // 1 warning
    }
}
