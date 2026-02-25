package ch25_annotations.simplest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author runningpig66
 * @date 2月24日 周二
 * @time 10:59
 * P.175 §4.3 用 javac 处理注解 §4.3.1 最简单的处理器
 * A bare-bones annotation
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE,
        ElementType.PACKAGE, ElementType.FIELD, ElementType.LOCAL_VARIABLE})
public @interface Simple {
    String value() default "-default-";
}
