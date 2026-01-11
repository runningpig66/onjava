package ch20_generics.notes;

import ch19_reflection.pets.Cat;
import ch19_reflection.pets.Dog;
import ch19_reflection.pets.Pet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author runningpig66
 * @date 2026/1/9 周五
 * @time 21:45
 * P.713 §20.10 问题 §20.10.5 基类会劫持接口
 * <p>
 * 遵循 PECS 原则（Producer Extends），协变引用（<? extends T>）仅能作为生产者安全地读取 T 类型的值；
 * 由于运行时具体子类型不确定，为防止破坏类型安全，编译器会阻止调用任何接受泛型类型参数的方法（即无法作为消费者写入）。
 */
public class CovarianceTest {
    public static void main(String[] args) {
        // ---------------------------------------------------------
        // 第一部分：协变的限制（不能作为消费者写入）
        // ---------------------------------------------------------
        MyComparable<Dog> dogComparator = new MyComparable<Dog>() {
            @Override
            public int compare(Dog other) {
                System.out.println("正在比较宠物：" + other.getClass().getSimpleName());
                return 0;
            }
        };
        // 协变引用赋值：这是一个比较器，它比较的是 Pet 的某种子类，但不知道具体是哪一种。
        MyComparable<? extends Pet> ref = dogComparator;
        // 编译报错：协变引用（? extends）导致泛型参数 T 被捕获为“未知的 Pet 子类”，无法作为消费者接收具体对象。
        // 编译器为防止类型不匹配（如将 Cat 传给 Dog 比较器），强制封锁了所有以 T 为入参的方法入口。
        ref.compare(new Cat());
        // MyComparableTest.java:28: error: incompatible types: Cat cannot be converted to CAP#1
        // ref.compare(new Cat());
        //             ^
        // where CAP#1 is a fresh type-variable:
        // CAP#1 extends Pet from capture of ? extends Pet
        // Note: Some messages have been simplified; recompile with -Xdiags:verbose to get full output
        // 1 error
        // 编译错误：由于泛型通配符 ? extends Pet 的不确定性，无法安全地将 Cat 实例传递给目标类型，
        // 实际指向的 MyComparable<Dog> 在擦除后通过桥接方法包含 CHECKCAST Dog 指令，会引发类型转换异常

        // ---------------------------------------------------------
        // 第二部分：协变的优势（能作为生产者读取）
        // ---------------------------------------------------------
        // 场景：我们需要处理一批宠物，但不管它们具体是什么列表（Dog列表或Cat列表）
        List<Dog> dogList = new ArrayList<>();
        dogList.add(new Dog("旺财"));
        // 1. 向上转型（多态性）：将具体的 List<Dog> 赋值给通用的 List<? extends Pet>
        // 如果没有 <? extends Pet> 协变，List<Dog> 是不能赋值给 List<Pet> 的（不变性）
        List<? extends Pet> petProducer = dogList;
        // 2. 安全读取（Producer）：编译器保证从里边取出来的“一定是 Pet”
        // 尽管不知道具体是 Dog 还是 Cat，但既然都继承自 Pet，那么作为 Pet 读取是绝对安全的。
        Pet p = petProducer.get(0); // 编译通过：作为生产者读取 T
        System.out.println("成功读取到宠物：" + p.getClass().getSimpleName()); // 输出：Dog
        // 3. 再次验证限制：不能写入
        // petProducer.add(new Dog()); // 编译错误：不能作为消费者写入
    }
}
