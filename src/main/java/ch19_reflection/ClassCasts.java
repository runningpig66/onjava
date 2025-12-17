package ch19_reflection;

/**
 * @author runningpig66
 * @date 2025/12/17 周三
 * @time 20:50
 * P.602 §19.2 Class 对象 §19.2.3 cast() 方法
 * <p>
 * 还有一个用于 Class 引用的类型转换语法，即 cast() 方法：cast() 方法接收参数对象并将其转换为 Class 引用的类型。
 * 但是，如果观察下面的代码，你会发现，与完成了相同工作的 main() 的最后一行相比，这种方式似乎做了很多额外的工作。
 * <p>
 * cast() 在你不能使用普通类型转换的情况下很有用。如果你正在编写泛型代码（你将在第 20 章中学习），
 * 并且存储了一个用于转型的 Class 引用，就可能会遇到这种情况。不过这很罕见————
 * 我发现在整个 Java 库中只有一个地方使用了 cast()（也就是在 com.sun.mirror.util.DeclarationFilter 中）。
 * <p>
 * 另一个在 Java 库中没有使用到的特性是 Class.asSubclass()。它会将类对象转换为更具体的类型。
 */
class Building {
}

class House extends Building {
}

public class ClassCasts {
    public static void main(String[] args) {
        Building b = new House();
        Class<House> houseType = House.class;
        // 使用 Class 对象的 cast() 方法进行向下转型，效果等同于 (House) b，但 cast() 是反射提供的类型转换方式
        // 如果 b 实际对象不是 House 或其子类，会抛 ClassCastException
        House h = houseType.cast(b);
        h = (House) b; // ... or just do this.
    }
}
