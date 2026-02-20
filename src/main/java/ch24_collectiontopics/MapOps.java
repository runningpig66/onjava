package ch24_collectiontopics;

import onjava.CountMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author runningpig66
 * @date 2月20日 周五
 * @time 22:36
 * P.140 §3.12 理解 Map §3.12.1 性能
 * Things you can do with Maps
 * <p>
 * 对 Map 中键的要求和对 Set 中元素的要求是一样的，我们在 TypesForSets.java 中已经看到了。任何键都必须有一个 equals() 方法。
 * 如果这个键被用在了使用哈希的 Map 中，它必须还有一个适当的 hashCode()。如果这个键被用在了 TreeMap 中，它必须实现 Comparable。
 * 下列示例使用之前定义的 CountMap 测试数据集，演示了可以在 Map 接口上执行的操作：
 * <p>
 * printKeys() 方法演示了如何生成 Map 的一个 Collection 视图。keySet() 方法会生成一个由 Map 中的键组成的 Set。
 * 打印 values() 方法的结果，得到一个包含了 Map 中所有值的 Collection。（注意，键必须是唯一的，但是值可以重复。）
 * 这些 Collection 的底层由 Map 中的结构支撑，所以对 Collection 的任何修改都会在与其关联的 Map 中体现出来。
 */
public class MapOps {
    public static void printKeys(Map<Integer, String> map) {
        System.out.print("Size = " + map.size() + ", ");
        System.out.print("Keys: ");
        // Produce a Set of the keys:
        System.out.println(map.keySet());
    }

    public static void test(Map<Integer, String> map) {
        System.out.println(map.getClass().getSimpleName());
        map.putAll(new CountMap(25));
        // Map has 'Set' behavior for keys:
        map.putAll(new CountMap(25));
        printKeys(map);
        // Producing a Collection of the values:
        System.out.print("Values:");
        System.out.println(map.values());
        System.out.println(map);
        System.out.println("map.containsKey(11): " + map.containsKey(11));
        System.out.println("map.get(11): " + map.get(11));
        System.out.println("map.containsValue(\"F0\"): " + map.containsValue("F0"));
        Integer key = map.keySet().iterator().next();
        System.out.println("First key in map: " + key);
        map.remove(key);
        printKeys(map);
        map.clear();
        System.out.println("map.isEmpty(): " + map.isEmpty());
        map.putAll(new CountMap(25));
        // Operations on the Set change the Map:
        map.keySet().removeAll(map.keySet());
        System.out.println("map.isEmpty(): " + map.isEmpty());
    }

