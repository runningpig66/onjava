package ch24_equalshashcode;

import onjava.Countries;
import org.jspecify.annotations.NonNull;

import java.util.*;

/**
 * @author runningpig66
 * @date 2月12日 周四
 * @time 21:04
 * P.463 §C.2 哈希和哈希码 §C.2.1 理解 hashCode() [IMP]
 * A Map implemented with ArrayLists
 */
public class SlowMap<K, V> extends AbstractMap<K, V> {
    private List<K> keys = new ArrayList<>();
    private List<V> values = new ArrayList<>();

    @Override
    public V put(K key, V value) {
        V oldValue = get(key); // The old value or null
        if (!keys.contains(key)) {
            keys.add(key);
            values.add(value);
        } else {
            values.set(keys.indexOf(key), value);
        }
        return oldValue;
    }

    /* 问题一：为什么 get() 的入参是 Object 而不是 K？
     * 1. 核心语义：基于相等性的查询。Map.get() 的本质是“查询”而非“写入”。底层依赖 equals(Object) 进行匹配，
     * 既然 equals 接受任何对象，查询操作也应允许传入任何对象，只要逻辑上相等即可。
     * 2. 泛型通配符的可用性：若设计为 get(K key)，在通配符引用（如 Map<?, V>）下，K 被捕获为 "unknown"，
     * 编译器会禁止传入任何非 null 参数，导致 Map 彻底丧失查询能力。设计为 Object 保证了只读视图依然可用。
     * - 核心洞察：通配符承诺的是“类型安全”（防止堆污染），而不是承诺“不可变性”。
     * - 安全性论证：get() 和 remove() 等操作属于“查询/提取”类，不会向容器内放入错误类型的对象（不会导致堆污染），
     * remove(Object key) 方法的核心判定逻辑是“基于对象相等性的查询”，附带了一个删除动作。因此使用 Object 是安全的妥协。
     * 如果它不妥协（设计成 remove(E)），那么在通配符引用下，我们就无法移除特定对象了，这会极大限制容器的可用性。
     * 为了让通配符引用依然具备基本的“查询能力”，List 被迫将 contains、remove、indexOf 的参数设计成了 Object。这与 Map.get(Object) 的妥协逻辑是完全一致的。
     * 3. API 设计的二律背反——Java 容器试图在一个接口中平衡两种冲突需求：
     * - 写入安全：必须用泛型 E (如 add(E))。代价是通配符下不可用。
     * - 查询灵活：必须用 Object (如 get(Object))。代价是丧失部分编译期检查。
     * 4. 历史兼容性：为了兼容 Java 5 之前的遗留代码（原生类型），保证旧代码迁移到泛型版本时方法签名保持一致，不会因方法签名变更而报错。
     */

    /*
     * 问题二：Warning: Suspicious call to 'List.contains()' & Suspicious call to 'List.indexOf()'
     * 这是一个 IDE 的误报。IDE 发现我们在 List<K> 中查找一个 Object 类型的 key，认为这可能导致类型不匹配从而永远找不到元素（返回 -1）。
     * 但在实现 Map.get(Object) 协议时，我们必须处理“用户传入错误类型的 key”这种情况。
     * List.indexOf() 返回 -1 正是我们需要的行为（代表 Map 中不存在该键，随后返回 null）。
     * 在基础设施代码中，利用这种“宽泛查询”是完全正确的。
     */

    @Override
    public V get(Object key) { // key: type Object, not K
        if (!keys.contains(key)) return null;
        return values.get(keys.indexOf(key));
    }

    @Override
    public @NonNull Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = new HashSet<>();
        Iterator<K> ki = keys.iterator();
        Iterator<V> vi = values.iterator();
        while (ki.hasNext()) set.add(new MapEntry<>(ki.next(), vi.next()));
        return set;
    }

    public static void main(String[] args) {
        SlowMap<String, String> m = new SlowMap<>();
        m.putAll(Countries.capitals(8));
        m.forEach((k, v) -> System.out.println(k + "=" + v));
        System.out.println(m.get("BENIN"));
        m.entrySet().forEach(System.out::println);
        // SlowMap 的字符串表示形式是由 AbstractMap 中定义的 toString() 方法自动生成的。
        System.out.println(m.toString());
    }
}
/* Output:
CAMEROON=Yaounde
ANGOLA=Luanda
BURKINA FASO=Ouagadougou
BURUNDI=Bujumbura
ALGERIA=Algiers
BENIN=Porto-Novo
CAPE VERDE=Praia
BOTSWANA=Gaberone
Porto-Novo
CAMEROON=Yaounde
ANGOLA=Luanda
BURKINA FASO=Ouagadougou
BURUNDI=Bujumbura
ALGERIA=Algiers
BENIN=Porto-Novo
CAPE VERDE=Praia
BOTSWANA=Gaberone
{CAMEROON=Yaounde, ANGOLA=Luanda, BURKINA FASO=Ouagadougou, BURUNDI=Bujumbura, ALGERIA=Algiers, BENIN=Porto-Novo, CAPE VERDE=Praia, BOTSWANA=Gaberone}
 */
