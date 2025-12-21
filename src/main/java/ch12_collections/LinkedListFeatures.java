package ch12_collections;

import ch19_reflection.pets.Hamster;
import ch19_reflection.pets.Pet;
import ch19_reflection.pets.PetCreator;
import ch19_reflection.pets.Rat;

import java.util.LinkedList;

/**
 * @author runningpig66
 * @date 2025-07-28
 * @time 上午 2:13
 */
public class LinkedListFeatures {
    public static void main(String[] args) {
        LinkedList<Pet> pets = new LinkedList<>(new PetCreator().list(5));
        System.out.println(pets);
        // 完全相同：
        System.out.println("pets.getFirst(): " + pets.getFirst());
        System.out.println("pets.element(): " + pets.element());
        // 仅当列表为空时存在区别：
        System.out.println("pets.peek(): " + pets.peek());
        // 完全相同：移除并返回第一个元素：
        System.out.println("pets.remove(): " + pets.remove());
        System.out.println("pets.removeFirst(): " + pets.removeFirst());
        // Only differs in empty-list behavior:
        System.out.println("pets.poll(): " + pets.poll());
        System.out.println(pets);
        pets.addFirst(new Rat());
        System.out.println("After addFirst(): " + pets);
        pets.offer(new PetCreator().get());
        System.out.println("After offer(): " + pets);
        pets.add(new PetCreator().get());
        System.out.println("After add(): " + pets);
        pets.addLast(new Hamster());
        System.out.println("After addLast(): " + pets);
        System.out.println("pets.removeLast(): " + pets.removeLast());
    }
}
/* Output:
[Rat, Manx, Cymric, Mutt, Pug]
pets.getFirst(): Rat
pets.element(): Rat
pets.peek(): Rat
pets.remove(): Rat
pets.removeFirst(): Manx
pets.poll(): Cymric
[Mutt, Pug]
After addFirst(): [Rat, Mutt, Pug]
After offer(): [Rat, Mutt, Pug, Rat]
After add(): [Rat, Mutt, Pug, Rat, Rat]
After addLast(): [Rat, Mutt, Pug, Rat, Rat, Hamster]
pets.removeLast(): Hamster
*/
