package ch20_generics;

import onjava.Tuple2;
import onjava.Tuple3;
import onjava.Tuple4;
import onjava.Tuple5;

import static onjava.Tuple.tuple;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 23:09
 * P.657 §20.4 泛型方法 §20.4.3 简化元组的使用
 * <p>
 * 我们修改一下 TupleTest.java，用来测试 Tuple.java：
 */
public class TupleTest2 {
    static Tuple2<String, Integer> f() {
        return tuple("hi", 47);
    }

    // Raw use of parameterized class 'Tuple2'
    static Tuple2 f2() {
        return tuple("hi", 47);
    }

    static Tuple3<Amphibian, String, Integer> g() {
        return tuple(new Amphibian(), "hi", 47);
    }

    static Tuple4<Vehicle, Amphibian, String, Integer> h() {
        return tuple(new Vehicle(), new Amphibian(), "hi", 47);
    }

    static Tuple5<Vehicle, Amphibian, String, Integer, Double> k() {
        return tuple(new Vehicle(), new Amphibian(), "hi", 47, 11.1);
    }

    public static void main(String[] args) {
        Tuple2<String, Integer> ttsi = f();
        System.out.println(ttsi);
        System.out.println(f2());
        System.out.println(g());
        System.out.println(h());
        System.out.println(k());
    }
}
/* Output:
(hi, 47)
(hi, 47)
(ch20_generics.Amphibian@4eec7777, hi, 47)
(ch20_generics.Vehicle@404b9385, ch20_generics.Amphibian@6d311334, hi, 47)
(ch20_generics.Vehicle@3d075dc0, ch20_generics.Amphibian@214c265e, hi, 47, 11.1)
 */
