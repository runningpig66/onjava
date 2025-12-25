package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 2:11
 * P.645 §20.2 简单泛型
 * <p>
 * 注意，h3 定义的右侧现在使用了“钻石”语法，而不用再将左边的类型信息复制一遍。在本书的剩余部分，你将随处看到这种用法。
 */
public class Diamond {
    public static void main(String[] args) {
        GenericHolder<Automobile> h3 = new GenericHolder<>();
    }
}
