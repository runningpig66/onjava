package ch27_lowlevel;

import onjava.Nap;
import onjava.TimedAbort;

/**
 * @author runningpig66
 * @date 3月9日 周一
 * @time 22:28
 * P.289 §6.3 共享资源
 * <p>
 * 我们可以通过实操，进一步看看 TimedAbort：
 */
public class TestAbort {
    public static void main(String[] args) {
        new TimedAbort(1);
        System.out.println("Napping for 4");
        new Nap(4); // 如果注释掉 Nap() 这一行，程序就会立刻退出，由此可以看出 TimedAbort 并未让程序保持运行。
    }
}
/* Output:
Napping for 4
TimedAbort 1.0
 */
