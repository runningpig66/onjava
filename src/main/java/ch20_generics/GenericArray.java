package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/30 周二
 * @time 1:26
 * P.682 §20.7 对类型擦除的补偿 §20.7.2 泛型数组
 * <p>
 * 我们来看一个稍微复杂一些的例子。考虑一个简单的泛型数组包装类：
 * <p>
 * 如之前所述，我们无法声明 T[] array = new T[sz]，因此我们可以创建一个对象数组，并对它进行转型。
 * rep() 方法返回一个 T[]，对应到 main() 中的 gai.rep() 则应该返回 Integer[]，但如果你调用了 rep()
 * 并试图将结果作为 Integer[] 的引用来获取，便会抛出 ClassCastException 异常，这仍然是因为运行时类型实际是 Object[]。
 * <p>
 * notes: 06-泛型擦除的边界：为什么 new T() 和 instanceof T 被禁止？.md
 */
public class GenericArray<T> {
    private T[] array;

    @SuppressWarnings({"unchecked"})
    public GenericArray(int sz) {
        // Warning: Unchecked cast: 'java.lang.Object[]' to 'T[]'
        array = (T[]) new Object[sz];
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index) {
        return array[index];
    }

    // Method that exposes the underlying representation:
    public T[] rep() {
        return array;
    }

    public static void main(String[] args) {
        GenericArray<Integer> gai = new GenericArray<>(10);
        try {
            Integer[] ia = gai.rep();
        } catch (ClassCastException e) {
            System.out.println(e.getMessage());
        }
        // This is OK:
        Object[] oa = gai.rep();
    }
}
/* Output: （用 -Xlint:unchecked 来编译：）
E:\IdeaProjects\onjava\src\main\java\ch20_generics\GenericArray.java:21: warning: [unchecked] unchecked cast
        array = (T[]) new Object[sz];
                      ^
  required: T[]
  found:    Object[]
  where T is a type-variable:
    T extends Object declared in class GenericArray
1 warning

Note: E:\IdeaProjects\onjava\src\main\java\ch20_generics\GenericArray.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
class [Ljava.lang.Object; cannot be cast to class [Ljava.lang.Integer; ([Ljava.lang.Object; and [Ljava.lang.Integer; are in module java.base of loader 'bootstrap')
 */
