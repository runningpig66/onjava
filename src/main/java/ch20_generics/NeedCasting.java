package ch20_generics;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * @author runningpig66
 * @date 2026/1/8 周四
 * @time 20:48
 * P.711 §20.10 问题 §20.10.3 类型转换和警告
 * <p>
 * 有时使用泛型并不意味着不需要转型，而这会导致编译器产生不正确的警告。例如：
 * <p>
 * 你则会被强制转型，而此时你还并未告知要这么做。（要解决这个问题，你需要使用 Java 5 中引入的转型方式，通过泛型类来转型：见 ClassCasting.java）
 * 翻译：你被迫需要在这里进行强转（因为 readObject 返回 Object），
 * 但你写的这个强转代码，并没有真正‘告知’运行时系统去检查 <Widget> 这个类型（因为运行时做不到）。
 */
public class NeedCasting {
    @SuppressWarnings("unchecked")
    public void f(String[] args) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(args[0]));
        // 在 Java 泛型中，没有任何完美的“强转”方案能够彻底消除“未检查（Unchecked）”的隐患。
        // 因为擦除。运行时 JVM 只能检查这个对象是不是 List，没办法检查里面装的是不是 Widget。
        List<Widget> shapes = (List<Widget>) in.readObject();
        // NeedCasting.java:18: warning: [unchecked] unchecked cast
        // List<Widget> shapes = (List<Widget>) in.readObject();
        //                                                   ^
        // required: List<Widget>
        // found:    Object
        // 1 warning
    }
}
