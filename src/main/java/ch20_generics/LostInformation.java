package ch20_generics;

import java.util.*;

/**
 * @author runningpig66
 * @date 2025/12/26 周五
 * @time 16:56
 * P.666 §20.6 类型擦除的奥秘
 * <p>
 * 当你开始更深入地了解泛型时，会发现有些问题起初看起来并不合理。
 * 举例来说，虽然声明 ArrayList.class 是合法的，但声明 ArrayList<Integer>.class 却不行。下面这个示例则更加让人迷惑：
 * <p>
 * 根据 JDK 文档的描述，Class.getTypeParameters() 会“返回一个由 TypeVariable 对象组成的数组，
 * 代表由泛型声明所声明的类型变量……”。这似乎在暗示可以发现参数的类型信息。
 * 然而，如程序输出所示，你只能发现作为参数占位符的标识符————这有点儿令人失望。
 * public TypeVariable<Class<T>>[] getTypeParameters()
 * <p>
 * 所以残酷的事实是：泛型代码内部并不存在有关泛型参数类型的可用信息。
 * <p>
 * 因此，你可以知道诸如类型参数的标识符和泛型类型的边界等信息，但你就是无法知道实际用于创建具体实例的类型参数。
 * 如果你曾经是 C++ 程序员，这个情况会让你特别沮丧，这也是使用 Java 泛型时必须处理的最基本的问题。
 * <p>
 * Java 泛型是通过类型擦除实现的。这意味着在使用泛型时，任何具体的类型信息都被擦除。
 * 在泛型内部，你唯一知道的就是你在使用对象。因此 List<String> 和 List<Integer> 在运行时实际上是相同的类型。
 * 两者的类型都被“擦除”为它们的原始类型（raw type）：List。理解类型擦除并掌握必要的处理方式，是学习泛型的过程中需要面对的最大难点之一。
 */
class Frob {
}

class Fnorkle {
}

class Quark<Q> {
}

class Particle<POSITION, MOMENTUM> {
}

public class LostInformation {
    public static void main(String[] args) {
        List<Frob> list = new ArrayList<>();
        Map<Frob, Fnorkle> map = new HashMap<>();
        Quark<Fnorkle> quark = new Quark<>();
        Particle<Long, Double> p = new Particle<>();
        System.out.println(Arrays.toString(
                list.getClass().getTypeParameters()));
        System.out.println(Arrays.toString(
                map.getClass().getTypeParameters()));
        System.out.println(Arrays.toString(
                quark.getClass().getTypeParameters()));
        System.out.println(Arrays.toString(
                p.getClass().getTypeParameters()));
    }
}
/* Output:
[E]
[K, V]
[Q]
[POSITION, MOMENTUM]
 */
