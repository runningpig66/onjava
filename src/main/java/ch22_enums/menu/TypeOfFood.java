package ch22_enums.menu;

import ch22_enums.menu.Food.Appetizer;
import ch22_enums.menu.Food.Coffee;
import ch22_enums.menu.Food.Dessert;
import ch22_enums.menu.Food.MainCourse;

/**
 * @author runningpig66
 * @date 2月4日 周三
 * @time 8:12
 * P.012 §1.7 使用接口来组织枚举
 */
public class TypeOfFood {
    public static void main(String[] args) {
        Food food = Appetizer.SALAD;
        food = MainCourse.LASAGNE;
        food = Dessert.GELATO;
        food = Coffee.CAPPUCCINO;
    }
}
