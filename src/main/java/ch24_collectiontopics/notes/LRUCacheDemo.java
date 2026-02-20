package ch24_collectiontopics.notes;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author runningpig66
 * @date 2月21日 周六
 * @time 3:58
 * P.143 §3.12 理解 Map §3.12.3 LinkedHashMap
 * <p>
 * 基于 LinkedHashMap 实现的简易 LRU (最近最少使用) 缓存机制演示。
 * 1. 开启 LinkedHashMap 的 accessOrder = true 属性。
 * 2. 重写 removeEldestEntry 方法自定义数据驱逐规则。
 * 3. 精确计算 initialCapacity 避免底层哈希表发生无意义的扩容。
 */
class SimpleLRUCache<K, V> extends LinkedHashMap<K, V> {
    // 缓存的业务最大容量
    private final int maxCapacity;

    public SimpleLRUCache(int maxCapacity) {
        // 容量计算公式: (最大条目数 / 负载因子) + 1。确保数据达到最大条目时，也不会触发底层数组的扩容。
        // 第三个参数 true 极其关键：代表开启基于访问顺序（而非插入顺序）的链表维护。
        super((int) Math.ceil(maxCapacity / 0.75f) + 1, 0.75f, true);
        this.maxCapacity = maxCapacity;
    }

    // 当底层调用 put() 插入新元素后，会自动调用此方法。
    // 如果返回 true，底层会自动将双向链表的头节点（即最久未访问的数据）从哈希表和链表中移除。
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxCapacity;
    }
}

public class LRUCacheDemo {
    public static void main(String[] args) {
        // 业务需求：创建一个最多只能存 3 个活跃用户的缓存
        SimpleLRUCache<Integer, String> cache = new SimpleLRUCache<>(3);

        System.out.println("1. 依次存入 3 个用户...");
        cache.put(1, "User_A");
        cache.put(2, "User_B");
        cache.put(3, "User_C");
        // 此时状态: {1=User_A, 2=User_B, 3=User_C}  (左侧最老，右侧最新)
        System.out.println("当前缓存: " + cache);

        System.out.println("\n2. 业务代码读取了 User_A (触发 LRU 机制)...");
        cache.get(1);
        // User_A 被移动到链表尾部（最安全的位置）。User_B 变成了最老的数据。
        System.out.println("当前缓存: " + cache);

        System.out.println("\n3. 新用户 User_D 登录，存入缓存...");
        cache.put(4, "User_D");
        // 容量超出 3，触发 removeEldestEntry。
        // 最老的 User_B 被无情驱逐，User_A 因为刚刚被访问过而免于一死。
        System.out.println("当前缓存: " + cache);

        System.out.println("\n4. 修改已存在的 User_C 的数据...");
        cache.put(3, "User_C_Updated");
        // put 操作同样算作一次“访问”，User_C 被更新并移动到了尾部。
        // 此时最老的数据变成了 User_A。
        System.out.println("当前缓存: " + cache);
    }
}
/* Output:
1. 依次存入 3 个用户...
当前缓存: {1=User_A, 2=User_B, 3=User_C}

2. 业务代码读取了 User_A (触发 LRU 机制)...
当前缓存: {2=User_B, 3=User_C, 1=User_A}

3. 新用户 User_D 登录，存入缓存...
当前缓存: {3=User_C, 1=User_A, 4=User_D}

4. 修改已存在的 User_C 的数据...
当前缓存: {1=User_A, 4=User_D, 3=User_C_Updated}
 */
