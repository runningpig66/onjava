package ch12_collections;

/**
 * @author runningpig66
 * @date 2025-07-29
 * @time 上午 2:49
 * {NewFeature} Since JDK 16
 */
record Value(int x) {
    Value(int x) { // With the parameter list
        x += 10;
        this.x = x; // 必须显式初始化
    }

    public static void main(String[] args) {
        System.out.println(new Value(10));
    }
}
/* Output:
Value[x=20]
 */
