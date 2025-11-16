package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/14 周五
 * @time 2:00
 * 代码清单 P.435 捕捉任何异常：精确地重新抛出异常
 * <p>
 * 在 Java7 之前，如果我们捕获了一个异常，那么只能重新抛出这种类型的异常。
 * 这会导致代码中出现不稍确的问题，Java7 已经修复了。所以在 Java7 之前，以下代码是无法通过编译的。
 */
class BaseException extends Exception {
}

class DerivedException extends BaseException {
}

public class PreciseRethrow {
    // 因为 catch 捕获了一个 BaseException。所以编译器会强制我们声明 catcher() throws BaseException,
    // 尽管它实际上要抛出的是更具体的 DerivedException. 从 Java7 开始，这段代码可以编译了，这个修复虽然很小，但很有用。
    void catcher() throws DerivedException {
        try {
            throw new DerivedException();
        } catch (BaseException e) {
            throw e;
        }
    }
}
