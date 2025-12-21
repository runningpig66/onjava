package ch12_collections;

import ch19_reflection.pets.Cat;
import ch19_reflection.pets.Dog;
import ch19_reflection.pets.Hamster;
import ch19_reflection.pets.Pet;

import java.util.HashMap;
import java.util.Map;

/**
 * @author runningpig66
 * @date 2025-07-28
 * @time 下午 20:59
 */
public class PetMap {
    public static void main(String[] args) {
        Map<String, Pet> petMap = new HashMap<>();
        petMap.put("My Cat", new Cat("Molly"));
        petMap.put("My Dog", new Dog("Ginger"));
        petMap.put("My Hamster", new Hamster("Bosco"));
        System.out.println(petMap);
        Pet dog = petMap.get("My Dog");
        System.out.println(dog);
        System.out.println(petMap.containsKey("My Dog"));
        System.out.println(petMap.containsValue(dog));
    }
}
/* Output:
{My Dog=Dog Ginger, My Cat=Cat Molly, My Hamster=Hamster Bosco}
Dog Ginger
true
true
*/
