package ch21_arrays;

import onjava.ArrayShow;
import onjava.Rand;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2026-03-28
 * @time 1:41
 * P.802 §21.18 用 Arrays.binarySearch() 进行二分查找
 * Using Arrays.binarySearch()
 * <p>
 * 一旦数组排好了序，便可以通过 Arrays.binarySearch() 来快速查找某个特定的元素。然而，如果你对未排序的数组使用 binarySearch() 查找，则结果可能无法预测。
 * 下面的示例使用 Rand.Pint 类创建了一个用随机 int 值填充的数组，然后调用了 getAsInt()（这是因为 Rand.Pint 是个 IntSupplier）来生成被查找的测试值：
 */
public class ArraySearching {
    public static void main(String[] args) {
        Rand.Pint rand = new Rand.Pint();
        int[] a = new Rand.Pint().array(25);
        Arrays.sort(a);
        ArrayShow.show("Sorted array", a);
        while (true) {
            int r = rand.getAsInt();
            /*
            当 Arrays.binarySearch() 未能在有序数组中查找到目标元素时，方法会返回一个负数值，其底层计算公式为 -(insertion point) - 1。
            其中 insertion point（插入点）定义为：若将目标值插入该数组且保持数组的有序状态，目标值应当占据的索引位置
            （即第一个大于目标值的元素索引，若目标值大于所有元素，则为数组长度）。该算法引入 - 1 偏移量的核心目的在于避免与索引 0 发生冲突。
            如果目标值小于数组中的所有元素，其理论插入点为 0，若仅返回负的插入点（即 -0），在 Java 内存体系中 -0 与 0 严格等价，
            这会导致程序无法区分“未找到且应插入在首位”与“在索引 0 处找到目标元素”。通过整体减 1 的位移操作，
            能够保证任何未找到的场景均返回严格的负数（例如插入点为 0 时返回 -1）。在实际工程中，若需动态维护有序数组，
            开发者可通过对返回的负数值执行 -r - 1（或利用按位取反操作符 ~r）精准还原出目标元素的应插索引。
            */
            // static int binarySearch(int[] a, int key)
            int location = Arrays.binarySearch(a, r);
            if (location >= 0) {
                System.out.println("Location of " + r + " is " + location +
                        ", a[" + location + "] is " + a[location]);
                break; // Out of while loop
            }
        }
    }
}
/* Output:
Sorted array: [125, 267, 635, 650, 1131, 1506, 1634, 2400, 2766, 3063, 3768, 3941, 4720, 4762, 4948, 5070, 5682, 5807, 6177, 6193, 6656, 7021, 8479, 8737, 9954]
Location of 635 is 2, a[2] is 635
 */
