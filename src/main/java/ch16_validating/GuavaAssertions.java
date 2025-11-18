package ch16_validating;

import com.google.common.base.VerifyException;

import static com.google.common.base.Verify.verify;
import static com.google.common.base.Verify.verifyNotNull;

/**
 * @author runningpig66
 * @date 2025/11/19 周三
 * @time 0:15
 * 代码清单 P.485 前置条件：断言：2. Guava 里的断言
 * Assertions that are always enabled.
 * <p>
 * 启用 Java 原生的断言很麻烦．因此 Guava 团队添加了一个 Verify 类，提供了始终启用的替换断言。
 * 他们建议静态导人 Verify 方法：
 * <p>
 * public static void verify(
 * boolean expression,
 * @Nullable String errorMessageTemplate,
 * @Nullable Object @Nullable ... errorMessageArgs)
 * <p>
 * public static <T> T verifyNotNull(
 * @Nullable T reference,
 * @Nullable String errorMessageTemplate,
 * @Nullable Object @Nullable ... errorMessageArgs)
 */
public class GuavaAssertions {
    public static void main(String[] args) {
        verify(2 + 2 == 4);
        try {
            verify(1 + 2 == 4);
        } catch (VerifyException e) {
            System.out.println(e);
        }
        try {
            verify(1 + 2 == 4, "Bad math");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            verify(1 + 2 == 4, "Bad math: %s", "not 4");
        } catch (VerifyException e) {
            System.out.println(e.getMessage());
        }
        String s = "";
        s = verifyNotNull(s);
        s = null;
        try {
            verifyNotNull(s);
        } catch (VerifyException e) {
            System.out.println(e.getMessage());
        }
        try {
            verifyNotNull(s, "Shouldn't be null: %s", "arg s");
        } catch (VerifyException e) {
            System.out.println(e.getMessage());
        }
    }
}
/* Output:
com.google.common.base.VerifyException
Bad math
Bad math: not 4
expected a non-null reference
Shouldn't be null: arg s
 */
