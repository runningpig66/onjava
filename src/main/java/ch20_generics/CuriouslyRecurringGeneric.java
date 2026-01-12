package ch20_generics;

/**
 * @author runningpig66
 * @date 2026/1/12 周一
 * @time 15:26
 * P.714 §20.11 自限定类型 §20.11.1 奇异递归泛型
 * <p>
 * 要理解自限定类型是什么意思，我们先来看一个该用法的简化版本，它并未使用自限定：
 * 泛型参数是无法直接继承的。不过，可以继承一个在自身定义中用到了该泛型参数的类。也就是说，你可以这样做：
 * <p>
 * 继 Jim Coplien 在 C++ 领域提出奇异递归模板模式（Curiously Recurring Template Pattern）后，
 * 这种方式可以称为奇异递归泛型（curiously recurring generics, CRG）。其中“奇异递归”指的是你的类奇怪地在自身的基类中出现的现象。
 */
class GenericType<T> {
}

public class CuriouslyRecurringGeneric extends GenericType<CuriouslyRecurringGeneric> {
}
