package ch20_generics.coffee;

import org.jspecify.annotations.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 17:39
 * P.651 §20.3 泛型接口
 * {java ch20_generics.coffee.CoffeeSupplier}
 * <p>
 * 泛型同样可用于接口。生成器（generator）是一种用于创建对象的类。一般来说，生成器只会定义一个方法，即用于生成新对象的方法。
 * java.util.function 库将生成器定义为 Supplier，其中的生成方法则称为 get()。get() 的返回类型被参数化为 T。
 * <p>
 * 现在我们可以实现一个生成不同的随机 Coffee 对象类型的 Supplier<Coffee> 了：
 * <p>
 * 参数化的 Supplier 接口会确保 get() 方法返回的是参数类型。CoffeeSupplier 同样实现了 Iterable 接口，
 * 因此它可以在 for-in 语句中使用。不过它必须知道何时该停止，而这是由第二个构造器指定的。
 */
public class CoffeeSupplier implements Supplier<Coffee>, Iterable<Coffee> {
    private Class<?>[] types = {
            Latte.class, Mocha.class, Cappuccino.class, Americano.class, Breve.class};
    private static Random rand = new Random(47);

    public CoffeeSupplier() {
    }

    // For iteration:
    private int size = 0;

    public CoffeeSupplier(int sz) {
        size = sz;
    }

    @Override
    public Coffee get() {
        try {
            return (Coffee) types[rand.nextInt(types.length)].getConstructor().newInstance();
            // Report programmer errors at runtime:
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    class CoffeeIterator implements Iterator<Coffee> {
        int count = size;

        @Override
        public boolean hasNext() {
            return count > 0;
        }

        @Override
        public Coffee next() {
            count--;
            return CoffeeSupplier.this.get();
        }

        @Override
        public void remove() { // Not implemented
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public @NonNull Iterator<Coffee> iterator() {
        return new CoffeeIterator();
    }

    public static void main(String[] args) {
        Stream.generate(new CoffeeSupplier())
                .limit(5)
                .forEach(System.out::println);

        for (Coffee c : new CoffeeSupplier(5)) {
            System.out.println(c);
        }
    }
}
/* Output:
Americano 0
Latte 1
Americano 2
Mocha 3
Mocha 4
Breve 5
Americano 6
Latte 7
Cappuccino 8
Cappuccino 9
 */
