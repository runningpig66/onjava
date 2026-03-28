package ch21_arrays.notes;

import onjava.ArrayShow;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author runningpig66
 * @date 2026-03-26
 * @time 20:53
 * P.799 §21.17 数组排序
 * Implementing a Comparator for a class
 * <p>
 * 使用 Comparator 的静态工厂方法与方法引用，为未实现 Comparable 接口的实体类构建支持多条件短路判断的链式自定义比较器。
 */
public class Transaction {
    private String category; // 类别（如：餐饮、交通）
    private double amount;   // 金额

    public Transaction(String category, double amount) {
        this.category = category;
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return category + ":" + amount;
    }

    public static void main(String[] args) {
        Transaction[] txs = {
                new Transaction("Dining", 120.5),
                new Transaction("Transport", 30.0),
                new Transaction("Dining", 30.0)
        };
        Arrays.sort(txs, Comparator.comparingDouble(Transaction::getAmount)
                .thenComparing(Transaction::getCategory));
        ArrayShow.show(txs);
    }
}
/* Output:
[Dining:30.0, Transport:30.0, Dining:120.5]
 */
