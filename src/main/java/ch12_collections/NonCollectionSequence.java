package ch12_collections;

import reflection.pets.Pet;
import reflection.pets.PetCreator;

import java.util.Iterator;

/**
 * @author runningpig66
 * @date 2025-07-30
 * @time 下午 14:07
 */
class PetSequence {
    protected Pet[] pets = new PetCreator().array(8);
}

public class NonCollectionSequence extends PetSequence {
    public Iterator<Pet> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < pets.length;
            }

            @Override
            public Pet next() {
                return pets[index++];
            }
        };
    }

    public static void main(String[] args) {
        NonCollectionSequence nc = new NonCollectionSequence();
        InterfaceVsIterator.display(nc.iterator());
    }
}
/* Output:
0:Rat 1:Manx 2:Cymric 3:Mutt 4:Pug 5:Cymric 6:Pug 7:Manx
 */
