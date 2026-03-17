package ch21_arrays;

import onjava.ArrayShow;

/**
 * @author runningpig66
 * @date 3月14日 周六
 * @time 18:06
 * P.753 §21.2 数组是一等对象
 * Initialization & re-assignment of arrays
 */
public class ArrayOptions {
    public static void main(String[] args) {
        // Arrays of objects:
        BerylliumSphere[] a; // Uninitialized local
        BerylliumSphere[] b = new BerylliumSphere[5];

        // The references inside the array are automatically initialized to null:
        ArrayShow.show("b", b);
        // 数组 c 演示了如何创建数组，并给 c 中所有的位置都分配了 BerylliumSphere 对象。
        BerylliumSphere[] c = new BerylliumSphere[4];
        for (int i = 0; i < c.length; i++) {
            if (c[i] == null) { // Can test for null reference
                c[i] = new BerylliumSphere();
            }
        }

        // “聚合初始化”一个数组：即在一个语句中既创建了数组对象（和数组 c 一样在堆上使用 new 关键字隐式地创建），
        // 同时又使用 BerylliumSphere 对象初始化了其中所有元素。
        // Aggregate initialization:
        BerylliumSphere[] d = {
                new BerylliumSphere(),
                new BerylliumSphere(),
                new BerylliumSphere()
        };

        // “动态聚合初始化”一个数组：之前数组 d 所使用的聚合初始化必须在定义 d 的时候使用，
        // 但是本例中使用的方法可以在任何地方创建并初始化数组对象。
        // Dynamic aggregate initialization:
        a = new BerylliumSphere[]{
                new BerylliumSphere(), new BerylliumSphere(),
        };
        // (Trailing comma is optional)

        System.out.println("a.length = " + a.length);
        System.out.println("b.length = " + b.length);
        System.out.println("c.length = " + c.length);
        System.out.println("d.length = " + d.length);
        a = d;
        System.out.println("a.length = " + a.length);

        // Arrays of primitives:
        int[] e; // Null reference
        int[] f = new int[5];

        // The primitives inside the array are automatically initialized to zero:
        ArrayShow.show("f", f);
        int[] g = new int[4];
        for (int i = 0; i < g.length; i++) {
            g[i] = i * i;
        }
        int[] h = {11, 47, 93};

        // Compile error: variable e might not have been initialized
        //- System.out.println("e.length = " + e.length);
        System.out.println("f.length = " + f.length);
        System.out.println("g.length = " + g.length);
        System.out.println("h.length = " + h.length);
        e = h;
        System.out.println("e.length = " + e.length);
        e = new int[]{1, 2};
        System.out.println("e.length = " + e.length);
    }
}
/* Output:
b: [null, null, null, null, null]
a.length = 2
b.length = 5
c.length = 4
d.length = 3
a.length = 3
f: [0, 0, 0, 0, 0]
f.length = 5
g.length = 4
h.length = 3
e.length = 3
e.length = 2
 */
