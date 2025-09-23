package ch13_functional;

/**
 * @author runningpig66
 * @date 2025/8/8 周五
 * @time 6:09
 * 代码清单 P.348
 */
public class RecursiveFactorial {
    /*
    注意：递归的 lambda 表达式必须被赋值给一个静态变量或一个实例变量，否则会出现编译错误。

    疑问：为什么不能在 Java 静态变量初始化时递归引用自身？
    Java 类的静态变量会在类加载时按声明顺序依次初始化，每个变量初始化时，该变量还没赋值完成。
    递归 lambda 表达式如果在静态变量定义时引用自身，会导致“非法向前引用”编译错误。
    正确做法是：先声明变量名，再在静态代码块或方法中赋值，这样 lambda 内部才能安全地递归引用自身。
     */
    static IntCall fact;

    public static void main(String[] args) {
        fact = n -> n == 0 ? 1 : n * fact.call(n - 1);
        for (int i = 0; i <= 10; i++) {
            System.out.println(fact.call(i));
        }
    }
}
/* Output:
1
1
2
6
24
120
720
5040
40320
362880
3628800
 */
