package ch16_validating;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author runningpig66
 * @date 2025/11/20 周四
 * @time 3:49
 * 代码清单 P.498 前置条件：使用 Guava 里的前置条件
 * <p>
 * 请注意，checkNotNull() 会返回其参数，因此你可以在表达式中通过内联的方式使用它。
 * 下面的示例在构造器中使用它来防止构造包含 null 值的对象：
 */
public class NonNullConstruction {
    private Integer n;
    private String s;

    NonNullConstruction(Integer n, String s) {
        this.n = checkNotNull(n);
        this.s = checkNotNull(s);
    }

    public static void main(String[] args) {
        NonNullConstruction nnc = new NonNullConstruction(null, "Trousers");
    }
}
