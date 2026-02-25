package ch25_annotations.database;

/**
 * @author runningpig66
 * @date 2月24日 周二
 * @time 5:13
 * P.170 §4.2 编写注解处理器 §4.2.3 生成外部文件
 * Sample of nested annotations
 */
public @interface Uniqueness {
    Constraints constraints() default @Constraints(unique = true);
}
