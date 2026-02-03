package ch20_generics;

/**
 * @author runningpig66
 * @date 2026-01-23
 * @time 22:48
 * P.739 §20.16 对于缺少（直接的）潜在类型机制的补偿 §20.16.2 将方法应用于序列
 * <p>
 * 为了测试 Apply，先创建一个 Shape 类：
 */
public class Shape {
    private static long counter = 0;
    private final long id = counter++;

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + id;
    }

    public void rotate() {
        System.out.println(this + " rotate");
    }

    public void resize(int newSize) {
        System.out.println(this + " resize " + newSize);
    }
}
