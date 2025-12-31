package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/29 周一
 * @time 23:55
 * P.681 §20.7 对类型擦除的补偿 §20.7.2 泛型数组
 * <p>
 * 有时你仍然需要创建泛型类型的数组（例如 ArrayList，其内部实现使用了数组）。
 * 你可以定义一个泛型类数组的引用（a reference to an array of generic types），以满足编译器的要求：
 * <p>
 * 编译器接受了这种方式，没有产生警告。但是你永远无法创建该确切类型（包括类型参数）的数组，因此这有点让人疑惑。
 * <p>
 * notes: 09-泛型数组创建限制的原因分析.md
 */
class Generic<T> {
}

public class ArrayOfGenericReference {
    // 你可以定义一个泛型类数组的引用
    static Generic<Integer>[] gia;

    public static void main(String[] args) {
        // 但是你永远无法创建该确切类型（包括类型参数）的数组
        //- gia = new Generic<Integer>[10];
    }
}
