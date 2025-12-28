package ch20_generics;

import onjava.Suppliers;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

/**
 * @author runningpig66
 * @date 2025/12/26 周五
 * @time 15:36
 * P.664 §20.5 构建复杂模型
 * Building a complex model using generic collections
 * <p>
 * 下面是第二个示例。尽管每个类都是构建块，但总的模块数还是很多的。此处的模型为一个带有通道、货架以及货品的商店：
 */
class Product {
    private final int id;
    private String description;
    private double price;

    Product(int idNumber, String descr, double price) {
        id = idNumber;
        description = descr;
        this.price = price;
        System.out.println(this);
    }

    @Override
    public String toString() {
        return id + ": " + description + ", price: $" + price;
    }

    public void priceChange(double change) {
        price += change;
    }

    public static Supplier<Product> generator = new Supplier<>() {
        private final Random rand = new Random(47);

        @Override
        public Product get() {
            return new Product(
                    rand.nextInt(1000),
                    "Test",
                    Math.round(rand.nextDouble() * 1000.0) + 0.99
            );
        }
    };
}

class Shelf extends ArrayList<Product> {
    Shelf(int nProducts) {
        // Shelf 通过工具 Suppliers.fill() 接收一个 Collection 类（第一个参数），
        // 并通过 Supplier（第二个参数）将 n（第三个参数）个元素填充进该 Collection。
        // Suppliers 类中的方法执行的全都是填充动作的各种变种操作，这个类的定义可在本章末尾找到。
        Suppliers.fill(this, Product.generator, nProducts);
    }
}

class Aisle extends ArrayList<Shelf> {
    Aisle(int nShelves, int nProducts) {
        for (int i = 0; i < nShelves; i++) {
            add(new Shelf(nProducts));
        }
    }
}

class CheckoutStand {
}

class Office {
}

public class Store extends ArrayList<Aisle> {
    private ArrayList<CheckoutStand> checkouts = new ArrayList<>();
    private Office office = new Office();

    public Store(int nAisles, int nShelves, int nProducts) {
        for (int i = 0; i < nAisles; i++) {
            add(new Aisle(nShelves, nProducts));
        }
    }

    // 从 Store.toString() 方法可以看出效果：集合具有很多层，但仍然是类型安全且便于管理的。
    // 令人赞叹是，组装这样一个模型对脑力的要求并非那么高不可攀。
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Aisle a : this) {
            for (Shelf s : a) {
                for (Product p : s) {
                    result.append(p);
                    result.append("\n");
                }
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(new Store(5, 4, 3));
    }
}
/* Output:
258: Test, price: $400.99
861: Test, price: $160.99
868: Test, price: $417.99
207: Test, price: $268.99
551: Test, price: $114.99
278: Test, price: $804.99
520: Test, price: $554.99
140: Test, price: $530.99
704: Test, price: $250.99
575: Test, price: $24.99
674: Test, price: $440.99
826: Test, price: $484.99
33: Test, price: $204.99
810: Test, price: $272.99
138: Test, price: $766.99
689: Test, price: $702.99
746: Test, price: $778.99
368: Test, price: $593.99
105: Test, price: $899.99
222: Test, price: $323.99
257: Test, price: $290.99
317: Test, price: $428.99
80: Test, price: $824.99
899: Test, price: $49.99
654: Test, price: $640.99
209: Test, price: $736.99
903: Test, price: $322.99
894: Test, price: $664.99
57: Test, price: $764.99
8: Test, price: $334.99
367: Test, price: $361.99
882: Test, price: $980.99
299: Test, price: $551.99
17: Test, price: $238.99
53: Test, price: $174.99
994: Test, price: $460.99
703: Test, price: $203.99
434: Test, price: $94.99
332: Test, price: $375.99
965: Test, price: $980.99
228: Test, price: $878.99
677: Test, price: $396.99
862: Test, price: $78.99
244: Test, price: $16.99
42: Test, price: $647.99
843: Test, price: $614.99
886: Test, price: $899.99
615: Test, price: $814.99
333: Test, price: $951.99
213: Test, price: $362.99
669: Test, price: $707.99
158: Test, price: $729.99
601: Test, price: $969.99
851: Test, price: $392.99
631: Test, price: $515.99
332: Test, price: $428.99
971: Test, price: $307.99
110: Test, price: $195.99
913: Test, price: $918.99
830: Test, price: $227.99
258: Test, price: $400.99
861: Test, price: $160.99
868: Test, price: $417.99
207: Test, price: $268.99
551: Test, price: $114.99
278: Test, price: $804.99
520: Test, price: $554.99
140: Test, price: $530.99
704: Test, price: $250.99
575: Test, price: $24.99
674: Test, price: $440.99
826: Test, price: $484.99
33: Test, price: $204.99
810: Test, price: $272.99
138: Test, price: $766.99
689: Test, price: $702.99
746: Test, price: $778.99
368: Test, price: $593.99
105: Test, price: $899.99
222: Test, price: $323.99
257: Test, price: $290.99
317: Test, price: $428.99
80: Test, price: $824.99
899: Test, price: $49.99
654: Test, price: $640.99
209: Test, price: $736.99
903: Test, price: $322.99
894: Test, price: $664.99
57: Test, price: $764.99
8: Test, price: $334.99
367: Test, price: $361.99
882: Test, price: $980.99
299: Test, price: $551.99
17: Test, price: $238.99
53: Test, price: $174.99
994: Test, price: $460.99
703: Test, price: $203.99
434: Test, price: $94.99
332: Test, price: $375.99
965: Test, price: $980.99
228: Test, price: $878.99
677: Test, price: $396.99
862: Test, price: $78.99
244: Test, price: $16.99
42: Test, price: $647.99
843: Test, price: $614.99
886: Test, price: $899.99
615: Test, price: $814.99
333: Test, price: $951.99
213: Test, price: $362.99
669: Test, price: $707.99
158: Test, price: $729.99
601: Test, price: $969.99
851: Test, price: $392.99
631: Test, price: $515.99
332: Test, price: $428.99
971: Test, price: $307.99
110: Test, price: $195.99
913: Test, price: $918.99
830: Test, price: $227.99
 */
