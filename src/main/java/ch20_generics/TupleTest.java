package ch20_generics;

import onjava.Tuple2;
import onjava.Tuple3;
import onjava.Tuple4;
import onjava.Tuple5;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 15:39
 * P.647 §20.2 简单泛型 §20.2.1 元组库
 * <p>
 * 如要使用元组，你就需要为函数定义长度合适的元组来作为返回值，然后创建该元组并返回。注意方法定义中的返回类型声明：
 * <p>
 * 有了泛型，只需编写该表达式，就可以很轻松地创建任何元组来返回任意一组类型。
 */
public class TupleTest {
    static Tuple2<String, Integer> f() {
        // Autoboxing converts the int to Integer:
        return new Tuple2<>("hi", 47);
    }

    static Tuple3<Amphibian, String, Integer> g() {
        return new Tuple3<>(new Amphibian(), "hi", 47);
    }

    static Tuple4<Vehicle, Amphibian, String, Integer> h() {
        return new Tuple4<>(new Vehicle(), new Amphibian(), "hi", 47);
    }

    static Tuple5<Vehicle, Amphibian, String, Integer, Double> k() {
        return new Tuple5<>(new Vehicle(), new Amphibian(), "hi", 47, 11.1);
    }

    public static void main(String[] args) {
        Tuple2<String, Integer> ttsi = f();
        System.out.println(ttsi);
        // ttsi.a1 = "there"; // Compile error: final
        System.out.println(g());
        System.out.println(h());
        System.out.println(k());
    }
}
/* Output:
(hi, 47)
(ch20_generics.Amphibian@776ec8df, hi, 47)
(ch20_generics.Vehicle@41629346, ch20_generics.Amphibian@404b9385, hi, 47)
(ch20_generics.Vehicle@682a0b20, ch20_generics.Amphibian@3d075dc0, hi, 47, 11.1)
 */
