package ch20_generics.notes;

import ch19_reflection.pets.Cat;
import ch19_reflection.pets.Manx;
import ch19_reflection.pets.Pet;

/**
 * @author runningpig66
 * @date 2026/1/10 周六
 * @time 16:37
 * P.713 §20.10 问题 §20.10.5 基类会劫持接口
 * <p>
 * 遵循 PECS 原则（Consumer Super），逆变引用（<? super T>）仅能作为消费者安全地传入 T 及其子类型；
 * 编译器确保了“下界”安全（Lower Bound），即底层消费者至少具备处理 T 的能力，因此写入 T 是类型安全的。
 */
public class ContravarianceTest {
    public static void main(String[] args) {
        // 1. 准备一个宽泛的比较器（父类型消费者）
        MyComparable<Pet> petComparator = new MyComparable<Pet>() {
            @Override
            public int compare(Pet other) {
                System.out.println("正在比较宠物：" + other.getClass().getSimpleName());
                return 0;
            }
        };
        // 逆变引用赋值：逻辑复用：将处理能力更强的父类比较器（Comparable<Pet>）赋值给处理子类的引用
        MyComparable<? super Cat> ref = petComparator;
        // 消费者写入：编译通过：由于确立了类型下界为 Cat，编译器允许传入 Cat 及其任何子类（如 Manx）。
        // 无论 ref 实际指向 Comparable<Pet> 还是 Comparable<Object>，它们都能安全“消费” Cat。
        ref.compare(new Cat());
        ref.compare(new Manx());
        // 编译错误：不能传入 T 的父类。因为 ref 可能实际指向 MyComparable<Cat>，
        // 若传入 Pet 实例会导致“父类对象传给子类消费者”的类型不安全（ClassCastException 风险）。
        ref.compare(new Pet());
        // ContravarianceTest.java:31: error: incompatible types: Pet cannot be converted to CAP#1
        // ref.compare(new Pet());
        //             ^
        // where CAP#1 is a fresh type-variable:
        // CAP#1 extends Object super: Cat from capture of ? super Cat
        // Note: Some messages have been simplified; recompile with -Xdiags:verbose to get full output
        // 1 error
    }
}