    public static void main(String[] args) {
        test(new HashMap<>());
        test(new TreeMap<>());
        test(new LinkedHashMap<>());
        // IdentityHashMap 的特殊性（测试结果最乱的一项）：在你的输出中，map.containsValue("F0") 返回了 false。
        // 这是因为 CountMap 每次生成的 "F0" 可能是不同的字符串对象（引用不同），在 IdentityHashMap 眼里，它们不是同一个东西。
        // 普通的 HashMap 使用 equals() 判断键是否相等，而 IdentityHashMap 使用 ==（引用相等）。
        // 它不调用对象的 hashCode()，而是使用 System.identityHashCode()。这个方法通常直接基于对象的内存地址计算。
        // 因为它内部没有使用“链表”来处理冲突，而是使用了线性探测法（如果坑位被占了，就找下一个空位）。
        // 由于它是直接按内存地址分布数据的，而内存地址是随机且不连续的，所以打印出来的 Key 顺序看起来完全没有规律。
        test(new IdentityHashMap<>());
        test(new ConcurrentHashMap<>());
        test(new WeakHashMap<>());
    }
}
/* Output:
HashMap
Size = 25, Keys: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24]
Values:[A0, B0, C0, D0, E0, F0, G0, H0, I0, J0, K0, L0, M0, N0, O0, P0, Q0, R0, S0, T0, U0, V0, W0, X0, Y0]
{0=A0, 1=B0, 2=C0, 3=D0, 4=E0, 5=F0, 6=G0, 7=H0, 8=I0, 9=J0, 10=K0, 11=L0, 12=M0, 13=N0, 14=O0, 15=P0, 16=Q0, 17=R0, 18=S0, 19=T0, 20=U0, 21=V0, 22=W0, 23=X0, 24=Y0}
map.containsKey(11): true
map.get(11): L0
map.containsValue("F0"): true
First key in map: 0
Size = 24, Keys: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24]
map.isEmpty(): true
map.isEmpty(): true
TreeMap
Size = 25, Keys: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24]
Values:[A0, B0, C0, D0, E0, F0, G0, H0, I0, J0, K0, L0, M0, N0, O0, P0, Q0, R0, S0, T0, U0, V0, W0, X0, Y0]
{0=A0, 1=B0, 2=C0, 3=D0, 4=E0, 5=F0, 6=G0, 7=H0, 8=I0, 9=J0, 10=K0, 11=L0, 12=M0, 13=N0, 14=O0, 15=P0, 16=Q0, 17=R0, 18=S0, 19=T0, 20=U0, 21=V0, 22=W0, 23=X0, 24=Y0}
map.containsKey(11): true
map.get(11): L0
map.containsValue("F0"): true
First key in map: 0
Size = 24, Keys: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24]
map.isEmpty(): true
map.isEmpty(): true
LinkedHashMap
Size = 25, Keys: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24]
Values:[A0, B0, C0, D0, E0, F0, G0, H0, I0, J0, K0, L0, M0, N0, O0, P0, Q0, R0, S0, T0, U0, V0, W0, X0, Y0]
{0=A0, 1=B0, 2=C0, 3=D0, 4=E0, 5=F0, 6=G0, 7=H0, 8=I0, 9=J0, 10=K0, 11=L0, 12=M0, 13=N0, 14=O0, 15=P0, 16=Q0, 17=R0, 18=S0, 19=T0, 20=U0, 21=V0, 22=W0, 23=X0, 24=Y0}
map.containsKey(11): true
map.get(11): L0
map.containsValue("F0"): true
First key in map: 0
Size = 24, Keys: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24]
map.isEmpty(): true
map.isEmpty(): true
IdentityHashMap
Size = 25, Keys: [5, 17, 18, 6, 19, 24, 4, 9, 1, 2, 23, 12, 22, 14, 20, 15, 16, 10, 0, 8, 3, 11, 7, 13, 21]
Values:[F0, R0, S0, G0, T0, Y0, E0, J0, B0, C0, X0, M0, W0, O0, U0, P0, Q0, K0, A0, I0, D0, L0, H0, N0, V0]
{5=F0, 17=R0, 18=S0, 6=G0, 19=T0, 24=Y0, 4=E0, 9=J0, 1=B0, 2=C0, 23=X0, 12=M0, 22=W0, 14=O0, 20=U0, 15=P0, 16=Q0, 10=K0, 0=A0, 8=I0, 3=D0, 11=L0, 7=H0, 13=N0, 21=V0}
map.containsKey(11): true
map.get(11): L0
map.containsValue("F0"): false
First key in map: 5
Size = 24, Keys: [18, 17, 6, 19, 24, 4, 9, 1, 2, 23, 12, 22, 14, 20, 15, 16, 10, 0, 8, 3, 11, 7, 13, 21]
map.isEmpty(): true
map.isEmpty(): true
ConcurrentHashMap
Size = 25, Keys: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24]
Values:[A0, B0, C0, D0, E0, F0, G0, H0, I0, J0, K0, L0, M0, N0, O0, P0, Q0, R0, S0, T0, U0, V0, W0, X0, Y0]
{0=A0, 1=B0, 2=C0, 3=D0, 4=E0, 5=F0, 6=G0, 7=H0, 8=I0, 9=J0, 10=K0, 11=L0, 12=M0, 13=N0, 14=O0, 15=P0, 16=Q0, 17=R0, 18=S0, 19=T0, 20=U0, 21=V0, 22=W0, 23=X0, 24=Y0}
map.containsKey(11): true
map.get(11): L0
map.containsValue("F0"): true
First key in map: 0
Size = 24, Keys: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24]
map.isEmpty(): true
map.isEmpty(): true
WeakHashMap
Size = 25, Keys: [24, 22, 23, 20, 21, 18, 19, 16, 17, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0]
Values:[Y0, W0, X0, U0, V0, S0, T0, Q0, R0, P0, O0, N0, M0, L0, K0, J0, I0, H0, G0, F0, E0, D0, C0, B0, A0]
{24=Y0, 22=W0, 23=X0, 20=U0, 21=V0, 18=S0, 19=T0, 16=Q0, 17=R0, 15=P0, 14=O0, 13=N0, 12=M0, 11=L0, 10=K0, 9=J0, 8=I0, 7=H0, 6=G0, 5=F0, 4=E0, 3=D0, 2=C0, 1=B0, 0=A0}
map.containsKey(11): true
map.get(11): L0
map.containsValue("F0"): true
First key in map: 24
Size = 24, Keys: [22, 23, 20, 21, 18, 19, 16, 17, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0]
map.isEmpty(): true
map.isEmpty(): true
 */
