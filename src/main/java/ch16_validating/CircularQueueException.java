package ch16_validating;

/**
 * @author runningpig66
 * @date 2025/11/19 周三
 * @time 15:11
 * 代码清单 P.489 前置条件：DbC(Design by Contract) 契约式设计 + 单元测试
 * <p>
 * 用于报告 CircularQueue 类的错误。
 */
public class CircularQueueException extends RuntimeException {
    public CircularQueueException(String why) {
        super(why);
    }
}
