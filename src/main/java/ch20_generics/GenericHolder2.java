package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/29 周一
 * @time 0:23
 * P.675 §20.6 类型擦除的奥秘 §20.6.4 边界的行为
 * <p>
 * 现在将泛型引入到 SimpleHolder.java 的代码中；get() 中不再需要进行类型转换了。但是我们仍然知道传给 set() 的值经过了编译期类型检查。
 * <p>
 * 得到的代码（字节码）完全相同。set() 中对传入类型的额外检查工作是编译器自动执行的。而对 get() 输出值的类型转换仍然存在，
 * 但并不比你自己来实现做得少————而且这是由编译器自动插入的，因此代码更简洁，在编写（和阅读）的时候会更清爽。
 * <p>
 * get() 和 set() 生成了相同的字节码，由此可知泛型所有的行为都发生在边界————包括对传入值额外的编译时检查，
 * 和对输出值插入的类型转换。记住“边界是行为发生的地方”，这有助于减少对类型擦除产生的困惑。
 * <p>
 * notes: Java 泛型擦除的字节码表现：指令同一性与 CHECKCAST 自动转型机制
 */
public class GenericHolder2<T> {
    private T obj;

    public void set(T obj) {
        this.obj = obj;
    }

    public T get() {
        return obj;
    }

    public static void main(String[] args) {
        GenericHolder2<String> holder = new GenericHolder2<>();
        holder.set("Item");
        String s = holder.get();
    }
}
/* 字节码（结果经过了编辑）
  // access flags 0x1
  // signature (TT;)V
  // declaration: void set(T)
  public set(Ljava/lang/Object;)V
   L0
    LINENUMBER 23 L0
    ALOAD 0
    ALOAD 1
    PUTFIELD ch20_generics/GenericHolder2.obj : Ljava/lang/Object;
   L1
    LINENUMBER 24 L1
    RETURN
   L2
    LOCALVARIABLE this Lch20_generics/GenericHolder2; L0 L2 0
    // signature Lch20_generics/GenericHolder2<TT;>;
    // declaration: this extends ch20_generics.GenericHolder2<T>
    LOCALVARIABLE obj Ljava/lang/Object; L0 L2 1
    // signature TT;
    // declaration: obj extends T
    MAXSTACK = 2
    MAXLOCALS = 2

  // access flags 0x1
  // signature ()TT;
  // declaration: T get()
  public get()Ljava/lang/Object;
   L0
    LINENUMBER 27 L0
    ALOAD 0
    GETFIELD ch20_generics/GenericHolder2.obj : Ljava/lang/Object;
    ARETURN
   L1
    LOCALVARIABLE this Lch20_generics/GenericHolder2; L0 L1 0
    // signature Lch20_generics/GenericHolder2<TT;>;
    // declaration: this extends ch20_generics.GenericHolder2<T>
    MAXSTACK = 1
    MAXLOCALS = 1

  // access flags 0x9
  public static main([Ljava/lang/String;)V
   L0
    LINENUMBER 31 L0
    NEW ch20_generics/GenericHolder2
    DUP
    INVOKESPECIAL ch20_generics/GenericHolder2.<init> ()V
    ASTORE 1
   L1
    LINENUMBER 32 L1
    ALOAD 1
    LDC "Item"
    INVOKEVIRTUAL ch20_generics/GenericHolder2.set (Ljava/lang/Object;)V
   L2
    LINENUMBER 33 L2
    ALOAD 1
    INVOKEVIRTUAL ch20_generics/GenericHolder2.get ()Ljava/lang/Object;
    CHECKCAST java/lang/String
    ASTORE 2
   L3
    LINENUMBER 34 L3
    RETURN
   L4
    LOCALVARIABLE args [Ljava/lang/String; L0 L4 0
    LOCALVARIABLE holder Lch20_generics/GenericHolder2; L1 L4 1
    // signature Lch20_generics/GenericHolder2<Ljava/lang/String;>;
    // declaration: holder extends ch20_generics.GenericHolder2<java.lang.String>
    LOCALVARIABLE s Ljava/lang/String; L3 L4 2
    MAXSTACK = 2
    MAXLOCALS = 3
 */
