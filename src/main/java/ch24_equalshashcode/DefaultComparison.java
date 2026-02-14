package ch24_equalshashcode;

/**
 * @author runningpig66
 * @date 2月12日 周四
 * @time 11:56
 * P.451 §C.1 经典的 equals()
 */
class DefaultComparison {
    private int i, j, k;

    DefaultComparison(int i, int j, int k) {
        this.i = i;
        this.j = j;
        this.k = k;
    }

    public static void main(String[] args) {
        DefaultComparison
                a = new DefaultComparison(1, 2, 3),
                b = new DefaultComparison(1, 2, 3);
        System.out.println(a == a);
        System.out.println(a == b);
    }
}
/* Output:
true
false
 */
