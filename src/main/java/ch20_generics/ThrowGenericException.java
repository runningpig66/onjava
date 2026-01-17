package ch20_generics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author runningpig66
 * @date 2026/1/13 周二
 * @time 17:27
 * P.722 §20.13 异常
 * <p>
 * 由于类型擦除的缘故，catch 子句无法捕获到泛型类型的异常，这是因为在编译时和运行时，都必须知晓异常的确切类型才行。
 * 同样，泛型类无法直接或间接地继承 Throwable（这还可以进一步防止你试图定义无法捕获的泛型异常）。
 * 不过，类型参数可以用于方法声明中的 throws 子句。这意味着你可以编写能够随受检查的异常类型而变化的泛型代码：
 * <p>
 * Processor (处理器) 实现了 process() 方法, 并且可能抛出类型 E 的异常。
 * process() 的结果被保存在 List<T> resultCollector 中 [ 称为采集参数 (collecting parameter) ]。
 * ProcessRunner 中包含用于执行其保存的每个 Process 对象的 processAll() 方法, 该方法返回 resultCollector。
 * <p>
 * 除非可以参数化抛出的异常, 否则由于检查型异常, 你无法泛化地编写该代码。
 */
interface Processor<T, E extends Exception> {
    void process(List<T> resultCollector) throws E;
}

class ProcessRunner<T, E extends Exception> extends ArrayList<Processor<T, E>> {
    List<T> processAll() throws E {
        List<T> resultCollector = new ArrayList<>();
        for (Processor<T, E> processor : this) {
            processor.process(resultCollector);
        }
        return resultCollector;
    }
}

class Failure1 extends Exception {
}

class Processor1 implements Processor<String, Failure1> {
    static int count = 3;

    @Override
    public void process(List<String> resultCollector) throws Failure1 {
        if (count-- > 1) {
            resultCollector.add("Hep!");
        } else {
            resultCollector.add("Ho!");
        }
        if (count < 0) {
            throw new Failure1();
        }
    }
}

class Failure2 extends Exception {
}

class Processor2 implements Processor<Integer, Failure2> {
    static int count = 2;

    @Override
    public void process(List<Integer> resultCollector) throws Failure2 {
        if (count-- == 0) {
            resultCollector.add(47);
        } else {
            resultCollector.add(11);
        }
        if (count < 0) {
            throw new Failure2();
        }
    }
}

public class ThrowGenericException {
    public static void main(String[] args) {
        ProcessRunner<String, Failure1> runner = new ProcessRunner<>();
        for (int i = 0; i < 3; i++) {
            runner.add(new Processor1());
        }
        try {
            System.out.println(runner.processAll());
        } catch (Failure1 e) {
            System.out.println(e);
        }

        ProcessRunner<Integer, Failure2> runner2 = new ProcessRunner<>();
        for (int i = 0; i < 3; i++) {
            runner2.add(new Processor2());
        }
        try {
            System.out.println(runner2.processAll());
        } catch (Failure2 e) {
            System.out.println(e);
        }
    }
}
/* Output:
[Hep!, Hep!, Ho!]
ch20_generics.Failure2
 */
