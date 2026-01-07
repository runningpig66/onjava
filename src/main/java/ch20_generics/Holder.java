package ch20_generics;

import java.util.Objects;

/**
 * @author runningpig66
 * @date 2026/1/1 周四
 * @time 17:32
 * P.693 §20.9 通配符 §20.9.1 编译器有多聪明？
 * <p>
 * Holder 中有一个以 T 为参数的 set() 方法，一个返回 T 的 get() 方法，以及一个以 Object 为参数的 equals() 方法。
 * 如你所见，如果你创建了 Holder<Apple>，就无法将其向上转型为 Holder<Fruit>，但是可以向上转型为 Holder<? extends Fruit>。
 * 如果调用 get()，则只能返回 Fruit ———— 通过给定的“任何继承 Fruit 的类型”边界信息，它只能知道这么多了。如果你知道此处是什么类型，
 * 便可以转型为某个具体的 Fruit 类型，并且不会产生相关的警告，但这有抛出 ClassCastException 的风险。
 * Apple 或 Fruit 都无法用于 set() 方法，因为 set() 方法的参数同样是 ? Extends Fruit，这意味着它可以是任意类型，
 * 而编译器无法为“任意类型”验证安全性。不过，equals() 方法没有问题，因为它接收 Object 作为参数，而不是 T。
 * 因此编译器只会关心传入和返回的对象类型。它并没有分析代码以检查你是否执行了任何实际的读写。
 */
public class Holder<T> {
    private T value;

    public Holder() {
    }

    public Holder(T val) {
        value = val;
    }

    public void set(T val) {
        value = val;
    }

    public T get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Holder && Objects.equals(value, ((Holder) o).value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    public static void main(String[] args) {
        Holder<Apple> apple = new Holder<>(new Apple());
        Apple d = apple.get();
        apple.set(d);
        // Holder<Fruit> Fruit = apple; // Cannot upcase
        Holder<? extends Fruit> fruit = apple; // OK
        Fruit p = fruit.get();
        d = (Apple) fruit.get(); // Returns 'Fruit', but we are casting it to 'Apple'
        try {
            Orange c = (Orange) fruit.get(); // No warning
        } catch (Exception e) {
            System.out.println(e);
        }
        // fruit.set(new Apple()); // Cannot call set()
        // fruit.set(new Fruit()); // Cannot call set()
        System.out.println(fruit.equals(d)); // OK
    }
}
/* Output:
java.lang.ClassCastException: class Apple cannot be cast to class Orange (Apple and Orange are in unnamed module of loader 'app')
false
 */
