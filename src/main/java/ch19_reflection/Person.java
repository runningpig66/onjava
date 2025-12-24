package ch19_reflection;

import java.util.Optional;

/**
 * @author runningpig66
 * @date 2025/12/23 周二
 * @time 14:39
 * P.626 §19.8 使用 Optional
 * Using Optional with regular classes
 * <p>
 * 当使用内置的 null 来表示对象不存在时，为了确保安全，你必须在每次使用对象的引用时都测试一下它是否为 null。
 * 这会变得很乏味，并产生冗长的代码。问题在于 null 没有自己的行为，而当你尝试用它做任何事情时，就会产生一个 NullPointerException。
 * 我们在第 13 章中介绍过 java.util.Optional，它创建了一个简单的代理来屏蔽潜在的 null 值。Optional 对象会阻止你的代码直接抛出 NPE。
 * <p>
 * 任何使用 Person 的人在访问这些字符串字段时都会被强制使用 Optional 接口，因此不会意外触发 NullPointerException。
 */
public class Person {
    public final Optional<String> first;
    public final Optional<String> last;
    public final Optional<String> address;
    // etc.
    public final boolean empty;

    Person(String first, String last, String address) {
        this.first = Optional.ofNullable(first);
        this.last = Optional.ofNullable(last);
        this.address = Optional.ofNullable(address);
        empty = this.first.isEmpty() &&
                this.last.isEmpty() &&
                // !this.address.isPresent();
                this.address.isEmpty();
    }

    Person(String first, String last) {
        this(first, last, null);
    }

    Person(String last) {
        this(null, last, null);
    }

    Person() {
        this(null, null, null);
    }

    @Override
    public String toString() {
        if (empty) {
            return "<Empty>";
        }
        return (first.orElse("") +
                " " + last.orElse("") +
                " " + address.orElse("")).trim();
    }

    public static void main(String[] args) {
        System.out.println(new Person());
        System.out.println(new Person("Smith"));
        System.out.println(new Person("Bob", "Smith"));
        System.out.println(new Person("Bob", "Smith",
                "11 Degree Lane, Frostbite Falls, MN"));
    }
}
/* Output:
<Empty>
Smith
Bob Smith
Bob Smith 11 Degree Lane, Frostbite Falls, MN
 */
