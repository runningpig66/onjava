[TOC]

# 10-类型兼容性：数组协变 vs 泛型不变 vs 通配符

**关联代码**：
`ch20_generics/CovariantArrays.java`
`ch20_generics/NonCovariantGenerics.java`
`ch20_generics/GenericsAndCovariance.java`

## 0. 概述：类型兼容性的演变

本节通过三个递进的示例（`CovariantArrays` -> `NonCovariantGenerics` -> `GenericsAndCovariance`），阐述了 Java 在处理“容器类型兼容性”问题上的演变与权衡。核心矛盾在于：如何在保持“类型安全”的前提下，提供“灵活性”。

- **类型安全 (Type Safety)**：防止容器内混入错误类型，避免运行时 `ClassCastException`。
- **灵活性 (Flexibility)**：允许通用代码处理多态集合（如将子类集合传递给父类引用）。

---

## 1. 数组的协变 (Covariant Arrays) —— "灵活但存在运行时风险"

**背景**：在 Java 1.0 时期，为了支持通用的数组操作：如 `Arrays.sort` 或 `System.arraycopy` 等通用方法能处理任何类型的数组，Java 做出了设计妥协，允许数组协变。

- **定义**：Java 数组支持**协变 (Covariance)**。如果 `Apple` 是 `Fruit` 的子类，则 `Apple[]` 也是 `Fruit[]` 的子类。
- **现象**：可以将子类数组赋值给父类数组引用 (`Fruit[] f = new Apple[10]`)。
- **缺陷 (逻辑漏洞)**：虽然引用是 `Fruit[]`，但底层存储实际上是 `Apple[]`。编译器允许向引用中放入 `Orange`（因为 Orange 也是 Fruit），但这违背了底层 `Apple[]` 的存储约束。
- **机制 (具体化 Reified)**：数组是**具体化**的。JVM 在运行时保留了数组的完整类型信息，因此会在写入非法类型时抛出 `ArrayStoreException` 以阻止堆污染。
- **总结**：编译期放行（灵活），运行期报错（安全底线）。

---

## 2. 泛型的不变性 (Invariant Generics) —— "绝对安全但僵化"

**演进**：为了修正数组协变带来的类型安全隐患，Java 5 引入的泛型采用了严格的**不变性**设计，在编译期彻底切断风险。

- **定义**：普通泛型是**不变的 (Invariant)**。无论 A 与 B 存在何种继承关系，`List<A>` 与 `List<B>` **均无继承关系**。
- **现象**：不能将 `ArrayList<Apple>` 赋值给 `List<Fruit>` 引用，编译器直接报错。
- **原因**：为了修正数组的缺陷。编译器无法确定 `List<Fruit>` 实际上指向的是装 Apple 的列表还是装 Orange 的列表。为了防止像数组那样发生“放入异构类型”的错误，编译器在赋值阶段就切断了可能性。且泛型在运行时会被**擦除 (Erasure)**，无法像数组那样在运行时拦截错误，因此必须在编译期禁止。
- **缺陷**：丧失了灵活性。即使只是为了读取数据（将 Apple 列表当作 Fruit 列表读取），也无法传递引用。
- **总结**：编译期禁止一切潜在风险，牺牲了多态的灵活性。

---

## 3. 通配符的妥协 (Wildcards) —— "由写换读的和平条约"

**平衡**：为了在“安全性”与“灵活性”之间寻找平衡，Java 引入了**上界通配符** (`? extends T`)，通过放弃写入权限来换取协变能力。

- **定义**：使用通配符 `? extends T` 实现泛型的协变。`List<? extends Fruit>` 表示“持有某种 Fruit 或其子类的列表”。
- **现象**：允许将 `ArrayList<Apple>` 赋值给 `List<? extends Fruit>` 引用，恢复了类似数组的灵活性。
- **权衡 (The Trade-off)**：
  1. **限制写入 (Write Prohibition)**：为了安全性，编译器禁止向此类 List 调用任何 `add()` 方法（null 除外）。这从根源上杜绝了向 Apple 列表放入 Orange 的可能性（解决了数组的问题）。
  2. **允许读取 (Read Permission)**：编译器允许读取元素，并保证读出的元素至少是 `Fruit` 类型（解决了普通泛型僵化的问题）。
- **赋值兼容性**：`List<? extends Fruit>` 引用并不关心右侧 List 内部实际存储的是单一类型（如 `List<Apple>`）
  还是混合类型（如 `List<Fruit>`），只要泛型参数符合继承边界，均可赋值。它提供了一个统一的**“只读视图”**。

---

## 4. 结论

通配符 `? extends` 不是为了让容器“什么都能做”，而是通过**“放弃写入权限”**这一代价，换取了**“引用子类容器”**的灵活性，从而在编译期实现了类型安全。
