package ch16_validating;

/**
 * @author runningpig66
 * @date 2025/11/20 周四
 * @time 16:38
 * P502 §16.3 测试驱动开发
 * <p>
 * 现在测试巨经写好．可以开始实现 StringInverter 了。我们从一个没什么功能的类开始，它直接返回传入的参数：
 */
public class Inverter1 implements StringInverter {
    @Override
    public String invert(String str) {
        return str;
    }
}
