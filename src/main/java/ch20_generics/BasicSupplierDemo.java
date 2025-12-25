package ch20_generics;

import onjava.BasicSupplier;

import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 22:08
 * P.656 §20.4 泛型方法 §20.4.2 通用 Supplier
 * <p>
 * BasicSupplier 可以很轻松地为 CountedObject 创建 Supplier:
 * <p>
 * 泛型方法减少了生成 Supplier 对象所必需的代码编写量。Java 泛型强制要求传入 Class 对象，因此你还可以将它用于 create() 方法中的类型推断。
 */
public class BasicSupplierDemo {
    public static void main(String[] args) {
        Stream.generate(BasicSupplier.create(CountedObject.class))
                .limit(5)
                .forEach(System.out::println);
    }
}
/* Output:
CountedObject 0
CountedObject 1
CountedObject 2
CountedObject 3
CountedObject 4
 */
