package ch19_reflection.pets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author runningpig66
 * @date 2025/12/18 周四
 * @time 17:07
 * P.609 §19.3 转型前检查 §19.3.1 使用类字面量
 * Using class literals
 * {java ch19_reflection.pets.PetCreator}
 * <p>
 * 如果我们使用类字面量重新实现 Creator，那么最终结果在许多方面都会显得更清晰：
 * <p>
 * 这一次，types 的创建代码并不需要放在 try 块里，因为它在编译时被检查，所以不会抛出任何异常。这和 Class.forName() 不一样。
 * <p>
 * 在即将出现的 PetCounter3.java 示例中，我们会预先加载一个包含所有 Pet 类型（不仅仅是那些随机生成的）的 Map，
 * 因此这个 ALL_TYPES 的 List 是必要的。这里的 types 列表是 ALL_TYPES（使用 List.subList() 创建）的一部分，
 * 它包含了确切的宠物类型，因此可以用来生成随机的 Pet。
 */
public class PetCreator extends Creator {
    // No try block needed.
    public static final List<Class<? extends Pet>> ALL_TYPES =
            // Can be replaced with 'List.of()' call
            Collections.unmodifiableList(Arrays.asList(
                    Pet.class, Dog.class, Cat.class, Rodent.class,
                    Mutt.class, Pug.class, EgyptianMau.class,
                    Manx.class, Cymric.class, Rat.class,
                    Mouse.class, Hamster.class
            ));
    // Types for random creation:
    private static final List<Class<? extends Pet>> TYPES =
            ALL_TYPES.subList(ALL_TYPES.indexOf(Mutt.class), ALL_TYPES.size());

    @Override
    public List<Class<? extends Pet>> types() {
        return TYPES;
    }

    public static void main(String[] args) {
        System.out.println(TYPES);
        List<Pet> pets = new PetCreator().list(7);
        System.out.println(pets);
    }
}
/* Output:
[class ch19_reflection.pets.Mutt, class ch19_reflection.pets.Pug, class ch19_reflection.pets.EgyptianMau, class ch19_reflection.pets.Manx, class ch19_reflection.pets.Cymric, class ch19_reflection.pets.Rat, class ch19_reflection.pets.Mouse, class ch19_reflection.pets.Hamster]
[Rat, Manx, Cymric, Mutt, Pug, Cymric, Pug]
 */
