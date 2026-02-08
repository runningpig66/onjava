package ch22_enums;

/**
 * @author runningpig66
 * @date 2月7日 周六
 * @time 9:44
 * P.036 §1.11 多路分发 §1.11.1 使用枚举类型分发
 * Switching one enum on another
 */
public interface Competitor<T extends Competitor<T>> {
    Outcome compete(T competitor);
}
