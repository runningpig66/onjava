package ch20_generics;

import java.util.ArrayList;

/**
 * @author runningpig66
 * @date 2025/12/26 周五
 * @time 16:47
 * P.665 §20.6 类型擦除的奥秘
 * <p>
 * 当你开始更深入地了解泛型时，会发现有些问题起初看起来并不合理。
 * 举例来说，虽然声明 ArrayList.class 是合法的，但声明 ArrayList<Integer>.class 却不行。看看下面的示例：
 * <p>
 * ArrayList<String> 和 ArrayList<Integer> 应该是不同的类型，而不同的类型具有不同的行为。
 * 举例来说，如果你试图将 Integer 放入 ArrayList<String>（这会失败），
 * 便应该得到和将 Integer 放入 ArrayList<Integer>（这会成功）的结果所不同的行为。
 * 然而下面的程序会认为这两者是相同的类型。
 */
public class ErasedTypeEquivalence {
    public static void main(String[] args) {
        Class c1 = new ArrayList<String>().getClass();
        Class c2 = new ArrayList<Integer>().getClass();
        System.out.println(c1.getName());
        System.out.println(c2.getName());
        System.out.println(c1 == c2);
    }
}
/* Output:
java.util.ArrayList
java.util.ArrayList
true
 */
