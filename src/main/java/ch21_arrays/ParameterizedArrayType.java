package ch21_arrays;

/**
 * @author runningpig66
 * @date 3月17日 周二
 * @time 3:20
 * P.761 §21.5 数组和泛型
 * <p>
 * 通常来说，数组和泛型并不能很好地结合。例如你无法实例化一个参数化类型的数组：
 * Peel<Banana>[] peels = new Peel<Banana>[10]; // Illegal
 * 类型擦除移除了参数的类型信息，所以数组必须清楚地知道自身所保存的具体类型，以保证类型安全。不过，数组自身的类型是可以参数化的：
 */
class ClassParameter<T> {
    public T[] f(T[] arg) {
        return arg;
    }
}

class MethodParameter {
    public static <T> T[] f(T[] arg) {
        return arg;
    }
}

public class ParameterizedArrayType {
    public static void main(String[] args) {
        Integer[] ints = {1, 2, 3, 4, 5};
        Double[] doubles = {1.1, 2.2, 3.3, 4.4, 5.5};
        Integer[] ints2 = new ClassParameter<Integer>().f(ints);
        Double[] doubles2 = new ClassParameter<Double>().f(doubles);
        ints2 = MethodParameter.f(ints);
        doubles2 = MethodParameter.f(doubles);
    }
}
