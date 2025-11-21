package ch16_validating;

import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author runningpig66
 * @date 2025/11/17 周一
 * @time 0:05
 * 代码清单 P.478 测试：单元测试
 * Simple use of JUnit to test CountedList.
 * <p>
 * 使用 JUnit 来简单地测试 CountedList
 * @BeforeAll 注解标注的方法会在任何测试执行之前运行一次。
 * @AfterAll 注解标注的方法在所有测试执行之后运行一次。
 * @BeforeEach 注解标注的方法通常用于创建和初始化一组公共对象，并在每次测试之前运行。
 * 你也可以将这些初始化操作放在测试类的构造器中，不过我认为 @BeforeEach 更清洗。
 * JUnit 为每个测试创建一个对象，以确保运行的测试之间没有副作用。
 * 不过，所有测试对应的全部对象都是提前一次性创建的（而不是在测试运行之前创建），
 * 所以使用 @BeforeEach 和构造器之间的唯一区别是，@BeforeEach 在测试之前才被调用。
 * 笔记：JUnit 4/5 默认在每个测试方法运行前创建一个新的测试类实例。
 * @AfterEach 如果在每次测试后必须执行清理（比如需要恢复修改过的 static 成员，
 * 需要关闭打开的文件、数据库或网络连接等），请使用 @AfterEach 注解来标注方法。
 */
public class CountedListTest {
    private CountedList list;

    @BeforeAll
    static void beforeAllMsg() {
        System.out.println(">>> Starting CountedListTest");
    }

    @AfterAll
    static void afterAllMsg() {
        System.out.println(">>> Finished CountedListTest");
    }

    @BeforeEach
    public void initialize() {
        list = new CountedList();
        System.out.println("Set up for " + list.getId());
        for (int i = 0; i < 3; i++) {
            list.add(Integer.toString(i));
        }
    }

    @AfterEach
    public void cleanup() {
        System.out.println("Cleaning up " + list.getId());
    }

    @Test
    public void insert() {
        System.out.println("Running testInsert()");
        assertEquals(3, list.size());
        list.add(1, "Insert");
        assertEquals(4, list.size());
        assertEquals("Insert", list.get(1));
    }

    @Test
    public void replace() {
        System.out.println("Running testReplace()");
        assertEquals(3, list.size());
        list.set(1, "Replace");
        assertEquals(3, list.size());
        assertEquals("Replace", list.get(1));
    }

    // A helper method to simplify the code. As long as it's not annotated with @Test,
    // it will not be automatically executed by JUnit.
    private void compare(List<String> list, String[] strings) {
        assertArrayEquals(strings, list.toArray(new String[0]));
    }

    @Test
    public void order() {
        System.out.println("Running testOrder()");
        compare(list, new String[]{"0", "1", "2"});
    }

    @Test
    public void remove() {
        System.out.println("Running testRemove()");
        assertEquals(3, list.size());
        list.remove(1);
        assertEquals(2, list.size());
        compare(list, new String[]{"0", "2"});
    }

    @Test
    public void addAll() {
        System.out.println("Running testAddAll()");
        list.addAll(Arrays.asList(new String[]{"An", "African", "Swallow"}));
        assertEquals(6, list.size());
        compare(list, new String[]{"0", "1", "2", "An", "African", "Swallow"});
    }
}
/* Output:
>>> Starting CountedListTest
CountedList #0
Set up for 0
Running testAddAll()
Cleaning up 0
CountedList #1
Set up for 1
Running testInsert()
Cleaning up 1
CountedList #2
Set up for 2
Running testRemove()
Cleaning up 2
CountedList #3
Set up for 3
Running testOrder()
Cleaning up 3
CountedList #4
Set up for 4
Running testReplace()
Cleaning up 4
>>> Finished CountedListTest
 */
