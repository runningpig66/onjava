package ch22_enums.menu;

import onjava.Enums;

/**
 * @author runningpig66
 * @date 2月4日 周三
 * @time 8:16
 * P.012 §1.7 使用接口来组织枚举
 */
public enum Course {
    APPETIZER(Food.Appetizer.class),
    MAINCOURSE(Food.MainCourse.class),
    DESSERT(Food.Dessert.class),
    COFFEE(Food.Coffee.class);

    private Food[] values;

    private Course(Class<? extends Food> kind) {
        values = kind.getEnumConstants();
    }

    public Food randomSelection() {
        return Enums.random(values);
    }
}
