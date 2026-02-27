package ch25_annotations;

import onjava.atunit.Test;

import java.util.HashSet;

/**
 * @author runningpig66
 * @date 2月26日 周四
 * @time 4:48
 * P.186 §4.4 基于注解的单元测试
 * {java -cp build/classes/java/main onjava.atunit.AtUnit
 * build/classes/java/main/ch25_annotations/HashSetTest.class}
 * <p>
 * 下面是用断言实现的非嵌入式测试，它对 java.util.HashSet 做了一些简单的测试：
 */
public class HashSetTest {
    HashSet<String> testObject = new HashSet<>();

    @Test
    void initialization() {
        assert testObject.isEmpty();
    }

    @Test
    void tContains() {
        testObject.add("one");
        assert testObject.contains("one");
    }

    @Test
    void tRemove() {
        testObject.add("one");
        testObject.remove("one");
        assert testObject.isEmpty();
    }
}
/* Output:
ch25_annotations.HashSetTest
  . tRemove
  . initialization
  . tContains
OK (3 tests)
 */
