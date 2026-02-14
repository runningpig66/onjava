package ch24_equalshashcode;

import java.util.Objects;

/**
 * @author runningpig66
 * @date 2月12日 周四
 * @time 14:55
 * P.454 §C.1 经典的 equals()
 */
public class SuccinctEquality extends Equality {
    public SuccinctEquality(int i, String s, double d) {
        super(i, s, d);
        System.out.println("made 'SuccinctEquality'");
    }

    /*@Override
    public boolean equals(Object rval) {
        return rval instanceof SuccinctEquality &&
                Objects.equals(i, ((SuccinctEquality) rval).i) &&
                Objects.equals(s, ((SuccinctEquality) rval).s) &&
                Objects.equals(d, ((SuccinctEquality) rval).d);
    }*/

    @Override
    public boolean equals(Object rval) {
        return rval instanceof SuccinctEquality se &&
                Objects.equals(i, se.i) &&
                Objects.equals(s, se.s) &&
                Objects.equals(d, se.d);
    }

    public static void main(String[] args) {
        Equality.testAll((i, s, d) -> new SuccinctEquality(i, s, d));
    }
}
/* Output:
made 'Equality'
made 'SuccinctEquality'
made 'Equality'
made 'SuccinctEquality'
made 'Equality'
made 'SuccinctEquality'
-- Testing null --
null instanceof Equality: false
Expected false, got false
-- Testing same object --
same object instanceof Equality: true
Expected true, got true
-- Testing different type --
different type instanceof Equality: false
Expected false, got false
-- Testing same values --
same values instanceof Equality: true
Expected true, got true
-- Testing different values --
different values instanceof Equality: true
Expected false, got false
 */
