package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/30 周二
 * @time 2:34
 * P.683 §20.7 对类型擦除的补偿 §20.7.2 泛型数组
 * <p>
 * 由于类型擦除的缘故，数组的运行时类型只能是 Object[]，如果我们立刻将其转型为 T[]，那么在编译时，
 * 数组的实际类型便会丢失，编译器就可能会错过对某些潜在错误的检查。因此，更好的办法是在集合内使用 Object[]，
 * 并在使用某个数组元素的时候增加转型为 T 的操作。我们来看看在示例 GenericArray2.java 中，这样做的效果如何：
 * <p>
 * 乍一看，这好像没多大变化，只是改变了类型转换的地方。如果没有 @SuppressWarnings 注解，则仍会产生“unchecked”警告。
 * 然而现在内部表达所用的是 Object[]，而不是 T[]。在调用 get() 方法时，该方法会将对象转型为 T，这实际上是正确的类型，
 * 因此是安全的。不过如果调用 rep()，该方法会再次试图将 Object[] 转型为 T[]，这仍然是错误的，
 * 并且会产生编译时警告和运行时异常。因此，没有任何办法可以推翻底层的数组类型，该类型只能是 Object[]。
 * 在内部将 array 当作 Object[] 而不是 T[] 来进行处理，这样做的好处是，可以减少由于你忘记了数组的运行时类型，
 * 而意外产生 bug 的可能性（虽然这样的 bug 大部分，或者说全部，可以很快地在运行时被检测出来）。
 */
public class GenericArray2<T> {
    private Object[] array;

    public GenericArray2(int sz) {
        array = new Object[sz];
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        // Warning: Unchecked cast: 'java.lang.Object' to 'T'
        return (T) array[index];
    }

    @SuppressWarnings("unchecked")
    public T[] rep() {
        // Warning: Unchecked cast: 'java.lang.Object[]' to 'T[]'
        return (T[]) array;
    }

    public static void main(String[] args) {
        GenericArray2<Integer> gai = new GenericArray2<>(10);
        for (int i = 0; i < 10; i++) {
            gai.put(i, i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.print(gai.get(i) + " ");
        }
        System.out.println();
        try {
            Integer[] ia = gai.rep();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
/* Output:
0 1 2 3 4 5 6 7 8 9
java.lang.ClassCastException: class [Ljava.lang.Object; cannot be cast to class [Ljava.lang.Integer; ([Ljava.lang.Object; and [Ljava.lang.Integer; are in module java.base of loader 'bootstrap')
 */
