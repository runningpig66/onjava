package ch20_generics;

import onjava.Suppliers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author runningpig66
 * @date 2月2日 周一
 * @time 10:18
 * P.744 §20.17 Java 8 中的辅助潜在类型机制 §使用 Supplier 的泛型方法
 * A very simple bank teller simulation
 * <p>
 * 下面在一个小的模拟程序中测试 Suppliers 工具，这里同样也用到在本章前面定义过的 RandomList：
 * <p>
 * notes: 17-泛型推断分析：构造器引用与类型约束.md
 */
class Customer {
    private static long counter = 1;
    private final long id = counter++;

    @Override
    public String toString() {
        return "Customer " + id;
    }
}

class Teller {
    private static long counter = 1;
    private final long id = counter++;

    @Override
    public String toString() {
        return "Teller " + id;
    }
}

class Bank {
    private List<BankTeller> tellers = new ArrayList<>();

    public void put(BankTeller bt) {
        tellers.add(bt);
    }
}

public class BankTeller {
    public static void serve(Teller t, Customer c) {
        System.out.println(t + " serves " + c);
    }

    public static void main(String[] args) {
        // Demonstrate create():
        RandomList<Teller> tellers = Suppliers.create(RandomList::new, Teller::new, 4);
        // Demonstrate fill():
        List<Customer> customers = Suppliers.fill(new ArrayList<>(), Customer::new, 12);
        customers.forEach(c -> serve(tellers.select(), c));
        // Demonstrate assisted latent typing:
        Bank bank = Suppliers.fill(new Bank(), Bank::put, BankTeller::new, 3);
        // Can also use second version of fill():
        List<Customer> customers2 = Suppliers.fill(new ArrayList<>(), List::add, Customer::new, 12);
    }
}
/* Output:
Teller 3 serves Customer 1
Teller 2 serves Customer 2
Teller 3 serves Customer 3
Teller 1 serves Customer 4
Teller 1 serves Customer 5
Teller 3 serves Customer 6
Teller 1 serves Customer 7
Teller 2 serves Customer 8
Teller 3 serves Customer 9
Teller 3 serves Customer 10
Teller 2 serves Customer 11
Teller 4 serves Customer 12
 */
