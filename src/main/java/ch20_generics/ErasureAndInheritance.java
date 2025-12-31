package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/26 周五
 * @time 21:02
 * P.671 §20.6 类型擦除的奥秘 §20.6.3 类型擦除存在的问题
 * <p>
 * 在基于类型擦除的实现中，泛型类型被视同于第二类类型处理，无法在某些重要的上下文中使用。
 * 泛型类型只在静态类型检查时期存在，在这之后，程序中所有的泛型类型都会被擦除，并替换为它们的非泛型上界。
 * 举例来说，List<T> 这样的类型注解会被擦除为 List，而普通的类型变量则被擦除为 Object，除非指定了边界。
 * <p>
 * 类型擦除的核心初衷是，希望让泛化的调用方程序可以依赖于非泛化的库正常使用，反之亦然。这通常称为迁移兼容性。
 * Java 泛型不仅必须支持向后兼容性（即保证已有的代码和类文件都依日是合法的，并且能继续保持原有的含义），而还必须支持迁移兼容性；
 * 类型擦除存在的主要理由就是充当从非泛型代码过渡到泛型化代码的中间过程，以及在不破坏现有库的情况下，将泛型融入 Java 语言中。
 * <p>
 * 类型擦除的代价也很大。泛型代码无法用于需要显式引用运行时类型的操作，比如类型转换、instanceof 操作，以及 new 表达式。
 * 因为关于参数的所有类型信息都丢失了，所以在编写泛型代码时，你必须时刻提醒自己，你只是看起来掌握了参数的类型信息而已。
 * <p>
 * 另外，类型擦除和迁移兼容性意味着泛型的使用并非是强制性的，尽管你可能希望如此：
 * <p>
 * notes: 01-泛型的历史抉择与“擦除”的必然性.md
 * notes: 02-实战中的警告与错误：堆污染与继承规则.md
 */
class GenericBase<T> {
    private T element;

    public void set(T arg) {
        element = arg;
    }

    public T get() {
        return element;
    }
}

class Derived1<T> extends GenericBase<T> {
}

// [1] Warning: Raw use of parameterized class 'GenericBase'
// 警告：对参数化类 'GenericBase' 的原生（Raw）使用
@SuppressWarnings("rawtypes")
class Derived2 extends GenericBase {
}

// [3] Strange error:
//   unexpected type
//   required: class or interface without bounds
//   found:    ?
// 错误：意外的类型；发现了：?（通配符）；需要：无边界的类或接口
// 编译器拒绝不确定的通配符作为父类，必须指定具体的泛型参数如 <String>，或者使用原始类型
// class Derived3 extends GenericBase<?> {
// }

public class ErasureAndInheritance {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Derived2 d2 = new Derived2();
        Object obj = d2.get();
        // [2] Warning: Unchecked call to 'set(T)' as a member of raw type 'GenericBase'
        // 警告：作为原生类型成员的 'set(T)' 方法的未检查调用
        d2.set(obj);
    }
}
