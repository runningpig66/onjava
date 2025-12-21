package ch19_reflection;

import ch19_reflection.pets.Pet;
import ch19_reflection.pets.PetCreator;
import onjava.TypeCounter;

/**
 * @author runningpig66
 * @date 2025/12/19 周五
 * @time 16:49
 * P.613 §19.3 转型前检查 §19.3.3 递归计数
 * <p>
 * notes: TypeCounter.md
 * Java 中 instanceof、isInstance() 和 isAssignableFrom() 的区别
 */
public class PetCounter4 {
    public static void main(String[] args) {
        TypeCounter counter = new TypeCounter(Pet.class);
        new PetCreator().stream()
                .limit(20)
                .peek(counter::count)
                .forEach(pet -> System.out.print(pet.getClass().getSimpleName() + " "));
        // 输出显示，该工具（TypeCounter）对基类型和确切类型都进行了计数。
        System.out.println("\n" + counter);
    }
}
/* Output:
Rat Manx Cymric Mutt Pug Cymric Pug Manx Cymric Rat EgyptianMau Hamster EgyptianMau Mutt Mutt Cymric Mouse Pug Mouse Cymric
{Manx=7, Pet=20, Mutt=3, EgyptianMau=2, Mouse=2, Cat=9, Hamster=1, Cymric=5, Rodent=5, Rat=2, Dog=6, Pug=3}
 */
