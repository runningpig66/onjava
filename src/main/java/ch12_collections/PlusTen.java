package ch12_collections;

/**
 * @author runningpig66
 * @date 2025-07-29
 * @time 上午 2:08
 * {NewFeature} Since JDK 16
 */
record PlusTen(int x) {
    PlusTen {
        // this.x += 10; // Error: Variable 'x' might not have been initialized
        /*
        在 record 的紧凑构造器（如 PlusTen { ... }）中，
        对 x += 10; 实际上是对构造参数 x 进行修改，而不是对成员变量 x。
        此时成员变量（即字段）this.x 还未初始化，
        不能直接用 this.x 或对其赋值、累加等操作，否则会报“变量尚未初始化”的错误。
        紧凑构造器结束后，编译器会自动执行 this.x = x;，将参数的最终值赋给成员变量。
        总之，紧凑构造器体内操作的是参数，不能直接操作字段。
         */
        x += 10;
    }

    // 对字段的调整只能在构造器中进行。像下面这样仍然是不合法的：
    void mutate() {
        // x += 10; // Error: Cannot assign a value to final variable 'x'
    }

    public static void main(String[] args) {
        System.out.println(new PlusTen(10));
    }
}
/* Output:
PlusTen[x=20]
*/
