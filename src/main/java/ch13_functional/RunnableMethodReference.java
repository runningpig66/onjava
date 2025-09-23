package ch13_functional;

/**
 * @author runningpig66
 * @date 2025/9/23 周二
 * @time 23:32
 * 代码清单 P.351
 * Method references with interface Runnable
 */
class Go {
    static void go() {
        System.out.println("Go::go()");
    }
}

public class RunnableMethodReference {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Anonymous");
            }
        }).start();

        new Thread(() -> System.out.println("lambda")).start();

        new Thread(Go::go).start();
    }
}
/* Output:
Anonymous
lambda
Go::go()
 */
