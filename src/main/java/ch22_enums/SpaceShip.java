package ch22_enums;

import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2月3日 周二
 * @time 16:07
 * P.005 §1.2 在枚举类型中增加自定义方法
 */
public enum SpaceShip {
    SCOUT, CARGO, TRANSPORT, CRUISER, BATTLESHIP, MOTHERSHIP;

    @Override
    public String toString() {
        String id = this.name();
        String lower = id.substring(1).toLowerCase();
        return id.charAt(0) + lower;
    }

    public static void main(String[] args) {
        Stream.of(values()).forEach(System.out::println);
    }
}
/* Output:
Scout
Cargo
Transport
Cruiser
Battleship
Mothership
 */
