package ch12_collections;

import reflection.pets.Pet;
import reflection.pets.PetCreator;

import java.util.List;
import java.util.ListIterator;

/**
 * @author runningpig66
 * @date 25/07/27/周日
 * @time 上午 1:35
 */
public class ListIteration {
    public static void main(String[] args) {
        // next()：返回游标右边的第一个元素，并将游标向后移动一格。
        // nextIndex()：返回下一个元素的索引（即 next() 会返回的元素的下标）。
        // previousIndex()：返回上一个元素的索引（即 previous() 会返回的元素的下标）。
        // previous()：返回游标左边的第一个元素，并将游标向前移动一格。
        List<Pet> pets = new PetCreator().list(8);
        ListIterator<Pet> it = pets.listIterator();
        while (it.hasNext()) {
            System.out.print(it.next() +
                    ", " + it.nextIndex() +
                    ", " + it.previousIndex() + "; ");
        }
        System.out.println();
        // 反向：
        while (it.hasPrevious()) {
            System.out.print(it.previous().id() + " ");
        }
        System.out.println();
        System.out.println(pets);
        it = pets.listIterator(3);
        while (it.hasNext()) {
            it.next();
            it.set(new PetCreator().get());
        }
        System.out.println(pets);
    }
}
/* Output:
Rat, 1, 0; Manx, 2, 1; Cymric, 3, 2; Mutt, 4, 3; Pug, 5, 4; Cymric, 6, 5; Pug, 7, 6; Manx, 8, 7;
7 6 5 4 3 2 1 0
[Rat, Manx, Cymric, Mutt, Pug, Cymric, Pug, Manx]
[Rat, Manx, Cymric, Rat, Rat, Rat, Rat, Rat]
*/
