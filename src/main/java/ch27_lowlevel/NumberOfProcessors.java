package ch27_lowlevel;

/**
 * @author runningpig66
 * @date 3月8日 周日
 * @time 3:33
 * P.279 §6.1 什么是线程？ §6.1.1 最佳线程数
 */
public class NumberOfProcessors {
    public static void main(String[] args) {
        // public native int availableProcessors();
        // 返回当前 Java 虚拟机可用的逻辑处理器（即硬件线程）数量。
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
/* Output:
16
 */
