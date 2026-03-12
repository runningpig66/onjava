package ch27_lowlevel;

/**
 * @author runningpig66
 * @date 3月12日 周四
 * @time 9:01
 * P.302 §6.5 原子性 §6.5.1 Josh 的序列号
 * <p>
 * 测试基础版本的 SerialNumbers 类时，运行失败：
 */
public class SerialNumberTest {
    public static void main(String[] args) {
        SerialNumberChecker.test(new SerialNumbers());
    }
}
/* Output:
Duplicate: 62324
Duplicate: 148304
Duplicate: 221932
Duplicate: 232399
 */
