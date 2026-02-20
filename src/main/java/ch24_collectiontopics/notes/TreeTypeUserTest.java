package ch24_collectiontopics.notes;

import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.TreeSet;

/**
 * @author runningpig66
 * @date 2月20日 周五
 * @time 3:18
 * P.129 §3.10 Set 与存储顺序
 * <p>
 * 关于 User 对象（包含 id 和 age）在 TreeSet 中按年龄排序的问题，核心矛盾在于：
 * 如何既按 age 排序，又避免 age 相同的不同对象被 TreeSet 误判为重复元素而丢弃。
 * <p>
 * 在使用 TreeSet 等基于红黑树的有序集合时，必须确保存入对象的 compareTo 方法与 equals 方法的逻辑保持严格一致。
 * TreeSet 判断元素唯一性的底层依据仅为 compareTo 方法的返回值是否为 0，而绝不会调用对象的 equals 或 hashCode 方法。
 * 如果 compareTo 仅根据某个非唯一的业务属性（如年龄、分数等）进行单条件比较，会导致具有相同该属性的不同对象在比较时返回 0，
 * 从而被 TreeSet 错误地判定为重复元素并抛弃。为避免此缺陷，在实现 compareTo 方法时，必须在主要排序属性比较相等的分支后，
 * 追加对对象唯一标识符（如 ID、UUID 等）的次级比较逻辑。以此保证只有在两个对象真正在业务和物理层面完全相等
 * （即 equals 返回 true）时，compareTo 才返回 0，从而兼顾排序规则的实现与 Set 集合数据唯一性的契约。
 */
class User implements Comparable<User> {
    private String id;
    private int age;

    public User(String id, int age) {
        this.id = id;
        this.age = age;
    }

    // 1. 重写 equals 方法，基于业务逻辑判断对象是否相等（通常通过唯一标识 id）
    @Override
    public boolean equals(Object o) {
        return o instanceof User user &&
                this.age == user.age &&
                Objects.equals(this.id, user.id);
    }

    // 2. 重写 hashCode 方法，保持与 equals 逻辑一致
    @Override
    public int hashCode() {
        return Objects.hash(age, id);
    }

    // 3. 重写 compareTo 方法（核心）
    @Override
    public int compareTo(@NonNull User o) {
        // 第一级比较：按照年龄进行排序（满足业务排序需求）
        int ageComparison = Integer.compare(this.age, o.age);
        if (ageComparison != 0) {
            return ageComparison; // 如果年龄不同，直接返回结果决定顺序
        }
        // 第二级比较：如果年龄相同，决不能直接返回 0！
        // 必须追加对唯一标识（id）的比较，以防止被 TreeSet 判定为同一元素并去重。
        return this.id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return "User(" + this.id + ", " + this.age + ")";
    }
}

public class TreeTypeUserTest {
    public static void main(String[] args) {
        User user0 = new User("000", 100);
        User user1 = new User("001", 20);
        User user2 = new User("002", 20);
        TreeSet<User> userSet = new TreeSet<>();
        userSet.add(user0);
        userSet.add(user1);
        userSet.add(user2);
        System.out.println(userSet);
    }
}
/* Output:
[User(001, 20), User(002, 20), User(000, 100)]
 */
