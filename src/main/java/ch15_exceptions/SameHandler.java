package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/11 周二
 * @time 17:39
 * 代码清单 P.430 捕捉任何异常：多重捕捉
 * <p>
 * 如果我们想以同样的方式处理一组异常，并且它们有一个共同的基类，那么直接捕捉这个基类即可。
 * 但是如果它们没有共同的基类，在 Java7 之前，必须为每一个异常写一个 catch 子句。
 */
class EBase1 extends Exception {
}

class Except1 extends EBase1 {
}

class EBase2 extends Exception {
}

class Except2 extends EBase2 {
}

class EBase3 extends Exception {
}

class Except3 extends EBase3 {
}

class EBase4 extends Exception {
}

class Except4 extends EBase4 {
}

public class SameHandler {
    void x() throws Except1, Except2, Except3, Except4 {
    }

    void process() {
    }

    void f() {
        try {
            x();
        } catch (Except1 e) {
            process();
        } catch (Except2 e) {
            process();
        } catch (Except3 e) {
            process();
        } catch (Except4 e) {
            process();
        }
    }
}
