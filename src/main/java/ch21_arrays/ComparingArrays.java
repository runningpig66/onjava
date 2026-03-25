package ch21_arrays;

import onjava.Count;
import onjava.Rand;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2026-03-25
 * @time 22:06
 * P.794 §21.15 数组比较
 * Using Arrays.equals()
 * <p>
 * Arrays 类提供了 equals() 方法来比较一维数组是否相等，以及 deepEquals() 方法来比较多维数组。这些方法都有针对所有基本类型和对象类型的重载版本实现。
 * 两个数组相等意味着其中的元素数量必须相同，并且每个相同位置的元素也必须相等——可以用 equals() 方法来判断
 * （对于基本类型，是由其包装类中的 equals() 方法来比较的，例如 int 元素实际是由 Integer.equals() 来负责比较）。
 */
public class ComparingArrays {
    public static final int SZ = 15;

    static String[][] twoDArray() {
        String[][] md = new String[5][];
        // Arrays.setAll(md, n -> new String[n]);
        Arrays.setAll(md, String[]::new);
        // for (int i = 0; i < md.length; i++) {
        //     Arrays.setAll(md[i], new Rand.String()::get);
        // }
        for (String[] strings : md) {
            Arrays.setAll(strings, new Rand.String()::get);
        }
        return md;
    }

    @SuppressWarnings("ArrayObjectsEquals")
    public static void main(String[] args) {
        int[] a1 = new int[SZ], a2 = new int[SZ];
        Arrays.setAll(a1, new Count.Integer()::get);
        Arrays.setAll(a2, new Count.Integer()::get);
        // static boolean equals(int[] a, int[] a2)
        System.out.println("a1 == a2: " + Arrays.equals(a1, a2));
        a2[3] = 11;
        System.out.println("a1 == a2: " + Arrays.equals(a1, a2));

        Integer[] a1w = new Integer[SZ], a2w = new Integer[SZ];
        Arrays.setAll(a1w, new Count.Integer()::get);
        Arrays.setAll(a2w, new Count.Integer()::get);
        System.out.println("a1w == a2w: " + Arrays.equals(a1w, a2w));
        a2w[3] = 11;
        System.out.println("a1w == a2w: " + Arrays.equals(a1w, a2w));

        String[][] md1 = twoDArray(), md2 = twoDArray();
        System.out.println(Arrays.deepToString(md1));
        /*
        Arrays.deepEquals() 方法专为比较多维数组或嵌套数组的内容而设计，用以弥补数组本身未重写 Object.equals() 方法从而导致默认只能比较内存地址的缺陷。
        其核心机制依赖于基于运行时类型检查（Runtime Type Checking）的递归调用。在遍历数组元素时，底层通过 deepEquals0 方法对元素类型进行精准路由：
        若元素属于对象数组（即满足 instanceof Object[]），则递归调用 deepEquals 进一步降维展开；
        若元素属于一维基本类型数组（如满足 instanceof int[]），则终止递归，直接派发至对应的 Arrays.equals() 重载方法执行线性数值比对；
        若元素为普通非数组对象，则最终调用该对象自身重写的 equals() 方法。这种设计既确保了多维结构的深度内容对比，又利用类型分支拦截了基本类型数组，有效避免了非法转换或无限递归。

        在 Java 严格的类型系统中，必须明确区分“对象”与“对象数组”的边界。基于“一切数组皆对象”的底层设计，
        任何维度的基本类型或引用类型数组实体均直接继承自 java.lang.Object，拥有完整的对象头与内存监视器，因此任何数组执行 instanceof Object 恒为 true。
        然而，一个数组是否属于对象数组（即满足 instanceof Object[]），严格取决于其内部元素的物理存储类型。
        对于一维基本类型数组（如 int[]），其内存连续空间中存储的是纯粹的原始数值而非对象引用，因此它属于 Object 但绝不是 Object[]。
        相反，对于引用类型数组（如 Person[]），其元素为对象引用，故必然属于 Object[]。尤其需要注意的是多维基本类型数组（如 int[][]），由于 Java 采用“数组的数组”实现多维结构，
        二维 int 数组的内部元素是一维的 int[]，而一维 int[] 本身是一个合法的 Object，因此从二维基本类型数组开始，它们均合法地属于 Object[]。
        */
        // static boolean deepEquals(Object[] a1, Object[] a2)
        System.out.println("deepEquals(md1, md2): " + Arrays.deepEquals(md1, md2));
        System.out.println("md1 == md2: " + Arrays.equals(md1, md2));
        md1[4][1] = "#$#$#$#";
        System.out.println(Arrays.deepToString(md1));
        System.out.println("deepEquals(md1, md2): " + Arrays.deepEquals(md1, md2));
    }
}
/* Output:
a1 == a2: true
a1 == a2: false
a1w == a2w: true
a1w == a2w: false
[[], [btpenpc], [btpenpc, cuxszgv], [btpenpc, cuxszgv, gmeinne], [btpenpc, cuxszgv, gmeinne, eloztdv]]
deepEquals(md1, md2): true
md1 == md2: false
[[], [btpenpc], [btpenpc, cuxszgv], [btpenpc, cuxszgv, gmeinne], [btpenpc, #$#$#$#, gmeinne, eloztdv]]
deepEquals(md1, md2): false
 */
