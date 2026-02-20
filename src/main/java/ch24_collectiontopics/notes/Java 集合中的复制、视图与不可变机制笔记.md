# Java 集合中的复制、视图与不可变机制笔记

在 Java 集合框架中，数据的传递和拷贝涉及多种不同的底层实现。根据使用的 API 不同，集合与原数据之间可能存在浅拷贝、视图共享或完全的隔离关系。理解这些差异对于避免 `ArrayStoreException`、`ConcurrentModificationException` 等运行时异常以及维护数据的不可变性具有实际意义。

## 1. 集合的浅拷贝实现

通过构造函数如 `new ArrayList<>(collection)` 实例化新集合时，底层执行的是浅拷贝（Shallow Copy）。

浅拷贝意味着 JVM 会在堆内存中为新集合分配一个全新的底层数组（即 `elementData`），但数组中存放的元素依然是原集合中对象的引用。因此，对新集合进行结构性修改（如 `add`、`remove`）不会影响原集合；但如果修改的是集合内某个可变对象的内部状态，两个集合都会反映出这一变化。

在 `ArrayList` 的构造函数源码中，有一个关于类型安全的细节处理。当调用传入集合的 `toArray()` 方法获取数组后，源码会判断该集合的具体类型是否为官方的原生 `ArrayList`。如果是，则直接将返回的数组赋值给 `elementData`，因为原生 `ArrayList.toArray()` 保证返回一个全新且类型纯正的 `Object[]`。如果传入的是其他类型的集合，源码会强制调用 `Arrays.copyOf(a, size, Object[].class)` 进行二次处理。

`ArrayList` 构造函数的核心实现如下：`ArrayList.java`

```java
    public ArrayList(Collection<? extends E> c) {
        Object[] a = c.toArray();
        if ((size = a.length) != 0) {
            if (c.getClass() == ArrayList.class) {
                elementData = a;
            } else {
                elementData = Arrays.copyOf(a, size, Object[].class);
            }
        } else {
            // replace with empty array.
            elementData = EMPTY_ELEMENTDATA;
        }
    }
```

而在 `else` 分支中用于防御性处理的 `Arrays.copyOf` 底层源码如下：`Arrays.java`

```java
    @IntrinsicCandidate
    public static <T,U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
        @SuppressWarnings("unchecked")
        T[] copy = ((Object)newType == (Object)Object[].class)
            ? (T[]) new Object[newLength]
            : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, 0, copy, 0,
                         Math.min(original.length, newLength));
        return copy;
    }
```

这一设计的历史原因在于 Java 数组的协变性。在早期的 Java 版本中，部分集合（如 `Arrays.asList` 返回的内部嵌套类）的 `toArray()` 方法可能会返回具体类型的数组（如 `String[]`），却向上转型为 `Object[]`。如果 `ArrayList` 直接接收这个底层真实类型为 `String[]` 的数组，后续向其中添加其他类型元素（如 `Integer`）时，会在运行时抛出 `ArrayStoreException`。强制调用 `Arrays.copyOf` 并指定 `Object[].class`，可以确保在内存中重新开辟一块纯正的 Object 数组，从而消除类型污染的隐患。

## 2. 视图机制与可选操作

与浅拷贝不同，视图（View）机制不会创建新的底层数据结构，而是直接与原集合或数组共享物理存储。

`list.subList(from, to)` 是典型的视图实现。它返回的（`ArrayList.SubList`）对象不包含独立的数组，内部仅维护了指向原 List 的引用以及起始偏移量（offset）和长度（size）。由于共享同一块内存，通过视图修改元素会直接同步到原集合。需要注意的是，如果绕过视图直接对原集合进行了结构性修改（例如添加或删除元素），会导致视图内部维护的索引状态失效，随后再操作该视图会引发 `ConcurrentModificationException`。

ArrayList.java

```java
    public List<E> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, size);
        return new SubList<>(this, fromIndex, toIndex);
    }

    private static class SubList<E> extends AbstractList<E> implements RandomAccess {...
```

