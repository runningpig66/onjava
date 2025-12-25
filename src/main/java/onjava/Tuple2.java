package onjava;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 15:10
 * P.645 §20.2 简单泛型 §20.2.1 元组库
 * <p>
 * 这个概念被称为元组（tuple），它将一组对象一起包装进了一个对象。该对象的接收方可以读取其中的元素，但不能往里放入新元素。
 * [这个概念也称为数据传输对象（Data Transfer Object）或者信使（Messenger）。]
 * <p>
 * 元组一般无长度限制，其中的每个对象都可以是不同的类型。不过我们会指定每个元素的类型，并且保证接收方读取元素值时得到的是正确的类型。
 * 对于多种不同长度的问题，我们通过创建多个不同的元组来解决。下面是一个持有两个对象的元组：
 */
public class Tuple2<A, B> {
    // public 字段上的 final 定义防止该字段在构造完成后被重新赋值
    public final A a1;
    public final B a2;

    public Tuple2(A a, B b) {
        this.a1 = a;
        this.a2 = b;
    }

    public String rep() {
        return a1 + ", " + a2;
    }

    @Override
    public String toString() {
        return "(" + rep() + ")";
    }
}
