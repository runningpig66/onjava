package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 1:53
 * P.644 §20.2 简单泛型
 * <p>
 * 因此相较于 Object，我们更倾向于指定一个类型占位符，并在晚些时候再决定（具体的类型）。
 * 要实现这个目的，你需要在类名后的尖括号内放置一个类型参数，然后在使用该类的时候再将其替换为实际的类型。
 * 对于 holder（持有者）类来说，情况如下例中所示，其中 T 就是类型参数：
 * <p>
 * 正如你在 main() 中所见，在创建 GenericHolder 的时候，需要用尖括号语法来指定它要保存的类型。
 * 你只能将该类型的对象（或者它的子类，因为继承机制仍然适用于泛型）放入该 holder。
 * 如果你调用 get() 来提取值，取出的值自动就是正确的类型。
 */
public class GenericHolder<T> {
    private T a;

    public GenericHolder() {
    }

    public void set(T a) {
        this.a = a;
    }

    public T get() {
        return a;
    }

    public static void main(String[] args) {
        GenericHolder<Automobile> h3 = new GenericHolder<Automobile>();
        h3.set(new Automobile()); // type checked
        Automobile a = h3.get(); // No cast needed
        //- h3.set("Not an Automobile"); // Error
        //- h3.set(1); // Error
    }
}
