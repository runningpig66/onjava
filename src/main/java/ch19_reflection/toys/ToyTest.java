package ch19_reflection.toys;

/**
 * @author runningpig66
 * @date 2025/12/17 周三
 * @time 11:54
 * P.594 §19.2 Class 对象
 * Testing class Class
 * {java ch19_reflection.toys.ToyTest}
 * <p>
 * 不管什么时候，只要在运行时用到类型信息，就必须首先获得相应的 Class 对象的引用。这时 Class.forName() 方法用起来就很方便了，
 * 因为不需要对应类型的对象就能获取 Class 引用。但是，如果已经有了一个你想要的类型的对象，就可以通过 getClass() 方法来获取 Class 引用，
 * 这个方法属于 Object 根类。它返回的 Class 引用表示了这个对象的实际类型。Class 类有很多方法，下面是其中的一部分：
 * <p>
 * public static Class<?> forName(String className)
 * public final native Class<?> getClass();
 */
interface HasBatteries {
}

interface Waterproof {
}

interface Shoots {
}

class Toy {
    // Comment out the following zero-argument constructor to see NoSuchMethodError
    public Toy() {
    }

    public Toy(int i) {
    }
}

class FancyToy extends Toy implements HasBatteries, Waterproof, Shoots {
    public FancyToy() {
        super(1);
    }
}

public class ToyTest {
    static void printInfo(Class cc) {
        // getName(): 返回类的完全限定名（包含包名）
        System.out.println("Class name: " + cc.getName() + " is interface? [" + cc.isInterface() + "]");
        // getSimpleName(): 返回类的简单名称（不包含包名）
        System.out.println("Simple name: " + cc.getSimpleName());
        // getCanonicalName(): 返回类的规范名称（通常是完全限定名，对于内部类等有特殊处理）
        System.out.println("Canonical name : " + cc.getCanonicalName());
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        Class c = null;
        try {
            // Class.forName(): 根据完全限定类名加载并返回对应的 Class 对象，这会触发类的静态初始化块执行
            c = Class.forName("ch19_reflection.toys.FancyToy");
        } catch (ClassNotFoundException e) {
            System.out.println("Can't find FancyToy");
            System.exit(1);
        }
        printInfo(c);
        // getInterfaces(): 返回该类实现的所有接口的 Class 对象数组
        for (Class face : c.getInterfaces()) {
            printInfo(face);
        }
        // getSuperclass(): 返回该类的直接父类的 Class 对象
        Class up = c.getSuperclass();
        Object obj = null;
        try {
            // Requires public zero-argument constructor:
            // newInstance(): 已过时的方法，使用默认构造函数创建类的实例
            // 注意：此方法要求类必须有可访问的无参构造函数，否则会抛出异常
            obj = up.newInstance();
            // 从 Java 9 开始被标记为 @Deprecated，建议使用 Constructor.newInstance() 替代
            // obj = up.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate");
        }
        printInfo(obj.getClass());
    }
}
/* Output:
Class name: ch19_reflection.toys.FancyToy is interface? [false]
Simple name: FancyToy
Canonical name : ch19_reflection.toys.FancyToy
Class name: ch19_reflection.toys.HasBatteries is interface? [true]
Simple name: HasBatteries
Canonical name : ch19_reflection.toys.HasBatteries
Class name: ch19_reflection.toys.Waterproof is interface? [true]
Simple name: Waterproof
Canonical name : ch19_reflection.toys.Waterproof
Class name: ch19_reflection.toys.Shoots is interface? [true]
Simple name: Shoots
Canonical name : ch19_reflection.toys.Shoots
Class name: ch19_reflection.toys.Toy is interface? [false]
Simple name: Toy
Canonical name : ch19_reflection.toys.Toy
 */
