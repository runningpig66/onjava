package ch12_collections;

/**
 * @author runningpig66
 * @date 2025-07-29
 * @time 上午 1:52
 * {NewFeature} Since JDK 16
 */
record Point(int x, int y) {
    void assertPositive(int val) {
        if (val < 0) {
            throw new IllegalArgumentException("negative");
        }
    }

    Point { // 紧凑：没有参数列表
        /*
        紧凑构造器是 record 独有的构造器写法，只需写类型名和大括号，
        在里面直接使用 record 参数字段，常用于参数校验等初始化逻辑，语法比普通构造器更简洁。
        这里直接使用 x, y，无需参数列表。这里写的内容在所有构造 Point 时都会执行。
         */
        assertPositive(x);
        assertPositive(y);
    }
}
