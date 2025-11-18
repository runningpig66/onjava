package ch16_validating;

import java.util.ArrayList;

/**
 * @author runningpig66
 * @date 2025/11/16 周日
 * @time 21:10
 * 代码清单 P.478 测试：单元测试
 * Keeps track of how many of itself are created.
 * <p>
 * CountedList 继承了 ArrayList, 并添加了一些信息来跟踪 CountedList 的创建数量：
 */
public class CountedList extends ArrayList<String> {
    private static int counter = 0;
    private int id = counter++;

    public CountedList() {
        System.out.println("CountedList #" + id);
    }

    public int getId() {
        return id;
    }
}
