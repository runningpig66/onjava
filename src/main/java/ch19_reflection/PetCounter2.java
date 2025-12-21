package ch19_reflection;

import ch19_reflection.pets.PetCreator;

/**
 * @author runningpig66
 * @date 2025/12/18 周四
 * @time 19:30
 * P.610 §19.3 转型前检查 §19.3.1 使用类字面量
 *
 * PetCounter.count() 接收一个 Creator 参数，因此我们可以很容易地测试 PetCreator。它的输出与 PetCounter.java 中的相同。
 */
public class PetCounter2 {
    public static void main(String[] args) {
        new PetCounter().count(new PetCreator());
    }
}
/* Output:
Rat Manx Cymric Mutt Pug Cymric Pug Manx Cymric Rat EgyptianMau Hamster EgyptianMau Mutt Mutt Cymric Mouse Pug Mouse Cymric
{EgyptianMau=2, Pug=3, Rat=2, Cymric=5, Mouse=2, Cat=9, Manx=7, Rodent=5, Mutt=3, Dog=6, Pet=20, Hamster=1}
 */
