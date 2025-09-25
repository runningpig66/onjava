package ch13_functional;

import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.function.*;

/**
 * @author runningpig66
 * @date 2025/9/25 周四
 * @time 23:10
 * 代码清单 P.357 * (* 代表练习代码）
 * java.util.function中的函数式接口练习
 */
public class FunctionalInterfaceTest {
    static class TypeData1 {
    }

    static class TypeData2 {
    }

    static class TypeData3 {
    }

    public static void main(String[] args) {
        // 没有参数；没有返回值
        Runnable runnable = () -> {
        };
        // 没有参数；可以返回任何类型
        Supplier<TypeData1> supplier = TypeData1::new;
        BooleanSupplier booleanSupplier = () -> true;
        IntSupplier intSupplier = () -> 1;
        LongSupplier longSupplier = () -> 1L;
        DoubleSupplier doubleSupplier = () -> 1D;
        // 没有参数；可以返回任何类型
        Callable<TypeData1> callable = TypeData1::new;
        // 一个参数；没有返回值
        Consumer<TypeData1> consumer = typeData1 -> {
        };
        IntConsumer intConsumer = intData -> {
        };
        LongConsumer longConsumer = longData -> {
        };
        DoubleConsumer doubleConsumer = doubleData -> {
        };
        // 两个参数的 Consumer
        BiConsumer<TypeData1, TypeData2> biConsumer = (typeData1, typeData2) -> {
        };
        // 两个参数的 Consumer；第一个参数是引用，第二个参数是基本类型
        ObjIntConsumer<TypeData1> objIntConsumer = (typeData1, intData) -> {
        };
        ObjLongConsumer<TypeData1> objLongConsumer = (typeData1, longData) -> {
        };
        ObjDoubleConsumer<TypeData1> objDoubleConsumer = (typeData1, doubleData) -> {
        };
        // 一个参数；返回值为不同类型
        Function<TypeData1, TypeData2> function = typeData1 -> new TypeData2();
        IntFunction<TypeData1> intFunction = intData -> new TypeData1();
        LongFunction<TypeData1> longFunction = longData -> new TypeData1();
        DoubleFunction<TypeData1> doubleFunction = doubleData -> new TypeData1();
        ToIntFunction<TypeData1> toIntFunction = typeData1 -> 1;
        ToLongFunction<TypeData1> toLongFunction = typeData1 -> 1L;
        ToDoubleFunction<TypeData1> toDoubleFunction = typeData1 -> 1D;
        IntToLongFunction intToLongFunction = intData -> 1L;
        IntToDoubleFunction intToDoubleFunction = intData -> 1D;
        LongToIntFunction longToIntFunction = longData -> 1;
        LongToDoubleFunction longToDoubleFunction = longData -> 1D;
        DoubleToIntFunction doubleToIntFunction = doubleData -> 1;
        DoubleToLongFunction doubleToLongFunction = doubleData -> 1L;
        // 一个参数；返回值为相同类型
        UnaryOperator<TypeData1> unaryOperator = (TypeData1 typeData1) -> new TypeData1();
        IntUnaryOperator intUnaryOperator = intData -> 1;
        LongUnaryOperator longUnaryOperator = longData -> 1L;
        DoubleUnaryOperator doubleUnaryOperator = doubleData -> 1D;
        // 两个相同类型的参数；返回值也是相同类型
        BinaryOperator<TypeData1> binaryOperator = (typeData1_1, typeData1_2) -> new TypeData1();
        IntBinaryOperator intBinaryOperator = (intData1, intData2) -> 1;
        LongBinaryOperator longBinaryOperator = (longData1, longData2) -> 1L;
        DoubleBinaryOperator doubleBinaryOperator = (doubleData1, doubleData2) -> 1D;
        // 两个相同类型的参数；返回 int
        Comparator<TypeData1> comparator = (typeData1_1, typeData1_2) -> 1;
        // 两个参数；返回 boolean
        Predicate<TypeData1> predicate = typeData1 -> true;
        BiPredicate<TypeData1, TypeData2> biPredicate = (typeData1, typeData2) -> false;
        IntPredicate intPredicate = intData -> true;
        LongPredicate longPredicate = longData -> false;
        DoublePredicate doublePredicate = doubleData -> true;
        // 基本类型的参数；返回值也是基本类型
        IntToLongFunction intToLongFunction1 = intData -> 1L;
        IntToDoubleFunction intToDoubleFunction1 = intData -> 1D;
        LongToIntFunction longToIntFunction1 = longData -> 1;
        LongToDoubleFunction longToDoubleFunction1 = longData -> 1D;
        DoubleToIntFunction doubleToIntFunction1 = doubleData -> 1;
        DoubleToLongFunction doubleToLongFunction1 = doubleData -> 1L;
        // 两个参数；不同类型
        BiFunction<TypeData1, TypeData2, TypeData3> filenameFilter = (typeData1, typeData2) -> new TypeData3();
        BiConsumer<TypeData1, TypeData2> biConsumer1 = (typeData1, typeData2) -> {
        };
        BiPredicate<TypeData1, TypeData2> biPredicate1 = (typeData1, typeData2) -> true;
        ToIntBiFunction<TypeData1, TypeData2> toIntBiFunction = (typeData1, typeData2) -> 1;
        ToLongBiFunction<TypeData1, TypeData2> toLongBiFunction = (typeData1, typeData2) -> 1L;
        ToDoubleBiFunction<TypeData1, TypeData2> toDoubleBiFunction = (typeData1, typeData2) -> 1D;
    }
}
