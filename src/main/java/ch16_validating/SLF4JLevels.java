package ch16_validating;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author runningpig66
 * @date 2025/11/22 周六
 * @time 0:29
 * P507 §16.4 日志：日志级别
 * <p>
 * SLF4J 提供了多个级别的报告。下面的示例按“严重性“的递增顺序显示了所有的级别：
 * Gradle 依赖：implementation("ch.qos.logback:logback-classic:1.5.21")
 * 配置文件：src/main/resources/logback.xml
 */
public class SLF4JLevels {
    private static Logger log = LoggerFactory.getLogger(SLF4JLevels.class);

    public static void main(String[] args) {
        log.trace("Hello");
        log.debug("Logging");
        log.info("Using");
        log.warn("The SLF4J");
        log.error("Facade");
    }
}
/* Output:
2025-11-22T00:32:17.399
[main] TRACE ch16_validating.SLF4JLevels - Hello
2025-11-22T00:32:17.402
[main] DEBUG ch16_validating.SLF4JLevels - Logging
2025-11-22T00:32:17.402
[main] INFO  ch16_validating.SLF4JLevels - Using
2025-11-22T00:32:17.402
[main] WARN  ch16_validating.SLF4JLevels - The SLF4J
2025-11-22T00:32:17.402
[main] ERROR ch16_validating.SLF4JLevels - Facade
 */
