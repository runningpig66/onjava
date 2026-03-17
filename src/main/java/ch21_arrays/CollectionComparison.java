package ch21_arrays;

import onjava.ArrayShow;
import onjava.Suppliers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author runningpig66
 * @date 3月14日 周六
 * @time 16:15
 * P.749 §21.1 数组为何特殊
 */
class BerylliumSphere {
    private static long counter;
    private final long id = counter++;

    @Override
    public String toString() {
        return "Sphere " + id;
    }
}

public class CollectionComparison {
    public static void main(String[] args) {
        BerylliumSphere[] spheres = new BerylliumSphere[10];
        for (int i = 0; i < 5; i++) {
            spheres[i] = new BerylliumSphere();
        }
        ArrayShow.show(spheres);
        System.out.println(spheres[4]);

        List<BerylliumSphere> sphereList = Suppliers.create(ArrayList::new, BerylliumSphere::new, 5);
        System.out.println(sphereList);
        System.out.println(sphereList.get(4));

        int[] integers = {0, 1, 2, 3, 4, 5};
        ArrayShow.show(integers);
        System.out.println(integers[4]);

        List<Integer> intList = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5));
        intList.add(97);
        System.out.println(intList);
        System.out.println(intList.get(4));
    }
}
/* Output:
[Sphere 0, Sphere 1, Sphere 2, Sphere 3, Sphere 4, null, null, null, null, null]
Sphere 4
[Sphere 5, Sphere 6, Sphere 7, Sphere 8, Sphere 9]
Sphere 9
[0, 1, 2, 3, 4, 5]
4
[0, 1, 2, 3, 4, 5, 97]
4
 */