另一个常见的视图是 `Arrays.asList()`。它返回的是 `Arrays` 类内部的一个私有静态嵌套类 `Arrays.ArrayList`，该类直接持有了传入的数组引用。由于 Java 原生数组的长度是固定的，这个内部类虽然实现了 `List` 接口，但不支持任何会改变容器容量的方法。如果调用 `add` 或 `remove`，会直接抛出 `UnsupportedOperationException`。这种通过抛出异常来屏蔽接口中不被支持的方法的设计，在 Java 集合框架中被称为“可选操作”。

Arrays.java

```java
    public static <T> List<T> asList(T... a) {
        return new ArrayList<>(a);
    }

	private static class ArrayList<E> extends AbstractList<E>
        implements RandomAccess, java.io.Serializable
    {...
```

## 3. 不可变集合的演进

为了限制外部对集合的修改，Java 提供了不可变集合的 API，但其底层实现经历了从代理包装到硬拷贝的演进。

使用 `Collections.unmodifiableList(list)` 生成的是一个只读代理视图。包装类内部保留了对原集合的引用，当调用读取方法时，它将请求转发给原集合；当调用修改方法时，它拦截并抛出 `UnsupportedOperationException`。这种方式的问题在于，它只能限制通过该包装器引用进行的修改。如果程序的其他部分仍持有原集合的直接引用，并通过该引用修改了数据，那么这个所谓的“不可变”包装器内部的数据也会随之改变，这在多线程或复杂数据传递场景下容易引发状态不一致。

Collections.java

```java
    static class UnmodifiableList<E> extends UnmodifiableCollection<E> implements List<E> {
        final List<? extends E> list;

        UnmodifiableList(List<? extends E> list) {
            super(list);
            this.list = list;
        }

        public E get(int index) {return list.get(index);}
        ...
```

为了提供真正的不可变保证，Java 9 引入了 `List.copyOf()` 和 `List.of()`。`List.copyOf()` 不再使用代理模式，而是执行防卫性拷贝。它在底层创建一个全新的私有数组，将原集合的数据逐一复制进去，并在复制过程中进行严格的非空校验（遇到 `null` 会抛出异常）。最终，这个私有数组被封装在一个专门的不可变类（如 `ListN`）中。由于外部没有任何途径可以获取到这个底层私有数组的引用，且该类本身不提供任何修改数据的方法，从而实现了结构上的绝对隔离与不可变。

ImmutableCollections.java

```java
    @SafeVarargs
    static <E> List<E> listFromArray(E... input) {
        // copy and check manually to avoid TOCTOU
        @SuppressWarnings("unchecked")
        E[] tmp = (E[])new Object[input.length]; // implicit nullcheck of input
        for (int i = 0; i < input.length; i++) {
            tmp[i] = Objects.requireNonNull(input[i]);
        }
        return new ListN<>(tmp, false);
    }

    @jdk.internal.ValueBased
    static final class ListN<E> extends AbstractImmutableList<E> implements Serializable {
        @Stable
        private final E[] elements;
        @Stable
        private final boolean allowNulls;

        // caller must ensure that elements has no nulls if allowNulls is false
        private ListN(E[] elements, boolean allowNulls) {
            this.elements = elements;
            this.allowNulls = allowNulls;
        }

        @Override
        public E get(int index) {
            return elements[index];
        }
        ...
```

## 4. 容器不可变与对象不可变

在讨论集合的不可变性时，需要区分“容器的不可变”与“数据对象的不可变”。

无论是通过旧 API 创建的只读视图，还是通过新 API `List.copyOf()` 创建的隔离集合，它们保证的仅仅是容器本身结构的不可变（即不能增删元素，不能替换某个索引位置的引用）。然而，集合内部存储的依然是对象的引用。如果集合中存放的是自定义的可变对象（例如提供了 setter 方法的 `User` 类），任何人只要从集合中读取出该对象的引用，依然可以调用其方法修改对象的内部属性。

因此，集合层面的 API 只能解决容器层面的安全问题。要实现业务数据的绝对不可变，必须同时保证容器结构不可变，且集合内存储的元素对象也是不可变的（例如 `String` 类型，或者将自定义类的属性均设计为 `final` 且不提供修改方法）。

