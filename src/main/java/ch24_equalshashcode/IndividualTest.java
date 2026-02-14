package ch24_equalshashcode;

import ch12_collections.MapOfList;
import ch19_reflection.pets.Individual;
import ch19_reflection.pets.Pet;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author runningpig66
 * @date 2月15日 周日
 * @time 2:26
 * P.473 §C.2 哈希和哈希码 §C.2.3 重写 hashCode()
 */
public class IndividualTest {
    public static void main(String[] args) {
        Set<Individual> pets = new TreeSet<>();
        for (List<? extends Pet> lp : MapOfList.petPeople.values()) {
            for (Pet p : lp) {
                pets.add(p); // 这里使用的容器是TreeSet，跟本小节介绍的hash没关系啊，这里是如何区分元素的？
            }
        }
        pets.forEach(System.out::println);
    }
}
/* Output:
Cat Elsie May
Cat Pinkola
Cat Shackleton
Cat Stanford
Cymric Molly
Dog Margrett
Mutt Spot
Pug Louie aka Louis Snorkelstein Dupree
Rat Fizzy
Rat Freckly
Rat Fuzzy
 */
