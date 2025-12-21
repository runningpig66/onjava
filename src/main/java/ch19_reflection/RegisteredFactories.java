package ch19_reflection;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/12/19 周五
 * @time 19:14
 * P.614 §19.4 注册工厂
 * Registering Factories in the base class
 * <p>
 * notes: RegisteredFactories.md
 * Java 类初始化中的潜在死锁风险：父类静态初始化引用子类的分析与 JVM 处理机制
 * <p>
 * 我们要做的另一处变更是使用工厂方法（Factory Method）设计模式来堆迟对象的创建，将其交给子类去完成。
 * 工厂方法可以被多态地调用，来创建适当类型的对象。实际上，java.util.function.Supplier 通过它的 T get()
 * 方法提供了一个工厂方法的原型。get() 方法可以通过协变返回类型为 Supplier 的不同子类返回对应的类型。
 * <p>
 * 在此示例中，基类 Part 包含了一个工厂对象（Supplier<Part>）的静态 List。对于本应该由 get() 方法生成的类型，
 * 它们的工厂类都被添加到了列表 prototypes 里，从而“注册”到了基类中。比较特别的一点是，这些工厂是对象本身的实例。
 * 这个列表中的每个对象都是用于创建其他对象的原型：
 * <p>
 * Part 实现了 Supplier<Part>，所以它可以通过自己的 get() 提供其他的 Part 对象。
 * 如果调用了基类 Part 的 get() 方法（或者通过 generate() 调用 get()），
 * 它会随机创建特定的 Part 子类型，每个子类型最终都继承自 Part，并重写了 get() 方法来生成自身的对象。
 */
class Part implements Supplier<Part> {
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    // Referencing subclass PowerSteeringBelt from superclass Part initializer might lead to class loading deadlock
    static List<Supplier<? extends Part>> prototypes =
            Arrays.asList(
                    new FuelFilter(),
                    new AirFilter(),
                    new CabinAirFilter(),
                    new OilFilter(),
                    new FanBelt(),
                    new PowerSteeringBelt(),
                    new GeneratorBelt()
            );
    private static Random rand = new Random(47);

    @Override
    public Part get() {
        int n = rand.nextInt(prototypes.size());
        // prototypes 是 List<Supplier<? extends Part>>，利用泛型协变（covariance）：
        // Supplier<FuelFilter>、Supplier<PowerSteeringBelt> 等子类型 Supplier 都可以视为 Supplier<? extends Part>
        // 因此列表能容纳各种 Part 子类的 Supplier，而调用其 get() 方法返回的对象一定是 Part 或其子类，安全匹配方法返回类型 Part
        return prototypes.get(n).get();
    }
}

class Filter extends Part {
}

class FuelFilter extends Filter {
    @Override
    public FuelFilter get() {
        return new FuelFilter();
    }
}

class AirFilter extends Filter {
    @Override
    public AirFilter get() {
        return new AirFilter();
    }
}

class CabinAirFilter extends Filter {
    @Override
    public CabinAirFilter get() {
        return new CabinAirFilter();
    }
}

class OilFilter extends Filter {
    @Override
    public OilFilter get() {
        return new OilFilter();
    }
}

class Belt extends Part {
}

class FanBelt extends Belt {
    @Override
    public FanBelt get() {
        return new FanBelt();
    }
}

class GeneratorBelt extends Belt {
    @Override
    public GeneratorBelt get() {
        return new GeneratorBelt();
    }
}

class PowerSteeringBelt extends Belt {
    @Override
    public PowerSteeringBelt get() {
        return new PowerSteeringBelt();
    }
}

public class RegisteredFactories {
    public static void main(String[] args) {
        Stream.generate(new Part())
                .limit(10)
                .forEach(System.out::println);
    }
}
/* Output:
GeneratorBelt
CabinAirFilter
GeneratorBelt
AirFilter
PowerSteeringBelt
CabinAirFilter
FuelFilter
PowerSteeringBelt
PowerSteeringBelt
FuelFilter
 */
