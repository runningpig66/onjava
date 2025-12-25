package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 1:41
 * P.643 §20.2 简单泛型
 * <p>
 * 在 Java 5 之前，我们会简单地通过持有一个 Object 对象来实现：
 * <p>
 * 这样 ObjectHolder 就可以持有任何东西了————在本例中，ObjectHolder 持有了 3 个不同类型的对象。
 */
public class ObjectHolder {
    private Object a;

    public ObjectHolder(Object a) {
        this.a = a;
    }

    public Object get() {
        return a;
    }

    public void set(Object a) {
        this.a = a;
    }

    public static void main(String[] args) {
        ObjectHolder h2 = new ObjectHolder(new Automobile());
        Automobile a = (Automobile) h2.get();
        h2.set("Not an Automobile");
        String s = (String) h2.get();
        h2.set(1); // Autoboxes to Integer
        Integer x = (Integer) h2.get();
    }
}
