package ch22_enums;

import java.util.EnumMap;
import java.util.Map;

import static ch22_enums.AlarmPoints.*;

/**
 * @author runningpig66
 * @date 2月4日 周三
 * @time 12:56
 * P.017 §1.9 使用 EnumMap
 * Basics of EnumMaps
 */
interface Command {
    void action();
}

public class EnumMaps {
    public static void main(String[] args) {
        EnumMap<AlarmPoints, Command> em = new EnumMap<>(AlarmPoints.class);
        em.put(KITCHEN, () -> System.out.println("Kitchen fire!"));
        em.put(BATHROOM, () -> System.out.println("Bathroom alert!"));
        for (Map.Entry<AlarmPoints, Command> e : em.entrySet()) {
            System.out.print(e.getKey() + ": ");
            e.getValue().action();
        }
        try { // If there's no value for a particular key:
            em.get(UTILITY).action();
        } catch (Exception e) {
            System.out.println("Expected: " + e);
        }
    }
}
/* Output:
BATHROOM: Bathroom alert!
KITCHEN: Kitchen fire!
Expected: java.lang.NullPointerException: Cannot invoke "ch22_enums.Command.action()" because the return value of "java.util.EnumMap.get(Object)" is null
 */
