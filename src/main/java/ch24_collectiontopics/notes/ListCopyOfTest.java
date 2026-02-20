package ch24_collectiontopics.notes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author runningpig66
 * @date 2月19日 周四
 * @time 22:59
 * P.127 §3.9 可选的操作 §不支持的操作
 * <p>
 * notes: Java 集合中的复制、视图与不可变机制笔记.md
 */
public class ListCopyOfTest {
    static class User {
        private String name;

        public User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "User name " + this.name;
        }
    }

    public static void main(String[] args) {
        List<User> originalList = new ArrayList<>(Arrays.asList(new User("A"), new User("B")));
        // List<User> unmodifiableView = Collections.unmodifiableList(originalList);
        List<User> unmodifiableView = List.copyOf(originalList);

        // 此时，两个list中的内容都是：[User name A, User name B]
        System.out.println(originalList); // [User name A, User name B]
        System.out.println(unmodifiableView); // [User name A, User name B]

        // 由于容器中存放的是可变引用对象，对originalList中对象的修改，还是影响到不可变列表unmodifiableView
        originalList.getFirst().setName("X");
        System.out.println(originalList); // [User name X, User name B]
        System.out.println(unmodifiableView); // [User name X, User name B]

        // 对originalList的容器结构性修改不会影响新的不可变unmodifiableView
        originalList.clear();
        System.out.println(originalList); // []
        System.out.println(unmodifiableView); // [User name X, User name B]

        // 同时我们不能对unmodifiableView进行结构性修改
        try {
            unmodifiableView.clear();
        } catch (Exception e) {
            System.out.println(e); // java.lang.UnsupportedOperationException
        }
    }
}
/* Output:
[User name A, User name B]
[User name A, User name B]
[User name X, User name B]
[User name X, User name B]
[]
[User name X, User name B]
java.lang.UnsupportedOperationException
 */
