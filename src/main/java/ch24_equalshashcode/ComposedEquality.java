package ch24_equalshashcode;

import java.util.Objects;

/**
 * @author runningpig66
 * @date 2月12日 周四
 * @time 15:40
 * P.455 §C.1 经典的 equals()
 */
class Part {
    String ss;
    double dd;

    Part(String ss, double dd) {
        this.ss = ss;
        this.dd = dd;
    }

    @Override
    public boolean equals(Object rval) {
        return rval instanceof Part p &&
                Objects.equals(ss, p.ss) &&
                Objects.equals(dd, p.dd);
    }
}

public class ComposedEquality extends SuccinctEquality {
    Part part;

    public ComposedEquality(int i, String s, double d) {
        super(i, s, d);
        part = new Part(s, d);
        System.out.println("made 'ComposedEquality'");
    }

    @Override
    public boolean equals(Object rval) {
        return rval instanceof ComposedEquality ce &&
                super.equals(ce) &&
                Objects.equals(part, ce.part);
    }

    public static void main(String[] args) {
        Equality.testAll((i, s, d) -> new ComposedEquality(i, s, d));
    }
}
/* Output:
made 'Equality'
made 'SuccinctEquality'
made 'ComposedEquality'
made 'Equality'
made 'SuccinctEquality'
made 'ComposedEquality'
made 'Equality'
made 'SuccinctEquality'
made 'ComposedEquality'
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
