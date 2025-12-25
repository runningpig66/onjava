package ch20_generics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 20:45
 * P.654 §20.4 泛型方法 §20.4.1 可变参数和泛型方法
 * <p>
 * 泛型方法和可变参数列表可以和平共处：这里的 makeList() 方法实现了和标准库中的 java.util.Arrays.asList() 方法一样的功能。
 */
public class GenericVarargs {
    // @SafeVarargs 注解表示我们承诺不会对变量参数列表做任何修改，实际上也是这样的，因为我们只会对它进行读取。
    // 如果没有这个注解，编译器便无法知道（我们不会修改变量参数列表），并会产生警告：Possible heap pollution from parameterized vararg type
    @SafeVarargs
    public static <T> List<T> makeList(T... args) {
        List<T> result = new ArrayList<>();
        // Collections.addAll(result, args); // Same thing
        for (T item : args) {
            result.add(item);
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> ls = makeList("A");
        System.out.println(ls);
        ls = makeList("A", "B", "C");
        System.out.println(ls);
        ls = makeList("ABCDEFFHIJKLMNOPQRSTUVWXYZ".split(""));
        System.out.println(ls);
    }
}
/* Output:
[A]
[A, B, C]
[A, B, C, D, E, F, F, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z]
 */
