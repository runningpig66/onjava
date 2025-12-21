package ch19_reflection.pets;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/12/18 周四
 * @time 16:21
 * P.606 §19.3 转型前检查
 * Creates random Pets
 * <p>
 * 接下来，我们需要一种方法来随机地创建 Pet 对象。为了使这个工具有不同的实现。我们将它定义为一个抽象类：
 * <p>
 * 抽象的 types() 方法需要在 Creator 的子类里实现，以生成一个包含了 Class 对象的 List。
 * 这是模板方法（Template Method）设计模式的一个例子。注意，List 的泛型参数被指定为“继承了 Pet 的任意子类”，
 * 因此 newInstance() 无须类型转换即可生成一个 Pet。get() 会查找 List 的索引来生成一个 Class 对象。
 * getConstructor() 会生成一个 Constructor 对象，而 newInstance() 使用该 Constructor 来创建一个对象。
 * <p>
 * Creator 类提供了几个工具方法来创建一组随机生成的 Pet：
 * stream() 方法生成一个 Stream<Pet>，array() 方法生成一个 Pet[]，list() 方法生成一个 List<Pet>。
 */
public abstract class Creator implements Supplier<Pet> {
    private Random rand = new Random(47);

    // The different types of Pet to create:
    public abstract List<Class<? extends Pet>> types();

    @Override
    public Pet get() { // Create one random Pet
        int n = rand.nextInt(types().size());
        try {
            return types().get(n).getConstructor().newInstance();
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Stream<Pet> stream() {
        return Stream.generate(this);
    }

    public Pet[] array(int size) {
        return stream().limit(size).toArray(Pet[]::new);
    }

    public List<Pet> list(int size) {
        return stream().limit(size).collect(Collectors.toCollection(ArrayList::new));
    }
}
