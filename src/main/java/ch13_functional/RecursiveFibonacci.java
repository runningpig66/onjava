package ch13_functional;

/**
 * @author runningpig66
 * @date 2025/8/8 周五
 * @time 6:32
 * 代码清单 P.349
 */
public class RecursiveFibonacci {
    // 注意：递归的 lambda 表达式必须被赋值给一个静态变量或一个实例变量，否则会出现编译错误。
    IntCall fib;

    RecursiveFibonacci() {
        // 斐波那契数列从第三项开始，每一项都等于前两项之和。
        fib = n -> n == 0 ? 0 :
                n == 1 ? 1 : fib.call(n - 1) + fib.call(n - 2);
    }

    int fibonacci(int n) {
        return fib.call(n);
    }

    public static void main(String[] args) {
        RecursiveFibonacci rf = new RecursiveFibonacci();
        for (int i = 0; i <= 10; i++) {
            System.out.println(rf.fibonacci(i));
        }
    }
}
/* Output:
0
1
1
2
3
5
8
13
21
34
55
 */
