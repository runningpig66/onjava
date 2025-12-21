package ch19_reflection;

import ch19_reflection.pets.*;

import java.util.HashMap;

/**
 * @author runningpig66
 * @date 2025/12/18 周四
 * @time 17:55
 * P.608 §19.3 转型前检查
 * Using instanceof
 * <p>
 * 如果想要知道 Pet 有多少，我们需要一个工具来跟踪各种不同类型的 Pet 的数量。此时采用 Map 就非常适合：键可以是 Pet 类型的名称，
 * 而值则是保存了 Pet 数量的 Integer。这样，你可以查询“有多少个 Hamster 对象”，使用 instanceof 来获得对应 Pet 的数量：
 * <p>
 * instanceof 有一个相当严格的限制：只能将其与命名类型进行比较，而不能与一个 Class 对象进行比较。在前面的例子中，
 * 你可能认为像这样写一大堆的 instanceof 表达式很乏味，的确是这样的。但是如果你想创建一个 Class 对象数组，
 * 并将其与那些对象进行比较，从而将 instanceof 巧妙地自动化，这是不可能的（不过稍后你会看到另一个替代方案）。
 * 这个限制其实并不像你想象的那么严重，因为最终你会明白，如果代码里有许多的 instanceof 表达式式，那么这个设计可能是存在缺陷的。
 * <p>
 * notes: TypeCounter.md
 * Java 中 instanceof、isInstance() 和 isAssignableFrom() 的区别
 */
public class PetCounter {
    static class Counter extends HashMap<String, Integer> {
        public void count(String type) {
            Integer quantity = get(type);
            if (quantity == null) {
                put(type, 1);
            } else {
                put(type, quantity + 1);
            }
        }
    }

    private Counter counter = new Counter();

    // 在 countPet() 中，我们使用 instanceof 来对流中的每个Pet进行测试和计数。
    private void countPet(Pet pet) {
        System.out.print(pet.getClass().getSimpleName() + " ");
        if (pet instanceof Pet) counter.count("Pet");
        if (pet instanceof Dog) counter.count("Dog");
        if (pet instanceof Mutt) counter.count("Mutt");
        if (pet instanceof Pug) counter.count("Pug");
        if (pet instanceof Cat) counter.count("Cat");
        if (pet instanceof EgyptianMau) counter.count("EgyptianMau");
        if (pet instanceof Manx) counter.count("Manx");
        if (pet instanceof Cymric) counter.count("Cymric");
        if (pet instanceof Rodent) counter.count("Rodent");
        if (pet instanceof Rat) counter.count("Rat");
        if (pet instanceof Mouse) counter.count("Mouse");
        if (pet instanceof Hamster) counter.count("Hamster");
    }

    public void count(Creator creator) {
        creator.stream().limit(20).forEach(pet -> countPet(pet));
        System.out.println();
        System.out.println(counter);
    }

    public static void main(String[] args) {
        new PetCounter().count(new ForNamePetCreator());
    }
}
/* Output:
Rat Manx Cymric Mutt Pug Cymric Pug Manx Cymric Rat EgyptianMau Hamster EgyptianMau Mutt Mutt Cymric Mouse Pug Mouse Cymric
{EgyptianMau=2, Pug=3, Rat=2, Cymric=5, Mouse=2, Cat=9, Manx=7, Rodent=5, Mutt=3, Dog=6, Pet=20, Hamster=1}
 */
