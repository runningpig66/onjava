package ch20_generics;

import java.util.Arrays;
import java.util.List;

/**
 * @author runningpig66
 * @date 2026/1/1 周四
 * @time 22:39
 * P.695 §20.9 通配符 §20.9.2 逆变性
 * <p>
 * 下面这个示例回顾了协变性和通配符：
 * <p>
 * notes: 11-泛型机制：PECS 原则、协变与逆变.md
 */
public class GenericReading {
    static List<Apple> apples = Arrays.asList(new Apple());
    static List<Fruit> fruit = Arrays.asList(new Fruit());

    static <T> T readExact(List<T> list) {
        return list.get(0);
    }

    // A static method adapts to each call:
    static void f1() {
        Apple a = readExact(apples);
        Fruit f = readExact(fruit);
        f = readExact(apples);
    }

    // A class type is established when the class is instantiated:
    static class Reader<T> {
        T readExact(List<T> list) {
            return list.get(0);
        }
    }

    static void f2() {
        Reader<Fruit> fruitReader = new Reader<>();
        Fruit f = fruitReader.readExact(fruit);
        //- Fruit a = fruitReader.readExact(apples);
        // error: incompatible types: List<Apple> cannot be converted to List<Fruit>
    }

    static class CovariantReader<T> {
        T readCovariant(List<? extends T> list) {
            return list.get(0);
        }
    }

    static void f3() {
        CovariantReader<Fruit> fruitReader = new CovariantReader<>();
        Fruit f = fruitReader.readCovariant(fruit);
        Fruit a = fruitReader.readCovariant(apples);
    }

    public static void main(String[] args) {
        f1();
        f2();
        f3();
    }
}
