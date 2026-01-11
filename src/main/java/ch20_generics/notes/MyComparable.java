package ch20_generics.notes;

/**
 * @author runningpig66
 * @date 2026/1/10 周六
 * @time 16:33
 * P.713 §20.10 问题 §20.10.5 基类会劫持接口
 * <p>
 * notes: 14-泛型逆变：从数据视角到能力视角.md
 */
public interface MyComparable<T> {
    int compare(T other);
}
