package ch16_validating;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author runningpig66
 * @date 2025/11/21 周五
 * @time 22:45
 * P506 §16.4 日志
 * <p>
 * SLF4J 提供了一个成熟的工具来报告有关程序的信息，其效率儿乎与前面示例中的技术相同。
 * 对于非常简单的信息记录，可以执行以下操作：
 * Gradle 依赖：implementation("ch.qos.logback:logback-classic:1.5.21")
 * 配置文件：src/main/resources/logback.xml
 */
public class SLF4JLogging {
    private static Logger log = LoggerFactory.getLogger(SLF4JLogging.class);

    public static void main(String[] args) {
        log.info("hello logging");
    }
}
/* Output:
2025-11-22T00:24:57.156
[main] INFO  ch16_validating. - hello logging
 */
