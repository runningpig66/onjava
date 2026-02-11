package ch23_references;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 2月10日 周二
 * @time 23:59
 * P.073 §2.2 创建本地副本 §2.2.7 深拷贝 ArrayList
 * You must go through a few gyrations to add cloning to your own class
 */
class Int2 implements Cloneable {
    private int i;

    Int2(int ii) {
        i = ii;
    }

    public void increment() {
        i++;
    }

    @Override
    public String toString() {
        return Integer.toString(i);
    }

    @Override
    public Int2 clone() {
        try {
            return (Int2) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

// Inheritance doesn't remove cloneability:
class Int3 extends Int2 {
    private int j; // Automatically duplicated

    Int3(int i) {
        super(i);
    }
}

public class AddingClone {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Int2 x = new Int2(10);
        Int2 x2 = x.clone();
        x2.increment();
        System.out.println("x = " + x + ", x2 = " + x2);
        // Anything inherited is also cloneable:
        Int3 x3 = new Int3(7);
        // 1. 运行时机制：尽管调用的是基类 Int2 的 clone()，Object.clone() 依据运行时类型 (RTTI) 识别为 Int3。
        //    它在堆上分配 Int3 的完整内存空间并执行全量按位复制，因此子类基本类型字段 j 被正确复制。
        // 2. 潜在风险：基类 Int2 的 clone() 逻辑无法感知子类新增的字段。若 Int3 后续新增【可变引用类型】成员（如 List/Date），
        //    默认实现仅执行引用地址的浅拷贝。此时必须在 Int3 中重写 clone() 以手动实现深拷贝，否则将导致新旧对象共享引用数据。
        x3 = (Int3) x3.clone();
        ArrayList<Int2> v = IntStream.range(0, 10)
                .mapToObj(Int2::new)
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("v: " + v);
        ArrayList<Int2> v2 = (ArrayList<Int2>) v.clone();
        // Now clone each element:
        IntStream.range(0, v.size())
                .forEach(i -> v2.set(i, v.get(i).clone()));
        // Increment all v2's elements:
        v2.forEach(Int2::increment);
        System.out.println("v2: " + v2);
        // See if it changed v's elements:
        System.out.println("v: " + v);
    }
}
/* Output:
x = 10, x2 = 11
v: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
v2: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
v: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
 */
