package ch25_annotations.ifx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author runningpig66
 * @date 2月25日 周三
 * @time 4:26
 * P.178 §4.3 用 javac 处理注解 §4.3.2 更复杂的处理器
 * javac-based annotation processing
 * <p>
 * 以下示例是一个注解，它从一个类中提取 public 方法，以将它们转换为接口：
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ExtractInterface {
    String interfaceName() default "-!!-";
}
