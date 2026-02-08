package ch22_enums;

/**
 * @author runningpig66
 * @date 2月4日 周三
 * @time 17:57
 * P.022 §1.10 常量特定方法
 */
public enum OverrideConstantSpecific {
    NUT, BOLT,
    WASHER {
        @Override
        void f() {
            System.out.println("Overridden method");

        }
    };

    void f() {
        System.out.println("default behavior");
    }

    public static void main(String[] args) {
        for (OverrideConstantSpecific ocs : values()) {
            System.out.print(ocs + ": ");
            ocs.f();
        }
    }
}
/* Output:
NUT: default behavior
BOLT: default behavior
WASHER: Overridden method
 */
